import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import javax.swing.*;
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
	
	public String savePath = "/home/silbernetic/Desktop/tests/";
	public String fullPath = "";
	
	Box theBox = Box.createVerticalBox();
	
	
	JLabel header = new JLabel("JavaGaits v0.01");
	JLabel subheader = new JLabel ("Aaron Silber, Creative Machines Lab");
	
	JTextField testName = new JTextField("untitled");
	
	JButton dumpData = new JButton("Save Data");
	
	JButton startLog = new JButton("Start Logging");
	
	JButton stopLog = new JButton("Stop Logging");
	
	JButton startCamera = new JButton("Start Camera");
	
	JButton calibrate = new JButton("Capture Calibration");
	
	JButton test = new JButton("test");
	
	String[] choices = {"0","1","2"};
	
	Component spacingStrut = Box.createVerticalStrut(20);
	
	public JComboBox<String> CameraChoice = new JComboBox<String>(choices);
	
	SettingsFrame(String title)
	{
		super(title);
		TITLE=title;
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		theBox.add(spacingStrut);
		theBox.add(Box.createVerticalGlue());
		theBox.add(Box.createVerticalStrut(20));
		header.setFont(new Font("Sans", Font.BOLD, 30));
		subheader.setFont(new Font("Sans", Font.PLAIN, 10));
		theBox.add(header);
		theBox.add(subheader);
		theBox.add(Box.createVerticalStrut(20));
		
		CameraChoice.setSelectedIndex(0);
		
		theBox.add(testName);
		theBox.add(new JLabel("Camera Device"));
		theBox.add(CameraChoice);
		theBox.add(spacingStrut);
		
		startCamera.addActionListener(new ActionListener() {
			 
			@Override
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
            	if (!Files.exists(java.nio.file.FileSystems.getDefault().getPath(savePath + "/" + testName.getText() + "/"), LinkOption.NOFOLLOW_LINKS))
				{
					System.out.println("no project dir yet! creating...");
					try {
						Files.createDirectory(java.nio.file.FileSystems.getDefault().getPath(savePath + "/" + testName.getText() + "/"));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else
				{
					Object[] options = {"Yes", "NO"};
					int n = JOptionPane.showOptionDialog(Main.settings,
						    "Test named '" + testName.getText() + "' exists."
						    + "\r\nOverwrite this data?",
						    "Overwrite existing data?",
						    JOptionPane.YES_NO_OPTION,
						    JOptionPane.WARNING_MESSAGE,
						    null, options, options[1]);
					if (n != JOptionPane.YES_OPTION)
					{
						testName.selectAll();
						testName.requestFocus();
						return;
					}
				}
				fullPath = savePath + "/" + testName.getText() + "/";
				if (!Main.backthread.thread.isAlive())
				{
					Main.backthread.thread.start(); // Start the thread
				}
			   Main.frame1.setVisible(true);  
			    Main.frame2.setVisible(true);  
            }
        });
		
		calibrate.addActionListener(new ActionListener() {
			 
			@Override
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
				Calibration.calibrate();
            }
        });
		
		theBox.add(startCamera);
		theBox.add(spacingStrut);
		theBox.add(calibrate);
		theBox.add(spacingStrut);
		
		CameraChoice.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		        
		    }
		});
		/*
		hue.addChangeListener(huelisten);
		sat.addChangeListener(satlisten);
		val.addChangeListener(satlisten);*/ //commented out because causing random data errors (synchronicity)
		
		/*theBox.add(huelabel);
		theBox.add(hue);
		theBox.add(satlabel);
		theBox.add(sat);
		theBox.add(vallabel);
		theBox.add(val);*/
		
		dumpData.addActionListener(new ActionListener() {
			 
			@Override
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                PrintStream out = null;
				try {
					String name = Main.settings.fullPath + "data.csv";
				    System.out.println("data saved to " + name);
	                out = new PrintStream(new FileOutputStream(name));
	                out.print(Main.logger.getCSV());
				} catch (IOException e2) {
				    
				} finally {
                    if (out != null) out.close();
				}
            }
        });   
		
		startLog.addActionListener(new ActionListener() {
			 
			@Override
            public void actionPerformed(ActionEvent e)
            {
				if (!Calibration.calibrationPerformed)
				{
					Object[] options = {"Yes", "NO"};
					int n = JOptionPane.showOptionDialog(Main.settings,
							"A calibration image has not been captured."
									+ "\r\nProceed to begin logging?",
									"No Calibration Captured",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.WARNING_MESSAGE,
									null, options, options[1]);
					if (n == JOptionPane.NO_OPTION)
					{
						return;
					}
				}
                //Execute when button is pressed
                Main.logger.setLogging(true);
                System.out.println("Logging has begun");
                return;
            }
        });
		
		theBox.add(startLog);
		theBox.add(spacingStrut);
		stopLog.addActionListener(new ActionListener() {
			 
			@Override
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                Main.logger.setLogging(false);
                System.out.println("Logging has ceased");
            }
        });
		
		theBox.add(stopLog);
		theBox.add(spacingStrut);
		theBox.add(dumpData);
		theBox.add(spacingStrut);
		theBox.add(Box.createVerticalGlue());
		
		this.add(theBox);
		for (Component component : theBox.getComponents())
		{
				JComponent temp = (JComponent) component;
				temp.setAlignmentX(CENTER_ALIGNMENT);
		}
		this.pack();
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
	/*void updateSliders(Scalar hsv)
	{
		hue.setValue((int) Math.round(hsv.val[0]));
		sat.setValue((int) Math.round(hsv.val[1]));
		val.setValue((int) Math.round(hsv.val[2]));
	}*/
}
