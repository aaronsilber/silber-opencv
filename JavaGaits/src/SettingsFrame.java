import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Scalar;

public class SettingsFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9002704047255549825L;

	String TITLE = "OpenCV Tuning Panel";
	/*JLabel huelabel = new JLabel("hue");
	JSlider hue = new JSlider(JSlider.HORIZONTAL, 0, 180, 90);
	SliderListen huelisten = new SliderListen(SliderListen.roles.hue);
	JLabel satlabel = new JLabel("sat");
	JSlider sat = new JSlider(JSlider.HORIZONTAL, 0, 255, 90);
	SliderListen satlisten = new SliderListen(SliderListen.roles.sat);
	JLabel vallabel = new JLabel("val");
	JSlider val = new JSlider(JSlider.HORIZONTAL, 0, 255, 90);
	SliderListen vallisten = new SliderListen(SliderListen.roles.val);*/
	
	JButton dumpCSV = new JButton("Dump CSV");
	
	JButton startLog = new JButton("Start Logging");
	
	JButton startCamera = new JButton("Start Camera");
	
	JButton test = new JButton("test");
	
	String[] choices = {"0","1","2"};
	
	public JComboBox<String> CameraChoice = new JComboBox<String>(choices);
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
		 * String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };

//Create the combo box, select item at index 4.
//Indices start at 0, so 4 specifies the pig.
JComboBox petList = new JComboBox(petStrings);
petList.setSelectedIndex(4);
petList.addActionListener(this);
		 */
		CameraChoice.setSelectedIndex(0);
		this.getContentPane().add(new JLabel("Camera Device"));
		this.getContentPane().add(CameraChoice);
		
		//this.getContentPane().add(new JLabel("Camera Device"));
		startCamera.addActionListener(new ActionListener() {
			 
			@Override
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                //Main.logger.setLogging(true);
                //System.out.println("Starting camera");
				if (!Main.backthread.thread.isAlive())
				{
			    Main.backthread.thread.start(); // Start the thread
				}
			   Main.frame1.setVisible(true);  
			    Main.frame2.setVisible(true);  
            }
        });
		
		this.getContentPane().add(startCamera);
		
		CameraChoice.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		        //changeDevice(getDevice());
		    }
		});
		/*
		hue.addChangeListener(huelisten);
		sat.addChangeListener(satlisten);
		val.addChangeListener(satlisten);*/ //commented out because causing random data errors (synchronicity)
		
		/*this.getContentPane().add(huelabel);
		this.getContentPane().add(hue);
		this.getContentPane().add(satlabel);
		this.getContentPane().add(sat);
		this.getContentPane().add(vallabel);
		this.getContentPane().add(val);*/
		
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
	void changeDevice(int index)
	{
		System.out.println("switching device to " + index);
		Main.backthread.swapDevice(index);
	}
	int getDevice()
	{
		return Integer.parseInt(CameraChoice.getSelectedItem().toString());
	}
	void updateSliders(Scalar hsv)
	{
		/*hue.setValue((int) Math.round(hsv.val[0]));
		sat.setValue((int) Math.round(hsv.val[1]));
		val.setValue((int) Math.round(hsv.val[2]));*/
	}
}
