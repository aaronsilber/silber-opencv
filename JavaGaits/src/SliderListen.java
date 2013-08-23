/*
 * NOTE!!
 * We don't use sliders any more.
 * 
 * Too problematic.
 * 
 * This class is obsolete.
 * 
 */

import java.awt.Color;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Scalar;


public class SliderListen implements ChangeListener {
public static enum roles {hue,sat,val};
roles role;
	public SliderListen(roles role) {
		this.role=role;
	}

	public void addTarget()
	{
		//updateTarget = target;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
	    JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) {
	        int value = (int)source.getValue();
	        	//updateTarget.val[0] = value;
	       System.out.println(e.toString());
	        //get current hsv and update only the field being changed
	        //get cur hsv
	        Scalar copy = Main.backthread.getHsvBlobColor();
	        Color col;
	        	switch(role)
	        	{
	        	case hue:
	        		System.out.println("hue slide val " + value);
	        		copy.val[0] = value;
	        		col = Color.getHSBColor((float) copy.val[0], (float) copy.val[1],(float)  copy.val[2]);
	        		Main.backthread.setBlobColor(copy, new Scalar(col.getBlue(),col.getGreen(),col.getRed()));
	        		System.out.println(copy.toString());
	        		break;
	        	case sat:
	        		System.out.println("sat slide val " + value);
	        		copy.val[1] = value;
	        		col = Color.getHSBColor((float) copy.val[0], (float) copy.val[1],(float)  copy.val[2]);
	        		Main.backthread.setBlobColor(copy, new Scalar(col.getBlue(),col.getGreen(),col.getRed()));
	        		System.out.println(copy.toString());
	        		break;
	        	case val:
	        		System.out.println("val slide val " + value);
	        		copy.val[2] = value;
	        		col = Color.getHSBColor((float) copy.val[0], (float) copy.val[1],(float)  copy.val[2]);
	        		Main.backthread.setBlobColor(copy, new Scalar(col.getBlue(),col.getGreen(),col.getRed()));
	        		System.out.println(copy.toString());
	        		break;
	        	}
	        }
	    }
	}
