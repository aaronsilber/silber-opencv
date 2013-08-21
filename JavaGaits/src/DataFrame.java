import org.opencv.core.Point;

public class DataFrame {
long nanotime;
long frame;
float distanceTraveled = 0.0F;
org.opencv.core.Point point;
	public DataFrame(long time, long frame, Point point) {
		// TODO Auto-generated constructor stub
		
		this.nanotime = time;
		this.frame = frame;
		this.point = point;
	}
	
	public long getNanotime()
	{
		return nanotime;
	}
	
	public long getFrame()
	{
		return frame;
	}
	
	public Point getPoint()
	{
		return point;
	}
	
	public float getDistance()
	{
		return distanceTraveled;
	}
	
	public void setDistance(float dist)
	{
		this.distanceTraveled = dist;
	}

	public void setNanotime(long time)
	{
		this.nanotime = time;
	}
	
	public void setFrame(long frame)
	{
		this.frame = frame;
	}
	
	public void setPoint(Point point)
	{
		this.point = point;
	}

}
