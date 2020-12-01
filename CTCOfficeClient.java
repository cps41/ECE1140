/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CTCOfficeClient {
    private static final int PORTNUMBER = 0;
    private static final String cpuIP = "18.216.251.172";
    private static ServerInterface mini;
    private static boolean connected = false;

    public static void configure(){
        Object one;
        Object two;
        Object three;
        Object four;
        Object five;
        Object six;
        Object seven;
        Object eight;
        do{
            one = receive("greenConfigurationInt");
            two = receive("redConfigurationInt");
            three = receive("greenConfigurationFloat");
            four = receive("redConfigurationFloat");
            five = receive("greenConfigurationString");
            six =  receive ("redConfigurationString");
            seven = receive("greenTrackSize");
            eight = receive("redTrackSize");
            System.out.println ("waiting for server");
        }while (one == null || two == null ||three == null ||four == null ||five == null ||six == null ||seven == null ||eight == null );
        try{
            CTCSystem.GREEN_MODEL.configurationInt = (int[][]) one;
            CTCSystem.RED_MODEL.configurationInt = (int[][]) two;
            CTCSystem.GREEN_MODEL.configurationFloat = (float[][]) three;
            CTCSystem.RED_MODEL.configurationFloat = (float[][]) four;
            CTCSystem.GREEN_MODEL.configurationString = (String[][]) five;
            CTCSystem.RED_MODEL.configurationString = (String[][]) six;
            CTCSystem.GREEN_MODEL.SIZE = (Integer) seven;
            CTCSystem.RED_MODEL.SIZE = (Integer) eight;
        }
        catch(Exception e){
            System.out.println("Server recieved incorrect data types");
            System.exit(0);
        }
    }
    public static void update(){
        if (CTCSystem.TRAINS.size() != 0){
            send("CTCOfficeTrainNodes",CTCSystem.TRAINS);
            try{
                CTCSystem.GREEN_MODEL.presence = (Boolean[][]) receive("TrackControllerGreenStatus");
                System.out.println(1);
                CTCSystem.GREEN_MODEL.presence = (Boolean[][]) receive("TrackControllerRedStatus");
                System.out.println(2);
                CTCSystem.RED_MODEL.switchPosition = (Boolean[]) receive("redPosition");
                System.out.println(3);
                CTCSystem.GREEN_MODEL.switchPosition = (Boolean[]) receive ("greenPosition");
                System.out.println(4);
            }
            catch(Exception e){
                System.out.println("Receiving incorrect server data types");
            }
        }
    }
    public static boolean send(String key, Object value) {
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