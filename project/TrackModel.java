package project;

public class TrackModel 
{
	//set for blue - must still extend for green and red line
	
	//infrustructure
	static boolean[] stations;
	static boolean[] switches;
	static boolean[] crossings;
	
	//detail
	static int[] blockLength;
	static int[] speedLimit;
	static int[] blockGrade;
	static int[] cumulativeElevation;
	
	//status
	static boolean[] switchPosition;
	//static boolean[] crossingStatus; 
	//static boolean[] stationStatus;
	//static boolean[] trackStatus; //broken rail condition
	
	//data
	//static double[] beaconData;
	
	//passed data
	static int speed;
	static int authority;
	
	
	public TrackModel()
	{
		//initialize for blue line
		switchPosition = new boolean[1];
	}
	
	//details
	public int getBlockLength(String line, int block)
	{
		return 50;
	}
	
	public int getSpeedLimit(String line, int block)
	{
		return 50;
	}
	
	public int getBlockGrade(String line, int block)
	{
		return 0;
	}
	
	public int getCumulativeElevation(String line, int block)
	{
		return 0;
	}
	
	//status
	public boolean getSwitchPosition(String line, int block)
	{
		return true;
	}
	/*
	public boolean getCrossingStatus(String line, int section)
	{
		return false;
	}
	
	public boolean getStationStatus(String line, int block)
	{
		return false;
	}
	
	public void setTrackStatus(int newStatus) 
	{
		trackStatus = newStatus;
	}
	
	public int getTrackStatus()
	{
		return trackStatus;
	}
	*/
	
	//passing data
	public void setSpeed(int newSpeed)
	{
		speed = newSpeed;
	}
	
	public int getSpeed()
	{
		return speed;
	}
	
	public void setAuthority(int newAuthority)
	{
		authority = newAuthority;
	}
	
	public int getAuthority()
	{
		return authority;
	}
}