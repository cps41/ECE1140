/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.io.*;
import project.*;

public class TrackControllerClient {
	//redToYard
	//greenToYard
	private static final boolean CONFIGURABLE = false;
	private static final boolean FULLSYSTEM = false;
	
    private static final int PORTNUMBER = 0;
    private static final String cpuIP = "18.216.251.172";
    private static ServerInterface mini;
    private static boolean connected = false;
    
    //initialization variables 
    private static Integer redTrackSize;
    private static Integer greenTrackSize;
    private static int[][] redConnectivity;			//0th row = switch, 1st row = crossing
    private static int[][] greenConnectivity;		//2nd row = direction of travel
    public static trackController controller;
    public static loginGUI login;
    public static trackControllerGUI controllerGUI;
    
    //deployment variables
    public static LinkedList<Integer> redAuthorities = new LinkedList<Integer>();
    public static LinkedList<Float> redTrainControl = new LinkedList<Float>();

    
    //monitoring variables
    private static boolean[][] redStatus;				//0th row = train presence
    private static boolean[][] greenStatus;				//1st row = broken track
    private static int[] redPresence;
    private static int[] greenPresence;
    private static int[] redPosition;
    private static int[] greenPosition; 
    
    public static void main(String[] args) {


    	//configure track
    	if (CONFIGURABLE) {
	    	redTrackSize = (Integer)receive("redTrackSize");
	    	greenTrackSize = (Integer) receive("greenTrackSize");
	    	while (redTrackSize == null || greenTrackSize == null)
	    	{
	    		System.out.println("Waiting for track Configurations");
	    		redTrackSize = (Integer)receive("redSize");
	        	greenTrackSize = (Integer) receive("greenSize");
	    	}
		    trackController controller = new trackController(redTrackSize, greenTrackSize);
	    	redConnectivity = (int[][])receive("redConfigurationInt");		
	    	greenConnectivity = (int[][])receive("greenConfigurationInt");	
    	}
    	else {
    		redTrackSize = 79;
    		redConnectivity = new int[redTrackSize][3];
    		redConnectivity[9][0] = 1;
    		redConnectivity[77][0] = -1;
    		redConnectivity[10][0] = -1;
    		redConnectivity[16][0] = 2;
    		redConnectivity[1][0] = -2;
    		redConnectivity[15][0] = -2;
    		redConnectivity[27][0] = 3;
    		redConnectivity[28][0] = -3;
    		redConnectivity[76][0] = -3;
    		redConnectivity[33][0] = 4;
    		redConnectivity[32][0] = -4;
    		redConnectivity[72][0] = -4;
    		redConnectivity[38][0] = 5;
    		redConnectivity[39][0] = -5;
    		redConnectivity[71][0] = -5;
    		redConnectivity[44][0] = 6;
    		redConnectivity[43][0] = -6;
    		redConnectivity[67][0] = -6;
    		redConnectivity[52][0] = 7;
    		redConnectivity[53][0] = -7;
    		redConnectivity[66][0] = -7;
    		redConnectivity[47][1] = 1;
    		redStatus = new boolean[redTrackSize][2];
    		for (int i=0; i<redTrackSize; i++)
    			redStatus[i][1] = true;
    		greenTrackSize = 153;
    		greenConnectivity = new int[greenTrackSize][3];
    		greenConnectivity[13][0] = 1;
    		greenConnectivity[12][0] = -1;
    		greenConnectivity[1][0] = -1;
    		greenConnectivity[29][0] = 2;
    		greenConnectivity[30][0] = -2;
    		greenConnectivity[150][0] = -2;
    		greenConnectivity[57][0] = 3;
    		greenConnectivity[58][0] = -3;
    		greenConnectivity[151][0] = -3;
    		greenConnectivity[63][0] = 4;
    		greenConnectivity[152][0] = -4;
    		greenConnectivity[62][0] = -4;
    		greenConnectivity[77][0] = 5;
    		greenConnectivity[76][0] = -5;
    		greenConnectivity[101][0] = -5;
    		greenConnectivity[85][0] = 6;
    		greenConnectivity[86][0] = -6;
    		greenConnectivity[100][0] = -6;
    		greenConnectivity[19][1] = 1;
    		greenStatus = new boolean[greenTrackSize][2];
    		for (int i=0; i<greenTrackSize; i++)
    			greenStatus[i][1] = true;
    	}
    	
    	//instantiate track
    	redPresence = new int[redTrackSize];
    	greenPresence = new int[greenTrackSize];
    	redPosition = new int[redTrackSize];
    	greenPosition = new int[greenTrackSize];
    	controller = new trackController(redTrackSize, greenTrackSize);
    	
    	//set crossing and switch existence
    	for (int i=0; i<redTrackSize; i++) {
    		boolean switchExistence = false;
    		boolean crossingExistence = false;
    		if (redConnectivity[i][0] > 0) switchExistence = true;
    		controller.setSwitchExistence("red", i, switchExistence);
    		if (redConnectivity[i][1] == 1) crossingExistence = true;
    		controller.setCrossingExistence("red", i, crossingExistence);
    	}
    	for (int i=0; i<greenTrackSize; i++) {
    		boolean switchExistence = false;
    		boolean crossingExistence = false;
    		if (greenConnectivity[i][0] > 0) switchExistence = true;
    		controller.setSwitchExistence("green", i, switchExistence);
    		if (greenConnectivity[i][1] == 1) crossingExistence = true;
    		controller.setCrossingExistence("green", i, crossingExistence);
    	}
    	
    	//set queues
    	if (FULLSYSTEM) {
	    	Object authoritiesQueue = receive("redAuthorities");
	    	Object controlQueue = receive("redTrainControl");
	    	redAuthorities = (LinkedList<Integer>) authoritiesQueue;
	    	redTrainControl = (LinkedList<Float>) controlQueue;
    	}
    	
    	
    	//instantiate GUIs 
	    login = new loginGUI();
	    while (login.frame.isShowing()) {
	    	//prompt user for login credentials
	    	System.out.println("Please enter login credentials to access Wayside Control Center.");
	    	//implement delay and prompt user again if no action is taken
	    	for (int delay=-1000000000; delay<1000000000; delay++)
	    		for (int doubleDelay=-1000000000; doubleDelay<1000000000; doubleDelay++);
	    }
	    System.out.println("Thank You");
	    controllerGUI = new trackControllerGUI();
	    

	    while(true) {
	    	//receive inputs
	    	if (FULLSYSTEM) {
	    		Object authoritiesQueue = receive("redAuthorities");
	        	Object controlQueue = receive("redTrainControl");
		    	Object redStat = receive("redStatus");
		    	Object greenStat = receive("greenStatus");
		    	redAuthorities = (LinkedList<Integer>) authoritiesQueue;
	        	redTrainControl = (LinkedList<Float>) controlQueue;
	        	redStatus = (boolean[][])redStat;
		    	greenStatus = (boolean[][])greenStat;
	    	}
	    	
	    	//set variables
	    	for (int i=0; i<redTrackSize; i++) {
	    		controller.setTrainPresence("red", i, redStatus[i][0]);
	    		controller.setTrackStatus("red", i, redStatus[i][1]);
	    		if (controller.getTrainPresence("red", i)) redPresence[i] = 1;
	    		else redPresence[i] = 0;
	    		if (controller.getSwitchPosition("red", i)) redPosition[i] = 1;
	    		else redPosition[i] = 0;
	    	}
	    	for (int i=0; i<greenTrackSize; i++) {
	    		controller.setTrainPresence("green", i, greenStatus[i][0]);
	    		controller.setTrackStatus("green", i, greenStatus[i][1]);
	    		if (controller.getTrainPresence("green", i)) greenPresence[i] = 1;
	    		else greenPresence[i] = 0;
	    		if (controller.getSwitchPosition("green", i)) greenPosition[i] = 1;
	    		else greenPosition[i] = 0;
	    	}
		    	
	        //send outputs
	    	if (FULLSYSTEM) {
		    	send("TrackControllerredAuthorities", redAuthorities);
		    	send("TrackControllerredTrainControl", redTrainControl);
		    	send("redPresence", redPresence);
		    	send("redPosition", redPosition);
				send("greenPresence", greenPresence);
				send("greenPosition", greenPosition);
				send("authorityRedCorrection", 0);
		    	send("authorityGreenCorrection", 0);
	    	}
			
			//runPLCs
			controllerGUI.run();
			
	    	//check safety;
	    	checkTrackSafety();
	    	int source=0, lower=0;
	    	for (int i=0; i<redTrackSize; i++) {
	    		if (redConnectivity[i][0] > 0) {
	    			source = i;
	    			for (int j=0; j<redTrackSize; j++) {
	    				if (redConnectivity[j][0] == -source) {
	    					lower = j;
	    					break;
	    				}
	    			}
	    			for (int j=lower+1; j<redTrackSize; j++) {
	    				if (redConnectivity[j][0] == -source) {
	    					checkSwitchSafety("red", source, j, lower);
	    					break;
	    				}
	    			}
	    		}
	    	}
	    	for (int i=0; i<greenTrackSize; i++) {
	    		if (greenConnectivity[i][0] > 0) {
	    			source = i;
	    			for (int j=0; j<greenTrackSize; j++) {
	    				if (greenConnectivity[j][0] == -source) {
	    					lower = j;
	    					break;
	    				}
	    			}
	    			for (int j=lower+1; j<greenTrackSize; j++) {
	    				if (greenConnectivity[j][0] == -source) {
	    					checkSwitchSafety("green", source, j, lower);
	    					break;
	    				}
	    			}
	    		}
	    	}
	    	
	    	
	    	/*checkSwitchSafety("red",16,15,1);
	    	checkSwitchSafety("red",27,76,28);
	    	checkSwitchSafety("red",33,72,32);
	    	checkSwitchSafety("red",38,71,39);
	    	checkSwitchSafety("red",44,67,43);
	    	checkSwitchSafety("red",52,66,53);
	    	checkSwitchSafety("green",13,12,1);
	    	checkSwitchSafety("green",29,150,30);
	    	checkSwitchSafety("green",57,58,0);
	    	checkSwitchSafety("green",63,62,0);
	    	checkSwitchSafety("green",77,101,76);
	    	checkSwitchSafety("green",85,100,86);
	    	*/
    	}   
    }
    
    public static void checkSwitchSafety (String line, int source, int higher, int lower) {
    	//check for train coming from source
    	if (controller.getTrainPresence(line, source)) {
    		//train on higher not lower section
    		if (controller.getTrainPresence(line, higher) && !controller.getTrainPresence(line, lower))
    			controller.setSwitchPosition(line, source, false);
    		//train on lower not higher section
    		else if (!controller.getTrainPresence(line, higher) && controller.getTrainPresence(line, lower))
    			controller.setSwitchPosition(line, source, true);
    		//train on both sections
    		else if (controller.getTrainPresence(line, higher) && controller.getTrainPresence(line, lower)) {
    			if (line.equals("red") && FULLSYSTEM) send("authorityRedCorrection", source);
    			else if (line.equals("green") && FULLSYSTEM) send("authorityGreenCorrection", source);
    		}
    	}
    	//check for train coming from higher section
    	if (controller.getTrainPresence(line, higher)) {
    		if (controller.getTrainPresence(line, source) || controller.getTrainPresence(line, lower)) {
    			if (line.equals("red") && FULLSYSTEM) send("authorityRedCorrection", higher);
    			if (line.equals("green") && FULLSYSTEM) send("authorityGreenCorrection", higher);
    		}
    		else 
    			controller.setSwitchPosition(line, source, true);
    	}
    	//check for train coming from lower section
    	if (controller.getTrainPresence(line, lower)) {
    		if (controller.getTrainPresence(line, source) || controller.getTrainPresence(line, higher)) {
    			if (line.equals("red") && FULLSYSTEM) send("authorityRedCorrection", lower);
    			if (line.equals("green") && FULLSYSTEM) send("authorityGreenCorrection", lower);
    		}
    		else 
    			controller.setSwitchPosition(line, source, false);
    	}
    }
    
    public static void checkTrackSafety () { 
    	for (int i=1; i<controller.redLine.length+1; i++) {
    		//change authority if safety train present or broken track
    		if (controller.getTrainPresence("red",i+1) || !controller.getTrackStatus("red",i+1) || controller.getTrainPresence("red",i-1) || !controller.getTrackStatus("red",i-1)) 
    			if (FULLSYSTEM) send("authorityRedCorrection", i);
    		//activate crossing if train nearby
    		if (controller.getCrossingExistence("red",i) && (controller.getTrainPresence("red", i-1) || controller.getTrainPresence("red", i+1)))
    			controller.setCrossingStatus("red", i, true);
    	}
    	for (int i=1; i<controller.greenLine.length+1; i++) {
    		//change authority if safety train present or broken track
    		if (controller.getTrainPresence("green",i+1) || !controller.getTrackStatus("green",i+1) || controller.getTrainPresence("green",i-1) || !controller.getTrackStatus("green",i-1)) 
    			if (FULLSYSTEM) send("authorityGreenCorrection", i);
    		//activate crossing if train nearby
    		if (controller.getCrossingExistence("green",i) && (controller.getTrainPresence("green", i-1) || controller.getTrainPresence("green", i+1)))
    			controller.setCrossingStatus("green", i, true);
    	}
    }
    
    static boolean send(String key, Object value) {
        if (!connected) {            connectClient();
        }
        try {
            mini.serverSend(key, value);
            return true;
        } catch (Exception e) {
            System.out.printf("Failed to set %s to %s\n", key, value);
            return false;
        }
    }
    static Object receive(String key) {
        if (!connected) {
            connectClient();
        }
        try {
            return mini.serverReceive(key);
        } catch (Exception e) {
            System.out.printf("Failed to receive %s\n", key);
        }
        return null;
    }
    private static void connectClient() {
        if (!connected) {
            try {
                Registry registry = LocateRegistry.getRegistry(cpuIP, PORTNUMBER);
                mini = (ServerInterface) registry.lookup("ServerInterface");
                connected = true;
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }
    static void call(String key, Object...argv) {
        if (!connected) {
            connectClient();
        }
        try {
            mini.serverCall(key, argv);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
    static Object[] getCall(String key) {
        if (!connected) {
            connectClient();
        }
        try {
            return mini.serverGetCall(key);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }
}


