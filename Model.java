import java.util.*;
public class Model{
    public int configurationInt[][] = null;
    public float configurationFloat[][] = null;
    public String configurationString[][] = null;
    public Integer SIZE = null;
    public ArrayList<Block> path = new ArrayList<Block>();
    public Block[] BLOCKS;
    public ArrayList<String> DESTINATIONS = new ArrayList<String>();
    public Boolean[][] presence;
    public Boolean[] switchPosition;
    public boolean isGreen;

    Model(boolean green){
        isGreen = green;
    }
    public void configure(){
        configureBlocks();
        findPath();
        findDestinations();
        presence = new Boolean[SIZE][];
        switchPosition = new Boolean[SIZE];
        for (int i=1;i<=SIZE+2;i++){
            
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
    public int dispatchSignal(int[] departure,int[] arrival,int destination){
        Queue<Float[]> blocks = new LinkedList<Float[]>();
        Queue<Integer> authorities = new LinkedList<Integer>();
        Float[] yardBlock = {-1f,-1f,-1f,-1f};
        blocks.add(yardBlock); 
        Integer authorityThere;
        Integer authorityBack;
        float setPoint = 50;
        for (int i=0;i<path.size()-1;i++){
            if (path.get(i).LIMIT > setPoint)
                setPoint = path.get(i).LIMIT;
            Float[] entry = {path.get(i).GRADE,(float)path.get(i).LIMIT,path.get(i).LENGTH,(float) path.get(i).NUMBER};
            blocks.add(entry);
        }
        for (int i=0;i<path.size();i++){
            if (destination == path.get(i).NUMBER){
                authorityThere = i+1;
                authorityBack = path.size()-authorityThere;
                authorities.add(authorityThere);
                authorities.add(authorityBack);
                break;
            }
        }
        blocks.add(yardBlock);
        ArrayList<Object> train = new ArrayList<Object>();
        train.add(authorities);
        train.add(blocks);
        train.add(50);//setpoint
        train.add(2);
        train.add(isGreen);
        train.add(3);
        train.add(Integer.toString(CTCSystem.dispatchCount++));
        CTCSystem.TRAINS.add(train);
        System.out.println (CTCSystem.dispatchCount);
        return -1;
    }
 }
