// Import the basic graphics classes.  
 // The problem here is that we read the image with OpenCV into a Mat object.  
 // But OpenCV for java doesn't have the method "imshow", so, we got to use  
 // java for that (drawImage) that uses Image or BufferedImage.  
 // So, how to go from one the other... Here is the way...  
import java.awt.*;  
import java.awt.image.BufferedImage;  
import javax.swing.*;  

public class Panel extends JPanel
{  
	private static final long serialVersionUID = 4010699211556699539L; //make Eclipse happy
	private BufferedImage image;  //store the current image here
	
	public Panel()
	{  
		super();  //call super's constructor
	}
	
	public BufferedImage getImage()
	{  
		return image; //getter for private image
	}  
	
	public void setImage(BufferedImage newimage)
	{  
		image = newimage;  //setter for private image
	}  
	
	public void paintComponent(Graphics g)
	{  
		BufferedImage temp = getImage(); //temp variable of current image
		if (temp != null)
		{
			g.drawImage(temp,0,0,temp.getWidth(),temp.getHeight(), this); //draw image at (0,0) full size  
		}
		else
		{
			g.fillRect(0, 0, 50,50); //well, a 50x50 square beats null stuff lying around, at least.
		}
	}  
}  