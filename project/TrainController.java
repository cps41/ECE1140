package project;

public class TrainController {
    public static int PASSENGER_COUNT;
    public TrainModel TRAIN;
    TrainController(TrainModel train) {
        TRAIN = train;
        PASSENGER_COUNT = TRAIN.PASSENGER_COUNT;
    }

    public void printPassengerCount() {
        System.out.println(PASSENGER_COUNT);
    }

    public static void main(String[] args){
        TrainModel train = new TrainModel();
        TrainController controller = new TrainController(train);
        controller.printPassengerCount();
    }
}
