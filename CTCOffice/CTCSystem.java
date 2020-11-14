import java.util.ArrayList;

public class CTCSystem {
   
    public static int SIZE = 2; 
    public static RailLine RED_LINE
    public static RailLine GREEN_LINE;
    public static Frame FRAME;
    //signals
    public static final int SUCCESSFULL_DISPATCH = 1;
    public static final int TRACK_TRAFFIC = 2;
    public static final int INVALID_TIME = 3;
    //main
    public static void main(String[] args) {
        configureSystem();
        FRAME = new Frame();
        while (FRAME.isShowing()){Client.update();}
    }
    //configurators
    private static void configureSystem(){
        Client.configure();
    }

}















/*
    public static Frame frame; 
    public static ArrayList<Integer> AL_NUMBER_0F_BLOCKS = new ArrayList<Integer>();
    public static ArrayList< ArrayList<Integer> > ALAL_SWITCH_LOCATIONS = new ArrayList< ArrayList<Integer> >();
    public static ArrayList< ArrayList<Integer[]> > ALAL_SWITCH_SOURCE_SINKS = new ArrayList< ArrayList<Integer[]> >();
    public static ArrayList< ArrayList<Boolean> > ALAL_CROSSINGS = new ArrayList< ArrayList<Boolean> >();
    public static ArrayList< ArrayList<Integer> > ALAL_DESTINATIONS_LOCATIONS = new ArrayList< ArrayList<Integer> >();
    public static int BLOCK_LENGTH = 100;
    public static int NUMBER_OF_LINES;
    public static String[] LINE_NAMES = {"blue","red","green","orange","yellow"};
    public static ArrayList<String> AL_LINE_NAMES = new ArrayList<String>();
    public static ArrayList<ArrayList<String> > AL_LINE_DESTINATIONS = new ArrayList< ArrayList<String> >();
    public static ArrayList<RailLineController> AL_LINE_CONTROLLERS = new ArrayList<RailLineController>();
    public static HashMap<String,Integer> HM_LINE_ASSIGNMENTS = new HashMap<String,Integer>();
    public static
    
    
            //ArrayList Names
        AL_LINE_NAMES.add("blue");  //i=0
        AL_LINE_NAMES.add("red");  //i=1
        AL_LINE_NAMES.add("green");  //i=2
        //
        NUMBER_OF_LINES = AL_LINE_NAMES.size();
        //ArrayList destinations
        String[] blueArrayDest = {"1","2","3","4"};
        String[] redArrayDest = {"one","two","three","four"};
        String[] greenArrayDest = {"green1","green2","green3","green4"};
        ArrayList<String> blueDest = new ArrayList<String>();
        ArrayList<String> redDest = new ArrayList<String>();
        ArrayList<String> greenDest = new ArrayList<String>();
        for (int i=0;i<4;i++){
            blueDest.add(blueArrayDest[i]);
            redDest.add(redArrayDest[i]);
            greenDest.add(greenArrayDest[i]);
        }
        AL_LINE_DESTINATIONS.add(blueDest);
        AL_LINE_DESTINATIONS.add(redDest);
        AL_LINE_DESTINATIONS.add(greenDest);
        //ArrayList LineControllers and HashMap Line Assignments
        for (int i=0;i<NUMBER_OF_LINES;i++){
            AL_LINE_CONTROLLERS.add(new RailLineController(i));
            HM_LINE_ASSIGNMENTS.put(AL_LINE_NAMES.get(i),i);*/