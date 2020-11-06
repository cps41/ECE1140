/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.io.*;
import project.*;

public class Client {
    private static final int PORTNUMBER = 0;
    private static final String cpuIP = "3.137.206.179";
    private static ServerInterface mini;
    private static boolean connected = false;
    
    private static int redTrackSize;
    private static int greenTrackSize;
    private static int[][] redConnectivity;			//0th row = switch
    private static int[][] greenConnectivity;		//1st row = crossing
    public static trackController controller;
    public static loginGUI login;
    public static trackControllerGUI controllerGUI;
    private static int[][] redStatus;				//0th row = train presence
    private static int[][] greenStatus;				//1st row = broken track
    private static int speed;
    private static int authority;
    private static int[] redPresence;
    private static int[] greenPresence;
    
    public static void main(String[] args) {

    	//instantiate track
    	/*redTrackSize = (int)receive("redSize");
    	greenTrackSize = (int) receive("greenSize");
    	while (redTrackSize == null || greenTrackSize == null)
    	{
    		redTrackSize = (int)receive("redSize");
        	greenTrackSize = (int) receive("greenSize");
    	}*/
	    //trackController controller = new trackController(redTrackSize, greenTrackSize);
    	//redConnectivity = (int[][])receive("redConnectivity");		
    	//greenConnectivity = (int[][])receive("greenConnectivity");	
    	//test call
    	controller = new trackController(100, 90);
    	
    	//set crossing and switch existence
    	/*for (int i=1; i<redTrackSize; i++) {
    		boolean switchExistence = false;
    		boolean crossingExistence = false;
    		if (redConnectivity[i][0] > 0) switchExistence = true;
    		controller.setSwitchExistence("red", i, switchExistence);
    		if (redConnectivity[i][1] == 1) crossingExistence = true;
    		controller.setCrossingExistence("red", i, crossingExistence);
    	}
    	for (int i=1; i<greenTrackSize; i++) {
    		boolean switchExistence = false;
    		boolean crossingExistence = false;
    		if (greenConnectivity[i][0] > 0) switchExistence = true;
    		controller.setSwitchExistence("green", i, switchExistence);
    		if (greenConnectivity[i][1] == 1) crossingExistence = true;
    		controller.setCrossingExistence("green", i, crossingExistence);
    	}*/
    	
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
	    	/*speed = (int)receive("CTCspeed");
	    	authority = (int)receive("CTCauthority");
	    	redStatus = (int[][])receive("redStatus");
	    	greenStatus = (int[][])receive("greenStatus");
	    	//recieve 2D array for switch queue
	    	
	    	//set inputs
	    	for (int i=1; i<redTrackSize; i++) {
	    		boolean presence = false;
	    		boolean notBroken = false;
	    		if (redStatus[i][0] == 1) presence = true;
	    		controller.setTrainPresence("red", i, presence);
	    		if (redStatus[i][1] == 1) notBroken = true;
	    		controller.setTrackStatus("red", i, notBroken);
	    	}
	    	for (int i=1; i<greenTrackSize; i++) {
	    		boolean presence = false;
	    		boolean notBroken = false;
	    		if (greenStatus[i][0] == 1) presence = true;
	    		controller.setTrainPresence("green", i, presence);
	    		if (greenStatus[i][1] == 1) notBroken = true;
	    		controller.setTrackStatus("green", i, notBroken);
	    	}
		    	
	        //send outputs
	        if (speed <= 40) send("trackControllerSpeed", speed);
	        else send("trackControllerSpeed", 40);
	        send("trackControllerAuthority", authority);
	    	for (int i=1; i<redTrackSize; i++) {
	    		if (controller.getTrainPresence("red", i)) redPresence[i] = 1;
	    		else redPresence[i] = 0;
	    	}
	    	send("redPresence", redPresence);
	    	for (int i=1; i<greenTrackSize; i++) {
	    		if (controller.getTrainPresence("green", i)) greenPresence[i] = 1;
	    		else greenPresence[i] = 0;
	    	}
			send("greenPresence", greenPresence);
	    	
	    	//check safety;
	    	//change once we decide how we are passing switches
	    	checkTrackSafety();
	    	checkSwitchSafety("red",16,15,1);
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
    			controller.setSwitchPosition(line, source, true);
    		//train on lower not higher section
    		else if (!controller.getTrainPresence(line, higher) && controller.getTrainPresence(line, lower))
    			controller.setSwitchPosition(line, source, false);
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
    	for (int i=1; i<controller.redLine.length; i++) {
    		//change authority if safety train present or broken track
    		if (controller.getTrainPresence("red",i+1) || !controller.getTrackStatus("red",i+1) || controller.getTrainPresence("red",i-1) || !controller.getTrackStatus("red",i-1)) 
    			send("authorityRedCorrection", i);
    		//activate crossing if train nearby
    		if (controller.getCrossingExistence("red",i) && (controller.getTrainPresence("red", i-1) || controller.getTrainPresence("red", i+1)))
    			controller.setCrossingStatus("red", i, true);
    	}
    	for (int i=1; i<controller.greenLine.length; i++) {
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


