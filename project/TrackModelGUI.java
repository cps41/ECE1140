package project;

import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Font;

public class TrackModelGUI extends Client
{
	private static JFrame frame;
	private static JPanel panel;
	private static JTable table;
	
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
		
		table = new JTable();
		
		
		
		panel.repaint();
		
	}
}