package project;

public class schema {
    public class TrainModel {
        public static final String velocity = "TrainModelVelocity";
        public static final String speed = "TrainModelSpeed";
        public static final String authority = "TrainModelAuthority";
        public static final String power = "TrainModelPower";
        public static final String length = "TrainModelLength";
        public static final String mass = "TrainModelMass";
        public static final String emergency_brake = "TrainModelEmergencyBrake";
        public static final String door_side = "TrainModelDoorSide";
        public static final String station = "TrainModelStationName";
    }

    public class TrainController {
        public static final String power = "TrainControllerPower";
        public static final String brake = "TrainControllerBrake";
        public static final String velocity = "TrainControllerVelocity";
        public static final String interior_lights = "TrainControllerInteriorLights";
        public static final String exterior_lights = "TrainControllerExteriorLights";
        public static final String left_doors = "TrainControllerLeftDoors";
        public static final String right_doors = "TrainControllerRightDoors";
		public static final String authority = "TrainControllerAuthority";
    }

    public class TrackController {
        public static final String speed = "trackControllerSpeed";
        public static final String authority = "trackControllerAuthority";
        public static final String red_presence = "redPresence";
        public static final String green_presence = "greenPresence";
        public static final String authority_red = "authorityRedCorrection";
        public static final String authority_green = "authorityGreenCorrection";
    }

    public class TrackModel {
        public static final String speed_limit = "TrackModelSpeedLimit";
        public static final String setpoint_speed = "TrackModelSpeed";
        public static final String authority = "TrackModelAuthority";
        public static final String pass_count = "TrackModelPassengerCount";
        public static final String crew_count = "TrackModelCrewCount";
        public static final String beacon = "beaconData";
        public static final String red_size = "redTrackSize";
        public static final String green_size = "greenTrackSize";
        public static final String red_connectivity = "redConnectivity";
        public static final String green_connectivity = "greenConnectivity";
        public static final String red_status = "redStatus";
        public static final String green_status = "greenStatus";
        public static final String block = "currentBlock";
        public static final String cars = "numberOfTrainCars";
    }

    public class CTCOffice {
        public static final String signal = "CTCSignal";      
    }

    public static void main(String[] args) {
        String var = TrainModel.velocity;
        System.out.println(var);
    }
}