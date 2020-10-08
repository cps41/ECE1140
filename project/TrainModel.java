package project;

import java.util.*;

public class TrainModel {
    private static float POWER; // key input
    private static float VELOCITY; // key output
    private static float AUTHORITY; // in meters
    private static float LENGTH;
    private static LinkedList<TrainCar> CARS = new LinkedList<>();
    private static float MASS; // in tons
    private static int CREW_COUNT;
    private static int PASSENGER_COUNT;
    private final float CAR_LENGTH = 32.2f; // in meters
    private final float CAR_HEIGHT = 3.42f; // in meters
    private final float CAR_WIDTH = 2.65f; // in meters

    public TrainModel() {
        AUTHORITY = 30.5f;
        for (int i=0; i<=5; i++) CARS.add(new TrainCar());
        LENGTH = CAR_LENGTH*CARS.size();
        MASS = 56.7f*CARS.size(); //in tons
        CREW_COUNT = 12;
        for (int j=0; j<CARS.size(); j++) {
            PASSENGER_COUNT += CARS.get(j).getPassengerCount();
        }
    }

    public static void setAuthority(float data) {
        AUTHORITY = data;
    }

    public static void setVelocity(float data) {
        AUTHORITY = data;
    }


}


