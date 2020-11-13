/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.io.*;
import project.*;

public class TrackControllerClient {
	private static final boolean CONFIGURABLE = false;
	
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
    //public static List<Queue<Boolean>> redQueue = new ArrayList<Queue<Boolean>>();
    //public static List<Queue<Boolean>> greenQueue = new ArrayList<Queue<Boolean>>();
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
	    	redConnectivity = (int[][])receive("redConnectivity");		
	    	greenConnectivity = (int[][])receive("greenConnectivity");	
    	}
    	else {
    		redTrackSize = 76;
    		greenTrackSize = 150;
    		redConnectivity = new int[redTrackSize][3];
    		greenConnectivity = new int[greenTrackSize][3];
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
    	//redQueue = (List<Queue<Boolean>>)receive("redSwitchQueue");
    	//greenQueue = (List<Queue<Boolean>>receive("greenSwitchQueue");
    	//controller.greenQueue = greenQueue;
    	Object authoritiesQueue = receive("redAuthorities");
    	Object controlQueue = receive("redTrainControl");
    	//controller.redQueue = redQueue;
    	redAuthorities = (LinkedList<Integer>) authoritiesQueue;
    	redTrainControl = (LinkedList<Float>) controlQueue;
    	
    	
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
	    	if (redQueue == null || authoritiesQueue == null || controlQueue == null) {
	    		System.out.println("Waiting for Schedule");
	    		redQueue = (List<Queue<Boolean>>)receive("redSwitchQueue");
	        	authoritiesQueue = receive("redAuthorities");
	        	controlQueue = receive("redTrainControl");
	        	controller.redQueue = redQueue;
	        	redAuthorities = (LinkedList<Integer>) authoritiesQueue;
	        	redTrainControl = (LinkedList<Float>) controlQueue;
	    	}
	    	
	    	//receive inputs
	    	Object redStat = receive("redStatus");
	    	redStatus = (boolean[][])redStat;
	    	Object greenStat = receive("greenStatus");
	    	greenStatus = (boolean[][])greenStat;
	    	
	    	//set inputs
	    	for (int i=0; i<redTrackSize; i++) {
	    		controller.setTrainPresence("red", i, redStatus[i][0]);
	    		controller.setTrackStatus("red", i, redStatus[i][1]);
	    	}
	    	for (int i=0; i<greenTrackSize; i++) {
	    		controller.setTrainPresence("green", i, greenStatus[i][0]);
	    		controller.setTrackStatus("green", i, greenStatus[i][1]);
	    	}
		    	
	        //send outputs
	    	send("TrackControllerredAuthorities", redAuthorities);
	    	send("TrackControllerredTrainControl", redTrainControl);

	    	for (int i=0; i<redTrackSize; i++) {
	    		if (controller.getTrainPresence("red", i)) redPresence[i] = 1;
	    		else redPresence[i] = 0;
	    		if (controller.getSwitchPosition("red", i)) redPosition[i] = 1;
	    		else redPosition[i] = 0;
	    	}
	    	send("redPresence", redPresence);
	    	send("redPosition", redPosition);
	    	for (int i=0; i<greenTrackSize; i++) {
	    		if (controller.getTrainPresence("green", i)) greenPresence[i] = 1;
	    		else greenPresence[i] = 0;
	    		if (controller.getSwitchPosition("green", i)) greenPosition[i] = 1;
	    		else greenPosition[i] = 0;
	    	}
			send("greenPresence", greenPresence);
			send("greenPosition", greenPosition);
			
			//runPLCs
			controllerGUI.run();
			
	    	//check safety;
	    	//checkTrackSafety();
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
	    	send("authorityRedCorrection", 0);
	    	send("authorityGreenCorrection", 0);
	    	
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
    			if (line.equals("red")) send("authorityRedCorrection", source);
    			else if (line.equals("green")) send("authorityGreenCorrection", source);
    		}
    	}
    	//check for train coming from higher section
    	if (controller.getTrainPresence(line, higher)) {
    		if (controller.getTrainPresence(line, source) || controller.getTrainPresence(line, lower)) {
    			if (line.equals("red")) send("authorityRedCorrection", higher);
    			if (line.equals("green")) send("authorityGreenCorrection", higher);
    		}
    		else 
    			controller.setSwitchPosition(line, source, true);
    	}
    	//check for train coming from lower section
    	if (controller.getTrainPresence(line, lower)) {
    		if (controller.getTrainPresence(line, source) || controller.getTrainPresence(line, higher)) {
    			if (line.equals("red")) send("authorityRedCorrection", lower);
    			if (line.equals("green")) send("authorityGreenCorrection", lower);
    		}
    		else 
    			controller.setSwitchPosition(line, source, false);
    	}
    }
    
    public static void checkTrackSafety () { 
    	for (int i=1; i<controller.redLine.length+1; i++) {
    		//change authority if safety train present or broken track
    		if (controller.getTrainPresence("red",i+1) || !controller.getTrackStatus("red",i+1) || controller.getTrainPresence("red",i-1) || !controller.getTrackStatus("red",i-1)) 
    			send("authorityRedCorrection", i);
    		//activate crossing if train nearby
    		if (controller.getCrossingExistence("red",i) && (controller.getTrainPresence("red", i-1) || controller.getTrainPresence("red", i+1)))
    			controller.setCrossingStatus("red", i, true);
    	}
    	for (int i=1; i<controller.greenLine.length+1; i++) {
    		//change authority if safety train present or broken track
    		if (controller.getTrainPresence("green",i+1) || !controller.getTrackStatus("green",i+1) || controller.getTrainPresence("green",i-1) || !controller.getTrackStatus("green",i-1)) 
    			send("authorityGreenCorrection", i);
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


