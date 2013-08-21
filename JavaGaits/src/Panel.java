// Import the basic graphics classes.  
 // The problem here is that we read the image with OpenCV into a Mat object.  
 // But OpenCV for java doesn't have the method "imshow", so, we got to use  
 // java for that (drawImage) that uses Image or BufferedImage.  
 // So, how to go from one the other... Here is the way...  
 import java.awt.*;  
 import java.awt.image.BufferedImage;  
 import javax.swing.*;  
 import org.opencv.core.Mat;  
 import org.opencv.highgui.VideoCapture;  
 public class Panel extends JPanel{  
   private static final long serialVersionUID = 1L;  
   private BufferedImage image;  
   // Create a constructor method  
   public Panel(){  
     super();  
   }  
   public BufferedImage getimage(){  
     return image;  
   }  
   public void setimage(BufferedImage newimage){  
     image=newimage;  
     return;  
   }  
   /**  
    * Converts/writes a Mat into a BufferedImage.  
    *  
    * @param matrix Mat of type CV_8UC3 or CV_8UC1  
    * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY  
    */  
   public void paintComponent(Graphics g){  
      BufferedImage temp=getimage();  
      if (temp != null)
      {
    	  g.drawImage(temp,0,0,temp.getWidth(),temp.getHeight(), this);  
      }
      else
      {
    	  g.fillRect(0, 0, 50,50);
      }
   }  
   /*public static void main(String arg[]){  
    // Load the native library.  
    //System.loadLibrary("opencv_java246");   
   }*/
 }  