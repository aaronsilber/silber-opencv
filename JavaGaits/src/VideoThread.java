import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class VideoThread implements Runnable {
	  private static boolean              mIsColorSelected = false;
	  private static Mat                  mRgba;
	  public static Scalar               mBlobColorRgba;
	  public static Scalar               mBlobColorHsv;
	  private static ColorBlobDetector    mDetector;
	  private static Mat                  mSpectrum;
	  private static Size                 SPECTRUM_SIZE;
	  private static Scalar               CONTOUR_COLOR;
	  
	  int frame= 0;
	  
	    VideoCapture capture = new VideoCapture(1);  
	  
	Panel raw;
	Panel out;
	JFrame inframe;
	JFrame outframe;
	Thread thread;
	Mat webcam_image = new Mat();
	Mat thresh_image = new Mat();
	VideoThread(Panel raw, Panel out, JFrame inframe, JFrame outframe) {
	    this.raw = raw;
	    this.out = out;
	    this.inframe=inframe;
	    this.outframe=outframe;
	    // Create a new, second thread
	    thread = new Thread(this, "Demo Thread");
	    System.out.println("Child thread: " + thread);
	    thread.start(); // Start the thread
	    mRgba = new Mat(400, 400, CvType.CV_8UC4);
        mDetector = new ColorBlobDetector();
        mSpectrum = new Mat();
        mBlobColorRgba = new Scalar(255,151,54);
        mBlobColorHsv = new Scalar(105.52,201,255); //79214, 89, 
        SPECTRUM_SIZE = new Size(200, 64);
        CONTOUR_COLOR = new Scalar(255,0,255,255);
	  }

	void setBlobColor(Scalar Rgba, Scalar Hsv)
	{
		this.mBlobColorRgba = Rgba;
		this.mBlobColorHsv = Hsv;
		mDetector.setHsvColor(mBlobColorHsv);
	}

	public Scalar getHsvBlobColor()
	{
		return this.mBlobColorHsv;
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
		  while(true)
		  {
			  	//Mat webcam_image=new Mat();
			    
			    BufferedImage temp;  
			    
			    if( capture.isOpened())  
			     {  
			      while( true )  
			      {  
			        capture.read(webcam_image);  
			        if( !webcam_image.empty() )  
			         {  
			        	frame++;
			           inframe.setSize(webcam_image.width(),webcam_image.height());  
			           outframe.setSize(webcam_image.width(),webcam_image.height());
			           temp=matToBufferedImage(webcam_image);  
			           raw.setimage(temp);  
			           raw.repaint();  
			           
			           //temp=matToBufferedImage(thresh_image);
			           mRgba = webcam_image;

			           //if (mIsColorSel_ected) {
			           mDetector.setHsvColor(mBlobColorHsv);
			               mDetector.process(mRgba);
			               List<MatOfPoint> contours = mDetector.getContours();
			               //Log.e(TAG, "Contours count: " + contours.size());
			               Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);

			               Mat colorLabel = mRgba.submat(4, 68, 4, 68);
			               colorLabel.setTo(mBlobColorRgba);

			               Mat spectrumLabel = mRgba.submat(4, 4 + mSpectrum.rows(), 70, 70 + mSpectrum.cols());
			               mSpectrum.copyTo(spectrumLabel);
			           //}

			           temp=matToBufferedImage(mRgba);
			           
			           //org.opencv.imgproc.Imgproc.threshold(webcam_image, thresh_image, 120, 250, org.opencv.imgproc.Imgproc.THRESH_BINARY);
			           //temp=matToBufferedImage(thresh_image);
			           out.setimage(temp);
			           out.repaint();
			           //org.opencv.imgproc.Imgproc.
			         }  
			         else  
			         {  
			           System.out.println(" --(!) No captured frame -- Break!");  
			           break;  
			         }  
			        }  
			       }  
			       return;  
			  //System.out.println("just ran again.");
		  }
	  }
} 