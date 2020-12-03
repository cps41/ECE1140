public class Controller{
    public Model MODEL;
    public View VIEW;
    Controller(){}
    public int destinationBlock(String destination){
        for (int i=1;i<MODEL.SIZE;i++){
            if (MODEL.BLOCKS[i].DESTINATION == destination)
                return i;
        }
        return -1;
    }
    public void configure(Model mod,View v){
        MODEL = mod;
        VIEW = v;
    }
}