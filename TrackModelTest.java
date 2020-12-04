import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TrackModelTest {
    private static final int PORTNUMBER = 0;
    private static final String cpuIP = "18.216.251.172";
    private static ServerInterface mini;
    private static boolean connected = false;
    
    public TrackModelTest() //send inputs to this Track Model and display corresponding outputs
    {
    	//not all tests currently added, this revision shows one of many test sets
    	//simulating all inputs and output
    	System.out.println("====================");
    	System.out.println("=====Just a test====");
    	System.out.println("====================");
    	while(true) //continue forever so client can also run forever
    	{
    		////CHANGE ALL OUTBOUND VALUES HERE////
    		//initialize -> send -> receive -> test
    		
    		System.out.print("Upload Config. File: ");
    		if(Client.greenSections != null && Client.greenStation != null)
    			System.out.println("Pass");
    		else
    			System.out.println("Fail");
    			
    		////////////////////////////////////////
    
    		boolean TrackControllerGreenToYard = true; //testing non defaulting values
    		boolean TrackControllerRedToYard = true;
    		
    		send("TrackControllerGreenToYard", TrackControllerGreenToYard);
    		send("TrackControllerRedToYard", TrackControllerRedToYard);
    		
    		boolean TrackModelGreenToYard = false;
    		boolean TrackModelRedToYard = false;
    		if(receive("TrackModelGreenToYard") != null)
    		{
    			TrackModelGreenToYard = (boolean)receive("TrackModelGreenToYard");
        		TrackModelRedToYard = (boolean)receive("TrackModelRedToYard");
    		}
    		
    		System.out.print("Green To Yard Pass Through: ");
    		if(TrackModelGreenToYard == TrackControllerGreenToYard)
    			System.out.println("Pass");
    		else
    			System.out.println("Fail");
    		System.out.print("Red To Yard Pass Through: ");
    		if(TrackModelRedToYard == TrackControllerRedToYard)
    			System.out.println("Pass");
    		else
    			System.out.println("Fail");
    		
    		///////////////////////////////////////////
    		
    		Boolean[] greenPosition = new Boolean[200];
    		for(int i = 0; i < 200; i++)
    		{
    			if(Client.greenStation[i].length() > 1)
    				greenPosition[i] = true;
    		}
    		
    		Boolean[] redPosition = new Boolean[200];
    		for(int i = 0; i < 200; i++)
    		{
    			if(Client.redStation[i].length() > 1)
    				redPosition[i] = true;
    		}
    		
    		send("greenPosition", greenPosition);
    		send("redPosition", redPosition);
    		
    		Boolean[] greenPosition2 = Client.greenSwitchPosition;
    		Boolean[] redPosition2 = Client.redSwitchPosition;
    		
    		boolean gflag = true;
    		boolean rflag = true;
    		for(int i = 0; i < 200; i++)
    		{
    			if(greenPosition[i] != greenPosition2[i])
    				gflag = false;
    			if(redPosition[i] != redPosition2[i])
    				rflag = false;
    		}
    		
    		System.out.print("Green Switch Position In: ");
    		if(gflag = true)
    			System.out.println("Pass");
    		else
    			System.out.println("Fail");
    		System.out.print("Red Switch Position In: ");
    		if(rflag = true)
    			System.out.println("Pass");
    		else
    			System.out.println("Fail");
    		
    		////////////////////////////////////////////////
    		
    		Boolean[] TrainModelGreenStatus = new Boolean[200];
    		Boolean[] TrainModelRedStatus = new Boolean[200];
    		
    		TrainModelGreenStatus[43] = true;
    		TrainModelRedStatus[57] = true;
    		
    		send("TrainModelGreenStatus", TrainModelGreenStatus);
    		send("TrainModelRedStatus", TrainModelRedStatus);
    		
    		Boolean[][] greenStatus = (Boolean[][])receive("greenStatus");
    		Boolean[][] redStatus = (Boolean[][])receive("redStatus");
    		
    		gflag = true;
    		rflag = true;
    		for(int i = 0; i < 200; i++)
    		{
    			if(greenStatus[i][0] != TrainModelGreenStatus[i])
    				gflag = false;
    			if(redStatus[i][0] != TrainModelRedStatus[i])
    				rflag = false;
    		}
    		
    		System.out.print("Green Presence Through: ");
    		if(gflag = true)
    			System.out.println("Pass");
    		else
    			System.out.println("Fail");
    		System.out.print("Red Presence Through: ");
    		if(rflag = true)
    			System.out.println("Pass");
    		else
    			System.out.println("Fail");
    		
    		//////////////////////////////////////////////
    		
    		
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
