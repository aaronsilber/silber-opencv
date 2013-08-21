import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class ColorBlobDetector {
    // Lower and Upper bounds for range checking in HSV color space
    private Scalar mLowerBound = new Scalar(0);
    private Scalar mUpperBound = new Scalar(0);
    // Minimum contour area in percent for contours filtering
    private static double mMinContourArea = 0.3;
    // Color radius for range checking in HSV color space
    private Scalar mColorRadius = new Scalar(100,30,23);
    private Mat mSpectrum = new Mat();
    private List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();

    // Cache
    Mat mPyrDownMat = new Mat();
    Mat mHsvMat = new Mat();
    Mat mMask = new Mat();
    Mat mDilatedMask = new Mat();
    Mat mHierarchy = new Mat();

    public void setColorRadius(Scalar radius) {
        mColorRadius = radius;
    }

    public void setHsvColor(Scalar hsvColor) {
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

        Imgproc.cvtColor(spectrumHsv, mSpectrum, Imgproc.COLOR_HSV2RGB_FULL, 4);
    }

    public Mat getSpectrum() {
        return mSpectrum;
    }

    public void setMinContourArea(double area) {
        mMinContourArea = area;
    }

    public void process(Mat rgbaImage) {
    	Mat tmp2 = new Mat();
    	//Imgproc.bilateralFilter(rgbaImage, tmp2, 5, 90, 90);
    	Imgproc.GaussianBlur(rgbaImage, tmp2, new Size(15,15), 2);
    	//rgbaImage=tmp2;
    	//Imgproc.bil
    	//tmp2=rgbaImage;
    	//Scalar ORANGE_MIN = new Scalar(18, 40, 90);
    		//	Scalar ORANGE_MAX = new Scalar(27, 255, 255);
    			//Scalar COLOR_MIN = mLowerBound;
    		//	Scalar COLOR_MAX = mUpperBound;
    	    	//Imgproc.cvtColor(rgbaImage, tmp2, Imgproc.COLOR_RGB2HSV_FULL);
    	//Core.inRange(tmp2, COLOR_MIN,COLOR_MAX, rgbaImage);

    			
    	
    	//contour detection commented out for testing
    	
        //Imgproc.pyrDown(rgbaImage, mPyrDownMat);
        ///Imgproc.pyrDown(mPyrDownMat, mPyrDownMat);

        Imgproc.cvtColor(tmp2, mHsvMat, Imgproc.COLOR_RGB2HSV_FULL);

        Core.inRange(mHsvMat, mLowerBound, mUpperBound, mMask);
        //rgbaImage=mMask;
        //mMask = rgbaImage;
        
        Imgproc.dilate(mMask, mDilatedMask, new Mat());
        
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Imgproc.findContours(mDilatedMask, contours, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        
        MatOfPoint largestContour = null;
        
        Imgproc.cvtColor(mMask, rgbaImage, Imgproc.COLOR_GRAY2BGR);
        // Find max contour area
        double maxArea = 0;
        Iterator<MatOfPoint> each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint wrapper = each.next();
            double area = Imgproc.contourArea(wrapper);
            if (area > maxArea)
            {
                maxArea = area;
                largestContour = wrapper;
            }
        }

        // Filter contours by area and resize to fit the original image size
        
        mContours.clear();
        each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint contour = each.next();
            if (Imgproc.contourArea(contour) > mMinContourArea*maxArea) {
                //Core.multiply(contour, new Scalar(4,4), contour);
                mContours.add(contour);
            }
        }
        
        //System.out.println("count: " + mContours.size());
        
        Moments moments = new Moments();
        if (largestContour != null)
        {
        moments = org.opencv.imgproc.Imgproc.moments(largestContour,false);
        Point2D.Float mass = new Point2D.Float();
        mass = new Point2D.Float((float) (moments.get_m10()/moments.get_m00()), (float) (moments.get_m01()/moments.get_m00()));
    	
        /*
      /// Get the moments
        Moments[] mu = new Moments[contours.size()];
        //vector<Moments> mu(contours.size() );
        
        for( int i = 0; i < contours.size(); i++ )
        {
        	//mu[i] = moments( contours[i], false );
        	mu[i] = org.opencv.imgproc.Imgproc.moments( contours.get(i), false);
        }

        ///  Get the mass centers:
        //Point2D point = new Point2D();
        Point2D.Float mc[] = new Point2D.Float[contours.size()];
        //vector<Point2f> mc( contours.size() );
        //org.opencv.imgproc.Imgproc.
        for( int i = 0; i < contours.size(); i++ )
           {
        	//mc[i] = Point2f( mu[i].m10/mu[i].m00 , mu[i].m01/mu[i].m00 );
        	mc[i] = new Point2D.Float((float) (mu[i].get_m10()/mu[i].get_m00()), (float) (mu[i].get_m01()/mu[i].get_m00()));
        	System.out.println(mc[i].toString());
           }*/
        
        /// Calculate the area with the moments 00 and compare with the result of the OpenCV function
        //System.out.println("Info: Area and Contour Length \n");
        /*for( int i = 0; i< contours.size(); i++ )
           {
             //System.out.println(" * Contour[%d] - Area (M_00) = %.2f - Area OpenCV: %.2f - Length: %.2f \n", i, mu[i].m00, contourArea(contours[i]), arcLength( contours[i], true ) );
            System.out.println("Contour[" + i + "] - Area " + mu[i].get_m00()); 
        	//Scalar color = Scalar( rng.uniform(0, 255), rng.uniform(0,255), rng.uniform(0,255) );
             //drawContours( drawing, contours, i, color, 2, 8, hierarchy, 0, Point() );
             //circle( drawing, mc[i], 4, color, -1, 8, 0 );
           }
        */
        
        /*
        Iterator<Contour> each2 = contours.iterator();
        while (each2.hasNext()) {
            MatOfPoint wrapper = each.next();
            double area = Imgproc.contourArea(wrapper);
            
        }*/
        
        //System.out.println(mc.toString());
     // where I(x,y) is the intensity of the pixel (x, y).
        /*org.opencv.imgproc.Imgproc.
        double momX10 = cvGetSpatialMoment(moments, 1, 0); // (x,y)
        double momY01 = cvGetSpatialMoment(moments, 0, 1);// (x,y)*/
        //if (mc.length > 0)
        //if (true)
        //{
        	//we want to ONLY log:
        	// the LARGEST contour's mass center
        	// and 
        	org.opencv.core.Point intpoint = new org.opencv.core.Point((int) mass.getX(), (int) mass.getY());
        	Core.line(rgbaImage,new org.opencv.core.Point((int) intpoint.x-8,(int) intpoint.y), new org.opencv.core.Point((int) intpoint.x+8,(int) intpoint.y), new Scalar(0,0,255,255),2);
        	Core.line(rgbaImage,new org.opencv.core.Point((int) intpoint.x,(int) intpoint.y-8), new org.opencv.core.Point((int) intpoint.x,(int) intpoint.y+8), new Scalar(0,0,255,255),2);
        	
        	//Core.line
        	//Core.line
        	Core.circle(rgbaImage, intpoint, 5, new Scalar(255,0,0,255));
        	DataFrame newframe = new DataFrame(System.nanoTime(), Main.backthread.frame, intpoint);
        	if (Main.logger.getLogging())
        	{
        		Main.logger.addFrame(newframe);
        	}
        	else
        	{
        		//System.out.println("count: " + mContours.size());
        	}
        	//System.out.println(Main.logger.count());
        	//System.out.println(mc[0].toString());
        	//System.out.println(Main.logger.getCSV());
        //}
        
        //array_
        
        /*if (contours.size() > 1)
        {
        	System.out.println("Too many contours! " + contours.size() + " detected.");
        }
        else
        {
        	System.out.println("Locked on. " + contours.size() + " contour.");
        }*/
        }
    }

    public List<MatOfPoint> getContours() {
        return mContours;
    }
}