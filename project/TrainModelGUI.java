package project;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class TrainModelGUI{

	//declare elements
	private TrainModel train;
	public JFrame frame;
	private JPanel panel;
	private JLabel Velocity;
	private JLabel Authority;
	private JLabel Power;
	private JLabel Brakes;
	private JLabel Pass;
	private JLabel Crew;
	private JLabel Length;
	private JLabel Mass;
	private JToggleButton EBrake;
	
	
	public TrainModelGUI(int num_cars) throws InterruptedException {
		train = new TrainModel(num_cars);

		//create elements
		frame = new JFrame();
		panel = new JPanel();
		
		//configure frame
		frame.setSize(1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Train Model");
		frame.setVisible(true);

		//configure panel
		frame.add(panel, "Center");
		GridLayout layout = new GridLayout(3, 3);
		layout.setHgap(5);
		layout.setVgap(5);
		panel.setLayout(layout);
		
		//display key inputs
		Velocity = new JLabel("Train Velocity: " + train.VELOCITY + "km/h");
		panel.add(Velocity);
		Authority = new JLabel("Train Authority: " + train.AUTHORITY + "km");
		panel.add(Authority);
		Power = new JLabel("Train Power Input: " + train.POWER + "kW");
		panel.add(Power);
		Pass = new JLabel("Passengers: " + train.PASSENGER_COUNT);
		panel.add(Pass);
		Crew = new JLabel("Crew Count: " + train.CREW_COUNT);
		panel.add(Crew);
		Length = new JLabel("Length: " + train.LENGTH + "m");
		panel.add(Length);
		Mass = new JLabel("Mass: " + train.MASS + "kg");
		panel.add(Mass);
		EBrake = new JToggleButton("Emergency Brake");
		EBrake.setForeground(Color.RED);
		EBrake.addActionListener(new EBrakeListener());
		panel.add(EBrake);

		String status;
		if (train.BRAKES) status = "on";
		else status = "off";
		Brakes = new JLabel("Train Brake Status: " + status);
		panel.add(Brakes);
	}

	public void refresh(HashMap<String, Object> inputs) {
		System.out.println("Refreshing.....");
		System.out.println("Authority: "+inputs.get(schema.TrackController.authority)+", Power: "+inputs.get(schema.TrainController.power));

		train.AUTHORITY = (float) inputs.get(schema.TrainController.authority);
		train.POWER = (float) inputs.get(schema.TrainController.power);
		train.BRAKES = (boolean) inputs.get(schema.TrainController.brake);
		train.INTERIOR_LIGHTS = (boolean) inputs.get(schema.TrainController.interior_lights);
		train.EXTERIOR_LIGHTS = (boolean) inputs.get(schema.TrainController.exterior_lights);
		train.LEFT_DOORS = (boolean) inputs.get(schema.TrainController.left_doors);
		train.RIGHT_DOORS = (boolean) inputs.get(schema.TrainController.right_doors);

		
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
		Velocity = new JLabel("Train Velocity: " + train.VELOCITY + "km/h");
		panel.add(Velocity);
		Authority = new JLabel("Train Authority: " + train.AUTHORITY + "km");
		panel.add(Authority);
		Power = new JLabel("Train Power Input: " + train.POWER + "kW");
		panel.add(Power);
		Pass = new JLabel("Passengers: " + train.PASSENGER_COUNT);
		panel.add(Pass);
		Crew = new JLabel("Crew Count: " + train.CREW_COUNT);
		panel.add(Crew);
		Length = new JLabel("Length: " + train.LENGTH + "m");
		panel.add(Length);
		Mass = new JLabel("Mass: " + train.MASS + "kg");
		panel.add(Mass);
		EBrake = new JToggleButton("Emergency Brake");
		EBrake.setForeground(Color.RED);
		EBrake.addActionListener(new EBrakeListener());
		panel.add(EBrake);

		String status;
		if (brakes) status = "on";
		else status = "off";
		Brakes = new JLabel("Train Brake Status: " + status);
		panel.add(Brakes);
		panel.setVisible(true);
	}

	private class EBrakeListener implements ActionListener {
		public void actionPerformed(ActionEvent press) {
			train.EBRAKE = true;
		}
	}

	private String doorStatus(boolean status) {
		if(status) return "open";
		else return "closed";
	}
}