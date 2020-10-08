package project;

public class TrainModel {
    public static float POWER = 0.0f; // key input
    public static float VELOCITY = 0.0f; // key output
    public static float AUTHORITY = 0.0f; // in meters
    public static float LENGTH = 100.0f;
    public static float MASS = 18.0f; // in tons
    public static int CREW_COUNT = 12;
    public static int PASSENGER_COUNT = 43;
    public final float CAR_LENGTH = 32.2f; // in meters
    public final float CAR_HEIGHT = 3.42f; // in meters
    public final float CAR_WIDTH = 2.65f; // in meters

    public static void setAuthority(float data) {
        AUTHORITY = data;
    }

    public static void setVelocity(float data) {
        AUTHORITY = data;
    }


}


