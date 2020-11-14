package project;

import java.util.*;

public class TrainModel {
    public float POWER; // key input
    public float VELOCITY; // key output
    public float AUTHORITY; // in meters
    public boolean BRAKES;
    public boolean EBRAKE;
    public float LENGTH;
    public float MASS; // in tons
    public float ACCELERATION; // in km/s^2
    public int PASSENGER_COUNT;
    public int CREW_COUNT;
    public boolean INTERIOR_LIGHTS;
    public boolean EXTERIOR_LIGHTS;
    public boolean LEFT_DOORS;
    public boolean RIGHT_DOORS;
    public float TEMPERATURE;
    public ArrayList<Car> CARS;
    public final float CAR_LENGTH = 32.2f; // in meters
    public final float CAR_HEIGHT = 3.42f; // in meters
    public final float CAR_WIDTH = 2.65f; // in meters
    public final float MAX_POWER = 120f; // in kW
    public final float MIN_CAR_WEIGHT = 40900f; // in kg
    public final float MAX_CAR_WEIGHT = 56700; // in kg
    public final float MED_ACCELERATION = 0.0005f; // in km/s^2
    public final float AVG_WEIGHT_PERSON = 62f; // in kg
    public HashMap <String, Object> outputs;

    public TrainModel(int num_cars) {
        POWER = 0.0f;
        VELOCITY = 0.0f;
        AUTHORITY = 0.0f;
        CARS = new ArrayList<>();
        for(int i=0; i<=num_cars; i++) {
            CARS.add(new Car());
        }
        LENGTH = CAR_LENGTH*num_cars;
        PASSENGER_COUNT = 0;
        CREW_COUNT = 0;
        BRAKES = true;
        MASS = MIN_CAR_WEIGHT;
        EBRAKE = false;
        INTERIOR_LIGHTS = false;
        EXTERIOR_LIGHTS = false;
        LEFT_DOORS = true;
        RIGHT_DOORS = true;
        outputs = new HashMap<>();
        outputs.put(schema.TrainModel.velocity, VELOCITY);
        outputs.put(schema.TrainModel.authority, AUTHORITY);
        outputs.put(schema.TrainModel.velocity, VELOCITY);
        outputs.put(schema.TrainModel.velocity, VELOCITY);
    }

    public void updateMass() {
        MASS = MIN_CAR_WEIGHT+(PASSENGER_COUNT+CREW_COUNT)*AVG_WEIGHT_PERSON;
    }

    public void getEmergencyBrakeStatus() {
        for(Car car : CARS) {
            if(car.getEmergencyBrakeStatus()) {
                EBRAKE = true;
                POWER = 0f;
            }
        }
    }

    public void setVelocity() {
        float force;
        if(VELOCITY == 0) force = MAX_CAR_WEIGHT*MED_ACCELERATION;
        else force = POWER/VELOCITY;

        float acceleration = force/MASS;
        VELOCITY = VELOCITY + (.5f)*(ACCELERATION + acceleration);
        ACCELERATION = acceleration;
    }

    public void updateCars() {
        for(Car car : CARS) {
            car.setTemperature(TEMPERATURE);
            car.lefttDoors(LEFT_DOORS);
            car.rightDoors(RIGHT_DOORS);
            car.interiorLights(INTERIOR_LIGHTS);
            car.exteriorLights(EXTERIOR_LIGHTS);
        }
    }
}


