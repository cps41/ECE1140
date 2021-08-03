/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class ClientCTC {
    private static final int PORTNUMBER = 0;
    private static final String cpuIP = "18.219.117.25";
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
        send("TimeFactor",Clock.timeFactor);
        send("CTCOfficeGreenMaintenance",CTCSystem.GREEN_MODEL.maintenance);
        send("CTCOfficeRedMaintenance",CTCSystem.RED_MODEL.maintenance);
        try{
            if (receive ("TrackControllerGreenStatus") != null && receive ("TrackControllerGreenPosition") != null && receive ("TrackControllerRedStatus") != null && receive ("TrackControllerRedPosition") != null && receive("TrainModelTrainInfo") != null &&  receive("greenPassengers") != null &&  receive("redPassengers") != null){
                CTCSystem.GREEN_MODEL.status = (boolean[][]) receive ("TrackControllerGreenStatus");
                CTCSystem.GREEN_MODEL.switchPosition = (Boolean[]) receive ("TrackControllerGreenPosition");
                CTCSystem.RED_MODEL.status = (boolean[][]) receive ("TrackControllerRedStatus");
                CTCSystem.RED_MODEL.switchPosition = (Boolean[]) receive ("TrackControllerRedPosition");
                CTCSystem.TRAINS_INFO = ( ArrayList< ArrayList<Object> > ) receive("TrainModelTrainInfo"); //passenger count - TrainModelTrainInfo - 3rd index is passenger count, ArrayList<ArrayList<object>> , 4th index = true means train in yard, 8th index is which line it is on (true is green)
                CTCSystem.GREEN_MODEL.passengerCount = (int) receive("greenPassengers");
                CTCSystem.RED_MODEL.passengerCount = (int) receive("redPassengers");
            }
            send("CTCOfficeGreenPosition",CTCSystem.GREEN_MODEL.switchPositionLocal);
            send("CTCOfficeRedPosition",CTCSystem.RED_MODEL.switchPositionLocal);
        }
        catch(Exception e){
            System.out.println("Receiving incorrect server data types");
        }
        if (CTCSystem.TRAINS.size() != 0){
            send("CTCOfficeTrainNodes",CTCSystem.TRAINS);
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
            CTCSystem.errorMessage("Failed to make a server send call");
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
            CTCSystem.errorMessage("Failed to make a server receive call");
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
