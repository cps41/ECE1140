import java.nio.charset.StandardCharsets; 
import java.nio.file.*; 
import java.io.*; 
import java.util.*;   
public class Model{
    public int configurationInt[][] = null;
    public float configurationFloat[][] = null;
    public String configurationString[][] = null;
    public Integer SIZE = null;
    public ArrayList<Block> path = new ArrayList<Block>();
    public Block[] BLOCKS;
    public ArrayList<String> DESTINATIONS = new ArrayList<String>();
    public boolean[][] status;
    public Boolean[] switchPosition;
    public Boolean[] switchPositionLocal;
    public Boolean[] maintenance;
    public boolean isGreen;
    public boolean validSchedule = false;
    public ArrayList<Integer> downedBlocks = new ArrayList<Integer>();
    public ArrayList<Integer> maintenanceBlocks = new ArrayList<Integer>();
    public int passengerCount;
    public long firstDepartureTime;
    public boolean firstDispatch = true;
    public float throughput = 0f;
    public ArrayList<Object> schedule = new ArrayList<Object>();
    public static int startedAutomaticMode;
    public boolean automaticMode = false;
    public int trainsDispatched = 0;
    public Controller CONTROLLER;
    Model(boolean green){
        isGreen = green;
    }
    public void configure(Controller control){
        CONTROLLER = control;
        configureBlocks();
        findPath();
        findDestinations();
        maintenance = new Boolean[SIZE+3];
        status = new boolean[SIZE+3][4];
        switchPosition = new Boolean[SIZE+3];
        switchPositionLocal = new Boolean[SIZE+3];
        for (int i=1;i<=SIZE+2;i++){
            maintenance[i] = false;
            status[i][1] = false;
            status[i][1] = false;
            status[i][1] = false;
            switchPosition[i] = false;
            switchPositionLocal[i] = false;
        }
    }

    private void configureBlocks(){
        if (configurationInt[SIZE][2] == 2){
            BLOCKS = new Block[SIZE+3];
            BLOCKS[SIZE+1] = new Block(SIZE+1);
            BLOCKS[SIZE+2] = new Block(SIZE+2);
        }
        else {
            BLOCKS = new Block[SIZE+3];
            BLOCKS[SIZE+1] = new Block(SIZE+1);
            BLOCKS[SIZE+2] = new Block(SIZE+2);
        }
        /** FRIST LOOP - creates the block and sets SWITCH,CROSSING,DIRECTION,LIMIT,LENGTH,GRADE,SECTION,
         * and DESTINATION. It also sets CONNECTS[0] and CONNECTS[1] for regular blocks and switches.
         * SECOND LOOP - for switches and sinks, sets CONNECTS[0] and CONNECTS[1]. 
         * THEN - the entering the yard is configured(only gets integer configurables
         * THEN - if entering yard block is not bi-directional, configure exiting block. 
         */ 
        for (int i=1;i<=SIZE;i++){
            BLOCKS[i] = new Block(i);
        }  
        for (int i=1;i<=SIZE;i++){
            BLOCKS[i].SWITCH = configurationInt[i][0];
            BLOCKS[i].CROSSING = configurationInt[i][1];
            BLOCKS[i].DIRECTION = configurationInt[i][2];
            BLOCKS[i].LIMIT = configurationInt[i][3];
            BLOCKS[i].LENGTH = configurationFloat[i][0];
            BLOCKS[i].GRADE =configurationFloat[i][1];
            BLOCKS[i].SECTION = configurationString[i][0];
            BLOCKS[i].DESTINATION = configurationString[i][1];
            if (BLOCKS[i].SWITCH >= 0){
                BLOCKS[i].CONNECTS[0] = BLOCKS[i-1];
                BLOCKS[i].CONNECTS[1] = BLOCKS[i+1];
            }
            if (BLOCKS[i].SWITCH > 0){
                if (BLOCKS[i-1].SWITCH == -BLOCKS[i].SWITCH )
                    BLOCKS[i].TYPE = false;
                else  
                    BLOCKS[i].TYPE = true; 

            }
        }
        for (int i=1;i<=SIZE;i++){if (BLOCKS[i].SWITCH > 0){
            for (int j=1;j<=SIZE;j++){ if (BLOCKS[i].SWITCH == -BLOCKS[j].SWITCH){ // if switch sink, enter loop
                if (j==i+1 || j==i-1){ // the one before or after, does not configure CONNECT[0] or CONNECT[1] for other sink
                    BLOCKS[j].CONNECTS[0] = BLOCKS[j-1];
                    BLOCKS[j].CONNECTS[1] = BLOCKS[j+1];
                }
                else{
                    BLOCKS[i].CONNECTS[2] = BLOCKS[j];
                    if (BLOCKS[i].SWITCH == -BLOCKS[i+1].SWITCH){
                        BLOCKS[j].CONNECTS[1] = BLOCKS[i];
                        BLOCKS[j].CONNECTS[0] = BLOCKS[j-1];
                    }
                    if (BLOCKS[i].SWITCH == -BLOCKS[i-1].SWITCH)  {
                        BLOCKS[j].CONNECTS[0] = BLOCKS[i];
                        BLOCKS[j].CONNECTS[1] = BLOCKS[j+1];
                    }
                }
            }}
        }}
        BLOCKS[SIZE+1].SWITCH = configurationInt[SIZE+1][0];
        BLOCKS[SIZE+1].DIRECTION = configurationInt[SIZE+1][2];
        BLOCKS[SIZE+1].LIMIT = -1;
        BLOCKS[SIZE+1].LENGTH = -1f;
        BLOCKS[SIZE+1].GRADE = -1f;
        BLOCKS[SIZE+1].CONNECTS[1] = null;
        for (int i=1;i<=SIZE;i++){if (BLOCKS[SIZE+1].SWITCH == -BLOCKS[i].SWITCH){
            BLOCKS[SIZE+1].CONNECTS[0] = BLOCKS[i];
            BLOCKS[i].CONNECTS[2] = BLOCKS[SIZE+1];
        }}
        if (BLOCKS[SIZE+1].DIRECTION != 2){
            BLOCKS[SIZE+2].SWITCH = configurationInt[SIZE+2][0];
            BLOCKS[SIZE+2].CROSSING = configurationInt[SIZE+2][1];
            BLOCKS[SIZE+2].DIRECTION = configurationInt[SIZE+2][2];
            BLOCKS[SIZE+2].LIMIT= -1;
            BLOCKS[SIZE+2].LENGTH = -1f;
            BLOCKS[SIZE+2].GRADE = -1f;
            BLOCKS[SIZE+2].CONNECTS[1] = null;
            for (int i=1;i<=SIZE;i++){if (BLOCKS[SIZE+2].SWITCH == -BLOCKS[i].SWITCH){
                BLOCKS[SIZE+2].CONNECTS[0] = BLOCKS[i];
                BLOCKS[i].CONNECTS[2] = BLOCKS[SIZE+2];
            }}
        }
        /*System.out.println(isGreen);
        for (int i=1;i<=SIZE+1;i++){
            System.out.println(BLOCKS[i].NUMBER+ " "+ BLOCKS[i].CONNECTS[0].NUMBER);
        }*/
    }
    private void findPath(){
        if (BLOCKS[SIZE+1].DIRECTION == 2){
            path.add(BLOCKS[SIZE+1]);
            path.add(BLOCKS[SIZE+1].CONNECTS[0]);
            path.add(BLOCKS[SIZE+1].CONNECTS[0].CONNECTS[0]);
            while (path.get(path.size()-1).CONNECTS[2] != BLOCKS[SIZE+1]){
                path.add( path.get( path.size()-1 ).fromTo( path.get(path.size()-2) ) );
            }
            path.add(BLOCKS[SIZE+1]);
        }
        else{
            path.add(BLOCKS[SIZE+2]);
            path.add(BLOCKS[SIZE+2].CONNECTS[0]);
            while ( (path.get(path.size()-1)).CONNECTS[2] != BLOCKS[SIZE+1] ){
                path.add( path.get( path.size()-1 ).fromTo( path.get(path.size()-2) ) );
            }
            path.add(BLOCKS[SIZE+1]);
        }
    }
    private void findDestinations(){
        for (int i=1;i<SIZE;i++){ 
            if (BLOCKS[i].DESTINATION.charAt(0) != '0' && !DESTINATIONS.contains(BLOCKS[i].DESTINATION))
                DESTINATIONS.add(BLOCKS[i].DESTINATION);
        }
    }   
    public int destinationBlock(String destination){
        for (int i=1;i<SIZE;i++){
            if (BLOCKS[i].DESTINATION == destination)
                return i;
        }
        return -1;
    }
    public boolean dispatchSignal(int[] departure,int[] arrival,int destination){
        int when = departure[0] * 60 + departure[1];
        int now = Clock.hour * 60 + Clock.minute;
        int whenArray[] = {(int)when/60,when%60};
        if (when < now && departure[0] != -1){
            CTCSystem.errorMessage("Departure time before current time");
            return false;
        }
        if (departure[0] != -1){

            whenArray[0] = (int)(when/60); 
            whenArray[1]= when%60;
            if (schedule.isEmpty())
                schedule.add(whenArray);
            else{
                int[] time;
                for (int i=0;i<schedule.size();i++){
                    time = (int[])schedule.get(i);
                    int minutes = time[0]*60 +time[1];
                    if (when < minutes){
                        schedule.add(i,whenArray);
                        return true;
                    }
                    if (i == schedule.size()-1){
                        schedule.add(whenArray);
                    }
                }

            }
            return true;
        }

        Queue<Float[]> blocks = new LinkedList<Float[]>();
        Queue<Integer> authorities = new LinkedList<Integer>();
        Float[] enterBlock = {-1f,-1f,-1f,152f};
        Float[] exitBlock = {-1f,-1f,-1f,151f};
        if (!isGreen){
            enterBlock[3] = 77f;
            enterBlock[3] = 77f;
        }
        blocks.add(enterBlock); 
        Integer authorityThere;
        Integer authorityBack;
        float setPoint = 10000000;
        //find set point speed
        for (int i=1;i<path.size()-1;i++){
            if (setPoint > path.get(i).LIMIT)
                setPoint = path.get(i).LIMIT;
        }
        for (int i=0;i<path.size()-1;i++){
            if (path.get(i).LIMIT > setPoint)
                setPoint = path.get(i).LIMIT;
            Float[] entry = {path.get(i).GRADE,(float)path.get(i).LIMIT,path.get(i).LENGTH,(float) path.get(i).NUMBER};
            blocks.add(entry);
        }
        for (int i=0;i<path.size();i++){
            if (destination == path.get(i).NUMBER ){
                authorityThere = i+1;
                authorityBack = path.size()-authorityThere;
                authorities.add(authorityThere);
                authorities.add(authorityBack);
                break;
            }
        }
        blocks.add(exitBlock);
        ArrayList<Object> train = new ArrayList<Object>();
        train.add(authorities);
        train.add(blocks);
        train.add(25);//setpoint
        train.add(2);
        train.add(isGreen);
        train.add(3);
        train.add(Integer.toString(CTCSystem.dispatchCount++));
        train.add(destination);
        CTCSystem.TRAINS.add(train);
        System.out.println (CTCSystem.dispatchCount);
        trainsDispatched++;
        return true;
    }
    public boolean parseSchedule(String filename){
        if (!filename.endsWith(".txt"))
            return false;
        List<String> lines = Collections.emptyList(); 
        try{ 
            lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8); 
        } 
        catch (IOException e) { 
            System.out.println("read all lines didnt work, sorry");
            return false;
        }

        //PASRSE file
        try{
            schedule.add((String)(lines.get(1).substring(11,15)));
            schedule.add((String)(lines.get(1).substring(16,20)));
            schedule.add((String)(lines.get(1).substring(21,25)));
            schedule.add((String)(lines.get(1).substring(26,30)));
            schedule.add((String)(lines.get(1).substring(31,35)));
            schedule.add((String)(lines.get(1).substring(36,40)));
            schedule.add((String)(lines.get(1).substring(41,45)));
            schedule.add((String)(lines.get(1).substring(46,50)));
            schedule.add((String)(lines.get(1).substring(51,55)));
            schedule.add((String)(lines.get(1).substring(56,60)));
        }
        catch (Exception e)
        {
            return false;
        }
        try{
            int current = Clock.hour * 60 + Clock.minute;
            
            for (int i=0;i<schedule.size();i++){
                String string = (String) schedule.get(i);
                int hour = Integer.parseInt(string.substring(0,1));
                int min = Integer.parseInt(string.substring(2,4));
                int timeTotal = current + (hour*60 +min);
                int[] time = {(int)timeTotal/60,timeTotal%60};
                schedule.set(i,time);
            }
        }
        catch (Exception e){
            return false;
        }
        return true;

    }
    public void update(){
        int size = SIZE;
        if (BLOCKS[SIZE+1].DIRECTION != 2)
            size = size + 2;
        else
            size = size + 1;
        for (Integer i=1;i<=size;i++){
            if ( (status[i][1] || status[i][2] || status[i][3]) && !downedBlocks.contains(i) )
                downedBlocks.add(i);
            if (  (!status[i][1] & !status[i][2] & !status[i][3])  && downedBlocks.contains(i) ){
                downedBlocks.remove((Object)i);
            }
            if (maintenance[i] && !maintenanceBlocks.contains(i))
                maintenanceBlocks.add(i);
        }
        if (!firstDispatch){
            long timePassed = System.currentTimeMillis() - firstDepartureTime;
            float hoursPassed = ((float)timePassed)/(3600000f) * Clock.timeFactor; 
            throughput = ((float)passengerCount)/hoursPassed;
        }
        if (!schedule.isEmpty()){
            int time[] = (int[]) schedule.get(0);
            int when;
            when = time[0] * 60 + time[1];
            if  ( when <= (Clock.hour * 60 + Clock.minute)  ) {
                int[] depart = {-1,-1};
                int[] arrive = {-1,-1};
                dispatchSignal(depart ,arrive, 22); 
                System.out.println("here");
                schedule.remove(0);
                CONTROLLER.VIEW.DISPATCH_LOG.update();
            }
        }
    }
    public void enterAutomaticMode(){
        startedAutomaticMode = Clock.hour * 60 + Clock.minute;
        automaticMode = true;
    }
}
    /*public void configureTest(Controller control){
        CONTROLLER = control;
        findPath();
        findDestinations();
        maintenance = new Boolean[SIZE+3];
        status = new boolean[SIZE+3][4];
        switchPosition = new Boolean[SIZE+3];
        for (int i=1;i<=SIZE+2;i++){
            maintenance[i] = false;
            status[i][1] = false;
            status[i][1] = false;
            status[i][1] = false;
            switchPosition[i] = false;
        }
        switchPositionLocal = switchPosition;
    }*/