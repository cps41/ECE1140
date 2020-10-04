import project.*;

public class main {
    public static void main(String[] args){
        TrainModel train = new TrainModel();
        TrainController controller = new TrainController(train);
        controller.printPassengerCount();
    }
}
