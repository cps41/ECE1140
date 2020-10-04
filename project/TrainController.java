package project;

public class TrainController {
    public static int PASSENGER_COUNT;
    public TrainModel TRAIN;
    public TrainController(TrainModel train) {
        TRAIN = train;
        PASSENGER_COUNT = TRAIN.PASSENGER_COUNT;
    }

    public void printPassengerCount() {
        System.out.println(PASSENGER_COUNT);
    }
} 
