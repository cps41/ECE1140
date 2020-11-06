package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class TrainModelGUI extends TrainModel {

	//declare elements
	public static JFrame frame;
	private static JPanel panel;
	private static JLabel VelocityPanel;
	private static JLabel AuthorityPanel;
	private static JLabel PowerPanel;
	private static JLabel BreaksPanel;
	private static JLabel PassCountPanel;
	private static JLabel 
	
	public TrainModelGUI() throws InterruptedException {
		
		//create elements
		frame = new JFrame();
		panel = new JPanel();
		
		//configure frame
		frame.setSize(1200, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Train Model");
		frame.setVisible(true);

		//configure panel
		frame.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		//display key inputs
		VelocityPanel = new JLabel("Train Velocity: " + super.VELOCITY);
		VelocityPanel.setBounds(500, 100, 200, 100);
		panel.add(VelocityPanel);
		AuthorityPanel = new JLabel("Train Authority: " + super.AUTHORITY);
		AuthorityPanel.setBounds(500,200,200,100);
		panel.add(AuthorityPanel);
		PowerPanel = new JLabel("Train Power Input: " + super.POWER);
		PowerPanel.setBounds(500,300,200,100);
		panel.add(PowerPanel);

		String status;
		if (super.BREAKS) status = "on";
		else status = "off";
		BreaksPanel = new JLabel("Train Break Status: " + status);
		BreaksPanel.setBounds(500,400,200,100);
		panel.add(BreaksPanel);
	}

	public static void refresh(float authority, float power, boolean breaks) {
		System.out.println("Refreshing.....");
		System.out.println("Authority: "+authority+", Power: "+power);
		//create elements
		frame.remove(panel);
		panel = new JPanel();
		
		//configure frame
		frame.setSize(1200, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Train Model");
		frame.setVisible(true);

		//configure panel
		frame.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		panel.removeAll();
		
		//display key inputs
		VelocityPanel = new JLabel("Train Velocity: " + power*2);
		VelocityPanel.setBounds(500, 100, 200, 100);
		panel.add(VelocityPanel);
		AuthorityPanel = new JLabel("Train Authority: " + authority);
		AuthorityPanel.setBounds(500,200,200,100);
		panel.add(AuthorityPanel);
		PowerPanel = new JLabel("Train Power Input: " + power);
		PowerPanel.setBounds(500,300,200,100);
		panel.add(PowerPanel);

		String status;
		if (breaks) status = "on";
		else status = "off";
		BreaksPanel = new JLabel("Train Break Status: " + status);
		BreaksPanel.setBounds(500,400,200,100);
		panel.add(BreaksPanel);
		panel.setVisible(true);
	}
}