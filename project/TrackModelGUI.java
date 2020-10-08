package project;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;

public class TrackModelGUI extends TrackModel
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
		
		JLabel speed = new JLabel("Speed: " + super.getSpeed());
		speed.setBounds(100, 50, 100, 25);
		panel.add(speed);
		
		JLabel authority = new JLabel("Authority: " + super.getAuthority());
		authority.setBounds(100,100,100,25);
		panel.add(authority);
		
		for(int i = 1; i <= 15; i++)
		{
			JLabel length = new JLabel("Block" + i + ":  Length:" + super.getBlockLength("Blue",i) + "   Speed Limit:" + super.getSpeedLimit("Blue",i)  + "   Grade:" + super.getBlockGrade("Blue",i));
			length.setBounds(100, 125+25*i, 400, 25);
			panel.add(length);
		}
		
		panel.repaint();
		
	}
}