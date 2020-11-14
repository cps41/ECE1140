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
        int array[][] = {
            {-2,0,2},//0
            {-1,0,2},//1
            {1,0,2},//2
            {0,0,2},//3
            {-2,0,2},//4
            {2,0,2},//5
            {0,0,2},//6
            {3,0,2},//7
            {-3,0,1},//8
            {-4,0,2},//9
            {4,0,2},//10
            {0,0,2},//11
            {0,0,2},//12
            {5,0,2},//13
            {-5,0,2},//14
            {0,0,2},//15
            {0,0,2},//16
            {-5,0,2},//17
            {-4,0,2},//18
            {0,0,2},//19
            {-3,0,2},//20
            {-1,0,2},//21
        };
        int length = 100;
        int grade = 10;
        int speed_limit = 25;
        //String redDestinations = receive("redDestinations");
        //String greenDestinations = receive("greenDestinations");
        String[] destinations = new String[22];
        for (int i=0;i<22;i++){
            if (i==3){ destinations[i] = "one";}
            else if (i==9){destinations[i]= "two";}
            else if (i==19){destinations[i] = "three";}
            else {destinations[i] = "0";}
        }
        
        int configuration[][] = {
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},     
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit},
            {length,grade,speed_limit}
  
        };
        Object one;
        Object two;
        Object three;
        Object four;
        Object five;
        Object six;
        Object seven;
        Object eight;
        do{
        one = receive("greenDestinations");
        two = receive("redDestinations");
        three = receive("greenTrackSize");
        four = receive("redTrackSize");
        five = receive("greenConnectivity");
        six =  receive ("redConnectivity");
        seven = receive("greenConfiguration");
        eight = receive("redConfiguration");
        System.out.println ("waiting for server");
        }while (one == null || two == null ||three == null ||four == null ||five == null ||six == null ||seven == null ||eight == null );

        int rSize = (int) four;
        int gSize = (int)three;
        String[] redDestinations = (String[]) two;
        String[] greenDestinations = (String[]) one;
        int[][] rconnect = (int[][])six;
        int[][] gconnect = (int[][])five;
        float[][] gconfig = (float[][]) seven;
        float[][] rconfig = (float[][]) eight;

        CTCSystem.GREEN_LINE_CONTROLLER = new RailLineController ("green",gSize,gconnect,gconfig,greenDestinations) ;
        CTCSystem.RED_LINE_CONTROLLER = new RailLineController ("red",rSize,rconnect,rconfig,redDestinations) ;
        /*
        CTCSystem.GREEN_LINE_CONTROLLERS = new RailLineController ( 0,(int)receive("greenTrackSize"),(int[][])receive("greenConnectivity"),(int[][])receive("greenConfiguration"),(String)receive("greenDestinations") );



        */
//     block_number,destination,source,sink1,sink2
        //CTCSystem.RAIL_LINE_CONTROLLERS[0] = new RailLineController ( "green",(int)receive("greenTrackSize"),(int[][])receive("greenConnectivity"),(int[][])receive("greenConfiguration"),(String)receive("greenDestinations") );
        //CTCSystem.RAIL_LINE_CONTROLLERS[1] = new RailLineController ( 1,(int)receive("redTrackSize"),(int[][])receive("redConnectivity"), (int[][])receive("redConfiguration", (String)receive("redDestinations")) );
    }
    public static void update(){
        send("redAuthorities",CTCSystem.RED_LINE_CONTROLLER.AUTHORITIES);
        send("greenAuthorities",CTCSystem.RED_LINE_CONTROLLER.AUTHORITIES);
        send("redTrainControl",CTCSystem.RED_LINE_CONTROLLER.TRAIN_CONTROL);
        send("greenTrainControl",CTCSystem.GREEN_LINE_CONTROLLER.TRAIN_CONTROL);
        //2-d array for each line constantly updated
        //dispatch signals for each line
      //other signals for each line
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

    /*
        int array[][] = {
            {-2,-1},//0
            {-1,-1},//1
            {1,-1},//2
            {0,0},//3
            {-2,-1},//4
            {2,-1},//5
            {0,-1},//6
            {3,-1},//7
            {-3,-1},//8
            {-4,1},//9
            {4,-1},//10
            {0,-1},//11
            {0,-1},//12
            {5,-1},//13
            {-5,-1},//14
            {0,-1},//15
            {0,2},//16
            {-5,-1},//17
            {-4,-1},//18
            {0,2},//19
            {-3,-1},//20
            {-1,-1},//21
        };*/