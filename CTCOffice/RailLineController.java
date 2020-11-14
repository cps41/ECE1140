import java.util.ArrayList;
import java.util.Queue;
import java.util.Date;
import java.util.LinkedList;
import java.lang.Math;
import java.util.Stack;

public class RailLineController{
    //variables 
    public RailLinePanel PANEL = new RailLinePanel();
    public ArrayList<String> DESTINATION_NAMES = new ArrayList<String>();
    public ArrayList<Queue<Boolean>> SWITCH_QUEUE = new ArrayList< Queue<Boolean> >();
    public LinkedList<Integer> AUTHORITIES = new LinkedList<Integer>();
    public LinkedList<Integer[]> TRAIN_CONTROL = new LinkedList<Integer[]>();
    public int NUMBER_OF_BLOCKS;
    public Block[] BLOCKS;
    public int NUMBER_OF_SWITCHES;
    public boolean DISPATCH_SIGNAL_TOGGLE;
    public int[] DISPATCH_SIGNAL = {-1         ,-1        ,-1          ,-1       ,-1};
    //                              destination,time(hour),time(minute),authority,speed
    //constructor
    RailLineController(String name,int num,int[][] array,float[][] configuration,String[] destinations){
        configureBlocks(num,array,configuration,destinations);
        for (int i=0;i<num;i++){
            SWITCH_QUEUE.add(new LinkedList<Boolean>());
        }

        PANEL.configurePanel(name,this,destinations,num);
        DISPATCH_SIGNAL_TOGGLE = false;
    }
    //configurators          number of blocks, block array
    private void configureBlocks(int num,int[][] array,float[][] configure,String[] destinations){
        BLOCKS = new Block[num];
        for (int i=0;i<num;i++){BLOCKS[i] = new Block(i);}
        for (int i=0;i<num;i++){
            if (destinations[i] != "0"){
                BLOCKS[i].DESTINATION = DESTINATION_NAMES.size();
                DESTINATION_NAMES.add(destinations[i]);
            }else{BLOCKS[i].DESTINATION = -1;}
        }
        NUMBER_OF_BLOCKS = num;
        int switchNum;
        for (int i=0;i<num;i++){
            BLOCKS[i].CROSSING = array[i][1];
            BLOCKS[i].DIRECTION = array[i][2];
            BLOCKS[i].BLOCK_LENGTH = (int)configure[i][0];
            BLOCKS[i].GRADE = (int)configure[i][1];
            BLOCKS[i].SPEED_LIMIT =(int)configure[i][2];
                }
        int enterSwitch = 0 ;
        for (int i=0;i<num-1;i++){ if (array[i][0] ==-array[num-1][0]){enterSwitch = i;}}
        BLOCKS[num-1].CONNECTS[0] = null;
        BLOCKS[num-1].CONNECTS[1] = BLOCKS[enterSwitch];
        BLOCKS[enterSwitch].CONNECTS[2] = BLOCKS[num-1];
        for (int i=0;i<num-1;i++){//configured Block array
            if (array[i][0] == 0){//not a switch, or affiliated with one 
                BLOCKS[i].CONNECTS[0] = BLOCKS[i-1];
                BLOCKS[i].CONNECTS[1] = BLOCKS[i+1];
                BLOCKS[i].CONNECTS[2] = null;
            }
            else if (array[i][0] > 0){//connected to a switch or is a switch
                switchNum = array[i][0];
                if( Math.abs(array[i-1][0]) == Math.abs(array[i][0])){
                    BLOCKS[i].CONNECTS[0] = BLOCKS[i+1];
                    BLOCKS[i].CONNECTS[1] = BLOCKS[i-1];
                    BLOCKS[i].SWITCH_TYPE = false;
                    BLOCKS[i-1].CONNECTS[0] = BLOCKS[i-2];
                    BLOCKS[i-1].CONNECTS[1] = BLOCKS[i];
                    for (int j=0;j<num-1;j++){ if (array[j][0] == -switchNum &&  j != (i-1) ){
                        BLOCKS[j].CONNECTS[0] = BLOCKS[i];
                        BLOCKS[j].CONNECTS[1] = BLOCKS[j+1];
                        BLOCKS[i].CONNECTS[2] = BLOCKS[j];
                    }}
                }else{
                    BLOCKS[i].CONNECTS[0] = BLOCKS[i-1];
                    BLOCKS[i].CONNECTS[1] = BLOCKS[i+1];
                    BLOCKS[i].SWITCH_TYPE = true;
                    BLOCKS[i+1].CONNECTS[0] = BLOCKS[i];
                    BLOCKS[i+1].CONNECTS[1] = BLOCKS[i+2];
                    for (int j=0;j<num-1;j++){ if (array[j][0] == -switchNum &&  j != (i+1) ){
                        BLOCKS[j].CONNECTS[1] = BLOCKS[i];
                        BLOCKS[j].CONNECTS[0] = BLOCKS[j-1];
                        BLOCKS[i].CONNECTS[2] = BLOCKS[j];
                    }}
                }
            }
        }

    }

    //methods
    public int dispatchSignal(int destination,int[] time){
        ArrayList<Block> pathThere = new ArrayList<Block>();
        ArrayList<Block> pathBack = new ArrayList<Block>();
        int destinationBlock = 0 ;
        for (int i = 0 ; i< NUMBER_OF_BLOCKS ; i++){
            if (BLOCKS[i].DESTINATION == destination){
                destinationBlock = i;
            }
        }
        pathThere = findPath(NUMBER_OF_BLOCKS-1,destinationBlock);
        pathBack = findPath(destinationBlock,NUMBER_OF_BLOCKS-1);
        if (!checkPath(pathThere)) {return CTCSystem.TRACK_TRAFFIC;}
        if (!checkTime(time,pathThere)){return CTCSystem.INVALID_TIME;}
        generateQueues(pathThere,pathBack);

        return CTCSystem.SUCCESSFULL_DISPATCH;
    }
    //find path
    private ArrayList<Block> findPath(int start,int destination) {
        Queue<Block> queue = new LinkedList<Block>();
        ArrayList<Block> explored = new ArrayList<Block>();
        Block pred[] = new Block[300];
        Block  destinationBlock  = BLOCKS[destination];
        ArrayList<Block> path = new ArrayList<Block>();
        for (int i=0;i<300;i++){pred[i]=null;}
        if (start == NUMBER_OF_BLOCKS-1){
            explored.add(BLOCKS[start]);
            start = BLOCKS[start].CONNECTS[1].BLOCK_NUMBER;
            pred[start] = BLOCKS[NUMBER_OF_BLOCKS-1];
        }

        explored.add(BLOCKS[start]);
        queue.add(BLOCKS[start]);
        while (!queue.isEmpty()) {
            Block currentBlock = queue.remove();
            if (currentBlock == destinationBlock){break;}
            if (currentBlock.CONNECTS[2] == null){
                if (currentBlock.DIRECTION == 1 && !explored.contains(currentBlock.CONNECTS[0])){
                    explored.add(currentBlock.CONNECTS[0]);
                    pred[currentBlock.CONNECTS[0].BLOCK_NUMBER] = currentBlock;
                    queue.add(currentBlock.CONNECTS[0]);
                    if (currentBlock.CONNECTS[0] == destinationBlock) {queue.clear();}
                }
                else if (currentBlock.DIRECTION == 0 && !explored.contains(currentBlock.CONNECTS[1])){
                    explored.add(currentBlock.CONNECTS[1]);
                    pred[currentBlock.CONNECTS[1].BLOCK_NUMBER] = currentBlock;
                    queue.add(currentBlock.CONNECTS[1]);
                    if (currentBlock.CONNECTS[1] == destinationBlock) {queue.clear();}
                }
                else{
                    if (!explored.contains(currentBlock.CONNECTS[1])){
                        explored.add(currentBlock.CONNECTS[1]);
                        pred[currentBlock.CONNECTS[1].BLOCK_NUMBER] = currentBlock;
                        queue.add(currentBlock.CONNECTS[1]);
                        if (currentBlock.CONNECTS[1] == destinationBlock) {queue.clear();}
                    }
                    if (!explored.contains(currentBlock.CONNECTS[0])){
                        explored.add(currentBlock.CONNECTS[0]);
                        pred[currentBlock.CONNECTS[0].BLOCK_NUMBER] = currentBlock;
                        queue.add(currentBlock.CONNECTS[0]);
                        if (currentBlock.CONNECTS[0] == destinationBlock) {queue.clear();}
                    }
                }
            }else{
                if (!explored.contains(currentBlock.CONNECTS[0])){
                    if (currentBlock.CONNECTS[0].DIRECTION == 2) {
                        explored.add(currentBlock.CONNECTS[0]);
                        pred[currentBlock.CONNECTS[0].BLOCK_NUMBER] = currentBlock;
                        queue.add(currentBlock.CONNECTS[0]);
                        if (currentBlock.CONNECTS[0] == destinationBlock) {queue.clear();}
                    }
                    else if (currentBlock.CONNECTS[0].DIRECTION == 1 && currentBlock.CONNECTS[0].BLOCK_NUMBER < currentBlock.BLOCK_NUMBER){
                        explored.add(currentBlock.CONNECTS[0]);
                        pred[currentBlock.CONNECTS[0].BLOCK_NUMBER] = currentBlock;
                        queue.add(currentBlock.CONNECTS[0]);
                        if (currentBlock.CONNECTS[0] == destinationBlock) {queue.clear();}
                    }
                    else if (currentBlock.CONNECTS[0].DIRECTION == 0 && currentBlock.CONNECTS[0].BLOCK_NUMBER > currentBlock.BLOCK_NUMBER){
                        explored.add(currentBlock.CONNECTS[0]);
                        pred[currentBlock.CONNECTS[0].BLOCK_NUMBER] = currentBlock;
                        queue.add(currentBlock.CONNECTS[0]);
                        if (currentBlock.CONNECTS[0] == destinationBlock) {queue.clear();}
                    }
                }
                else{
                    if (!explored.contains(currentBlock.CONNECTS[2])) {
                        if (currentBlock.CONNECTS[2].DIRECTION == 2) {
                            explored.add(currentBlock.CONNECTS[2]);
                            pred[currentBlock.CONNECTS[2].BLOCK_NUMBER] = currentBlock;
                            queue.add(currentBlock.CONNECTS[2]);
                            if (currentBlock.CONNECTS[2] == destinationBlock) {queue.clear();}
                        }
                        else if (currentBlock.CONNECTS[2].DIRECTION == 1 && currentBlock.CONNECTS[2].CONNECTS[1] == currentBlock){
                            explored.add(currentBlock.CONNECTS[2]);
                            pred[currentBlock.CONNECTS[2].BLOCK_NUMBER] = currentBlock;
                            queue.add(currentBlock.CONNECTS[2]);
                            if (currentBlock.CONNECTS[2] == destinationBlock) {queue.clear();}
                        }
                    }
                    if (!explored.contains(currentBlock.CONNECTS[1])){
                        if (currentBlock.CONNECTS[1].DIRECTION == 2) {
                            explored.add(currentBlock.CONNECTS[1]);
                            pred[currentBlock.CONNECTS[1].BLOCK_NUMBER] = currentBlock;
                            queue.add(currentBlock.CONNECTS[1]);
                            if (currentBlock.CONNECTS[1] == destinationBlock) {queue.clear();}
                        }
                        else if (currentBlock.CONNECTS[1].DIRECTION == 1 && currentBlock.CONNECTS[1].BLOCK_NUMBER < currentBlock.BLOCK_NUMBER){
                            explored.add(currentBlock.CONNECTS[1]);
                            pred[currentBlock.CONNECTS[1].BLOCK_NUMBER] = currentBlock;
                            queue.add(currentBlock.CONNECTS[1]);
                            if (currentBlock.CONNECTS[1] == destinationBlock) {queue.clear();}
                        }
                        else if (currentBlock.CONNECTS[1].DIRECTION == 0 && currentBlock.CONNECTS[1].BLOCK_NUMBER > currentBlock.BLOCK_NUMBER){
                            explored.add(currentBlock.CONNECTS[1]);
                            pred[currentBlock.CONNECTS[1].BLOCK_NUMBER] = currentBlock;
                            queue.add(currentBlock.CONNECTS[1]);
                            if (currentBlock.CONNECTS[1] == destinationBlock) {queue.clear();}
                        }
                    }
                }
            }
        }
        int crawl = destination;
        path.add(BLOCKS[crawl]);
        System.out.println("path size: "+path.size()+"block:"+path.get(path.size()-1).BLOCK_NUMBER);
        while (pred[crawl] != null) {
            path.add(pred[crawl]);
            System.out.println("path size: "+path.size()+"block:"+path.get(path.size()-1).BLOCK_NUMBER);
            crawl = pred[crawl].BLOCK_NUMBER;
        }
       return path;
    }
    private boolean checkPath(ArrayList<Block> path){
        for (int i=0;i<path.size();i++){ if (path.get(i).TRAIN_PRESENCE == true){return false;} }
        return true;
    }
    private boolean checkTime(int[] time,ArrayList<Block> path){
        float limit =(float)path.get(0).SPEED_LIMIT;
        for (int i=0;i<path.size();i++){ 
            if (limit > path.get(i).SPEED_LIMIT) {limit = path.get(i).SPEED_LIMIT;}
        }
        float length = (float) getPathLength(path);
        Date date = new Date();
        String dateString = date.toString();
        int[] currentTime = new int[4];
        currentTime[0] = Integer.parseInt(String.valueOf(dateString.charAt(11))); 
        currentTime[1] = Integer.parseInt(String.valueOf(dateString.charAt(12))); 
        currentTime[2] = Integer.parseInt(String.valueOf(dateString.charAt(14))); 
        currentTime[3] = Integer.parseInt(String.valueOf(dateString.charAt(15))); 
        float currentTimeHour = (float)(currentTime[0]*10)+currentTime[1];
        float currentTimeMinute = (float)(currentTime[2]*10)+currentTime[3];
        float timeHour = (float)(time[0]*10)+time[1];
        float timeMinute = (float)(time[2]*10)+time[3];
        if (timeHour < currentTimeHour) {return false;}
        if (timeHour == currentTimeHour && timeMinute <= currentTimeMinute){return false;}
        float hoursNeeded = (length*(1f/1000f))/limit;
        float hoursHad = (timeHour + timeMinute/60f) - (currentTimeHour + currentTimeMinute/60f);
        if (hoursNeeded > hoursHad) {return false;}
        return true;
    }     
    private int getPathLength(ArrayList<Block> path){
        int length = 0;
        for (int i=0;i<path.size();i++){
            length = length + path.get(i).BLOCK_LENGTH;
        }
        return length;
    }
    private void generateQueues(ArrayList<Block> there, ArrayList<Block> back){
        AUTHORITIES.add(there.size());
        AUTHORITIES.add(back.size());
        Integer[] info = new Integer[3];
        for (int i = there.size()-1; i>=0;i--){
            info[0] = there.get(i).GRADE;
            info[1] = there.get(i).SPEED_LIMIT;
            info[2] = there.get(i).BLOCK_LENGTH;
            TRAIN_CONTROL.add(info);/*
            if (there.get(i).CONNECTS[2] != null){
                if (there.get(i-1) == there.get(i).CONNECTS[1] || there.get(i-1) == there.get(i).CONNECTS[2]{

                }
                else {

                }
 
                if(there.get(i+1) == there.get(i).CONNECTS[2]){
                    if (there.get(i).CONNECTS[2].BLOCK_NUMBER > there.get(i).CONNECTS[1].BLOCK_NUMBER){
                        SWITCH_QUEUE.get(there.get(i).BLOCK_NUMBER).add(true);
                        System.out.println("add true at "+there.get(i).BLOCK_NUMBER);
                    }
                    else{
                        SWITCH_QUEUE.get(there.get(i).BLOCK_NUMBER).add(false);
                        System.out.println("add false at "+there.get(i).BLOCK_NUMBER);
                    }
                }
                else{
                    if (there.get(i).CONNECTS[1].BLOCK_NUMBER > there.get(i).CONNECTS[2].BLOCK_NUMBER){
                        SWITCH_QUEUE.get(there.get(i).BLOCK_NUMBER).add(true);
                        System.out.println("add true at "+there.get(i).BLOCK_NUMBER);
                    }
                    else{
                        SWITCH_QUEUE.get(there.get(i).BLOCK_NUMBER).add(false);
                        System.out.println("add false at "+there.get(i).BLOCK_NUMBER);
                    }
                }
            }*/
        }
        for (int i = back.size()-1; i>=0;i--){
            info[0] = back.get(i).GRADE;
            info[1] = back.get(i).SPEED_LIMIT;
            info[2] = back.get(i).BLOCK_LENGTH;
            TRAIN_CONTROL.add(info);/*
            if (back.get(i).CONNECTS[2] != null){
                if(back.get(i-1) == back.get(i).CONNECTS[2]){
                    if (back.get(i).CONNECTS[2].BLOCK_NUMBER > back.get(i).CONNECTS[1].BLOCK_NUMBER){
                        SWITCH_QUEUE.get(back.get(i).BLOCK_NUMBER).add(true);
                        System.out.println("add true at "+back.get(i).BLOCK_NUMBER);
                    }
                    else{
                        SWITCH_QUEUE.get(back.get(i).BLOCK_NUMBER).add(false);
                        System.out.println("add false at "+back.get(i).BLOCK_NUMBER);
                    }
                }
                else{
                    if (back.get(i).CONNECTS[1].BLOCK_NUMBER > back.get(i).CONNECTS[2].BLOCK_NUMBER){
                        SWITCH_QUEUE.get(back.get(i).BLOCK_NUMBER).add(true);
                        System.out.println("add true at "+back.get(i).BLOCK_NUMBER);
                    }
                    else{
                        SWITCH_QUEUE.get(back.get(i).BLOCK_NUMBER).add(false);
                        System.out.println("add false at "+back.get(i).BLOCK_NUMBER);
                    }
                }*/
        }
    }
}





/*




 current = queue.remove();
            explored.add(current);
            if (current == destinationBlock){ //found destination
                while (current != start ){ //build path
                    path.add(current);
                    if (CONNECTIVITY[current][0] == 0){current = current + 1;}  // not a switch, add next block to path
                    else {
                        for (int i=0;i<NUMBER_OF_BLOCKS;i++){  //if switch, find source/sink, and add to path
                            if (CONNECTIVITY[current][0] == -CONNECTIVITY[i][0]){
                                current = i;
                                break;
                            }
                        }   
                    }
                }  
            }
            else{
                if (CONNECTIVITY[current][0] == 0){ queue.add((current+1));}
                else {
                    for (int i=0;i<NUMBER_OF_BLOCKS;i++){  //if switch, find source/sink
                        if (CONNECTIVITY[current][0] == -CONNECTIVITY[i][0]){
                            queue.add
                        }
                    }   
                }
            }


        CONNECTIVITY = new int[num][3]; 
        NUMBER_OF_BLOCKS = num;
        BLOCKS = new Block[num];
        for (int i=0;i<num;i++){BLOCKS[i] = new Block();}
        ArrayList<Integer> explored = new ArrayList<Integer>();
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(0);
        int current = 0;
        while(current<num){
            if (!explored.contains(current)){
                if (array[current][0] != 0){if (explored.contains(current) == false){
                    for (int i=0;i<num;i++){if (Math.abs(array[current][0]) == Math.abs(array[i][0])){
                        BLOCKS[i].DESTINATION = array[i][1];
                        explored.add(i);
                    }}
                    int threeBlocks[] = {explored.get(explored.size()-1), explored.get(explored.size()-2), explored.get(explored.size()-3) };
                    for (int i=0;i<3;i++){
                        if (array[threeBlocks[i]][0] > 0 ){
                            BLOCKS[threeBlocks[i]].ROOT = BLOCKS[threeBlocks[i]-1];
                            if (i == 1){
                                BLOCKS[threeBlocks[i]].BRANCH = BLOCKS[threeBlocks[0]-1];
                                BLOCKS[threeBlocks[i]].BRANCH2 = BLOCKS[threeBlocks[2]-1];
                            }
                            else if (i==2){
                                BLOCKS[threeBlocks[i]].BRANCH = BLOCKS[threeBlocks[0]-1];
                                BLOCKS[threeBlocks[i]].BRANCH2 = BLOCKS[threeBlocks[1]-1];
                            }
                            else{
                                BLOCKS[threeBlocks[i]].BRANCH = BLOCKS[threeBlocks[1]-1];
                                BLOCKS[threeBlocks[i]].BRANCH2 = BLOCKS[threeBlocks[2]-1];
                            }
                        }
                    }
                }}
                else{
                    BLOCKS[current].DESTINATION = array[current][1];
                    explored.add(current);
                } 
            }
            current = current + 1;
        }
        System.out.print(BLOCKS[8].DESTINATION);



        Queue<Integer> queue = new LinkedList<Integer>();
        ArrayList<Integer> explored = new ArrayList<Integer>();
        ArrayList<Integer> path = new ArrayList<Integer>()
        queue.add(BLOCKS[start]);
        System.out.println();
        while (!queue.isEmpty()){
            currentBlock = queue.remove();
            explored.add(currentBlock);
            if (currentBlock.DESTINATION == destination) {
                while ( !(currentBlock == BLOCKS[start]) ){
                    path.add(currentBlock);
                    currentBlock = currentBlock.SOURCE;
                }
                queue.clear(); 
            }
            else if (explored.contains(currentBlock.SINK1) == false ) {queue.add(currentBlock.SINK1);}
            else if (currentBlock.SINK2 != null && explored.contains(currentBlock.SINK1) == false){
                queue.add(currentBlock.SINK2);}
        }
        //remove!!
        path.add(BLOCKS[0]);
        return path;

*/