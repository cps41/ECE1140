package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class trackControllerGUI extends trackController {

	//declare elements
	private static JFrame frame;
	private static JPanel panel;
	
	public trackControllerGUI() {
		
		//create elements
		frame = new JFrame();
		panel = new JPanel();
		
		//configure frame
		frame.setSize(1200, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Login");;
		frame.setVisible(true);

		//configure panel
		frame.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		//display key inputs
		JLabel speed = new JLabel("Train speed from CTC: " + super.speed);
		speed.setBounds(500, 300, 200, 100);
		panel.add(speed);
		JLabel authority = new JLabel("Train authority from CTC: " + super.authority);
		authority.setBounds(500,400,200,100);
		panel.add(authority);
		JLabel brokenRail = new JLabel("Track status from track model: " + super.brokenRail);
		brokenRail.setBounds(500, 500, 300, 100);
		panel.add(brokenRail);
		JLabel trainPresence = new JLabel("Train presence from track model: " + super.trainPresence);
		trainPresence.setBounds(500, 600, 300, 100);
		panel.add(trainPresence);
	}
}