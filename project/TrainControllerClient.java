
/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import javax.swing.*;
import project.*;

public class TrainControllerClient {
    private static final int PORTNUMBER = 0;
    private static final String cpuIP = "3.131.133.188";
    private static ServerInterface mini;
    private static boolean connected = false;
    
    
 public static void main(String[] args) throws InterruptedException {
    	
    	//instantiation
	    //TrainController train = new TrainController();
	    TrainControllerGUI gui = new TrainControllerGUI();
	    
	    //send key outputs
        send("brakes", TrainController.BRAKE);
        send("power", TrainController.POWER);
        send("authority", TrainController.AUTHORITY);
	    
	    while(gui.frame.isShowing()) {
	        
	        //receive key inputs
	        //ArrayList<ArrayList<Integer>> trains = (ArrayList<ArrayList<Integer>>) receive("trains");
	        Object velocity = receive("velocity");
	        Object authority = receive("authority");
	        
	        //set key inputs
	        //train.setVelocity(trains.get(1).get(0));
	        //train.setAuthority(trains.get(1).get(1));
	        gui.refresh((float) authority, (float) velocity);
	        
	        
	        //send key outputs
	        if (authority == null) authority = 0.0f;
            if (velocity == null) velocity = 0.0f;
	        send("brakes", 1);
	        send("power", 50);
	        send("authority", 20);
	        Thread.sleep(1000);
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