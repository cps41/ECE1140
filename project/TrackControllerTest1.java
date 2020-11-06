import project.loginGUI;
import project.trackController;
import project.trackControllerGUI;

//Test case to show Instantiation of 3 Track Controller classes and GUI functionality


public class TrackControllerTest1 {

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
	}

}
