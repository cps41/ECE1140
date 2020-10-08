package project;

public class trackController {
	
	static int speed = 0;
	static int authority = 0;
	static boolean brokenRail = true;
	static boolean trainPresence = false;

	public trackController() {

	}
	
	//setters and getters
	public void setSwitchPosition(String line, int section, boolean position) {
	
	}
	
	public boolean getSwitchPosition(String line, int section) {
		return true;
	}
	
	public void setCrossingStatus(String line, int section, boolean active) {
		
	}
	
	public boolean getCrossingStatus(String line, int section) {
		return true;
	}
	
	public boolean getTrackOccupancy(String line, int section) {
		return true;
	}
	
	public void setSpeed(int newSpeed) {
		speed = newSpeed;
	}
	
	public void setAuthority(int newAuthority) {
		authority = newAuthority;
	}
	
	public void setTrackStatus(boolean broken) {
		brokenRail = broken; 
	}
	
	public void setTrainPresence(boolean presence) {
		trainPresence = presence; 
	}
}
