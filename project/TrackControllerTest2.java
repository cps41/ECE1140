import project.loginGUI;
import project.trackController;
import project.trackControllerGUI;

//Test case to show switch/crossing method functionality of trackController class (results can be viewed in GUI)

public class TrackControllerTest2 {

	public static void main(String[] args) {
		trackController controller = new trackController(100, 90);
	    loginGUI login = new loginGUI();
	    
	    while (login.frame.isShowing()) {
	    	//prompt user for login credentials
	    	System.out.println("Please enter login credentials to access Wayside Control Center.");
	    	//implement delay and prompt user again if no action is taken
	    	for (int delay=-1000000000; delay<1000000000; delay++)
	    		for (int doubleDelay=-1000000000; doubleDelay<1000000000; doubleDelay++);
	    }
	    System.out.println("Thank You");
	    trackControllerGUI controllerGUI = new trackControllerGUI();
	    //Sometimes the controllerGUI won't show all the GUI elements unless the window is maximized and de-maximized
	    
	    //set certain sections of red to have switches and green to have crossings
	    for (int i=1; i<controller.redLine.length; i++)
	    	if (i%5 == 0) controller.setSwitchExistence("red", i, true);
	    for (int i=1; i<controller.greenLine.length; i++)
	    	if (i%5 == 0) controller.setCrossingExistence("green", i, true);
	    
	    //change half of newly validated switches 
	    for (int i=1; i<controller.redLine.length; i++)
	    	if (controller.getSwitchExistence("red", i) && i%5==0) 
	    		controller.setSwitchPosition("red", i, true);
	    for (int i=1; i<controller.greenLine.length; i++)
	    	if (controller.getCrossingExistence("green", i) && i%5==0) 
	    		controller.setCrossingStatus("green", i, true);
	}

}
