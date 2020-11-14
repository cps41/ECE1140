/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import project.*;

public class test {    
    private static final int PORTNUMBER = 0;    
    private static final String cpuIP = "18.216.251.172";    
    private static ServerInterface mini;    
    private static boolean connected = false;    
    private static TrainModel train = new TrainModel(3);
    public static void main(String[] args) {        
        int count = 0;
        while(count<1001) {       
            send(schema.TrackModel.authority, 1023.0f);
            send(schema.TrainController.power, 18.3f);       
            System.out.println(count);
            System.out.printf("Server %s = %s\n", "Authority", receive(schema.TrackModel.authority));
            count++;
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