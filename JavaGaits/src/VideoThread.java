import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JFrame;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class VideoThread implements Runnable {
	  private static Mat mRgba;
	  public static Scalar mBlobColorRgba = new Scalar(255,131,38);
	  public static Scalar mBlobColorHsv =  new Scalar(107.93,215,240);
	  private static ColorBlobDetector mDetector;
	  private static Mat mSpectrum;
	  private static Scalar CONTOUR_COLOR = new Scalar(255,0,255,255);
	  
	  int frame = 0;
	  
	  Panel raw;
	  Panel out;
	  JFrame inframe;
	  JFrame outframe;
	  Thread thread;
	  Mat webcam_image = new Mat();
	  Mat thresh_image = new Mat();
	
	  synchronized void swapDevice(int index)
	  {
		  if (Main.capture != null)
		  {
			  Main.capture.release();
		  }
		  Main.capture.open(index);
	  }
	
	  VideoThread(Panel raw, Panel out, JFrame inframe, JFrame outframe) {
		  this.raw = raw;
		  this.out = out;
		  this.inframe=inframe;
		  this.outframe=outframe;
		  
		  thread = new Thread(this, "Video Processing Thread");
		  System.out.println("Child thread: " + thread);
		  mRgba = new Mat(400, 400, CvType.CV_8UC4);
		  mDetector = new ColorBlobDetector();
		  mSpectrum = new Mat();
	  }

	void setBlobColor(Scalar Rgba, Scalar Hsv)
	{
		VideoThread.mBlobColorRgba = Rgba;
		VideoThread.mBlobColorHsv = Hsv;
		mDetector.setHsvColor(mBlobColorHsv);
	}

	public Scalar getHsvBlobColor()
	{
		return VideoThread.mBlobColorHsv;
	}
	
	  public static BufferedImage matToBufferedImage(Mat matrix) {  
		     int cols = matrix.cols();  
		     int rows = matrix.rows();  
		     int elemSize = (int)matrix.elemSize();  
		     byte[] data = new byte[cols * rows * elemSize];  
		     int type;  
		     matrix.get(0, 0, data);  
		     switch (matrix.channels()) {  
		       case 1:  
		         type = BufferedImage.TYPE_BYTE_GRAY;  
		         break;  
		       case 3:  
		         type = BufferedImage.TYPE_3BYTE_BGR;  
		         // bgr to rgb  
		         byte b;  
		         for(int i=0; i<data.length; i=i+3) {  
		           b = data[i];  
		           data[i] = data[i+2];  
		           data[i+2] = b;  
		         }  
		         break;  
		       default:  
		         return null;  
		     }  
		     BufferedImage image2 = new BufferedImage(cols, rows, type);  
		     image2.getRaster().setDataElements(0, 0, cols, rows, data);  
		     return image2;  
		   }  
	
	  @Override
	  public void run() {
      	mDetector.setHsvColor(mBlobColorHsv);
		  try
		  {
				//changeDevice(getDevice());
				Main.capture = new VideoCapture(Main.settings.getDevice());  
		  }
		  catch (Exception ex)
		  {
			  System.out.println("Caught a camera exception (fatal?) " + ex.toString());
		  }
		  while(true)
		  {
			  	//Mat webcam_image=new Mat();
			    
			    BufferedImage temp;  
			   // capture.
			    if( Main.capture.isOpened())  
			     {  
			      while( true )  
			      {  
			       Main.safeRead(webcam_image);  
			        if( !webcam_image.empty() )  
			         {  
			        	frame++;
			        	inframe.setSize(webcam_image.width(),webcam_image.height());  
			        	outframe.setSize(webcam_image.width(),webcam_image.height());
			        	temp=matToBufferedImage(webcam_image);  
			        	raw.setImage(temp);  
			        	raw.repaint();  
			        	
			        	mRgba = webcam_image.clone();
			           
			        	mDetector.process(mRgba);
			        	List<MatOfPoint> contours = mDetector.getContours();
			            Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);

			            Mat colorLabel = mRgba.submat(4, 68, 4, 68);
			            colorLabel.setTo(mBlobColorRgba);

			            Mat spectrumLabel = mRgba.submat(4, 4 + mSpectrum.rows(), 70, 70 + mSpectrum.cols());
			            mSpectrum.copyTo(spectrumLabel);
			           
			            temp=matToBufferedImage(mRgba);
			           
			            out.setImage(temp);
			            out.repaint();
			           
			         }  
			         else  
			         {  
			        	 System.out.println(" --(!) No captured frame -- Break!");  
			        	 break;  
			         }  
			      }  
			     }  
			    return;  
		  }
	  }
} 