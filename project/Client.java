package project;

/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class Client {    
    private static final int PORTNUMBER = 0;    
    private static final String cpuIP = "3.131.133.188";    
    private static ServerInterface mini;    
    private static boolean connected = false;    
    public static void main(String[] args) {        
        send("key1", "value 1");        
        send("key2", "value 2");        
        send("key3", 500.0);        
        int[] arrVal = new int[]{1, 2, 3};        
        send("key4", arrVal);        
        System.out.printf("Server %s = %s\n", "key1", receive("key1"));        
        System.out.printf("Server %s = %s\n", "key2", receive("key2"));        
        System.out.printf("Server %s = %s\n", "key3", receive("key3"));        
        System.out.printf("Server %s = %s\n", "key4", Arrays.toString((int[]) receive("key4")));    
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