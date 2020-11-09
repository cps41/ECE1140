package project;

import java.utils.*;

public class TrainModel {
    public float POWER; // key input
    public float VELOCITY; // key output
    public float AUTHORITY; // in meters
    public boolean BRAKES;
    public float LENGTH;
    public float MASS; // in tons
    public int PASSENGER_COUNT;
    public ArrayList<Car> CARS;
    public final float CAR_LENGTH = 32.2f; // in meters
    public final float CAR_HEIGHT = 3.42f; // in meters
    public final float CAR_WIDTH = 2.65f; // in meters
    public final float MAX_POWER = 120f; // in kW
    public final float MIN_CAR_WEIGHT = 40900f; // in kg
    public final float AVG_WEIGHT_PERSON = 62f; // in kg

    public TrainModel(int num_cars) {
        POWER = 0.0f;
        VELOCITY = 0.0f;
        AUTHORITY = 0.0f;
        CARS = new ArrayList<>()
        for(int i : num_cars) {
            CARS.add(new Car());
        }
        LENGTH = CAR_LENGTH*num_cars;
        PASSENGER_COUNT = 0;
        BRAKES = true;
        MASS = MIN_CAR_WEIGHT;
    }

    public void setAuthority(float data) {
        AUTHORITY = data;
    }

    public void setVelocity() {
        float force = MA;
        if(POWER == 0) {
            
        } 
    }

    public void setPower(float data) {
        POWER = data;
    }
}


