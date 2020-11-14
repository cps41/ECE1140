public class Block {
    public boolean PRESENCE;
    public Block[] CONNECTS = {null,null,null};//before,after,deviation
    public int SWITCH,CROSSING,DIRECTION,LIMIT,NUMBER;
    public float LENGTH,GRADE;
    String SECTION,DESTINATION;
    Block(int num){ 
    NUMBER = num;
    }
}