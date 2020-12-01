
/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import javax.swing.*;
import project.*;

public class TrainControllerClient {
    private static final int PORTNUMBER = 0;
    private static final String cpuIP = "18.216.251.172";
    private static ServerInterface mini;
    private static boolean connected = false;
    
    private static ArrayList<TrainControllerGUI> trains = new ArrayList<>();
    private static ArrayList<ArrayList<Object>> controllers = new ArrayList<ArrayList<Object> >();
    private static ArrayList<ArrayList<Object>> models = new ArrayList<ArrayList<Object> >();
    private static ArrayList<ArrayList<Object>> nodes = new ArrayList<ArrayList<Object> >();
    private static ArrayList<Object> controller = new ArrayList<Object>();
    
 @SuppressWarnings("unchecked")
public static void main(String[] args) throws InterruptedException {
	 	nodes = (ArrayList<ArrayList<Object> >) receive("TrainModelTrainNodes");
	 	models = (ArrayList<ArrayList<Object> >) receive("TrainModelTrainInfo");
	 	
	 	//print out for testing
	 	if(nodes != null) System.out.print("\nNODE" + nodes.toString());
	 	if(models!= null) System.out.print("\nMODEL" + models.toString());
	 	
	 	//check for null
	 	while((nodes == null)) {
		 	nodes = (ArrayList<ArrayList<Object> >) receive("TrainModelTrains");
	 	}
	 	System.out.print("\nNODE" + nodes.toString());
	 	
	 	while((models == null)) {
	 		models = (ArrayList<ArrayList<Object> >) receive("TrainModelTrainInfo");
	 	}
	 	System.out.print("\nMODEL" + models.toString());
	 	
	 	ArrayList<Object> model = models.get(0);
	 	ArrayList<Object> node = nodes.get(0);
        boolean line = (boolean) node.get(4);
        
        int redLength = (int) receive("TrainModelRedSize");
        int greenLength = (int) receive("TrainModelGreenSize");
	 	
    	//instantiation
	 	Queue<Integer> rAuthorities = (Queue<Integer>) node.get(0);
	 	Queue<Float[]> rTrainControl = (Queue<Float[]>) node.get(1);
		LinkedList<Float[]> redtrainControl = (LinkedList<Float[]>) rTrainControl;
	    
		int velocityCommandReceive = (int) node.get(2);
        float velocityCommand = (float) velocityCommandReceive;
		
		TrainControllerGUI gui = new TrainControllerGUI("key", (LinkedList<Integer>) rAuthorities, (LinkedList<Float[]>) redtrainControl, 
	    								line, redLength, greenLength, velocityCommand);
        
		//add elements to TrainControllerNode
	    controller.add(0, 0);
	    controller.add(1, 0);
	    controller.add(2, 0);
	    controller.add(3, 0);
	    controller.add(4, 0);
	    controller.add(5, 0);
	    controller.add(6, 0);
	    controller.add(7, 0);
	    controller.add(8, 0);
	    while(gui.frame.isShowing()) {
	        //receive key inputs
		 	models = (ArrayList<ArrayList<Object> >) receive("TrainModelTrainInfo");
		 	//check for null values due to lag
		 	while((models == null)) {
		 		models = (ArrayList<ArrayList<Object> >) receive("TrainModelTrainInfo");
		 		if(models!= null) System.out.print("\nMODEL" + models.toString());
		 	}
		 	model = models.get(0);
	        System.out.print("\nmodel:" + model.toString());
	        double velocityReceive = (double) model.get(2);
	        System.out.print("\nvelocity received double" + Double.toString(velocityReceive));
	        float velocity = (float) velocityReceive;
	        System.out.print("\nvelocity received " + Float.toString(velocity));
	        String stationName = (String) model.get(1);
	        boolean eBrake = (boolean) model.get(0);

	        
	        //check for null
	        //if (velocity == null) velocity = 0.0f;
	        //if(velocityCommand == null) velocityCommand = 0.0f;
	        //if (stationName == null) stationName = "Station Name";
	        //if(eBrake == null) eBrake = false;
	        
	       gui.refresh((float) velocity, (String) stationName, (boolean) eBrake,
	    		    (LinkedList<Integer>) rAuthorities, redtrainControl, line);
	        
	      //send key outputs
	      controller.set(0, TrainController.POWER_COMMAND);
	      controller.set(1, TrainController.LEFT_DOORS);
	      controller.set(2, TrainController.RIGHT_DOORS);
	      controller.set(3, TrainController.TEMPERATURE);
	      controller.set(4, TrainController.BLOCK_NUMBER);
	      controller.set(5, TrainController.CONTROL_ARRAY);
	      controller.set(6, TrainController.INTERIOR_LIGHTS);
	      controller.set(7, TrainController.EXTERIOR_LIGHTS);
	      controller.set(8, TrainController.BRAKE);
	      controllers.add(0, controller);
	      send("TrainControllerTrainNodes", controllers);
	      if( (boolean) node.get(4) == false) {
	    	  send("TrainControllerRedStatus", TrainController.RED_STATUS);
	      }
	      if( (boolean) node.get(4) == true) {
	    	  System.out.print("\n63 Status" + Boolean.toString(TrainController.GREEN_STATUS[63]));
	    	  System.out.print("\n64 Status" + Boolean.toString(TrainController.GREEN_STATUS[64]));
	    	  System.out.print("\n65 Status" + Boolean.toString(TrainController.GREEN_STATUS[65]));
	    	  send("TrainControllerGreenStatus", TrainController.GREEN_STATUS);
	      }
	        
	      //to yard
	      if(TrainController.RED_TO_YARD == true) {
	    	  send("TrainControllerRedToYard", TrainController.RED_TO_YARD);
	      }
	      if(TrainController.GREEN_TO_YARD == true) {
	    	  send("TrainControllerGreenToYard", TrainController.GREEN_TO_YARD);
	      }
	      
	        Thread.sleep(250);
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