/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.*;
import project.*;

public class Client {    
    private static final int PORTNUMBER = 0;    
    private static final String cpuIP = "3.131.133.188";    
    private static ServerInterface mini;    
    private static boolean connected = false;

    public static void main(String[] args) throws InterruptedException {
        TrainModelGUI train = new TrainModelGUI();
        send("power", TrainModel.POWER);
        send("authority", TrainModel.AUTHORITY);

        while(train.frame.isShowing()) {	 
            System.out.println("Showing...");       
	        //receive key inputs
	        Object power = receive("power");
            Object authority = receive("authority");
            //send key outputs
            if (authority == null) authority = 0.0f;
            if (power == null) power = 0.0f;
            send("authority", authority);
            send("velocity", (float) power*2);
            //set key inputs
            train.refresh((float) authority, (float) power);
        
            //wait so screen is visible
            Thread.sleep(1000);
        }
        send("authority", null);
        send("velocity", null);
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
    }*/
}
