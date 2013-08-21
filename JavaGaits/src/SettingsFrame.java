import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

import org.opencv.core.Scalar;

public class SettingsFrame extends JFrame {
	String TITLE = "OpenCV Tuning Panel";
	JLabel huelabel = new JLabel("hue");
	JSlider hue = new JSlider(JSlider.HORIZONTAL, 0, 180, 90);
	SliderListen huelisten = new SliderListen(SliderListen.roles.hue);
	JLabel satlabel = new JLabel("sat");
	JSlider sat = new JSlider(JSlider.HORIZONTAL, 0, 255, 90);
	SliderListen satlisten = new SliderListen(SliderListen.roles.sat);
	JLabel vallabel = new JLabel("val");
	JSlider val = new JSlider(JSlider.HORIZONTAL, 0, 255, 90);
	SliderListen vallisten = new SliderListen(SliderListen.roles.val);
	
	JButton dumpCSV = new JButton("Dump CSV");
	
	JButton startLog = new JButton("Start");
	
	JButton test = new JButton("test");
	SettingsFrame(String title)
	{
		super(title);
		TITLE=title;
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
		//this.getContentPane().setLayout(null);
		//this.add(hue);
		
		/*
		hue.addChangeListener(huelisten);
		sat.addChangeListener(satlisten);
		val.addChangeListener(satlisten);*/ //commented out because causing random data errors (synchronicity)
		
		this.getContentPane().add(huelabel);
		this.getContentPane().add(hue);
		this.getContentPane().add(satlabel);
		this.getContentPane().add(sat);
		this.getContentPane().add(vallabel);
		this.getContentPane().add(val);
		
		dumpCSV.addActionListener(new ActionListener() {
			 
			@Override
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                System.out.println(Main.logger.getCSV());
            }
        });   
		
		startLog.addActionListener(new ActionListener() {
			 
			@Override
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                Main.logger.setLogging(true);
                System.out.println("Logging has begun");
            }
        });
		
		this.getContentPane().add(dumpCSV);
		this.getContentPane().add(startLog);
		
		pack();
		//this.getContentPane().add(hue);
	}
	void updateSliders(Scalar hsv)
	{
		hue.setValue((int) Math.round(hsv.val[0]));
		sat.setValue((int) Math.round(hsv.val[1]));
		val.setValue((int) Math.round(hsv.val[2]));
	}
}
