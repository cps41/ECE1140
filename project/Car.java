package project;

public class Car {
    private boolean EmergencyBrake; //false = not used
    private float Temperature;
    private boolean Lights; //false = off
    private boolean LeftDoors; //false = closed
    private boolean RightDoors; //false = closed

    public Car() {
        EmergencyBrake = false;
        Temperature = 70.0f;
        Lights = false;
        LeftDoors = false;
        RightDoors = false;
    }

    public boolean getEmergencyBrakeStatus() {
        return EmergencyBrake;
    }

    public void setTemperature(float temp) {
        Temperature = temp;
    }

    public boolean lights(boolean status) {
        Lights = status;
        return Lights;
    }

    public boolean lefttDoors(boolean status) {
        LeftDoors = status;
        return LeftDoors;
    }

    public boolean rightDoors(boolean status) {
        RightDoors = status;
        return RightDoors;
    }
}
