package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


@SuppressWarnings("unchecked")
public class TrainModelGUI{

	//declare elements
	public TrainModel train;
	public JFrame frame;
	private JPanel panel;
	private JLabel Velocity;
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
	private HashMap<String, Object> INPUTS;
	
	
	public TrainModelGUI(int num_cars, String key) throws InterruptedException {
		train = new TrainModel(num_cars, key);

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
		Velocity.setForeground(Color.WHITE);
		Velocity.setBackground(Color.BLUE);
		panel.add(Velocity);

		Power = new JLabel("Train Power Input: " + train.POWER + "kW");
		Power.setForeground(Color.WHITE);
		Power.setBackground(Color.BLUE);
		panel.add(Power);

		Brakes = new JLabel("Train Brake Status: " + otherStatus(train.BRAKES));
		Brakes.setForeground(Color.WHITE);
		Brakes.setBackground(Color.BLUE);
		panel.add(Brakes);

		Pass = new JLabel("Passengers: " + train.PASSENGER_COUNT);
		Pass.setForeground(Color.WHITE);
		Pass.setBackground(Color.BLUE);
		panel.add(Pass);

		Crew = new JLabel("Crew Count: " + train.CREW_COUNT);
		Crew.setForeground(Color.WHITE);
		Crew.setBackground(Color.BLUE);
		panel.add(Crew);

		Length = new JLabel("Length: " + train.LENGTH + "m");
		Length.setForeground(Color.WHITE);
		Length.setBackground(Color.BLUE);
		panel.add(Length);

		Mass = new JLabel("Mass: " + train.MASS + "kg");
		Mass.setForeground(Color.WHITE);
		Mass.setBackground(Color.BLUE);
		panel.add(Mass);

		Interior = new JLabel("Interior Lights: " + otherStatus(train.INTERIOR_LIGHTS));
		Interior.setForeground(Color.WHITE);
		Interior.setBackground(Color.BLUE);
		panel.add(Interior);

		Exterior = new JLabel("Interior Lights: " + otherStatus(train.EXTERIOR_LIGHTS));
		Exterior.setForeground(Color.WHITE);
		Exterior.setBackground(Color.BLUE);
		panel.add(Exterior);

		LeftDoors = new JLabel("Left Doors: " + doorStatus(train.LEFT_DOORS));
		LeftDoors.setForeground(Color.WHITE);
		LeftDoors.setBackground(Color.BLUE);
		panel.add(LeftDoors);

		RightDoors = new JLabel("Right Doors: " + doorStatus(train.RIGHT_DOORS));
		RightDoors.setForeground(Color.WHITE);
		RightDoors.setBackground(Color.BLUE);
		panel.add(RightDoors);

		Temperature = new JLabel("Temperature: " + train.TEMPERATURE +"ºF");
		Temperature.setForeground(Color.WHITE);
		Temperature.setBackground(Color.BLUE);
		panel.add(Temperature);

		EBrake = new JToggleButton("Emergency Brake");
		EBrake.setBackground(Color.RED);
		EBrake.addActionListener(new EBrakeListener());
		panel.add(EBrake);
	}

	public HashMap<String, Object> refresh(HashMap<String, Object> inputs) throws InterruptedException{
		System.out.println("Refreshing.....");
		System.out.println("Authority: "+inputs.get(schema.TrackController.authority)+", Power: "+inputs.get(schema.TrainController.power));

		INPUTS = inputs;
		HashMap<String, Object> outputs = new HashMap<>();

		Iterator<Map.Entry<String, Object>> inputs_iterator = inputs.entrySet().iterator();
		while(inputs_iterator.hasNext()) {
			Map.Entry element = (Map.Entry) inputs_iterator.next();

		}

		//if(inputs.get(schema.TrainController.power) != null) train.POWER = (float) inputs.get(schema.TrainController.power);
		if(inputs.get(schema.TrainController.brake) != null) train.BRAKES = (boolean) getObj(schema.TrainController.brake);
		if(inputs.get(schema.TrainController.interior_lights) != null) train.INTERIOR_LIGHTS = (boolean) getObj(schema.TrainController.interior_lights);
		if(inputs.get(schema.TrainController.exterior_lights) != null) train.EXTERIOR_LIGHTS = (boolean) getObj(schema.TrainController.exterior_lights);
		if(inputs.get(schema.TrainController.left_doors) != null) train.LEFT_DOORS = (boolean) getObj(schema.TrainController.left_doors);
		if(inputs.get(schema.TrainController.right_doors) != null) train.RIGHT_DOORS = (boolean) getObj(schema.TrainController.right_doors);
		if(inputs.get(schema.TrackModel.pass_count) != null) train.PASSENGER_COUNT = (int) getObj(schema.TrackModel.pass_count);
		train.CREW_COUNT = 4;
		System.out.println(train.POWER+"");

		train.updateCars();
		train.updateMass();
		train.decodeBeacon((String) inputs.get(schema.TrackModel.beacon));
		train.getEmergencyBrakeStatus();
		train.setVelocity();
		
		//display key inputs
		Velocity.setText("Train Velocity: " + train.VELOCITY + "km/h");
		Power.setText("Train Power Input: " + train.POWER + "kW");
		Brakes.setText("Train Brake Status: " + otherStatus(train.BRAKES));
		Pass.setText("Passengers: " + train.PASSENGER_COUNT);
		Crew.setText("Crew Count: " + train.CREW_COUNT);
		Length.setText("Length: " + train.LENGTH + "m");
		Mass.setText("Mass: " + train.MASS + "kg");
		Interior.setText("Interior Lights: " + otherStatus(train.INTERIOR_LIGHTS));
		Exterior.setText("Exterior Lights: " + otherStatus(train.EXTERIOR_LIGHTS));
		LeftDoors.setText("Left Doors: " + doorStatus(train.LEFT_DOORS));
		RightDoors.setText("Right Doors: " + doorStatus(train.RIGHT_DOORS));
		Temperature.setText("Temperature: " + train.TEMPERATURE +"ºF");

		outputs.put(schema.TrainModel.velocity, train.VELOCITY);
		outputs.put(schema.TrainModel.speed, inputs.get(schema.TrackModel.setpoint_speed));
		outputs.put(schema.TrainModel.authority_queue, inputs.get(schema.TrackModel.authority_queue));
		outputs.put(schema.TrainModel.block_queue, inputs.get(schema.TrackModel.block_queue));
		outputs.put(schema.TrainModel.emergency_brake, train.EBRAKE);
		outputs.put(schema.TrainModel.station, train.STATION);


		System.out.println("Or here...");
		return outputs;
	}

	private class EBrakeListener implements ActionListener {
		public void actionPerformed(ActionEvent press) {
			AbstractButton abstractButton = (AbstractButton) press.getSource();
			boolean selected = abstractButton.getModel().isSelected(); 
			if(selected) train.EBRAKE = true;
			else train.EBRAKE = false;
		}
	}

	private String doorStatus(boolean status) {
		if(status) return "closed";
		else return "open";
	}

	private String otherStatus(boolean status) {
		if(status) return "on";
		else return "off";
	}

	private Object getObj(String key) {
		HashMap<String, Object> hash = (HashMap<String, Object>) INPUTS.get(key);
		return hash.get(train.KEY);
	}
}