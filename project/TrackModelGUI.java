package project;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;

public class TrackModelGUI 
{
	private static JFrame frame;
	private static JPanel panel;
	
	
	public TrackModelGUI()
	{
		frame = new JFrame();
		frame.setSize(900,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Track Model");
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		panel = new JPanel();
		panel.setLayout(null);
		frame.add(panel, BorderLayout.CENTER);
		
	}
}
