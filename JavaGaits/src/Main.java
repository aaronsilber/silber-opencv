import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc.*;
import org.opencv.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
class Main {
  static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
  static Panel inpanel = new Panel();
  static Panel outpanel = new Panel();
  static public JFrame frame1 = new JFrame("Input"); 
  static public JFrame frame2 = new JFrame("Output");
  static public SettingsFrame settings = new SettingsFrame("test");
  static VideoThread backthread = new VideoThread(inpanel,outpanel,frame1,frame2);
  
  static public DataLogger logger = new DataLogger();
  
  static Scalar hsv = new Scalar(0,0,0);
  
  /*public static void updateHSV(int hue)
  {
		backthread.mDetector
  }*/
  
  public static void main(String[] args) {
      inpanel.addMouseListener(new PickingListener());
      
      frame1.setResizable(false);
	    frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	    frame1.setContentPane(inpanel);       
	    frame1.setVisible(true);  
	    
	     frame2.setResizable(false);
	    frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
	    frame2.setContentPane(outpanel);       
	    frame2.setVisible(true);  
	    
	    settings.setSize(400,400);
	    settings.setVisible(true);
	    
	   }  
  }
  
