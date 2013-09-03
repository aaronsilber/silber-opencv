import java.util.ArrayList;
import java.util.Iterator;

import org.opencv.core.Point;

public class DataLogger {
	private DataFrame lastFrame; //previous frame logged, a back-cache
	private ArrayList<DataFrame> backend = new ArrayList<DataFrame>(); //holds the data!
	long startTime = 0; //absolute time logging began
	boolean isLogging = false; //logging flag
	boolean ctg = true; //attempt at crude thread-locking
	
	public DataLogger()
	{
		startTime = System.currentTimeMillis(); //we use milliseconds now. nanoseconds are pointlessly large
	}
	
	public void addFrame(DataFrame frame)
	{
		ctg = false; //"lock" things
		frame.setTime(frame.getTime() - startTime);
		if (lastFrame != null) //sanity
		{
			//compute distance from last datapoint
			float prevdist = lastFrame.getDistance();
			Point pt0 = lastFrame.getPoint();
			double x0 = pt0.x;
			double y0 = pt0.y;
			Point pt = frame.getPoint();
			double x1 = pt.x;
			double y1 = pt.y;
			double dist = prevdist + Math.sqrt(Math.pow(x1-x0,2)+Math.pow(y1-y0,2));
			
			//assign this distance to the new frame
			frame.setDistance((float) dist);
		}
		backend.add(frame); //add to data list
		lastFrame = frame; //store the frame for later...
		ctg = true; //"unlock" things
	}
	
	public int count()
	{
		return backend.size();
	}
	
	public void setLogging(boolean val)
	{
		isLogging = val;
	}
	
	public boolean getLogging()
	{
		return isLogging;
	}
	
	public String getCSV()
	{
		while(!ctg)
		{
			//this may be the most hacky way to "lock" the thread
			//and it doesn't work most of the time
			//call this function a couple of times
			//and it might behave properly
		}
		
		//initialize a StringBuilder and iteratively compose CSV lines
		StringBuilder stringy = new StringBuilder();
		stringy.append("time,frame,x_pixels,y_pixels,sqrt_dist\r\n");
        Iterator<DataFrame> each = backend.iterator();
        while (each.hasNext())
        {
            DataFrame wrapper = each.next();
            stringy.append(wrapper.getTime() + "," + wrapper.getFrame() + "," + wrapper.getPoint().x + "," + wrapper.getPoint().y + "," + wrapper.getDistance() + "\r\n");
        }
        
        //return full CSV
        return stringy.toString();
	}

}
