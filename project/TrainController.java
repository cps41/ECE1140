package project;

import java.util.*;


public class TrainController {
	public String KEY;
	public static float POWER_COMMAND = 0.0f;
	public static float VELOCITY = 0.0f; //velocity from train model
	public static float V = 0.0f; //previous velocity for authority calculation
	public static float VELOCITY_ERROR = 0.0f; //commanded_velocity - velocity
	public static float VELOCITY_COMMAND = 0.0f; //from CTC at dispatch
	public static float COMMANDED_SPEED = 0.0f; //current velocity goal
	public static float SETPOINT_SPEED = 0.0f; //from Driver
	public static float SPEED_LIMIT = 0.0f;
	public static int AUTHORITY; //distance until train has to stop
	public static float BLOCK_LENGTH = 0;
	public static int RED_LENGTH;
	public static int GREEN_LENGTH;
	public static int BLOCK_NUMBER = 0;
	public static float DISTANCE = 0.0f;
	public static float Kp = 1.0f; //proportional gain
	public static float Ki = 0.5f; //integral gain
	public static boolean BRAKE = false; //brakes off if false
	public static boolean EMERGENCY_BRAKE = false; //brakes off if false
	public static boolean LEFT_DOORS = true; //doors closed if true
	public static boolean RIGHT_DOORS = true; //doors closed if true
	public static boolean INTERIOR_LIGHTS = true; //lights on if true
	public static boolean EXTERIOR_LIGHTS = true; //lights on if true
	public static boolean MODE = false; // true if in manual mode
	public static float TEMPERATURE = 70.0f;
	public static float POWER_MAXIMUM = 120.0f; //KiloWatts
	public static float CONTROL = 0.0f; //u_k used to calculate power command
	public static String STATION_NAME = "Station Name";
	public static boolean LEFT_DOOR_SIDE; //true if side should open at station
	public static boolean RIGHT_DOOR_SIDE; //true if side should open at station
	public static Boolean[]  RED_STATUS; //first array is train presence
	public static Boolean[] GREEN_STATUS; //first array is train presence
	public static boolean RED_TO_YARD = false;
	public static boolean GREEN_TO_YARD = false;
	public static boolean LINE; //0 = red, 1 = green
	public static Queue<Float[]> CONTROL_QUEUE = new LinkedList<Float[]>();
	public static Queue<Integer> AUTHORITY_QUEUE = new LinkedList<Integer>();
	public static Float[] CONTROL_ARRAY; //values from train control queue
	public static boolean DONE = false; //true when train reaches yard
	
	public TrainController(String key) {
		KEY = key;
	}
	
	//set values
	public void setPowerCommand(float data) {
		POWER_COMMAND = data;
		return;
	}
	
	public void setVelocity(float data) {
		VELOCITY = data;
		if(data < 0.0f) {
			VELOCITY = 0.0f;
		}
		return;
	}
	
	public void setV(float data) {
		V = data;
		return;
	}
	
	public void setVelocityCommand(float data) {
			if(MODE == false) {
				COMMANDED_SPEED = data;
				VELOCITY_COMMAND = data;
			}
			else if(MODE == true){
				VELOCITY_COMMAND = data;
			}
		return;
	}
	
	public void setSpeedLimit(float data) {
		SPEED_LIMIT = data;
		//check for speeds exceeding speed limit
		if(COMMANDED_SPEED > SPEED_LIMIT) {
			COMMANDED_SPEED = SPEED_LIMIT;
		}
		return;
		
	}
	
	public void setSetpointSpeed(float data) {
		SETPOINT_SPEED = data;
		if(SETPOINT_SPEED <= SPEED_LIMIT) {
			COMMANDED_SPEED = SETPOINT_SPEED;
		}
		else if(SETPOINT_SPEED > SPEED_LIMIT) {
			COMMANDED_SPEED = SPEED_LIMIT;
		}
		return;
	}
	
	public void setAuthority(int data) {
		AUTHORITY = data;
		
		//check for authority of 0
		if(AUTHORITY == 0) {
			COMMANDED_SPEED = 0.0f;
		}
		return;
	}
	
	public void setAuthorityQueue(Queue<Integer> data) {
		AUTHORITY_QUEUE = data;
		return;
	}
	
	public void setControlQueue(Queue<Float[]> data) {
		CONTROL_QUEUE = data;
		return;
	}
	
	public void setLine(boolean data) {
		LINE = data;
	}
	
	public void setBlockLengths(int red, int green) {
		RED_LENGTH = red;
		//RED_STATUS = new boolean[RED_LENGTH];
		RED_STATUS = new Boolean[200];
		Arrays.fill(RED_STATUS,  false);
		GREEN_LENGTH = green;
		//GREEN_STATUS = new boolean[GREEN_LENGTH];
		GREEN_STATUS = new Boolean[200];
		Arrays.fill(GREEN_STATUS,  false);
	}
	
	public void setKp(float data) {
		Kp = data;
		return;
	}
	
	public void setKi(float data) {
		Ki = data;
		return;
	}
	
	public void setLeftDoorSide(boolean data) {
		LEFT_DOOR_SIDE = data;
		return;
	}
	
	public void setRightDoorSide(boolean data) {
		RIGHT_DOOR_SIDE = data;
		return;
	}
	
	public void setInteriorLights(boolean data) {
		if(MODE == true) {
			INTERIOR_LIGHTS = data;
		}
		return;
	}
	
	public void setExteriorLights(boolean data) {
		if(MODE == true) {
			EXTERIOR_LIGHTS = data;
		}
		return;
	}
	
	public void setRightDoors(boolean data) {
		if((MODE == true) && (VELOCITY == 0.0f)) {
			RIGHT_DOORS = data;
		}
		return;
	}
	
	public void setLeftDoors(boolean data) {
		if((MODE == true) && (VELOCITY == 0.0f)) {
			LEFT_DOORS = data;
		}
		return;
	}
	
	public void setStationName(String data) {
		STATION_NAME = data;
		return;
	}
	
	public void setBrake(boolean data) {
		BRAKE = data;
		if(BRAKE == true) {
			COMMANDED_SPEED = 0.0f;
		}
		return;
	}
	
	public void setEmergencyBrake(boolean data) {
		EMERGENCY_BRAKE = data;
		return;
	}
	
	//get values
	public float getPowerCommand() {
		return POWER_COMMAND;
	}
	
	public float getCommandedSpeed() {
		return COMMANDED_SPEED;
	}
	
	public float getVelocity() {
		return VELOCITY;
	}
	
	public int getAuthority() {
		return AUTHORITY;
	}
	
	public double getKp() {
		return Kp;
	}
	
	public double getKi() {
		return Ki;
	}
	
	public boolean getInteriorLights() {
		return INTERIOR_LIGHTS;
	}
	
	public boolean getExteriorLights() {
		return EXTERIOR_LIGHTS;
	}
	
	public boolean getRightDoors() {
		return RIGHT_DOORS;
	}
	
	public boolean getLeftDoors() {
		return LEFT_DOORS;
	}
	
	//calculations
	public void updatePowerCommand() {
		float Verror = VELOCITY_ERROR;
		
		System.out.print("\ncommanded speed " + Float.toString(COMMANDED_SPEED));
		if(VELOCITY > COMMANDED_SPEED) {
			VELOCITY = COMMANDED_SPEED;
		}
		
		System.out.print("\nvelocity" + Float.toString(VELOCITY));
		VELOCITY_ERROR = COMMANDED_SPEED - VELOCITY;
		System.out.print("\nvelocity error" + Float.toString(VELOCITY_ERROR));
		float powerCommandAlt = (Kp * VELOCITY_ERROR)+(Ki * CONTROL);
		CONTROL = CONTROL + 0.125f * (VELOCITY_ERROR + Verror);
		
		POWER_COMMAND = (Kp * VELOCITY_ERROR)+(Ki * CONTROL);
		if(POWER_COMMAND > POWER_MAXIMUM) {
			POWER_COMMAND = powerCommandAlt;
			POWER_COMMAND = POWER_MAXIMUM;
		}
		if(POWER_COMMAND < 0) {
			POWER_COMMAND = 0.0f;
		}
		
		System.out.print("\npower command " + Float.toString(POWER_COMMAND));
		
		return;
	}
	
	public void updateAuthority() {
		if(COMMANDED_SPEED > 0.0f) {
			if(DISTANCE == 0.0f) {
				
				//when returning to yard
				CONTROL_ARRAY = CONTROL_QUEUE.poll();
				if(CONTROL_QUEUE.peek() == null) {
					if(LINE == false) RED_TO_YARD = true;
					if(LINE == true) GREEN_TO_YARD = true;
				}
				else {
					RED_TO_YARD = false;
					GREEN_TO_YARD = false;
				}
				
				if(CONTROL_ARRAY != null) {
					SPEED_LIMIT = (float) CONTROL_ARRAY[1];
					BLOCK_LENGTH = (float) CONTROL_ARRAY[2];
					float number = (float) CONTROL_ARRAY[3];
					BLOCK_NUMBER = (int) number;
					if((BLOCK_LENGTH < 0.0f) || (SPEED_LIMIT < 0.0f)) {
						CONTROL_ARRAY = CONTROL_QUEUE.poll();
						SPEED_LIMIT = (float) CONTROL_ARRAY[1];
						BLOCK_LENGTH = (float) CONTROL_ARRAY[2];
						number = (float) CONTROL_ARRAY[3];
						BLOCK_NUMBER = (int) number;
					}
				}
				System.out.print("\n\nNEW BLOCK NUMBER\n\n" + Integer.toString(BLOCK_NUMBER));
				if(LINE == false) RED_STATUS[BLOCK_NUMBER] = true;
				if(LINE == true) GREEN_STATUS[BLOCK_NUMBER] = true;
				
				//check velocity against speed limit
				if((SPEED_LIMIT > 0.0f) && (VELOCITY > SPEED_LIMIT)) {
					VELOCITY = SPEED_LIMIT;
				}
				System.out.print("\nBLOCK_LENGTH: " + Float.toString(BLOCK_LENGTH));
			}
			
			DISTANCE = DISTANCE + (0.125f * ( (VELOCITY + V)/3.6f) );
			System.out.print("\nDISTANCE: " + Float.toString(DISTANCE));
			if(DISTANCE >= BLOCK_LENGTH) {
				DISTANCE = 0.0f;
				AUTHORITY = AUTHORITY - 1;
				System.out.print("\nAUTHORITY" + Integer.toString(AUTHORITY));

				if(LINE == false) {
					RED_STATUS[BLOCK_NUMBER] = false;
				}
				if(LINE == true) {
					GREEN_STATUS[BLOCK_NUMBER] = false;
				}
			}
			
			System.out.print("\nBLOCK NUMBER\n" + Integer.toString(BLOCK_NUMBER));

			
			if(AUTHORITY == 0) {
				if(AUTHORITY_QUEUE.peek() == null) {
					DONE = true;
					return;
				}
				else {
					AUTHORITY = AUTHORITY_QUEUE.poll();
					System.out.print("\nAUTHORITY" + Integer.toString(AUTHORITY));
				}
			}
			
			if(VELOCITY == 0.0f) {
				if(LEFT_DOOR_SIDE = true) {
					LEFT_DOORS = false;
				}
				if(RIGHT_DOOR_SIDE = true) {
					RIGHT_DOORS = false;
				}
			}
			else if(VELOCITY != 0.0f) {
				LEFT_DOORS = true;
				RIGHT_DOORS = true;
			}
		}
		else {
			if(VELOCITY == 0.0f) {
				BRAKE = false;
			}
			if((BRAKE == false) && (EMERGENCY_BRAKE == false) && (COMMANDED_SPEED == 0.0f)) {
				COMMANDED_SPEED = VELOCITY_COMMAND;
			}
		}
		return;
	}
	
	//distance x_k = x_k-1 + (T/2)(v_k + v_k-1)
}