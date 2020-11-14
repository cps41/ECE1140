public class Block {
    public boolean TRAIN_PRESENCE,SWITCH_TYPE;// false for less
    public Block[] CONNECTS = {null,null,null};//source,sink1,sink2
    public int DESTINATION,BLOCK_NUMBER,SPEED_LIMIT,DIRECTION,BLOCK_LENGTH,CROSSING,SWITCH,GRADE;
    Block(int num){ 
    BLOCK_NUMBER = num;
    }
}