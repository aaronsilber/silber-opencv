import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;


public class Calibration {
	static public Panel calibpanel = new Panel(); //output content pane
	static public JFrame framepanel = new JFrame("Calibrate");
	static Scalar floorTileHsv = new Scalar(78.75,14.57,140);
	static Scalar floorTileRgba = new Scalar(137,140,132);
	private static Scalar mLowerBound = new Scalar(0);
    private static Scalar mUpperBound = new Scalar(0);
    // Color radius for range checking in HSV color space
    private static Scalar mColorRadius = new Scalar(280,180,50);
    private static Mat mSpectrum = new Mat();
    //private List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
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
	  public static void setHsvColor(Scalar hsvColor)
	  {
		  	//my memory is so backwards I just have to swap HSV/VSH and RGB/BGR/RGBA until
		  	//things make enough sense
	    	//sorry
	    	//-aaron
	    	
	        double minH = (hsvColor.val[0] >= mColorRadius.val[0]) ? hsvColor.val[0]-mColorRadius.val[0] : 0;
	        double maxH = (hsvColor.val[0]+mColorRadius.val[0] <= 255) ? hsvColor.val[0]+mColorRadius.val[0] : 255;

	        mLowerBound.val[0] = minH;
	        mUpperBound.val[0] = maxH;

	        mLowerBound.val[1] = hsvColor.val[1] - mColorRadius.val[1];
	        mUpperBound.val[1] = hsvColor.val[1] + mColorRadius.val[1];

	        mLowerBound.val[2] = hsvColor.val[2] - mColorRadius.val[2];
	        mUpperBound.val[2] = hsvColor.val[2] + mColorRadius.val[2];

	        mLowerBound.val[3] = 0;
	        mUpperBound.val[3] = 255;

	        Mat spectrumHsv = new Mat(1, (int)(maxH-minH), CvType.CV_8UC3);

	        for (int j = 0; j < maxH-minH; j++) {
	            byte[] tmp = {(byte)(minH+j), (byte)255, (byte)255};
	            spectrumHsv.put(0, j, tmp);
	        }

	        //convert to RGB
	        Imgproc.cvtColor(spectrumHsv, mSpectrum, Imgproc.COLOR_HSV2RGB_FULL, 4);
	  	}
	  static org.opencv.core.Point[] toXY(double[] data)
	  {
		  double rho, theta;
		  org.opencv.core.Point pt1 = new org.opencv.core.Point();
		  org.opencv.core.Point pt2 = new org.opencv.core.Point();

		  double a, b;
		  double x0, y0;
   
		  rho = data[0];
		  theta = data[1];
		  a = Math.cos(theta);
		  b = Math.sin(theta);
		  x0 = a*rho;
		  y0 = b*rho;
		  pt1.x = (int) Math.round(x0 + 1000*(-b));
		  pt1.y = (int) Math.round(y0 + 1000*a);
		  pt2.x = (int) Math.round(x0 - 1000*(-b));
		  pt2.y = (int) Math.round(y0 - 1000 *a);
		  org.opencv.core.Point[] pts = {pt1,pt2};
		  return pts;
	  }
	  
	  boolean acceptLinePair(float[] line1, float[] line2, float minTheta)
	  {
		  float theta1 = line1[1], theta2 = line2[1];

		  if(theta1 < minTheta)
		  {
			  theta1 += Math.PI;
		  }

		  if(theta2 < minTheta)
		  {
			  theta2 += Math.PI; // dealing with 0 and 180 ambiguities...
		  }

		  return Math.abs(theta1 - theta2) > minTheta;
	  }
	  
	  static org.opencv.core.Point findIntersection(org.opencv.core.Point line1,org.opencv.core.Point line12,
		  org.opencv.core.Point line2,org.opencv.core.Point line22) {
		  float xD1,xD2,yD2,xD3,yD3;
		  float yD1;
		  float dot,deg,len1,len2;
		  float segmentLen1,segmentLen2;
		  float ua,ub,div;
		    
		  // calculate differences
		  xD1=(float) (line12.x-line1.x);
		  xD2=(float) (line22.x-line2.x);
		  yD1=(float) (line12.y-line1.y);
		  yD2=(float) (line22.y-line2.y);
		  xD3=(float) (line1.x-line2.x);
		  yD3=(float) (line1.y-line2.y);  
		  
		  // calculate the lengths of the two lines
		  len1=(float) Math.sqrt(xD1*xD1+yD1*yD1);
		  len2=(float) Math.sqrt(xD2*xD2+yD2*yD2);

		  // calculate angle between the two lines.
		  dot=(xD1*xD2+yD1*yD2); // dot product
		  deg=dot/(len1*len2);
		  
		  // if abs(angle)==1 then the lines are parallell,
		  // so no intersection is possible
		  if(Math.abs(deg)==1) return null;

		  // find intersection Pt between two lines  
		  org.opencv.core.Point pt=new org.opencv.core.Point(0,0);
		  div=yD2*xD1-xD2*yD1;
		  ua=(xD2*yD3-yD2*xD3)/div;
		  ub=(xD1*yD3-yD1*xD3)/div;
		  pt.x=(int) (line1.x+ua*xD1);
		  pt.y=(int) (line1.y+ua*yD1);
		  
		  // calculate the combined length of the two segments
		  // between Pt-p1 and Pt-p2
		  xD1=(float) (pt.x-line1.x);
		  xD2=(float) (pt.x-line12.x);
		  yD1=(float) (pt.y-line1.y);
		  yD2=(float) (pt.y-line12.y);
		  segmentLen1=(float) (Math.sqrt(xD1*xD1+yD1*yD1)+Math.sqrt(xD2*xD2+yD2*yD2));
		  
		  // calculate the combined length of the two segments
		  // between Pt-p3 and Pt-p4
		  xD1=(float) (pt.x-line2.x);
		  xD2=(float) (pt.x-line22.x);
		  yD1=(float) (pt.y-line2.y);
		  yD2=(float) (pt.y-line22.y);
		  segmentLen2=(float) (Math.sqrt(xD1*xD1+yD1*yD1)+Math.sqrt(xD2*xD2+yD2*yD2));

		  // if the lengths of both sets of segments are the same as
		  // the lenghts of the two lines the point is actually 
		  // on the line segment.

		  // if the point isn't on the line, return null
		  if(Math.abs(len1-segmentLen1)>0.01 || Math.abs(len2-segmentLen2)>0.01) 
		    return null;

		  // return the valid intersection
		  return pt;
	}

	public static void calibrate()
	{
		setHsvColor(floorTileHsv);
		framepanel.setContentPane(calibpanel);
		System.out.println("Performing calibration");
		if (Main.capture.isOpened())
		{
			Mat temp2 = new Mat();
			Mat shot = new Mat();
			while (shot.empty())
			{
				Main.safeRead(shot);
			}
			BufferedImage temp=matToBufferedImage(shot);
	
			try
			{
				File outputfile = new File(Main.settings.fullPath + "calibration.png");
				ImageIO.write(temp, "png", outputfile);
			} catch (IOException e) {
				//catch an IOException here??
			}
		
			calibpanel.setImage(temp);
			framepanel.setSize(new Dimension(temp.getWidth(),temp.getHeight()));
			framepanel.setVisible(true);
		
			Imgproc.GaussianBlur(shot,temp2, new Size(9,9), 2); //gaussian kernel filter (blur)
		
			Imgproc.cvtColor(temp2,temp2, Imgproc.COLOR_RGB2HSV_FULL); //convert RGB to hsv colorspace

			Mat mMask = new Mat();
			Mat mDilatedMask = new Mat();
			Mat edges = new Mat();
			Mat lines = new Mat();
			Core.inRange(temp2, mLowerBound, mUpperBound, mMask); //threshold between lower/upper HSV bounds
			Imgproc.dilate(mMask, mDilatedMask, new Mat()); //dilate: reduce "holes" in matte
			Imgproc.Canny(mDilatedMask, edges, 66.0,133.0);
        
			Imgproc.HoughLines( edges, lines, 1, Math.PI/180, 78);
        
			Mat temp3 = new Mat();
			
			Imgproc.cvtColor(temp2,temp3,Imgproc.COLOR_HSV2RGB_FULL);
        
			ArrayList<org.opencv.core.Point[]> lineList = new ArrayList<org.opencv.core.Point[]>();
        
			double[] data;
			
			org.opencv.core.Point pt1 = new org.opencv.core.Point();
			org.opencv.core.Point pt2 = new org.opencv.core.Point();
        
			Scalar color = new Scalar(0,0,0);
			for (int i = 0; i < lines.cols(); i++)
			{
				data = lines.get(0, i);
				org.opencv.core.Point[] pts = toXY(data);
				pt1 = pts[0];
            	pt2 = pts[1];
            	lineList.add(pts);
            	Core.line(temp3,pt1,pt2,color,1);
			}
        
			ArrayList<org.opencv.core.Point> intersections = new ArrayList<org.opencv.core.Point>();
        
			for (org.opencv.core.Point[] line1 : lineList)
			{
				for (org.opencv.core.Point[] line2 : lineList)
				{
					org.opencv.core.Point pt = findIntersection(line1[0],line1[1],line2[0],line2[1]);
					if (pt!=null)
					{
						intersections.add(pt);
						Core.circle(temp3, pt, 2, new Scalar(255,255,255));
					}
				}
			}
        
			temp=matToBufferedImage(temp3);
			calibpanel.setImage(temp);
			calibpanel.repaint();
			try {
				File outputfile = new File(Main.settings.fullPath + "calibration-parsed.png");
				ImageIO.write(temp, "png", outputfile);
			} catch (IOException e) {
				//catch this?
			}
		}
	}
}
