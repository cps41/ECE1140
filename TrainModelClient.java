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

    private static HashMap<String, Object> inputs = new HashMap<>();
    private static HashMap<String, Object> outputs = new HashMap<>();
    private static boolean signal_failure = false;

    public static void main(String[] args) throws InterruptedException {
        Object num_cars = receive(schema.TrackModel.cars);
        if (num_cars == null) num_cars = 3;
        TrainModelGUI train = new TrainModelGUI((int) num_cars);

        while(train.frame.isShowing()) {     
        //     //receive key inputs
        //     Object power = new Object(), authority = new Object(), brake = new Object(), speed_limit = new Object(), speed = new Object(), 
        //            interior_lights = new Object(), exterior_lights = new Object(), left_doors = new Object(), right_doors = new Object(), 
        //            passenger_count = new Object(), crew_count = new Object(), block = new Object();

            String authority_queue = schema.TrackModel.authority_queue;
            String block_queue = schema.TrackModel.block_queue;
            String setpoint_speed = schema.TrackModel.setpoint_speed;
            String pass_count = schema.TrackModel.pass_count;
            String crew_count = schema.TrackModel.crew_count;
            String beacon = schema.TrackModel.beacon;
            String cars = schema.TrackModel.cars;
            String power = schema.TrainController.power;
            String interior_lights = schema.TrainController.interior_lights;
            String exterior_lights = schema.TrainController.exterior_lights;
            String left_doors = schema.TrainController.left_doors;
            String right_doors = schema.TrainController.right_doors;
            
            // receive values for all keys
            // check if null
            // if so, go into failure mode
            putFloat(setpoint_speed, receive(setpoint_speed));
            putFloat(power, receive(power));
            putInt(pass_count, receive(pass_count));
            putInt(crew_count, receive(crew_count));
            putInt(cars, receive(cars));
            putBool(interior_lights, receive(interior_lights));
            putBool(exterior_lights, receive(exterior_lights));
            putBool(left_doors, receive(left_doors));
            putBool(right_doors, receive(right_doors));
            putString(beacon, receive(beacon));

            //set key outputs
            outputs = train.refresh(inputs);
            Iterator outputs_iterator = outputs.entrySet().iterator();
            while(outputs_iterator.hasNext()) {
                Map.Entry element = (Map.Entry) outputs_iterator.next();
                send((String)element.getKey(), element.getValue());
            }
        
            //wait so screen is visible
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
    public static void putInt(String key, Object input) {
        if(input != null) inputs.put(key, input);
        else inputs.put(key, 0);
    }

    public static void putFloat(String key, Object input) {
        if(input != null) inputs.put(key, input);
        else inputs.put(key, 0f);
    }

    public static void putString(String key, Object input) {
        if(input != null) inputs.put(key, input);
        else inputs.put(key, "");
    }

    public static void putBool(String key, Object input) {
        if(input != null) inputs.put(key, input);
        else inputs.put(key, true);
    }
}
