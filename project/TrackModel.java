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
	static boolean[] trackStatus; //broken rail condition
	
	//data
	//static double[] beaconData;
	
	//passed data
	static int speed;
	static int authority;
	
	
	public static void main(String[] args)
	{
		TrackModelLogin loginDisplay = new TrackModelLogin(); //start GUI
		
		//initialize for blue line
		switchPosition = new boolean[1];
	}
	
	public void getSwitchPosition(String line, int block)//track only get switch status, not set switch
	{
		return true;
	}
	
	public void getCrossingStatus(String line, int section)
	{
		return false;
	}
	
}
