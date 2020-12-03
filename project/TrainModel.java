package project;

import java.util.*;


public class TrainModel {
    public int KEY, AUTHORITY, CREW_COUNT, CURRENT_BLOCK, STATION_TIMER, 
               PASSENGER_COUNT, LAST_STATION, OFF_COUNT, TIME, DESTINATION;
    public double VELOCITY; // key output
    public String BEACON, STATION;
    public String[] RED_BEACON_ARRAY, GREEN_BEACON_ARRAY;
    public float POWER, LENGTH, MASS, TEMPERATURE; // in tons
    public double ACCELERATION; // in km/s^2
    public int[] PASSENGERS_ARRAY;
    public Float[] BLOCK;
    public ArrayList<Car> CARS;
    public boolean INTERIOR_LIGHTS, EXTERIOR_LIGHTS, LEFT_DOORS, RIGHT_DOORS, 
                   LEFT_DOORS_STATION, RIGHT_DOORS_STATION, BRAKES, EBRAKE, 
                   LINE, ARRIVING, AT_STATION, DONE, REACHED_DESTINATION;

    public final float EBRAKE_ACC = -2.73f;
    public final float MIN_ACC = -1.2f;
    public final float MAX_ACC = 0.5f;
    public final float CAR_LENGTH = 32.2f; // in meters
    public final float CAR_HEIGHT = 3.42f; // in meters
    public final float CAR_WIDTH = 2.65f; // in meters
    public final float MAX_POWER = 120f; // in kW
    public final float MIN_CAR_WEIGHT = 40900f; // in kg
    public final float MAX_CAR_WEIGHT = 56700f; // in kg
    public final float AVG_WEIGHT_PERSON = 62f; // in kg
    public final float GRAVITATIONAL_FORCE = 9.807f; // in m/s^2

    public TrainModel(ArrayList<Object> info, int key, int[] gp, int[] rp, String[] red_beacon, String[] green_beacon) {
        KEY = key;
        POWER = 0.0f;
        VELOCITY = 0.0f;
        AUTHORITY = 0;
        CARS = new ArrayList<>();
        for(int i=0; i<=(int) info.get(5); i++) {
            CARS.add(new Car());
        }
        LENGTH = CAR_LENGTH*(int) info.get(5);
        LINE = (boolean) info.get(4);
        CREW_COUNT = (int) info.get(3);
        BRAKES = true;
        MASS = MIN_CAR_WEIGHT;
        EBRAKE = false;
        INTERIOR_LIGHTS = false;
        EXTERIOR_LIGHTS = false;
        LEFT_DOORS = true;
        RIGHT_DOORS = true;
        LEFT_DOORS_STATION = true;
        RIGHT_DOORS_STATION = true;
        TEMPERATURE = 68;
        STATION = "";
        RED_BEACON_ARRAY = red_beacon;
        GREEN_BEACON_ARRAY = green_beacon;
        CURRENT_BLOCK = 0;
        PASSENGER_COUNT = 0;
        LAST_STATION = 0;
        if(LINE) {
            BEACON = GREEN_BEACON_ARRAY[CURRENT_BLOCK];
            PASSENGERS_ARRAY = gp;
        }
        else {
            BEACON = RED_BEACON_ARRAY[CURRENT_BLOCK];
            PASSENGERS_ARRAY = rp;
        }
        BLOCK = new Float[3];
        BLOCK[0] = 1f; BLOCK[1] = 1f; BLOCK[2] = 1f;
        TIME = 1;
        AT_STATION = false;
        BEACON = "";
        DESTINATION = (int) info.get(7);
        REACHED_DESTINATION = false;
    }

    public void decodeBeacon() {
        System.out.println("Decoding beacon... current beacon = "+BEACON);
        String beacon;
        // set beacon based on line
        if(LINE) beacon = GREEN_BEACON_ARRAY[CURRENT_BLOCK];
        else beacon = RED_BEACON_ARRAY[CURRENT_BLOCK];
        if(beacon == null) return;

        // set station door status based on beacon value
        try {
            int doors = Integer.parseInt(beacon.charAt(0)+"");
            switch(doors) {
                case 0: // left doors open
                    LEFT_DOORS_STATION = false;
                    RIGHT_DOORS_STATION = true;
                    break;
                case 1: // right doors open
                    LEFT_DOORS_STATION = true;
                    RIGHT_DOORS_STATION = false;
                    break;
                case 2: //both doors open
                    LEFT_DOORS_STATION = false;
                    RIGHT_DOORS_STATION = false;
                    break;
                default:
                    break;
            }
            // rest of beacon is the station name
            STATION = beacon.substring(1);
        }catch(Exception e) {
            System.out.println(e);
            return;
        }

        // if a new beacon is detected initiate station arrival sequence
        if(!BEACON.equals(beacon)) {
            BEACON = beacon; // set beacon to new beacon
            LAST_STATION = CURRENT_BLOCK;
            ARRIVING = true; // initiate stopAtStation
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
        System.out.println("Setting velocity....");
        double angle = Math.atan(Math.toRadians(BLOCK[0]));
        double gravity = GRAVITATIONAL_FORCE*Math.sin(angle);
        double force;
        double v = VELOCITY/3.6; // convert km/h to m/s

        if(POWER < 0 || EBRAKE || BRAKES) POWER = 0;
        double p = POWER*1000; // convert kW to W
        if(VELOCITY == 0) force = MASS*MAX_ACC-gravity;
        else force = p/v;

        double acceleration = (force/MASS);
        if(acceleration == 0) acceleration = MIN_ACC;
        if(acceleration > MAX_ACC) acceleration = MAX_ACC;
        if(BRAKES) acceleration = MIN_ACC;
        if(EBRAKE) acceleration = EBRAKE_ACC;
        else VELOCITY = (VELOCITY + (3.6)*TIME*(ACCELERATION + acceleration));
        ACCELERATION = acceleration;
        if (VELOCITY>70f) VELOCITY = 70f;
        if(VELOCITY>BLOCK[1]) VELOCITY = BLOCK[1];
        if (VELOCITY<0f) VELOCITY = 0f;
        System.out.println("Velocity: "+VELOCITY+", Acceleration: "+ACCELERATION);
    }

    public void updateCars() {
        for(Car car : CARS) {
            car.setTemperature(TEMPERATURE);
            car.leftDoors(LEFT_DOORS);
            car.rightDoors(RIGHT_DOORS);
            car.interiorLights(INTERIOR_LIGHTS);
            car.exteriorLights(EXTERIOR_LIGHTS);
        }
    }

    public void stopAtStation() {
        // stopping sequence initiated upon new beacon
        if(ARRIVING) {
            System.out.println("Arriving at station....");
            BRAKES = true; // override train controller
            if(VELOCITY != 0f) { // train is not stopped yet
                setVelocity();
            }
            else {
                if(LINE && CURRENT_BLOCK == 56) {
                    OFF_COUNT += PASSENGER_COUNT;
                    PASSENGER_COUNT = 0;
                }
                else if(CURRENT_BLOCK == 7) {
                    OFF_COUNT += PASSENGER_COUNT;
                    PASSENGER_COUNT = 0;
                }
                AT_STATION = true; // train is stopped we are at the station and initiate stationArrival
                Random rand = new Random();
                int off_count;
                if(PASSENGER_COUNT>0) off_count = rand.nextInt(PASSENGER_COUNT);
                else off_count = 0;
                PASSENGER_COUNT -= off_count; // passengers get off train
                OFF_COUNT += off_count;
                System.out.println(PASSENGER_COUNT);
            }
        }
    }

    public void stationArrival() {
        // train is at station and doors must open
        if(AT_STATION) {
            System.out.println("At station!!!!!!!!!!");
            LEFT_DOORS = LEFT_DOORS_STATION;
            RIGHT_DOORS = RIGHT_DOORS_STATION;
        }
    }

    public void stationStop() {
        // train is at station and must stop for at least 60 seconds
        if(AT_STATION) {
            // train is still waiting at station
            if(STATION_TIMER < Math.ceil((double) 60/TIME)) {
                System.out.println("Timer: "+STATION_TIMER);
                updateMass();
                STATION_TIMER++;
            }
            // train is ready to leave station
            else {
                PASSENGER_COUNT += PASSENGERS_ARRAY[CURRENT_BLOCK]; // all passengers at station get on
                System.out.println(PASSENGERS_ARRAY[CURRENT_BLOCK]);
                updateMass();
                // reset values for station arrival sequence
                ARRIVING = false;
                AT_STATION = false;
                STATION_TIMER = 0;
                // close doors
                LEFT_DOORS = true;
                RIGHT_DOORS = true;

                OFF_COUNT = 0;
                if(CURRENT_BLOCK == DESTINATION) REACHED_DESTINATION = true;
                System.out.println("LEAVING");
            }
        }
    }
}


