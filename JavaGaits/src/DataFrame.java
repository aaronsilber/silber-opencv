import org.opencv.core.Point;

public class DataFrame {
	long time;
	long frame;
	float distanceTraveled = 0.0F;
	org.opencv.core.Point point;
	public DataFrame(long time, long frame, Point point)
	{
		this.time = time;
		this.frame = frame;
		this.point = point;
	}
	
	public long getTime()
	{
		return time;
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

	public void setTime(long time)
	{
		this.time = time;
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
