/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import javax.swing.*;
import project.*;

public class TrainModelClient {    
    private static final int PORTNUMBER = 0;    
    private static final String cpuIP = "18.216.251.172";    
    private static ServerInterface mini;    
    private static boolean connected = false;

    public static void main(String[] args) throws InterruptedException {
        Object num_cars = receive(schema.TrackModel.cars);
        if (num_cars == null) num_cars = 1;
        TrainModelGUI train = new TrainModelGUI((int) num_cars);
        send("power", 0f);
        send("authority", 0f);

        while(train.frame.isShowing()) {     
            //receive key inputs
            Object power = new Object(), authority = new Object(), brake = new Object(), speed_limit = new Object(), speed = new Object(), 
                   interior_lights = new Object(), exterior_lights = new Object(), left_doors = new Object(), right_doors = new Object(), 
                   passenger_count = new Object(), crew_count = new Object(), block = new Object();

            String[] keys = {schema.TrainController.power, schema.TrackModel.authority, schema.TrackModel.authority, 
                             schema.TrackModel.speed_limit, schema.TrackModel.setpoint_speed, schema.TrainController.interior_lights, 
                             schema.TrainController.exterior_lights, schema.TrainController.left_doors, schema.TrainController.right_doors, 
                             schema.TrackModel.pass_count, schema.TrackModel.crew_count, schema.TrackModel.block, schema.TrackModel.cars};

            HashMap<String, Object> inputs = new HashMap<>();
            
            // receive values for all keys
            // check if null
            // if so, go into failure mode
            for(int i=0; i<keys.length; i++) {
                inputs.put(keys[i], receive(keys[i]));
                if (inputs.get(keys[i]) == null) {
                    authority = 0.0f;
                    power = 0.0f;
                    brake = true;
                }
            }

            //set key inputs
            train.refresh(inputs);


        
            //wait so screen is visible
            Thread.sleep(1000);
        }
    }    
    static boolean send(String key, Object value) {        
        if (!connected) {            
            connectClient();        
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
    /*static void call(String key, Object...argv) {        
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
    ArrayList< Queue<boolean> >      */
    //ArrayList
    //int arr[] 
}
