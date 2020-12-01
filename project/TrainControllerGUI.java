package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class TrainControllerGUI{
	public static JFrame frame;
	public static JPanel infoPanel;
	public static JLabel infoLabel1;
	public static JLabel infoLabel2;
	public static JLabel infoLabel3;
	public TrainController train;
	
	public TrainControllerGUI(String key, LinkedList<Integer> rAuthorities, LinkedList<Float[]> redtrainControl, 
							boolean line, int redLength, int greenLength, float velocityCommand) {
		train = new TrainController(key);
        train.setAuthorityQueue((LinkedList<Integer>) rAuthorities);
        train.setControlQueue(redtrainControl);
        train.setLine(line);
        train.setBlockLengths(redLength, greenLength);
        train.setVelocityCommand(velocityCommand);
        
		//Create and set up the window.
        frame = new JFrame("Train Controller");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create the menu bar.  Make it have a green background.
        JMenuBar MenuBar = new JMenuBar();
        MenuBar.setPreferredSize(new Dimension(1000, 40));
      //set table
		String row[][] = { {"50.0", 
			"60.0",
			"55.0",
			"50.0"}
		};
		
		String column[] = { "Setpoint Speed", 
							"Speed Limit",
							"Commanded Speed",
							"Velocity"
		};
		
		//configure table
		JTable speedInfo = new JTable(row, column);
		speedInfo.setBounds(10, 10, 10, 10);
		JScrollPane sp = new JScrollPane(speedInfo);
		sp.setPreferredSize(new Dimension(200, 40));
 
        //Create a yellow label to put in the content pane.
        JLabel yellowLabel = new JLabel();
        yellowLabel.setOpaque(true);
        yellowLabel.setBackground(new Color(248, 213, 131));
        yellowLabel.setPreferredSize(new Dimension(500, 180));
        
        JLabel redLabel = new JLabel();
        redLabel.setOpaque(true);
        redLabel.setBackground(new Color(255, 0, 0));
        redLabel.setPreferredSize(new Dimension(500, 180));
        
        //info panel
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoLabel1 = new JLabel();
        infoLabel2 = new JLabel();
        infoLabel3 = new JLabel();
        infoLabel1.setText("Commanded Speed: " + train.COMMANDED_SPEED + " km/h");
        infoLabel2.setText("Velocity: " + train.VELOCITY + " km/h");
        infoLabel3.setText("Speed Limit: " + train.SPEED_LIMIT + " km/h");
        infoPanel.add(infoLabel1);
        infoPanel.add(infoLabel2);
        infoPanel.add(infoLabel3);
        
     // create a ToggleButton 
        JButton Button1 = new JButton("Left-Side Doors"); 
        JPanel buttonPanel = new JPanel();
        JLabel buttonLabel1 = new JLabel();
        buttonLabel1.setText("Door Status: CLOSED");
        JLabel buttonLabel2 = new JLabel();
        buttonLabel2.setText("Door Status: CLOSED");
        // Define ActionListener 
  
        ActionListener actionListenerLeftSideDoors = new ActionListener() 
        { 
  
            // actionPerformed() method is invoked  
            // automatically whenever you click on 
            // registered component 
            public void actionPerformed(ActionEvent actionEventLeftSideDoors) 
            { 
                train.setLeftDoors(!train.getLeftDoors());
                if(train.getLeftDoors() == true) {
                    buttonLabel1.setText("Left-Side Doors: CLOSED");
                }
                else {
                    buttonLabel1.setText("Left-Side Doors: OPEN");
                }
                
            } 
          }; 
          
          /////////////////
       // create a Button 
          JButton Button2 = new JButton("Right-Side Doors"); 
          // Define ActionListener 
    
          ActionListener actionListenerRightSideDoors = new ActionListener() 
          { 
    
              // actionPerformed() method is invoked  
              // automatically whenever you click on 
              // registered component 
              public void actionPerformed(ActionEvent actionEventRightSideDoors) 
              { 
                  train.setRightDoors(!train.getRightDoors());
                  if(train.getRightDoors() == true) {
                      buttonLabel2.setText("Right-Side Doors: CLOSED");
                  }
                  else {
                      buttonLabel2.setText("Right-Side Doors: OPEN");
                  }
                  
              } 
            }; 
            
            /////////
            JButton brakeButton = new JButton("BRAKE"); 
            JPanel brakeButtonPanel = new JPanel();
            JLabel brakeButtonLabel = new JLabel();
            brakeButtonLabel.setText("Brake Status: OFF");
            // Define ActionListener 
      
            ActionListener actionListenerBrake = new ActionListener() 
            { 
      
                // actionPerformed() method is invoked  
                // automatically whenever you click on 
                // registered component 
                public void actionPerformed(ActionEvent actionEventBrake) 
                { 
                    brakeButtonLabel.setText("Brake Status: ON");
                    train.setBrake(true);
                    
                } 
              }; 
              
           // create a Button 
              JButton Button3 = new JButton("Interior Lights"); 
              JPanel buttonPanel2 = new JPanel();
              JLabel buttonLabel3 = new JLabel();
              buttonLabel3.setText("Interior Lights: ON");
              JLabel buttonLabel4 = new JLabel();
              buttonLabel4.setText("Exterior Lights: ON");
              // Define ActionListener 
        
              ActionListener actionListenerInteriorLights = new ActionListener() 
              { 
        
                  // actionPerformed() method is invoked  
                  // automatically whenever you click on 
                  // registered component 
                  public void actionPerformed(ActionEvent actionEventInteriorLights) 
                  { 
                      train.setInteriorLights(!train.getInteriorLights());
                      if(train.getInteriorLights() == true) {
                          buttonLabel3.setText("Interior Lights: ON");
                      }
                      else {
                          buttonLabel3.setText("Interior Lights: OFF");
                      }
                      
                  } 
                }; 
                
                /////////////////
             // create a ToggleButton 
                JButton Button4 = new JButton("Exterior Lights"); 
                // Define ActionListener 
          
                ActionListener actionListenerExteriorLights = new ActionListener() 
                { 
          
                    // actionPerformed() method is invoked  
                    // automatically whenever you click on 
                    // registered component 
                    public void actionPerformed(ActionEvent actionEventExteriorLights) 
                    { 
                        train.setExteriorLights(!train.getExteriorLights());
                        if(train.getExteriorLights() == true) {
                            buttonLabel3.setText("Exterior Lights: ON");
                        }
                        else {
                            buttonLabel3.setText("Exterior Lights: OFF");
                        }
                        
                    } 
                  }; 
            
            
        // Attach Listeners 
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            
        Button1.addActionListener(actionListenerLeftSideDoors); 
        Button1.setPreferredSize(new Dimension(200, 50));
        buttonPanel.add(Button1);
        buttonPanel.add(buttonLabel1);
        
        Button2.addActionListener(actionListenerRightSideDoors); 
        Button2.setPreferredSize(new Dimension(200, 50));
        buttonPanel.add(Button2);        
        buttonPanel.add(buttonLabel2);
        /////////
        
        buttonPanel2.setLayout(new BoxLayout(buttonPanel2, BoxLayout.Y_AXIS));
        
        Button3.addActionListener(actionListenerInteriorLights); 
        Button3.setPreferredSize(new Dimension(200, 50));
        buttonPanel2.add(Button3);
        buttonPanel2.add(buttonLabel3);
        
        Button4.addActionListener(actionListenerExteriorLights); 
        Button4.setPreferredSize(new Dimension(200, 50));
        buttonPanel2.add(Button4);        
        buttonPanel2.add(buttonLabel4);
        
        ////////
        brakeButton.addActionListener(actionListenerBrake);
        brakeButton.setPreferredSize(new Dimension(200, 50));  
        brakeButtonPanel.add(brakeButton);
        brakeButtonPanel.add(brakeButtonLabel);
        
 
        //Set the menu bar and add the label to the content pane.
        frame.setJMenuBar(MenuBar);
        //frame.getContentPane().add(sp, BorderLayout.PAGE_START);
        frame.getContentPane().add(infoPanel, BorderLayout.PAGE_START);
        frame.getContentPane().add(buttonPanel2, BorderLayout.LINE_END);
        frame.getContentPane().add(brakeButtonPanel, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.LINE_START);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
	}
	
	public void refresh(float velocity, String stationName, boolean eBrake,
 		    LinkedList<Integer> rAuthorities, LinkedList<Float[]> redtrainControl, boolean line) {
		//set key inputs
        train.setVelocity(velocity);
        train.setStationName((String) stationName);
        train.setEmergencyBrake((boolean) eBrake);
        
        
        //calculations
        train.updateAuthority();
        train.updatePowerCommand();
        train.setV(train.getVelocity());
        
      //update gui values
        infoLabel1.setText("Commanded Speed: " + TrainController.COMMANDED_SPEED + " km/h");
        infoLabel2.setText("Velocity: " + TrainController.VELOCITY + " km/h");
        infoLabel3.setText("Speed Limit: " + TrainController.SPEED_LIMIT + " km/h");
        if(TrainController.DONE == true) frame.dispose();
	}
}