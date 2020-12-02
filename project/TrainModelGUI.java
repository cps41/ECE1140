package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;


public class TrainModelGUI{

	//declare elements
	public int Key;
	public TrainModel train;
	public JPanel frame;
	public JPanel panel, images;
	private JLabel Velocity, Power, Brakes, Pass, Crew, Length, Mass, Interior, 
				   Exterior, LeftDoors, RightDoors, Temperature, Front, Top, Arriving, Timer;
	private JToggleButton EBrake;
	private Icon intOff = new ImageIcon("img/intOff.png");
	private Icon intOn = new ImageIcon(new File("img/intOn.png").getAbsolutePath());
	private Icon intOffExt = new ImageIcon(new File("img/intOffExt.png").getAbsolutePath());
	private Icon intOnExt = new ImageIcon(new File("img/intOnExt.png").getAbsolutePath());
	private Icon none = new ImageIcon(new File("img/none.png").getAbsolutePath());
	private Icon noneExt = new ImageIcon(new File("img/noneExt.png").getAbsolutePath()); 
	private Icon left = new ImageIcon(new File("img/left_50.png").getAbsolutePath());
	private Icon right = new ImageIcon(new File("img/right.png").getAbsolutePath());
	private Icon both = new ImageIcon(new File("img/both.png").getAbsolutePath());
	private Icon leftExt = new ImageIcon(new File("img/leftExt.png").getAbsolutePath());
	private Icon rightExt = new ImageIcon(new File("img/rightExt.png").getAbsolutePath());
	private Icon bothExt = new ImageIcon(new File("img/bothExt.png").getAbsolutePath());
	
	
	public TrainModelGUI(ArrayList<Object> info, int train_num, int[] rp, int[] gp, String[] red_beacon, String[] green_beacon) {
		System.out.println("Width: "+intOff.getIconWidth());
		train = new TrainModel(info, train_num, rp, gp, red_beacon, green_beacon);
		Key = train_num;

		//create elements
		frame = new JPanel();
		panel = new JPanel();
		images = new JPanel();
		
		//configure frame
		frame.setSize(1000, 800);
		GridLayout frame_layout = new GridLayout(1, 2);
		frame.setLayout(frame_layout);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setTitle("Train Model");
		frame.setVisible(true);

		//configure panel
		frame.add(panel, "Center");
		GridLayout layout = new GridLayout(4, 4);
		layout.setHgap(5);
		layout.setVgap(5);
		panel.setLayout(layout);

		// configure image panel
		frame.add(images, "Center");
		GridLayout img_layout = new GridLayout(2, 1);
		img_layout.setHgap(10);
		img_layout.setVgap(10);
		images.setLayout(img_layout);
		Front = new JLabel(intOff);
		images.add(Front);
		Top = new JLabel(none);
		images.add(Top);
		// panel.add(images);

		//display key inputs
		Velocity = new JLabel("Train Velocity: " + train.VELOCITY + "km/h");
		panel.add(Velocity);

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

		Exterior = new JLabel("Exterior Lights: " + otherStatus(train.EXTERIOR_LIGHTS));
		panel.add(Exterior);

		LeftDoors = new JLabel("Left Doors: " + doorStatus(train.LEFT_DOORS));
		panel.add(LeftDoors);

		RightDoors = new JLabel("Right Doors: " + doorStatus(train.RIGHT_DOORS));
		panel.add(RightDoors);

		Temperature = new JLabel("Temperature: " + train.TEMPERATURE +"ºF");
		panel.add(Temperature);

		Arriving = new JLabel("Not Arriving at Station");
		panel.add(Arriving);

		Timer = new JLabel("");
		panel.add(Timer);

		EBrake = new JToggleButton("Emergency Brake");
		EBrake.addActionListener(new EBrakeListener());
		panel.add(EBrake);
	}

	public ArrayList<Object> refresh(ArrayList<Object> inputs, int time_factor) throws InterruptedException{
		System.out.println("Refreshing.....");

		ArrayList<Object> outputs; // return value of ebrake, station, and velocity

		// update train vals
		if(inputs != null) {
			System.out.println(inputs);
			train.POWER = (float) inputs.get(0);
			train.LEFT_DOORS = (boolean) inputs.get(1);
			train.RIGHT_DOORS = (boolean) inputs.get(2);
			train.TEMPERATURE = (float) inputs.get(3);
			train.CURRENT_BLOCK = (int) inputs.get(4);
			train.BLOCK = (Float[]) inputs.get(5);
			train.INTERIOR_LIGHTS = (boolean) inputs.get(6);
			train.EXTERIOR_LIGHTS = (boolean) inputs.get(7);
			train.BRAKES = (boolean) inputs.get(8);
			train.DONE = (boolean) inputs.get(9);
			train.TIME = time_factor;
		}

		if(train.DONE) {
			// frame.dispose();
			frame.setEnabled(false);
			train.PASSENGER_COUNT = 0;
		}

		train.decodeBeacon();
		train.stopAtStation();
		train.stationArrival();
		train.stationStop();
		train.updateCars();
		train.getEmergencyBrakeStatus();
		train.setVelocity();
		
		//display key inputs
		String v = String.format("%.3f", train.VELOCITY);
		Velocity.setText("Train Velocity: " + v + "km/h");
		String p = String.format("%.3f", train.POWER);
		Power.setText(("Train Power Input: " + p + " kW"));
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

		if(train.EXTERIOR_LIGHTS) {
			if(!train.LEFT_DOORS && !train.RIGHT_DOORS) Top.setIcon(bothExt);
			else if(!train.LEFT_DOORS && train.RIGHT_DOORS) Top.setIcon(leftExt);
			else if(train.LEFT_DOORS && !train.RIGHT_DOORS) Top.setIcon(rightExt);
			else Top.setIcon(noneExt);

			if(train.INTERIOR_LIGHTS) Front.setIcon(intOnExt);
			else Front.setIcon(intOffExt);
		}
		else {
			if(!train.LEFT_DOORS && !train.RIGHT_DOORS) Top.setIcon(both);
			else if(!train.LEFT_DOORS && train.RIGHT_DOORS) Top.setIcon(left);
			else if(train.LEFT_DOORS && !train.RIGHT_DOORS) Top.setIcon(right);
			else Top.setIcon(none);
			
			if(train.INTERIOR_LIGHTS) Front.setIcon(intOn);
			else Front.setIcon(intOff);
		}

		if(train.ARRIVING) {
			Arriving.setText("Arriving at "+train.STATION);
			Timer.setText("Timer: "+train.TIME);
		}
		else {
			Arriving.setText("Not Arriving at Station");
			Timer.setText("");
		}


		outputs = new ArrayList<>();
		outputs.add(train.EBRAKE);
		outputs.add(train.STATION);
		outputs.add(train.VELOCITY);
		outputs.add(train.PASSENGER_COUNT);
		outputs.add(train.DONE);
		System.out.println("Outputs"+outputs.toString());
		return outputs;
	}

	private class EBrakeListener implements ActionListener {
		public void actionPerformed(ActionEvent press) {
			AbstractButton abstractButton = (AbstractButton) press.getSource();
			boolean selected = abstractButton.getModel().isSelected(); 
			if(selected) {
				train.EBRAKE = true;
				train.POWER = 0f;
			}
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
}