package project;

public class Car {
    private boolean EmergencyBrake; //false = not used
    private float Temperature;
    private boolean InteriorLights; //false = off
    private boolean ExteriorLights; //false = off
    private boolean LeftDoors; //false = closed
    private boolean RightDoors; //false = closed

    public Car() {
        EmergencyBrake = false;
        Temperature = 70.0f;
        InteriorLights = false;
        ExteriorLights = false;
        LeftDoors = true;
        RightDoors = true;
    }

    public boolean getEmergencyBrakeStatus() {
        return EmergencyBrake;
    }

    public void setTemperature(float temp) {
        Temperature = temp;
    }

    public boolean lefttDoors(boolean status) {
        LeftDoors = status;
        return LeftDoors;
    }

    public boolean rightDoors(boolean status) {
        RightDoors = status;
        return RightDoors;
    }

	public void interiorLights(boolean iNTERIOR_LIGHTS) {
        InteriorLights = iNTERIOR_LIGHTS;
	}

	public void exteriorLights(boolean eXTERIOR_LIGHTS) {
        ExteriorLights = eXTERIOR_LIGHTS;
	}
}
