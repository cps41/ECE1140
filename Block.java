public class Block {
    public boolean PRESENCE;
    public Block[] CONNECTS = {null,null,null};//before,after,deviation
    public int SWITCH,CROSSING,DIRECTION,LIMIT,NUMBER;
    public float LENGTH,GRADE;
    String SECTION,DESTINATION;
    boolean TYPE; // false for less switch, true for one greater

    Block(int num){ 
        NUMBER = num;
    }
    public Block fromTo(Block from){
        if (from == null)
            return null;
        else if (from == CONNECTS[0]){
            if (SWITCH <= 0){
                if (DIRECTION != 1)
                    return CONNECTS[1];
            }
            else
                return CONNECTS[1];
        }
        else if (from == CONNECTS[1]){
            if (SWITCH<=0){
                if (DIRECTION != 0)
                    return CONNECTS[0];
                else
                    return null;
            }
            else{
                if (CONNECTS[1].DIRECTION != 0 ){
                    if (CONNECTS[2].DIRECTION == 2)
                        return CONNECTS[2];
                    else if (CONNECTS[0].DIRECTION >= 1)
                        return CONNECTS[0];
                    else
                        return CONNECTS[2];
                }
            }
        }
        else if (from == CONNECTS[2]){
            if (SWITCH <= 0)
                return null;
            else{
                if (from.DIRECTION == 2){
                    if (!TYPE)
                        return CONNECTS[1];
                    else
                        return CONNECTS[0];
                }
                if (from.DIRECTION != 1)
                     return CONNECTS[0];
                else   
                    return CONNECTS[1];
            }

        }
        return null;
    }
}