/* Needed on Client only */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Font;
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
    //green infrustructure
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
  	
  	//TODO//static int[] greenPassengerCount;
  	
  	
  	////////////////////red/////////////////////////////////
  	//red infrustructure
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
  	
  	//TODO//static int[] redPassengerCount;
  	
    
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
		greenBelowGround = new boolean[200];
		greenSwitchPosition = new boolean[200];
		greenTrainPresence = new boolean[200];
		greenCrossingStatus = new boolean[200];
		greenStationStatus = new boolean[200];
		greenTrackStatus = new boolean[200];
		greenHeaters = new boolean[200];
		greenBeaconData = new String[200];
	
		//TODO//greenPassengerCount = new int[200];
		
		
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
		redBelowGround = new boolean[200];
		redSwitchPosition = new boolean[200];
		redTrainPresence = new boolean[200];
		redCrossingStatus = new boolean[200];
		redStationStatus = new boolean[200];
		redTrackStatus = new boolean[200];
		redHeaters = new boolean[200];
		redBeaconData = new String[200];
	
		//TODO//int[] redPassengerCount = new int[200];
		
		
		////////////parsing input configuration .txt file////////////////
		try 
		{
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
			if(parts[0] != "-1")
			{
				greenDirection[greenSize+2] = Integer.parseInt(parts[0]);
				greenSwitch[greenSize+2] = Integer.valueOf(parts[1]);
			}
			
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
			if(parts[0] != "-1")
			{
				redDirection[redSize+2] = Integer.parseInt(parts[0]);
				redSwitch[redSize+2] = Integer.valueOf(parts[1]);
			}
			
		}
		catch(Exception e) 
		{
			System.out.println(e);
		}
		
		////////////Call main login GUI -> which calls main Track Model GUI//////////////
		TrackModelLogin mainLogin = new TrackModelLogin();
		
		///////////////Server Calls//////////////////////
    	while(true)
    	{
    		
    		//send initializing data to server about track configuration
   
    		//track size of each line
    		send("greenTrackSize", greenSize);
    		send("redTrackSize", redSize);
        	
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
    		send("greenConfigurationInt", greenConfigurationInt);
    		
    		int[][] redConfigurationInt = new int[200][4];
    		for(int i = 1; i <= redSize+2; i++) //+2 to account for yard sections @ end
    		{
    			redConfigurationInt[i][0] = greenSwitch[i];
    			if(redCrossing[i] == false)
    				redConfigurationInt[i][1] = 0;
    			else
    				redConfigurationInt[i][1] = 1;
    			redConfigurationInt[i][2] = greenDirection[i];
    			redConfigurationInt[i][3] = greenSpeedLimit[i];
    		}
    		send("redConfigurationInt", redConfigurationInt);
    		
    		// *ConfigurationFloat:   	(index = block #)
    		//						[][0] = Blocks length
    		//						[][1] = Block grade
    		
    		float[][] greenConfigurationFloat = new float[200][2];
    		for(int i = 1; i <= greenSize; i++)
    		{
    			greenConfigurationFloat[i][0] = (float)greenBlockLength[i];
    			greenConfigurationFloat[i][1] = (float)greenBlockGrade[i];
    		}
    		send("greenConfigurationFloat", greenConfigurationFloat);
    		
    		float[][] redConfigurationFloat = new float[200][2];
    		for(int i = 1; i <= redSize; i++)
    		{
    			redConfigurationFloat[i][0] = (float)redBlockLength[i];
    			redConfigurationFloat[i][1] = (float)redBlockGrade[i];
    		}
    		send("redConfigurationFloat", redConfigurationFloat);
    		
    		// *ConfigurationString:   	(index = block #)
    		//						[][0] = Blocks section letter
    		//						[][1] = Station names for appropriate blocks
    		
    		String[][] greenConfigurationString = new String[200][2];
    		for(int i = 1; i <= greenSize; i++)
    		{
    			greenConfigurationString[i][0] = greenSections[i];
    			greenConfigurationString[i][1] = greenStation[i];
    		}
    		send("greenConfigurationString", greenConfigurationString);
    		
    		String[][] redConfigurationString = new String[200][2];
    		for(int i = 1; i <= redSize; i++)
    		{
    			redConfigurationString[i][0] = redSections[i];
    			redConfigurationString[i][1] = redStation[i];
    		}
    		send("redConfigurationString", redConfigurationString);
    		
    		//beaconData for each station
    		String[] greenBeaconData = new String[200];
    		for(int i = 1; i <= greenSize; i++)
    		{
    			if(greenStation[i].length() != 1)//if there is a station
    			{
    				greenBeaconData[i] = greenSide[i] + greenStation[i];
    			}
    		}
    		
    		
        	//send/receive/update data continuously
	    	////////////////receive///////////////////////
    
    		boolean greenStatus[][] = new boolean[200][2];  //receive green train presence from train (just for simulation)
    		boolean redStatus[][] = new boolean[200][2];   //receive red train presence from train (just for simulation)		

    		Object PregreenSwitchPosition = receive("greenPosition"); //receive switch position from track controller
    		if(PregreenSwitchPosition != null) //if not receiving yet, dont atttempt to receive any more
    		{
		    	greenSwitchPosition = (boolean[])PregreenSwitchPosition;
	    		redSwitchPosition = (boolean[])receive("redPosition");
	    		
	    		//TODO// receive train presence from Catrina
	    		
	    		//TODO//receive authorityGreenCorrection and authorityRedCorrection (int - block #'s authority that needs to be set to 0)
	    		//merge with hashmap authority before sending
	    		
	    		//TODO//receive both setpointspeed and authority HashMaps
    		}
    		
	    	///////////////////send///////////////////////
    		
    		for(int i = 1; i <= greenSize; i++)  //send train presence and track status to track controller
    		{
    			greenStatus[i][0] = greenTrainPresence[i];
    			greenStatus[i][1] = greenTrackStatus[i];
    		}
    		send("greenStatus", greenStatus);
    		
    		for(int i = 1; i <= redSize; i++)
    		{
    			redStatus[i][0] = redTrainPresence[i];
    			redStatus[i][1] = redTrackStatus[i];
    		}
    		send("redStatus", redStatus);
    		
    		//TODO//send hashmaps
    		
    		//TODO//send passenger count
    		
    		
    		
    		
    		///////////////////updates/////////////////////////
    		
    		//TODO//clear passenger count upon departure
    		
    		for(int i = 1; i <= greenSize; i++) //green crossing status & lights
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
    		
    		for(int i = 1; i <= redSize; i++) //red crossing status & lights
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
    		
    		for(int i = 1; i <= greenSize; i++) //green station status
    		{
    			if(greenStation[i].length() != 1 && greenTrainPresence[i] == true) //if station and train in station
    				greenStationStatus[i] = true;
    			else
    				greenStationStatus[i] = false;
    		}
	    	
    		for(int i = 1; i <= redSize; i++) //red station status
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

