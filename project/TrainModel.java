package project;

import java.util.*;

public class TrainModel {
    public String KEY;
    public float POWER; // key input
    public float VELOCITY; // key output
    public int AUTHORITY; // in meters
    public boolean BRAKES;
    public String BEACON;
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
    public boolean LEFT_DOORS_STATION;
    public boolean RIGHT_DOORS_STATION;
    public String STATION;
    public float TEMPERATURE;
    public Float[] BLOCK;
    public ArrayList<Car> CARS;
    public final float CAR_LENGTH = 32.2f; // in meters
    public final float CAR_HEIGHT = 3.42f; // in meters
    public final float CAR_WIDTH = 2.65f; // in meters
    public final float MAX_POWER = 120f; // in kW
    public final float MIN_CAR_WEIGHT = 40900f; // in kg
    public final float MAX_CAR_WEIGHT = 56700; // in kg
    public final float MED_ACCELERATION = 0.0005f; // in km/s^2
    public final float AVG_WEIGHT_PERSON = 62f; // in kg
    public final float GRAVITATIONAL_FORCE = 0.009807f; // in km/s^2
    public HashMap <String, Object> outputs;

    public TrainModel(int num_cars, String key) {
        KEY = key;
        POWER = 0.0f;
        VELOCITY = 0.0f;
        AUTHORITY = 0;
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
        LEFT_DOORS_STATION = true;
        RIGHT_DOORS_STATION = true;
        TEMPERATURE = 70;
        STATION = "";
        BEACON = " ";
        BLOCK = new Float[3];
        BLOCK[0] = 1f; BLOCK[1] = 1f; BLOCK[2] = 1f;
        outputs = new HashMap<>();
    }

    public void decodeBeacon(String beacon) throws InterruptedException {
        try {
            int doors = Integer.parseInt(beacon.charAt(0)+"");
            switch(doors) {
                case 0:
                    LEFT_DOORS_STATION = false;
                    RIGHT_DOORS_STATION = true;
                    break;
                case 1:
                    LEFT_DOORS_STATION = true;
                    RIGHT_DOORS_STATION = false;
                    break;
                case 2:
                    LEFT_DOORS_STATION = false;
                    RIGHT_DOORS_STATION = false;
                    break;
            }
            STATION = beacon.substring(1);
        }catch(Exception e) {
            return;
        }
        if(!BEACON.equals(beacon)) {
            BEACON = beacon;
            stationArrival();
        }
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
        // if(POWER == 0) {
        //     VELOCITY = 0;
        //     return;
        // }
        float block_length = BLOCK[2];
        double elevation = BLOCK[0]*block_length*0.00001;
        double angle = Math.atan(Math.toRadians(elevation/block_length));
        double gravity = GRAVITATIONAL_FORCE*Math.sin(angle);
        double force;
        if(VELOCITY == 0) force = MAX_CAR_WEIGHT*MED_ACCELERATION - gravity;
        else {
            force = (POWER/VELOCITY);
        }

        float acceleration = (float) force/MASS;
        System.out.println(acceleration);
        VELOCITY = VELOCITY + (float)(.5/2)*(ACCELERATION + acceleration);
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

    public void stationArrival() throws InterruptedException {
        POWER = 0f;
        while(VELOCITY != 0f) {
            setVelocity();
        }
        LEFT_DOORS = LEFT_DOORS_STATION;
        RIGHT_DOORS = RIGHT_DOORS_STATION;
        Thread.sleep(60000);
        LEFT_DOORS = true;
        RIGHT_DOORS = true;
    }
}


