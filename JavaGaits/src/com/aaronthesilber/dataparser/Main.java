package com.aaronthesilber.dataparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.apache.commons.io.IOUtils;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.FileDataSet;
import com.panayotis.gnuplot.swing.JPlot;

public class Main {
public static final float distThresh = 2.5F;
static String logcache = "";
final static String line_ending = "\n";
public enum LogDestination {
	STDOUT,FILE,PLOT,NONE
}
static final LogDestination LOG_OUTPUT = LogDestination.FILE;
static final String logFilenameBase = "/home/silbernetic/Desktop/testoutputs/";
	/**
	 * @param args
	 */
	private static void logline(String line)
	{
		if (LOG_OUTPUT == LogDestination.STDOUT)
		{
			System.out.println(line);
		}
		else
		{
			logcache = logcache + line + line_ending;
		}
	}
/* This demo code displays plot on screen using image terminal */
    private static JavaPlot JPlotTerminal() {
        JPlot plot = new JPlot();
        plot.getJavaPlot().addPlot("sqrt(x)/x");
        plot.getJavaPlot().addPlot("x*sin(x)");
        
        //com.panayotis.gnuplot.dataset.PointDataSet pts2;
        //com.panayotis.gnuplot.dataset.
        //plot.getJavaPlot().addPlot(Dataset)
        //plot.plot();
        
        JFrame f = new JFrame();
        //f.getContentPane().add(plot);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        
        return plot.getJavaPlot();
    }
    private static ArrayList<Float[]> parseFile(String filename, String id)
    {
    	boolean startTrigger = false;
    	Float maxdist = 0F;
    	Float lastx=0F, lasty = 0F;
    	Float finalx = 0F, finaly = 0F;
    	Float firstx=0F, firsty = 0F;
    	Float startval = 0F;
    	//JPlotTerminal();
        ArrayList<Float[]> datapoints = new ArrayList<Float[]>();
		 try {
		    FileInputStream inputStream = new FileInputStream(filename);
		    try {
		        String everything = IOUtils.toString(inputStream);
		        String[] lines = everything.split("\r\n");
	        	Float deltx=0F, delty = 0F;
	        	Float dist = 0F;
	        	int linenum = 0;
	        	
		        for (String line : lines)
		        {
		        	if (linenum==0)
		        	{
		        		linenum++;
		        		continue;
		        	}
		        	String[] split = line.split(",");
		        	Float[] pts = new Float[split.length+1]; //adjust the size of this array to the num of fields
		        	int i = 0;
		        	for (String num : split)
		        	{
		        		try
		        		{
		        			Float tmp = Float.parseFloat(num);
		        			boolean breaker = false;
		        			switch(i)
		        			{
		        				case 0:
		        					if (linenum==1)
		        					{
		        						startval = tmp;
		        					}
		        					tmp = tmp - startval;
		        					//timestamp
		        					break;
		        				case 1:
		        					//frame
		        					//breaker = true;
		        					break;
		        				case 2:
		        					//x_pix
		        					tmp = tmp / 3.6F; //conversion factor to cm
		        					if (linenum == 2)
		        					{
		        						firstx = tmp;
		        					}
		        					deltx = Math.abs(lastx-tmp);
		        					//logline("lastx was " + lastx + " now " + deltx + " changed");
		        					lastx = tmp;
		        					finalx = tmp;
		        					break;
		        				case 3:
		        					//y_pix
		        					tmp = tmp / 3.6F; //conversion factor to cm
		        					
		        					if (linenum == 2)
		        					{
		        						firsty = tmp;
		        					}
		        					delty = Math.abs(lasty-tmp);
		        					lasty = tmp;
		        					finaly = tmp;
		        					break;
		        				case 4:
		        					//computedsqrt - drop this silly field
		        					dist += (float) Math.sqrt(Math.pow(deltx, 2)+Math.pow(delty,2));
		        					tmp = dist;
		        					//breaker=true;
		        					break;
	        					default:
	        						break;
		        			}
		        			if (breaker) { continue; }
		        			pts[i] = tmp;
		        			//logline(Float.parseFloat(num));
		        		}
		        		catch (Exception e)
		        		{
		        			logline("exception processing a point");
		        			e.printStackTrace();
		        		}
		        		i++;
		        	}
        			//logline("[" + pts[0] + "] " + "lastx,y:" + lastx + "," + lasty + "(" + dist + ")");
		        	//
		        	//what to do: compute distance from first x and first y
		        	//logline(pts[0] + "," + pts[2] + "," + pts[3] + "," + dist + "," + reldist);
        	     	Float reldist = (float) Math.sqrt(Math.pow(firstx-pts[2],2) + Math.pow(firsty-pts[3], 2));
        	     	if (reldist > maxdist) { maxdist = reldist; }
        	     	pts[5] = (float) Math.sqrt(Math.pow(firstx-pts[2],2) + Math.pow(firsty-pts[3],2));
        	     	//this is: timestamp, x, y, dist, computed_val_1
        	     	if (pts[5] < distThresh && !startTrigger) //checking if it has moved less than "3"
        	     	{
        	     		logcache = ""; //reset the log
        	     		//logHeader(id); //add a new header
        	     	}
        	     	else if (startTrigger)
        	     	{
        	     		datapoints.add(pts);
            	     	logline(pts[0] + "," + pts[2] + "," + pts[3] + "," + pts[4] + "," + pts[5]);
        	     	}
        	     	else if (pts[5] > distThresh && !startTrigger && linenum > 4) //first line to be logged properly
        	     	{
        	     		//reset the timestamp
        	     		startval += pts[0];
        	     		//datapoints.add(pts);
        	     		startTrigger = true;
        	     	}
		        	linenum++;
		        }
		        
		       for (int i=0;i<datapoints.size();i++)
		       {
		    	   int samp = 3; //sample size, radius
		    	   if (!(i-samp<0||i+samp>datapoints.size()))
		    	   {
			    	   List<Float[]> samples = datapoints.subList(i-samp, i+samp);
			    	   Float timesum = 0.0F;
			    	   Float xsum = 0.0F;
			    	   Float ysum = 0.0F;
			    	   for (int i2=0;i2<samples.size();i2++)
			    	   {
			    		   Float[] f = samples.get(i2);
			    		   //logline(f.toString());
			    		   timesum += f[0];
			    		   xsum += f[2];
			    		   ysum += f[3];
			    	   }
			    	   Float timeavg = timesum / samples.size();
			    	   Float xavg = xsum / samples.size();
			    	   Float yavg = ysum / samples.size();
			    	   //logline(timeavg + "," + xavg + "," + yavg);
		    	   }
		       }
		    } finally {
		        inputStream.close();
		    }
		    /*FileInputStream inputStream = new FileInputStream(args[0]);
		    try {
		        String everything = IOUtils.toString(inputStream);
		        logline(everything);
		        JavaPlot p = new JavaPlot();
		        p.addPlot(new FileDataSet(new File(args[0])));
		        p.plot();
		        //JPlotTerminal(null);
		    } finally {
		        inputStream.close();
		    }*/

		    
			} catch (Exception e) {
				logline("FATAL: Exception on file: '" + filename + "'\r\n");
				e.printStackTrace();
			}
     	//Float direction = (float) Math.atan(finaly-firsty/finalx-firstx);
     	Float direction = (float) Math.toDegrees(Math.atan2(finaly-firsty, finalx-firstx));
     	Float dist = (float) Math.sqrt(Math.pow(firstx-finalx,2)+Math.pow(firsty-finaly, 2));
		//logline(id + "," + dist);
     	return datapoints;
    }
	public static void main(String[] args) {
		/*if (args.length < 1)
		{
			logline("Too few arguments!!");
			return;
		}*/
		String[] tests = { "test1-trial1",
				"test1-trial2",
				"test1-trial3",
				"test1-trial4",
				"test2-trial1",
				"test2-trial2",
				"test2-trial3",
				"test2-trial4",
				"test3-trial1",
				"test3-trial2",
				"test3-trial3",
				"test3-trial4",
				"test4-trial1",
				"test4-trial2",
				"test4-trial3",
				"test4-trial4",
				"test5-trial1",
				"test5-trial2",
				"test5-trial3",
				"test6-trial1",
				"test6-trial2",
				"test6-trial3",
				"test6-trial4",
				"test7-trial1",
				"test7-trial2",
				"test7-trial3",
				"test7-trial4",
				"test8-trial1",
				"test8-trial2",
				"test8-trial3",
				"test8-trial4",
				"test9-trial1",
				"test9-trial2",
				"test9-trial3",
				"test9-trial4",
				"test10-trial1",
				"test10-trial2",
				"test10-trial3",
				"test10-trial4",
				"test11-trial1",
				"test11-trial2",
				"test11-trial3",
				"test11-trial4",
				"test12-trial1",
				"test12-trial2",
				"test12-trial3",
				"test12-trial4",
				"test13-trial1",
				"test13-trial2",
				"test13-trial3",
				"test13-trial4"};
		/*
		 * comment back in for iterative testing
		 * */
		int i =0;
		for (String test : tests)
		{
			String filename = "/home/silbernetic/Desktop/tests/" + test + "/data.csv";
			parseFile(filename, Integer.toString(i));
			if (LOG_OUTPUT == LogDestination.FILE) fileLog(Integer.toString(i));
			i++;
		}
		//single file testing
		//parseFile("/home/silbernetic/Desktop/tests/test11-trial3/data.csv", "1"); // was trial2
		
		//String filename = "/home/silbernetic/Desktop/tests/test2-trial2/data.csv";
		// String filename = args[0];
		 //parseFile(filename);
	}
	
	static void fileLog(String id)
	{
		PrintWriter writer;
		try {
			id = String.format("%3s", id).replace(' ','0');
			logHeader(id);
			writer = new PrintWriter(logFilenameBase + id + ".csv", "UTF-8");
			writer.print(logcache);
			writer.close();
			System.out.println("Looks like the file was written properly! [File ID '" + id + "']");
			logcache = "";
			
		} catch (FileNotFoundException e) {
			// uh, wrong filename bro?
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// this sucks because it means your system does not support UTF-8
			e.printStackTrace();
		}
	}
	static void logHeader(String id)
	{
		logcache = id + "\n" + logcache;
	}

}
