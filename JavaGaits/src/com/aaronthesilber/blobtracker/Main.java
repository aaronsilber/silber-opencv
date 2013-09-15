package com.aaronthesilber.blobtracker;
import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.VideoCapture;

class Main {
	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); } //inline load opencv's native backend
	// NOTE: OpenCV 2.4.6 for Java does not have any windowing built-in (like OpenCV C++/python does)
	// Therefore, we build our own in Swing. Not that bad.
	static Panel inpanel = new Panel(); //input content pane
	static Panel outpanel = new Panel(); //output content pane
	static public JFrame frame1 = new JFrame("Input");  //input Swing frame
	static public JFrame frame2 = new JFrame("Output"); //output Swing frame
	static public SettingsFrame settings = new SettingsFrame("JavaGaits Controller"); //the settings Swing frame
	static VideoThread backthread = new VideoThread(inpanel,outpanel,frame1,frame2); //initialize the worker thread for video
	
	static public DataLogger logger = new DataLogger(); //initialize DataLogger for recording, well, data
  
	static Scalar hsv = new Scalar(0,0,0); //not sure why this variable is here?
	static volatile public VideoCapture capture = new VideoCapture();
	public static synchronized void safeRead(Mat dst)
	{
		capture.read(dst);
	}
	public static void main(String[] args)
	{  
		inpanel.addMouseListener(new PickingListener()); //listen for clicks + change target color
		
		//resizing isn't very helpful (not implemented). might as well disallow it.
		frame1.setResizable(false);
		frame2.setResizable(false);
	  
		//kill program on closing any window
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		settings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//set up the content panes for input/output video
		frame1.setContentPane(inpanel);          
		frame2.setContentPane(outpanel);       
	  
		//initialize settings window size + display it
		//settings.setSize(400,400);
	  	settings.setVisible(true);
	}  
}