/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.Scanner;
import java.io.*;

public class Client {
    private static final int PORTNUMBER = 0;
    private static final String cpuIP = "18.216.251.172";
    private static ServerInterface mini;
    private static boolean connected = false;
    
    
    //C:\Users\alexa\Desktop\DefaultTrackFile.txt
    
    //C:\Users\alexa\eclipse-workspace\Track Model2\src
    
    //ssh ec2-user@18.216.251.172
    
    //////////////////Defining variables////////////////////
    
    ////////////////////////green///////////////////////////
    //green infrastructure
    static String[] greenSections; //sections letters
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
  	static double[] greenBlockElevation;
  	static boolean[] greenBelowGround; //0 = above, 1 = below
  	//green status
  	static Boolean[] greenSwitchPosition;
  	static Boolean[] greenTrainPresence;
  	static boolean[] greenCrossingStatus; 
  	static boolean[] greenStationStatus;
  	static boolean[] greenTrackStatus; //broken rail failure condition
  	static boolean[] greenCircuitStatus; //broken track circuit failure condition
  	static boolean[] greenPowerStatus; //power outage failure condition
  	static Boolean[] greenMaintenance; //close block signal from CTC
  	static boolean[] greenHeaters;
  	static boolean[] greenLight; //index = blocks; 1 = light is on
  	//green data
  	static String[] greenBeaconData;
  	static int[] greenPassengerCount; //random number of passengers at stations
  	static Integer[] TrainModelGreenPassCount;  //number of passengers currently getting off at each station
  	static int greenPassengers; //total number of passengers from every station
    
    static Object TrainModelGreenPassCountObj;
  	
  	
  	////////////////////red/////////////////////////////////
  	//red infrastructure
  	static String[] redSections; //section letters
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
  	static double[] redBlockElevation;
  	static boolean[] redBelowGround; //0 = above, 1 = below
  	//red status
  	static Boolean[] redSwitchPosition;
  	static Boolean[] redTrainPresence;
  	static boolean[] redCrossingStatus; 
  	static boolean[] redStationStatus;
  	static boolean[] redTrackStatus; //broken rail/maintenance condition
  	static boolean[] redCircuitStatus; //broken track circuit failure condition
  	static boolean[] redPowerStatus; //power outage failure condition
  	static Boolean[] redMaintenance; //close block signal from CTC
  	static boolean[] redHeaters;
  	//red data
  	static String[] redBeaconData;
  	static int[] redPassengerCount;
  	static Integer[] TrainModelRedPassCount;  //number of passengers currently getting off at each station
  	static int redPassengers; //total number of passengers from every station
  	
  	static Object TrainModelRedPassCountObj;
  	
  	static Random rand = new Random();
    
    public static void main(String[] args) 
    {
    	//initializing data
    	System.out.print("Enter track file name: "); //enter track file
    	Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        FileInputStream fin;
        BufferedReader reader;
        
        
	//////////////////////green line/////////////////////////////
        greenSections = new String[200];
		greenStation = new String[200];
		greenSide = new int[200];
		greenSwitch = new int[200];
		greenDirection = new int[200];
		greenCrossing = new boolean[200];
		greenLight = new boolean[200];
		greenBlockLength = new double[200];
		greenSpeedLimit = new int[200];
		greenBlockGrade = new double[200];
		greenBlockElevation = new double[200];
		greenBelowGround = new boolean[200];
		greenSwitchPosition = new Boolean[200];
		greenTrainPresence = new Boolean[200];
		greenCrossingStatus = new boolean[200];
		greenStationStatus = new boolean[200];
		greenTrackStatus = new boolean[200];
		greenCircuitStatus = new boolean[200];
		greenPowerStatus = new boolean[200];
		greenMaintenance = new Boolean[200];
		greenHeaters = new boolean[200];
		greenBeaconData = new String[200];
		TrainModelGreenPassCount = new Integer[200];
		greenPassengerCount = new int[200];
		
	//////////////////red line//////////////////////////////
		redSections = new String[200];
		redStation = new String[200];
		redSide = new int[200];
		redSwitch = new int[200];
		redDirection = new int[200];
		redCrossing = new boolean[200];
		redLight = new boolean[200];
		redBlockLength = new double[200];
		redSpeedLimit = new int[200];
		redBlockGrade = new double[200];
		redBlockElevation = new double[200];
		redBelowGround = new boolean[200];
		redSwitchPosition = new Boolean[200];
		redTrainPresence = new Boolean[200];
		redCrossingStatus = new boolean[200];
		redStationStatus = new boolean[200];
		redTrackStatus = new boolean[200];
		redCircuitStatus = new boolean[200];
		redPowerStatus = new boolean[200];
		redMaintenance = new Boolean[200];
		redHeaters = new boolean[200];
		redBeaconData = new String[200];
		TrainModelRedPassCount = new Integer[200];
		redPassengerCount = new int[200];
		
		
		////////////parsing input configuration .txt file////////////////
		try 
		{
			System.out.println("Reading track configuration file");
			//////////////////////green line/////////////////////////////
			fin = new FileInputStream(fileName);
			reader = new BufferedReader(new InputStreamReader(fin));
			String temp = reader.readLine();
			String[] parts;
			
			greenSize = Integer.parseInt(temp);
			
			for(int i = 1; i <= greenSize; i++) //i = block # - 1
			{
				temp = reader.readLine();
				parts = temp.split(" ");
				greenSections[i] = parts[0];
				greenBlockLength[i] = Double.parseDouble(parts[1]);
				greenBlockGrade[i] = Double.parseDouble(parts[2]);
				greenBlockElevation[i] = greenBlockLength[i] * greenBlockGrade[i] / 100;
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
			greenDirection[greenSize+1] = Integer.parseInt(parts[0]);
			greenSwitch[greenSize+1] = Integer.valueOf(parts[1]);
			temp = reader.readLine();
			parts = temp.split(" ");
			greenDirection[greenSize+2] = Integer.parseInt(parts[0]);  //must set even if -1 since I rely on this to test # of yard blocks
			if(parts[0] != "-1")
				greenSwitch[greenSize+2] = Integer.valueOf(parts[1]);
			
			//////////////////red line//////////////////////////////
			temp = reader.readLine();
			redSize = Integer.parseInt(temp);
			
			for(int i = 1; i <= redSize; i++) //i = block # - 1
			{
				temp = reader.readLine();
				parts = temp.split(" ");
				redSections[i] = parts[0];
				redBlockLength[i] = Double.parseDouble(parts[1]);
				redBlockGrade[i] = Double.parseDouble(parts[2]);
				redBlockElevation[i] = redBlockLength[i] * redBlockGrade[i] / 100;
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
			redDirection[redSize+1] = Integer.parseInt(parts[0]);
			redSwitch[redSize+1] = Integer.valueOf(parts[1]);
			temp = reader.readLine();
			parts = temp.split(" ");
			redDirection[redSize+2] = Integer.parseInt(parts[0]); //must set even if -1 since I rely on this to test # of yard blocks
			if(parts[0] != "-1")
				redSwitch[redSize+2] = Integer.valueOf(parts[1]);
			
		}
		catch(Exception e) 
		{
			System.out.println(e);
		}
		
		
		////////////Call main login GUI -> which calls main Track Model GUI//////////////
		TrackModelLogin mainLogin = new TrackModelLogin();
		
		
		/////////////////Initialize Static Configuration Variables///////////////////////////
		
		// *ConfigurationInt:   	(index = block #)
		//						[][0] = Blocks w/ common switch #
		//						[][1] = crossing (0,1)
		//						[][2] = direction (0,1,2)
		//						[][3] = blocks speed limit
		
		int[][] greenConfigurationInt = new int[200][4];
		for(int i = 1; i <= greenSize+2; i++) //+2 to account for yard sections @ end
		{
			greenConfigurationInt[i][0] = greenSwitch[i];
			if(greenCrossing[i] == false)
				greenConfigurationInt[i][1] = 0;
			else
				greenConfigurationInt[i][1] = 1;
			greenConfigurationInt[i][2] = greenDirection[i];
			greenConfigurationInt[i][3] = greenSpeedLimit[i];
		}
		
		int[][] redConfigurationInt = new int[200][4];
		for(int i = 1; i <= redSize+2; i++) //+2 to account for yard sections @ end
		{
			redConfigurationInt[i][0] = redSwitch[i];
			if(redCrossing[i] == false)
				redConfigurationInt[i][1] = 0;
			else
				redConfigurationInt[i][1] = 1;
			redConfigurationInt[i][2] = redDirection[i];
			redConfigurationInt[i][3] = redSpeedLimit[i];
		}
		
		// *ConfigurationFloat:   	(index = block #)
		//						[][0] = Blocks length
		//						[][1] = Block grade
		
		float[][] greenConfigurationFloat = new float[200][2];
		for(int i = 1; i <= greenSize; i++)
		{
			greenConfigurationFloat[i][0] = (float)greenBlockLength[i];
			greenConfigurationFloat[i][1] = (float)greenBlockGrade[i];
		}
		
		float[][] redConfigurationFloat = new float[200][2];
		for(int i = 1; i <= redSize; i++)
		{
			redConfigurationFloat[i][0] = (float)redBlockLength[i];
			redConfigurationFloat[i][1] = (float)redBlockGrade[i];
		}
		
		// *ConfigurationString:   	(index = block #)
		//						[][0] = Blocks section letter
		//						[][1] = Station names for appropriate blocks
		
		String[][] greenConfigurationString = new String[200][2];
		for(int i = 1; i <= greenSize; i++)
		{
			greenConfigurationString[i][0] = greenSections[i];
			greenConfigurationString[i][1] = greenStation[i];
		}
		
		String[][] redConfigurationString = new String[200][2];
		for(int i = 1; i <= redSize; i++)
		{
			redConfigurationString[i][0] = redSections[i];
			redConfigurationString[i][1] = redStation[i];
		}
		
		//beaconData for each station on green line
		String[] greenBeaconData = new String[200];
		for(int i = 1; i <= greenSize; i++)
		{
			if(greenStation[i].length() != 1)//if there is a station
			{
				greenBeaconData[i] = greenSide[i] + greenStation[i];
			}
		}
		
		//beaconData for each station on red line
		String[] redBeaconData = new String[200];
		for(int i = 1; i <= redSize; i++)
		{
			if(redStation[i].length() != 1)//if there is a station
			{
				redBeaconData[i] = redSide[i] + redStation[i];
			}
		}
		System.out.println("Initializing static configuration variables");
		
		//////////////Initialize Status/Changing Variables///////////////////	
		
		greenPassengers = 0; //set number of total passenger to 0
		redPassengers = 0;
		
		boolean[] greenStationFlag = new boolean[200];
		boolean[] redStationFlag = new boolean[200];
		
		//local variables used with server calls
		boolean greenStatus[][] = new boolean[200][4];  //receive green train presence from train (just for simulation)
		boolean redStatus[][] = new boolean[200][4];   //receive red train presence from train (just for simulation)
		
		boolean TrackModelGreenToYard = false; //signal to trigger train to go back to yard
		boolean TrackModelRedToYard = false;

		//objects for testing whether received value or not yet, test for null
		Object trainNodes;       
		Object TrackModelGreenToYardObj;
		Object greenSwitchPositionObj;
		Object greenTrainPresenceObj;
		Object redTrainPresenceObj;
		Object greenStationStatusObj;
		Object TrackControllerGreenMaintenanceObj;
		
		System.out.println("Initializing dynamic configuration variables");
		
		int k = 1; //keep track of server call iterations
		///////////////Server Calls//////////////////////
    	while(true)
    	{
    		/////////////////initialize/////////////////////// must send continuously to allow modules to start anytime
    		send("greenTrackSize", greenSize); //track size for each line
    		send("redTrackSize", redSize);
        	
    		send("greenConfigurationInt", greenConfigurationInt); 
    		send("redConfigurationInt", redConfigurationInt);
    		
    		send("greenConfigurationFloat", greenConfigurationFloat);
    		send("redConfigurationFloat", redConfigurationFloat);
    		
    		send("greenConfigurationString", greenConfigurationString);
    		send("redConfigurationString", redConfigurationString);
    		
    		send("greenBeaconData", greenBeaconData); //beacon data for each station for each line
    		send("redBeaconData", redBeaconData);
    		
    		send("greenUnderground", greenBelowGround);
    		send("redUnderground", redBelowGround);
    		
	    	////////////////receive///////////////////////
    		
    		trainNodes = receive("TrackControllerTrainNodes"); //train nodes (ArrayList<ArrayList<Object>>)
    		
    		TrackModelGreenToYardObj = receive("TrackControllerGreenToYard");
    		if(TrackModelGreenToYardObj != null)
    		{
    			TrackModelGreenToYard = (boolean)TrackModelGreenToYardObj;
        		TrackModelRedToYard = (boolean)receive("TrackControllerRedToYard");
    		}
    		
    		greenSwitchPositionObj = receive("TrackControllerGreenPosition"); //receive switch position from track controller
    		if(greenSwitchPositionObj != null) //if not receiving yet, do not attempt to receive any more
    		{
		    	greenSwitchPosition = (Boolean[])greenSwitchPositionObj;
	    		redSwitchPosition = (Boolean[])receive("TrackControllerRedPosition");
    		}
    		
    		greenTrainPresenceObj = receive("TrainModelGreenStatus");//receive train presence on each line
    		if(greenTrainPresenceObj != null)  //if not yet receiving, do not attempt to receive any more
    		{
    				greenTrainPresence = (Boolean[])greenTrainPresenceObj;
    		}
    		
    		redTrainPresenceObj = receive("TrainModelRedStatus");//receive train presence on each line
    		if(redTrainPresenceObj != null)  //if not yet receiving, do not attempt to receive any more
    		{
    				redTrainPresence = (Boolean[])redTrainPresenceObj;
    		}
    		
    		TrainModelGreenPassCountObj = receive("TrainModelGreenPassCount"); //receive number of passengers disembarking train at each station
    		if(TrainModelGreenPassCountObj != null)
    		{
    			TrainModelGreenPassCount = (Integer[])TrainModelGreenPassCountObj;
    		}
    		
    		TrainModelRedPassCountObj = receive("TrainModelRedPassCount"); //receive number of passengers disembarking train at each station
    		if(TrainModelRedPassCountObj != null)
    		{
    			TrainModelRedPassCount = (Integer[])TrainModelRedPassCountObj;
    		}
    		
    		TrackControllerGreenMaintenanceObj = receive("TrackControllerGreenMaintenance"); //receive which blocks are closed from CTC
    		if(TrackControllerGreenMaintenanceObj != null)
    		{
    			greenMaintenance = (Boolean[])TrackControllerGreenMaintenanceObj;
    			redMaintenance = (Boolean[])receive("TrackControllerRedMaintenance");
    		}
    		
    		greenStationStatusObj = receive("StationArrayGreen");
    		if(greenStationStatusObj != null)
    		{
    			greenStationStatus = (boolean[])greenStationStatusObj;
    			redStationStatus = (boolean[])receive("StationArrayRed");
    		}
    	    
    		
	    	///////////////////send///////////////////////
    		
    		if(trainNodes != null) //only send onward if received first
    		{
	    		send("TrackModelTrainNodes", trainNodes);
    		}
    		
    		send("TrackModelGreenToYard", TrackModelGreenToYard);
    		send("TrackModelRedToYard", TrackModelRedToYard);
    		
    		if(greenTrainPresence[1] != null)
    		{
    			for(int i = 1; i <= greenSize; i++)  //send train presence and track status to track controller
        		{
        			greenStatus[i][0] = greenTrainPresence[i];
        			greenStatus[i][1] = greenTrackStatus[i];
        			greenStatus[i][2] = greenCircuitStatus[i];
        			greenStatus[i][3] = greenPowerStatus[i];
        		}
        		send("greenStatus", greenStatus);
    		}
    		
    		if(redTrainPresence[1] != null)
    		{
    			for(int i = 1; i <= redSize; i++) //send status array to track controller
        		{
        			redStatus[i][0] = redTrainPresence[i];
        			redStatus[i][1] = redTrackStatus[i];
        			redStatus[i][2] = redCircuitStatus[i];
        			redStatus[i][3] = redPowerStatus[i];
        		}
        		send("redStatus", redStatus);
    		}
    		
    		send("greenPassengerCount", greenPassengerCount); //number of passengers waiting at each station
    		send("redPassengerCount", redPassengerCount);
    		
    		send("greenPassengers", greenPassengers); //send total number of passengers
    		send("redPassengers", redPassengers);
    		
    		///////////////////updates///////////////////////
    		
    		//counter
    		System.out.println(k);
    		k++;
    		
    		/////////////////////////////////////////////////  	
    		
    		System.out.println("Total green passengers: " + greenPassengers + "   Total red passengers: " + redPassengers);
    		
    		//update passengers waiting at green line stations: when train leaves station, add waiting passengers to total and reset passenger count to 0
    		if(greenStationStatus != null)
    		{
        		for(int i = 1; i <= greenSize; i++)
        		{
        			if(greenStationStatus[i] == true)  //if train at station, set boarding flag
        				greenStationFlag[i] = true;
        			if(greenStationFlag[i] == true && greenStationStatus[i] == false) //if train just left station
        			{
        				greenPassengers += greenPassengerCount[i]; //add passengers to total
        				greenPassengerCount[i] = 0;               //reset passengers waiting to 0
        				greenStationFlag[i] = false;
        			}
        		}
    		}
    		//update passengers waiting at red line stations
    		if(redStationStatus != null)
    		{
        		for(int i = 1; i <= redSize; i++)
        		{
        			if(redStationStatus[i] == true)  //if train at station, set boarding flag
        				redStationFlag[i] = true;
        			if(redStationFlag[i] == true && redStationStatus[i] == false) //if train just left station
        			{
        				redPassengers += redPassengerCount[i]; //add passengers to total
        				redPassengerCount[i] = 0;               //reset passengers waiting to 0
        				redStationFlag[i] = false;
        			}
        		}
    		}
    		
    		//update green line crossing status and crossing/switch track lights
    		if(greenTrainPresence[1] != null)
    		{
        		for(int i = 1; i <= greenSize + 2; i++)
        		{			
        			//Green crossing status and lights
        			if(greenCrossing[i] == true && greenTrainPresence[i] == true)
        			{
        				if(greenPowerStatus[i] == false) //if power outage, no lights
        				{
        					greenLight[i] = true;
        				}
        				greenCrossingStatus[i] = true;
        			}
        			else
        			{
        				greenLight[i] = false;
        				greenCrossingStatus[i] = false;
        			}
        			//green switch lights
        			if(greenSwitch[i] != 0 && greenTrainPresence[i] == true)  //if switch block and train on block
    				{
						if(greenPowerStatus[i] == true) //if power outage, no lights
        					greenLight[i] = false;
        				else
        					greenLight[i] = true;
    				}
    				else
    					greenLight[i] = false;
        		}
    		}
    		
    		//update red line crossing status and crossing/switch lights
    		if(redTrainPresence[1] != null)
    		{
    			for(int i = 1; i <= redSize + 2; i++)
        		{
    				//Red crossing status and lights
        			if(redCrossing[i] == true && redTrainPresence[i] == true)
        			{
        				if(redPowerStatus[i] == false) //if power outage, no lights
        				{
        					redLight[i] = true;
        				}
        				redCrossingStatus[i] = true;
        			}
        			else
        			{
        				redLight[i] = false;
        				redCrossingStatus[i] = false;
        			}
        			//red switch lights
        			if(redSwitch[i] != 0 && redTrainPresence[i] == true)  //if switch block and train on block
    				{
						if(redPowerStatus[i] == true) //if power outage, no lights
        					redLight[i] = false;
        				else
        					redLight[i] = true;
    				}
    				else
    					redLight[i] = false;
        		}
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