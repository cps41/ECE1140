
/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

import javax.swing.*;
import java.awt.*;
import project.*;

public class TrainModelClient {    
    private static final int PORTNUMBER = 0;    
    private static final String cpuIP = "18.216.251.172";    
    private static ServerInterface mini;    
    private static boolean connected = false;
    private static ArrayList<Object> base = new ArrayList<>();

    private static ArrayList<TrainModelGUI> TRAINS = new ArrayList<>();
    private static HashMap<String, Object> inputs;
    private static HashMap<String, Object> outputs;

    private static JFrame frame;
    private static JTabbedPane Tabs;
    private static int num = 1;

    @SuppressWarnings("unchecked")
    public static void main (String[] args) throws InterruptedException {
        frame = new JFrame();
		frame.setSize(1000, 800);
		GridLayout frame_layout = new GridLayout(1, 2);
		frame.setLayout(frame_layout);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Train Model");
        frame.setVisible(true);
        
        Tabs = new JTabbedPane();
        frame.add(Tabs);

        base.add(0f); // power
        base.add(true); // left doors
        base.add(true); // right doors
        base.add(70f); // temp
        base.add(0); // block num
        base.add(new Float[] {1f,1f,1f}); // block info
        base.add(false); // int lights
        base.add(false); // ext lights
        base.add(true); // brake
        base.add(false); // running
        
        
        while(true) {
            inputs = new HashMap<>();
            outputs = new HashMap<>();
            ArrayList<ArrayList<Object>> train_info = null;
            String trains = schema.TrackModel.trains;
            String red_pass = schema.TrackModel.red_pass;
            String green_pass = schema.TrackModel.green_pass;
            String red_beacon = schema.TrackModel.red_beacon;
            String green_beacon = schema.TrackModel.green_beacon;
            String train_nodes = schema.TrainController.train_nodes;
            String red_status = schema.TrainController.red_status;
            String green_status = schema.TrainController.green_status;
            String red_size = schema.TrackModel.red_size;
            String green_size = schema.TrackModel.green_size;
            String ry = schema.TrainController.ry;
            String gy = schema.TrainController.gy;
            String time = "TimeFactor";
            String rc = schema.TrackModel.rc;
            String gc = schema.TrackModel.gc;

            String[] input_key = {red_pass, green_pass, red_beacon, green_beacon, train_nodes, red_status, green_status, red_size, green_size, ry, gy, time, rc, gc};
            
            // receive values for all keys
            // check if null
            // if so, go into failure mode

            while(true) {
                System.out.println("Searching");
                ArrayList<ArrayList<Object>> trains_obj = (ArrayList<ArrayList<Object>>) receive(trains);
                int[] rp = (int[]) receive(red_pass); 
                int[] gp = (int[]) receive(green_pass); 
                String[] rb = (String[]) receive(red_beacon);
                String[] gb = (String[]) receive(green_beacon);
                if (trains_obj!=null && rp!=null && gp!=null && rb!=null && gb!=null) {
                    for(int i=0; i<200; i++) System.out.println("gp["+i+"]"+gp[i]);
                    for(int i=0; i<200; i++) System.out.println("rp["+i+"]"+rp[i]);
                    checkForNewTrains(trains_obj, rp, gp, rb, gb);
                    inputs.put(trains, trains_obj);
                    inputs.put(red_beacon, rb);
                    inputs.put(green_beacon, gb);
                    break;
                }
                else {
                    Integer[] red_p = new Integer[200], green_p = new Integer[200];
                    for(int i=0; i<200; i++) {
                        red_p[i] = 0;
                        green_p[i] = 0;
                    }
                    boolean[] green_station_array = new boolean[200], red_station_array = new boolean[200];
                    for(int i=0; i<green_station_array.length; i++) {
                        green_station_array[i] = false;
                        red_station_array[i] = false;
                    }
                    train_info = iterateThroughTrains(green_p, red_p, gp, rp, green_station_array, red_station_array);
                    if(train_info != null) outputs.put(schema.TrainModel.train_info, train_info);
                }
            }

            Object input;
            for(String key : input_key) {
                input = receive(key);
                putObj(key, input);
            }

            Integer[] green_passengers = null, red_passengers = null;
            if(inputs.get(green_pass)!=null) {
                int[] track_green_pass = (int[]) inputs.get(green_pass);
                green_passengers = new Integer[track_green_pass.length];
                for(int i=0; i<green_passengers.length; i++) green_passengers[i] = 0;
            }
            if(inputs.get(red_pass)!=null) {
                int[] track_red_pass = (int[]) inputs.get(red_pass);
                red_passengers = new Integer[track_red_pass.length];
                for(int i=0; i<red_passengers.length; i++) red_passengers[i] = 0;
            }

            boolean[] green_station_array = new boolean[green_passengers.length], red_station_array = new boolean[green_passengers.length];
            for(int i=0; i<green_station_array.length; i++) {
                green_station_array[i] = false;
                red_station_array[i] = false;
            }

            // update trains and set outputs
            train_info = iterateThroughTrains(green_passengers, red_passengers, (int[]) inputs.get(green_pass), (int[]) inputs.get(red_pass), green_station_array, red_station_array);

            outputs.put(schema.TrainModel.trains, inputs.get(trains));
            if(train_info != null) outputs.put(schema.TrainModel.train_info, train_info);
            if(inputs.get(red_status)!= null) outputs.put(schema.TrainModel.red_presence, inputs.get(red_status));
            if(inputs.get(green_status)!=null) outputs.put(schema.TrainModel.green_presence, inputs.get(green_status));
            if(inputs.get(red_size)!= null) outputs.put(schema.TrainModel.red_size, inputs.get(red_size));
            if(inputs.get(green_size)!=null) outputs.put(schema.TrainModel.green_size, inputs.get(green_size));
            if(inputs.get(ry)!= null) outputs.put(schema.TrainModel.ry, inputs.get(ry));
            if(inputs.get(gy)!=null) outputs.put(schema.TrainModel.gy, inputs.get(gy));
            if(green_passengers!=null) outputs.put(schema.TrainModel.gp, green_passengers);
            if(red_passengers!=null) outputs.put(schema.TrainModel.rp, red_passengers);
            if(inputs.get(rc)!= null) outputs.put(schema.TrainModel.ry, inputs.get(rc));
            if(inputs.get(gc)!=null) outputs.put(schema.TrainModel.gy, inputs.get(gc));
            if(green_station_array!=null) outputs.put(schema.TrainModel.sag, green_station_array);
            if(red_station_array!=null) outputs.put(schema.TrainModel.sar, red_station_array);


            // send outputs
            Iterator<Map.Entry<String, Object>> outputs_iterator = outputs.entrySet().iterator();
            while(outputs_iterator.hasNext()) {
                Map.Entry element = (Map.Entry) outputs_iterator.next();
                send((String)element.getKey(), element.getValue());
            }

            //wait so screen is visible
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
            System.out.println(e);
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
            System.out.println(e);
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
    public static void putObj(String key, Object input) {
        if(input != null) inputs.put(key, input);
    }

    public static void checkForNewTrains(ArrayList<ArrayList<Object>> trains, int[] rp, int[] gp, String[] red_beacon, String[] green_beacon) throws InterruptedException{
        if(trains.size() > TRAINS.size()) {
            for(int i=trains.size()-1; i>=TRAINS.size(); i--) {
                TrainModelGUI new_train = new TrainModelGUI(trains.get(i), i, rp, gp, red_beacon, green_beacon);
                Tabs.addTab("Train "+num++, new_train.frame);
                TRAINS.add(new_train);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<ArrayList<Object>> iterateThroughTrains(Integer[] green_pass, Integer[] red_pass, int[] gp, int[] rp, boolean[] sag, boolean[] sar) throws InterruptedException {
        // update trains and set outputs
        ArrayList<ArrayList<Object>> train_controller_inputs = (ArrayList<ArrayList<Object>>) inputs.get(schema.TrainController.train_nodes);
        System.out.println(train_controller_inputs);
        int time_factor = 1;
        if(inputs.get("TimeFactor")!=null) time_factor = (int) inputs.get("TimeFactor");
        System.out.println("Time factor: "+time_factor);
        if(train_controller_inputs==null) {
            System.out.println("No train controller info");
            train_controller_inputs = new ArrayList<>();
            for(int i=0; i<TRAINS.size(); i++) train_controller_inputs.add(base);
        }
        
        System.out.println(train_controller_inputs);
        ArrayList<ArrayList<Object>> train_info = new ArrayList<ArrayList<Object>>();
        for(int i=0; i<TRAINS.size(); i++) {
            TrainModelGUI current_train = TRAINS.get(i);
            ArrayList<Object> current_input;
            if(train_controller_inputs.size() <= i) current_input = base;
            else current_input = train_controller_inputs.get(i);
            ArrayList<Object> train_outputs = current_train.refresh(current_input, time_factor, gp, rp);
            train_info.add(train_outputs);
            if(current_train.train.LINE && green_pass!=null) {
                int index = current_train.train.LAST_STATION;
                green_pass[index] = current_train.train.OFF_COUNT;
            }
            if(!current_train.train.LINE && red_pass!=null) {
                int index = current_train.train.LAST_STATION;
                red_pass[index] = current_train.train.OFF_COUNT;
            }

            if(current_train.train.LINE) sag[current_train.train.CURRENT_BLOCK] = current_train.train.ARRIVING;
            else sar[current_train.train.CURRENT_BLOCK] = current_train.train.ARRIVING;

        }
        return train_info;
    }
}
