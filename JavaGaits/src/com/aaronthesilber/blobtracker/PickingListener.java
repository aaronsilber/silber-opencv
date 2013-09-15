package com.aaronthesilber.blobtracker;
import java.awt.Color;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import org.opencv.core.Scalar;

public class PickingListener implements MouseListener {

	public PickingListener()
	{
		//(empty) constructor
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("clicked on " + e.getX() + ", " + e.getY()); //debug

		//determine what color was clicked on: grab frame and get the pixel data
		BufferedImage clone = Main.inpanel.getImage();
		Color color = new Color(clone.getRGB(e.getX(), e.getY()),true);
		float[] hsvf = Color.RGBtoHSB(color.getRed(),color.getGreen(),color.getBlue(),null);
		System.out.println(color.toString()); //debug
		
		//create hsv and rgba equivalents (correct?
		System.out.println("hsvf: " + hsvf.toString());
		Scalar hsv = new Scalar(hsvf[0]*180, hsvf[1]*255, hsvf[2]*255);
		Scalar rgba = new Scalar(color.getBlue(),color.getGreen(),color.getRed());
		
		System.out.println("HSV selected: " + hsv.toString()); //debug
		
		//set this as the new color
		Main.backthread.setBlobColor(rgba, hsv);
		
		//we don't use sliders any more
		//Main.settings.updateSliders(hsv);
	}

	/*
	 * these functions must be overridden
	 * even if they are completely empty
	 * what a bummer
	 * :P
	 */
	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }

}
