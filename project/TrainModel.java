package project;

public class TrainModel {
    public static float POWER; // key input
    public static float VELOCITY; // key output
    public static float AUTHORITY; // in meters
    public static boolean BRAKES = false;
    public static float LENGTH;
    public static float MASS; // in tons
    public static int PASSENGER_COUNT;
    public final float CAR_LENGTH = 32.2f; // in meters
    public final float CAR_HEIGHT = 3.42f; // in meters
    public final float CAR_WIDTH = 2.65f; // in meters

    public TrainModel(int num_cars) {
        POWER = 0.0f;
        VELOCITY = 0.0f;
        AUTHORITY = 0.0f;
        
    }

    public static void setAuthority(float data) {
        AUTHORITY = data;
    }

    public static void setVelocity(float data) {
        AUTHORITY = data;
    }

    public static void setPower(float data) {
        POWER = data;
    }

    public static void calculateLength() {
        LENGTH = data;
    }
}


