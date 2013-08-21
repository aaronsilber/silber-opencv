import java.util.ArrayList;
import java.util.Iterator;

import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;


public class DataLogger {
	private DataFrame lastFrame;
	private ArrayList<DataFrame> backend = new ArrayList();
	//private MatOfPoint2f points = new MatOfPoint2f();
	long startTime = 0;
	boolean isLogging = false;
	boolean ctg = true;
	
	public DataLogger() {
		// TODO Auto-generated constructor stub
		startTime = System.nanoTime();
	}
	public void addFrame(DataFrame frame)
	{
		ctg = false;
		frame.nanotime = frame.nanotime - startTime;
		if (lastFrame != null)
		{
			float prevdist = lastFrame.getDistance();
			
			Point pt0 = lastFrame.getPoint();
			double x0 = pt0.x;
			double y0 = pt0.y;
			
			Point pt = frame.getPoint();
			double x1 = pt.x;
			double y1 = pt.y;
			
			//System.out.println(pt0.toString() + pt.toString());
			double dist = prevdist + Math.sqrt(Math.pow(x1-x0,2)+Math.pow(y1-y0,2));
			//System.out.println(dist);
			
			frame.setDistance((float) dist);
		}
		backend.add(frame);
		lastFrame = frame;
		ctg = true;
		//points.push_back(new Mat());
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
		
		}
		StringBuilder stringy = new StringBuilder();
        Iterator<DataFrame> each = backend.iterator();
        while (each.hasNext())
        {
            DataFrame wrapper = each.next();
            stringy.append(wrapper.getNanotime() + "," + wrapper.getFrame() + "," + wrapper.getPoint().x + "," + wrapper.getPoint().y + "," + wrapper.getDistance() + "\n");
        }
        
        return stringy.toString();
	}

}
