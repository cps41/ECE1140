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
	private JLabel Interior;
	private JLabel Exterior;
	private JLabel LeftDoors;
	private JLabel RightDoors;
	private JLabel Temperature;
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
		GridLayout layout = new GridLayout(4, 4);
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

		Brakes = new JLabel("Train Brake Status: " + otherStatus(train.BRAKES));
		panel.add(Brakes);

		Pass = new JLabel("Passengers: " + train.PASSENGER_COUNT);
		panel.add(Pass);

		Crew = new JLabel("Crew Count: " + train.CREW_COUNT);
		panel.add(Crew);

		Length = new JLabel("Length: " + train.LENGTH + "m");
		panel.add(Length);

		Mass = new JLabel("Mass: " + train.MASS + "kg");
		panel.add(Mass);

		Interior = new JLabel("Interior Lights: " + otherStatus(train.INTERIOR_LIGHTS));
		panel.add(Interior);

		Exterior = new JLabel("Interior Lights: " + otherStatus(train.EXTERIOR_LIGHTS));
		panel.add(Exterior);

		LeftDoors = new JLabel("Left Doors: " + doorStatus(train.LEFT_DOORS));
		panel.add(LeftDoors);

		RightDoors = new JLabel("Right Doors: " + doorStatus(train.RIGHT_DOORS));
		panel.add(RightDoors);

		Temperature = new JLabel("Temperature: " + train.TEMPERATURE +"ºF");
		panel.add(Temperature);

		EBrake = new JToggleButton("Emergency Brake");
		EBrake.setForeground(Color.RED);
		EBrake.addActionListener(new EBrakeListener());
		panel.add(EBrake);
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
		train.PASSENGER_COUNT = (int) inputs.get(schema.TrackModel.pass_count);
		train.CREW_COUNT = 4;

		train.updateCars();
		train.updateMass();
		train.getEmergencyBrakeStatus();
		train.setVelocity();
		
		//display key inputs
		Velocity.setText("Train Velocity: " + train.VELOCITY + "km/h");

		Authority.setText("Train Authority: " + train.AUTHORITY + "km");

		Power.setText("Train Power Input: " + train.POWER + "kW");

		Brakes.setText("Train Brake Status: " + otherStatus(train.BRAKES));

		Pass.setText("Passengers: " + train.PASSENGER_COUNT);

		Crew.setText("Crew Count: " + train.CREW_COUNT);

		Length.setText("Length: " + train.LENGTH + "m");

		Mass.setText("Mass: " + train.MASS + "kg");

		Interior.setText("Interior Lights: " + otherStatus(train.INTERIOR_LIGHTS));

		Exterior.setText("Interior Lights: " + otherStatus(train.EXTERIOR_LIGHTS));

		LeftDoors.setText("Left Doors: " + doorStatus(train.LEFT_DOORS));

		RightDoors.setText("Right Doors: " + doorStatus(train.RIGHT_DOORS));

		Temperature.setText("Temperature: " + train.TEMPERATURE +"ºF");
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

	private String otherStatus(boolean status) {
		if(status) return "on";
		else return "off";
	}
}
