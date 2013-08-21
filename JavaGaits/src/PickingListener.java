import java.awt.Color;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import org.opencv.core.Scalar;

public class PickingListener implements MouseListener {

	public PickingListener() {
		//constructor
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//we wthis is what wa te
		//System.out.println("clikced");
		System.out.println("clicked on " + e.getX() + ", " + e.getY());
		//determine what freaking color that is
		BufferedImage clone = Main.inpanel.getimage();
		Color color = new Color(clone.getRGB(e.getX(), e.getY()),true);
		float[] hsvf = new float[3];
		Color.RGBtoHSB(color.getRed(),color.getGreen(),color.getBlue(),hsvf);
		System.out.println(color.toString());
		//System.out.println("HSV: " + hsv[0]*256 + ", " + hsv[1]*256 + ", " + hsv[2]*256);
		//Scalar hsv = new Scalar(hsvf[0]*256, hsvf[1]*256, hsvf[2]*256);
		Scalar hsv = new Scalar(hsvf[0]*180, hsvf[1]*255, hsvf[2]*255);
		Scalar rgba = new Scalar(color.getBlue(),color.getGreen(),color.getRed());
		System.out.println("HSV selected: " + hsv.toString());
		Main.backthread.setBlobColor(rgba, hsv);
		Main.settings.updateSliders(hsv);
		//ideoThrea
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }

}
