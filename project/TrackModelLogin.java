package project;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

public class TrackModelLogin extends TrackModel
{
	private static JFrame frame;
	private static JPanel panel;
	private static JLabel title;
	private static JLabel username;
	private static JTextField usernameInput;
	private static JLabel password;
	private static JPasswordField passwordInput;
	private static JButton login;
	
	//main/login screen
	public TrackModelLogin()
	{
		//init
		frame = new JFrame();
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Login");
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		panel = new JPanel();
		panel.setLayout(null);
		frame.add(panel, BorderLayout.CENTER);
		
		//config
		//title
		title = new JLabel("Track Model Login");
		title.setBounds(170,30,250,40);
		title.setFont(new Font("Arial", 0, 30));
		panel.add(title);
		
		//username
		username = new JLabel("Enter Username:");
		username.setBounds(90,130,100,25);
		panel.add(username);
		usernameInput = new JTextField(35);
		usernameInput.setBounds(200,130,300,25);
		panel.add(usernameInput);
		//password
		password = new JLabel("Enter Password:");
		password.setBounds(90,170,100,25);
		panel.add(password);
		passwordInput = new JPasswordField(35);
		passwordInput.setBounds(200,170,300,25);
		panel.add(passwordInput);
		//login button
		login = new JButton("Login");
		login.setBounds(225,240,150,50);
		
		login.addActionListener(e ->
		{
			frame.dispose();
			TrackModelGUI mainDisplay = new TrackModelGUI();
		});
		panel.add(login);
		
		panel.repaint(); //ensure display updates
	}
}
