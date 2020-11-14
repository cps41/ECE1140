/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
//import javax.xml.namespace.QName;

public class Client {
    private static final int PORTNUMBER = 0;
    private static final String cpuIP = "18.216.251.172";
    private static ServerInterface mini;
    private static boolean connected = false;

    public static void configure(){
        do{
        Object one = receive("greenConfigurationInt");
        Object two = receive("redConfigurationInt");
        Object three = receive("greenConfigurationFloat");
        Object four = receive("redConfigurationFloat");
        Object five = receive("greenConfigurationString");
        Object six =  receive ("redConfigurationString");
        Object seven = receive("greenSize");
        Object eight = receive("redSize");
        System.out.println ("waiting for server");
        }while (one == null || two == null ||three == null ||four == null ||five == null ||six == null ||seven == null ||eight == null );
        try{
            int[][] greenConfigurationInt = (int[][]) one;
            int[][] redConfigurationInt = (int[][]) two;
            float[][] greenConfigurationFloat = (float[][]) three;
            float[][] redConfigurationFloat = (float[][]) four;
            String[][] greenConfigurationString = (String[][]) five;
            String[][] redConfigurationString = (String[][]) six;
            int greenSize = (int) seven;
            int redSize = (int) eight;
        }catch(Exception e){
            System.out.println("casting issue, check server values");
        }
        CTCSystem.GREEN_LINE = new RailLine (greenSize,greenConfigurationInt,greenConfigurationFloat,greenConfigurationString);
        CTCSystem.RED_LINE = new RailLine (redSize,redConfigurationInt,redConfigurationFloat,redConfigurationString);
    }
    public static void update(){

    }
    public static boolean send(String key, Object value) {
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
    public static Object receive(String key) {
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