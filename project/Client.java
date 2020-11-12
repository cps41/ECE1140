package project;
/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Client {
    private static final int PORTNUMBER = 0;
    private static final String cpuIP = "18.216.251.172";
    private static ServerInterface mini;
    private static boolean connected = false;
    
    
     //C:\Users\alexa\Desktop\DefaultTrackFile.txt
    
    
    ////////////////////////green///////////////////////////
    //green infrustructure
  	static String[] greenStation; //index = blocks; data = contain station names
  	static int[] greenSide; //index = blocks; data = station side (0=L, 1=R, 2=Both)
  	static int[] greenSwitch; //index = blocks; data = common switch number
  	static int[] greenDirection; //index = block, data = 0 forward, 1 backward, 2 both
  	static boolean[] greenCrossing; //index = blocks, data = 1 if contain crossing
  	//green detail
  	static int greenSize; //number of blocks
  	static double[] greenBlockLength;
  	static int[] greenSpeedLimit;
  	static double[] greenBlockGrade;
  	static boolean[] greenBelowGround; //0 = above, 1 = below
  	//green status
  	static boolean[] greenSwitchPosition;
  	static boolean[] greenTrainPresence;
  	static boolean[] greenCrossingStatus; 
  	static boolean[] greenStationStatus;
  	static boolean[] greenTrackStatus; //broken rail/maintenance condition
  	static boolean[] greenHeaters;
  	static boolean[] greenLight; //index = blocks; 1 = light is on
  	//green data
  	static String[] greenBeaconData;
  	//passed through data per block/train
  	static int[] greenSpeed;
  	static int[] greenAuthority;
  	static int[] greenPassengerCount;
  	
  	
  	////////////////////red/////////////////////////////////
  	//red infrustructure
  	static String[] redStation; //index = blocks; data = contain station names
  	static int[] redSide; //index = blocks; data = station side (0=L, 1=R, 2=Both)
  	static int[] redSwitch; //index = blocks; data = common switch number
  	static int[] redDirection; //index = block, data = 0 forward, 1 backward, 2 both
  	static boolean[] redCrossing; //index = blocks, data = 1 if contain crossing
  	static boolean[] redLight; //index = blocks; 1 = block contains light
  	//red detail
  	static int redSize;  //number of blocks
  	static double[] redBlockLength;
  	static int[] redSpeedLimit;
  	static double[] redBlockGrade;
  	static boolean[] redBelowGround; //0 = above, 1 = below
  	//red status
  	static boolean[] redSwitchPosition;
  	static boolean[] redTrainPresence;
  	static boolean[] redCrossingStatus; 
  	static boolean[] redStationStatus;
  	static boolean[] redTrackStatus; //broken rail/maintenance condition
  	static boolean[] redHeaters;
  	//red data
  	static String[] redBeaconData;
  	//passed through data per block/train
  	static int[] redSpeed;
  	static int[] redAuthority;
  	static int[] redPassengerCount;
  	
    
    public static void main(String[] args) 
    {
    	//initializing data
    	System.out.print("Enter track file name: "); //enter track file
    	Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        FileInputStream fin;
        BufferedReader reader;
        
	//////////////////////green line/////////////////////////////
		String[] greenStation = new String[200];
		int[] greenSide = new int[200];
		int[] greenSwitch = new int[200];
		int[] greenDirection = new int[200];
		boolean[] greenCrossing = new boolean[200];
		boolean[] greenLight = new boolean[200];
		double[] greenBlockLength = new double[200];
		int[] greenSpeedLimit = new int[200];
		double[] greenBlockGrade = new double[200];
		boolean[] greenBelowGround = new boolean[200];
		boolean[] greenSwitchPosition = new boolean[200];
		boolean[] greenTrainPresence = new boolean[200];
		boolean[] greenCrossingStatus = new boolean[200];
		boolean[] greenStationStatus = new boolean[200];
		boolean[] greenTrackStatus = new boolean[200];
		boolean[] greenHeaters = new boolean[200];
		String[] greenBeaconData = new String[200];
		
		int[] greenSpeed = new int[200];
		int[] greenAuthority = new int[200];
		int[] greenPassengerCount = new int[200];
		
		
	//////////////////red line//////////////////////////////
		String[] redStation = new String[200];
		int[] redSide = new int[200];
		int[] redSwitch = new int[200];
		int[] redDirection = new int[200];
		boolean[] redCrossing = new boolean[200];
		boolean[] redLight = new boolean[200];
		double[] redBlockLength = new double[200];
		int[] redSpeedLimit = new int[200];
		double[] redBlockGrade = new double[200];
		boolean[] redBelowGround = new boolean[200];
		boolean[] redSwitchPosition = new boolean[200];
		boolean[] redTrainPresence = new boolean[200];
		boolean[] redCrossingStatus = new boolean[200];
		boolean[] redStationStatus = new boolean[200];
		boolean[] redTrackStatus = new boolean[200];
		boolean[] redHeaters = new boolean[200];
		String[] redBeaconData = new String[200];
		
		int[] redSpeed = new int[200];
		int[] redAuthority = new int[200];
		int[] redPassengerCount = new int[200];
		
		
		try 
		{
			//////////////////////green line/////////////////////////////
			fin = new FileInputStream(fileName);
			reader = new BufferedReader(new InputStreamReader(fin));
			String temp = reader.readLine();
			String[] parts;
			
			greenSize = Integer.parseInt(temp);
			
			for(int i = 0; i < greenSize; i++) //i = block # - 1
			{
				temp = reader.readLine();
				parts = temp.split(" ");
				greenBlockLength[i] = Double.parseDouble(parts[1]);
				greenBlockGrade[i] = Double.parseDouble(parts[2]);
				greenSpeedLimit[i] = Integer.parseInt(parts[3]);
				if(parts[4].equals("0"))
					greenBelowGround[i] = false;
				else
					greenBelowGround[i] = true;
				greenDirection[i] = Integer.parseInt(parts[5]);
				greenSwitch[i] = Integer.valueOf(parts[6]);
				if(parts[7].equals("0"))
					greenCrossing[i] = false;
				else
					greenCrossing[i] = true;
				greenStation[i] = parts[8];
				greenSide[i] = Integer.parseInt(parts[9]);
			}
			temp = reader.readLine();
			parts = temp.split(" ");
			greenDirection[greenSize] = Integer.parseInt(parts[0]);
			greenSwitch[greenSize] = Integer.valueOf(parts[1]);
			temp = reader.readLine();
			parts = temp.split(" ");
			if(parts[0] != "-1")
			{
				greenDirection[greenSize+1] = Integer.parseInt(parts[0]);
				greenSwitch[greenSize+1] = Integer.valueOf(parts[1]);
			}
			
			//////////////////red line//////////////////////////////
			temp = reader.readLine();
			redSize = Integer.parseInt(temp);
			
			for(int i = 0; i < redSize; i++) //i = block # - 1
			{
				temp = reader.readLine();
				parts = temp.split(" ");
				redBlockLength[i] = Double.parseDouble(parts[1]);
				redBlockGrade[i] = Double.parseDouble(parts[2]);
				redSpeedLimit[i] = Integer.parseInt(parts[3]);
				if(parts[4].equals("0"))
					redBelowGround[i] = false;
				else
					redBelowGround[i] = true;
				redDirection[i] = Integer.parseInt(parts[5]);
				redSwitch[i] = Integer.valueOf(parts[6]);
				if(parts[7].equals("0"))
					redCrossing[i] = false;
				else
					redCrossing[i] = true;
				redStation[i] = parts[8];
				redSide[i] = Integer.parseInt(parts[9]);
			}
			temp = reader.readLine();
			parts = temp.split(" ");
			redDirection[redSize] = Integer.parseInt(parts[0]);
			redSwitch[redSize] = Integer.valueOf(parts[1]);
			temp = reader.readLine();
			parts = temp.split(" ");
			if(parts[0] != "-1")
			{
				redDirection[redSize+1] = Integer.parseInt(parts[0]);
				redSwitch[redSize+1] = Integer.valueOf(parts[1]);
			}
			
		}
		catch(Exception e) 
		{
			System.out.println(e);
		}
    
		TrackModelLogin mainLogin = new TrackModelLogin(); //opens main GUI following login
		
		
    	while(true)
    	{
    		
    		//send initializing data to server
    		
    		send("greenTrackSize", greenSize);
    		send("redTrackSize", redSize);
        	
    		int[][] greenConnectivity = new int[200][3];
    		for(int i = 0; i < greenSize+2; i++) //+2 to account for yard sections
    		{
    			greenConnectivity[i][0] = greenSwitch[i];
    			if(greenCrossing[i] == false)
    				greenConnectivity[i][1] = 0;
    			else
    				greenConnectivity[i][1] = 1;
    			greenConnectivity[i][2] = greenDirection[i];
    		}
    		send("greenConnectivity", greenConnectivity);
    		
    		int[][] redConnectivity = new int[200][3];
    		for(int i = 0; i < redSize+2; i++) //+2 to account for yard sections
    		{
    			redConnectivity[i][0] = redSwitch[i];
    			if(redCrossing[i] == false)
    				redConnectivity[i][1] = 0;
    			else
    				redConnectivity[i][1] = 1;
    			redConnectivity[i][2] = redDirection[i];
    		}
    		send("redConnectivity", redConnectivity);
    		
    		send("greenDestinations", greenStation);
    		send("redDestinations", redStation);
    		
    		double[][] greenConfiguration = new double[200][3];
    		for(int i = 0; i < greenSize; i++)
    		{
    			greenConfiguration[i][0] = greenBlockLength[i];
    			greenConfiguration[i][1] = greenBlockGrade[i];
    			greenConfiguration[i][2] = greenSpeedLimit[i];
    		}
    		send("greenConfiguration", greenConfiguration);
    		
    		double[][] redConfiguration = new double[200][3];
    		for(int i = 0; i < greenSize; i++)
    		{
    			redConfiguration[i][0] = redBlockLength[i];
    			redConfiguration[i][1] = redBlockGrade[i];
    			redConfiguration[i][2] = redSpeedLimit[i];
    		}
    		send("redConfiguration", redConfiguration);
    		
    		
    		
    		
        	//send/receive/update data continuously
    		
	    	////////////////receive///////////////////////
    		float trackControllerSpeed = (float)receive("trackControllerSpeed"); //receive speed from track controller
    		
	    	greenSwitchPosition = (boolean[])receive("greenPosition");
    		redSwitchPosition = (boolean[])receive("redPosition");
    		
    		
    		boolean greenStatus[][] = new boolean[200][2];  //receive green train presence from train (just for simulation)
	    	greenStatus = (boolean[][])receive("greenStatus");
	    	for(int i = 0; i < greenSize; i++)
	    	{
	    		greenTrainPresence[i] = greenStatus[i][0];
	    	}
	    	
	    	boolean redStatus[][] = new boolean[200][2];   //receive red train presence from train (just for simulation)
	    	redStatus = (boolean[][])receive("redStatus");
	    	for(int i = 0; i < redSize; i++)
	    	{
	    		redTrainPresence[i] = redStatus[i][0];
	    	}
    		
    		
    		//add authorityGreenCorrection and authorityRedCorrection (int - block #'s authority that needs to be set to 0)
    		
	    	///////////////////send///////////////////////
	    	send("TrackModelSpeed", trackControllerSpeed);
	    	
    		for(int i = 0; i < greenSize; i++)  //send train presence and track status to track controller
    		{
    			greenStatus[i][0] = greenTrainPresence[i];
    			greenStatus[i][1] = greenTrackStatus[i];
    		}
    		send("greenStatus", greenStatus);
    		
    		for(int i = 0; i < redSize; i++)
    		{
    			redStatus[i][0] = redTrainPresence[i];
    			redStatus[i][1] = redTrackStatus[i];
    		}
    		send("redStatus", redStatus);
    		
    		
    		
    		
    		
    		///////////////////updates/////////////////////////
    		for(int i = 0; i < greenSize; i++) //green crossing status & lights
    		{
    			if(greenCrossing[i] == true && greenTrainPresence[i] == true)
    			{
    				greenLight[i] = true;
    				greenCrossingStatus[i] = true;
    			}
    			else
    			{
    				greenLight[i] = false;
    				greenCrossingStatus[i] = false;
    			}
    		}
    		
    		for(int i = 0; i < redSize; i++) //red crossing status & lights
    		{
    			if(redCrossing[i] == true && redTrainPresence[i] == true)
    			{
    				redLight[i] = true;
    				redCrossingStatus[i] = true;
    			}
    			else
    			{
    				redLight[i] = false;
    				redCrossingStatus[i] = false;
    			}
    		}
    		
    		for(int i = 0; i < greenSize; i++) //green station status
    		{
    			if(!(greenStation[i].equals("0")) && greenTrainPresence[i] == true)
    				greenStationStatus[i] = true;
    			else
    				greenStationStatus[i] = false;
    		}
	    	
    		for(int i = 0; i < redSize; i++) //red station status
    		{
    			if(!(redStation[i].equals("0")) && redTrainPresence[i] == true)
    				redStationStatus[i] = true;
    			else
    				redStationStatus[i] = false;
    		}
    	}
    	
    	
    	
    }
    
    
    
    
    static boolean send(String key, Object value) 
    {
        if (!connected) 
        {            
        	connectClient();
        }
        try 
        {
            mini.serverSend(key, value);
            return true;
        } 
        catch (Exception e) 
        {
            System.out.printf("Failed to set %s to %s\n", key, value);
            return false;
        }
    }
    
    static Object receive(String key) 
    {
        if (!connected) 
        {
            connectClient();
        }
        try 
        {
            return mini.serverReceive(key);
        } 
        catch (Exception e) 
        {
            System.out.printf("Failed to receive %s\n", key);
        }
        return null;
    }
    
    private static void connectClient() 
    {
        if (!connected) 
        {
            try
            {
                Registry registry = LocateRegistry.getRegistry(cpuIP, PORTNUMBER);
                mini = (ServerInterface) registry.lookup("ServerInterface");
                connected = true;
            } 
            catch (Exception e) 
            {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }
    
    static void call(String key, Object...argv) 
    {
        if (!connected) 
        {
            connectClient();
        }
        try 
        {
            mini.serverCall(key, argv);
        } 
        catch (Exception e) 
        {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
    static Object[] getCall(String key) 
    {
        if (!connected) 
        {
            connectClient();
        }
        try 
        {
            return mini.serverGetCall(key);
        } 
        catch (Exception e) 
        {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }
}