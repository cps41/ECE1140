package project;

public class schema {
    public class TrainModel {
        public static final String train_keys = "TrainModelKeys";
        public static final String trains = "TrainModelTrainNodes";
        public static final String train_info = "TrainModelTrainInfo";
        public static final String red_presence = "TrainModelRedStatus";
        public static final String green_presence = "TrainModelGreenStatus";
        public static final String red_size = "TrainModelRedSize";
        public static final String green_size = "TrainModelGreenSize";
        public static final String ry = "TrainModelRedToYard";
        public static final String gy = "TrainModelGreenToYard";
        public static final String rp = "TrainModelRedPassCount";
        public static final String gp = "TrainModelGreenPassCount";
        public static final String rc = "red";
        public static final String gc = "green";
        public static final String sag = "StationArrayGreen";
        public static final String sar = "StationArrayRed";
    }

    public class TrainController {
        public static final String train_nodes = "TrainControllerTrainNodes";
        public static final String red_status = "TrainControllerRedStatus";
        public static final String green_status = "TrainControllerGreenStatus";
        public static final String ry = "TrainControllerRedToYard";
        public static final String gy = "TrainControllerGreenToYard";
    }

    public class TrackModel {
        public static final String pass_count = "TrackModelPassengerCount";
        public static final String red_beacon = "redBeaconData";
        public static final String green_beacon = "greenBeaconData";
        public static final String train_keys = "TrackModelTrainKeys";
        public static final String trains = "TrackModelTrainNodes";
        public static final String red_size = "redTrackSize";
        public static final String green_size = "greenTrackSize";
        public static final String red_pass = "redPassengerCount";
        public static final String green_pass = "greenPassengerCount";
        public static final String ry = "TrackModelRedToYard";
        public static final String gy = "TrackModelGreenToYard";
        public static final String rc = "TrackModelAuthorityRedCorrection";
        public static final String gc = "TrackModelAuthorityGreenCorrection";
    }
}