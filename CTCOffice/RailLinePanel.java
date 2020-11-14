import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Color;
import java.util.ArrayList;

public class RailLinePanel extends JPanel {
  private String LINE_NAME;
  public DispatchPanel DISPATCH_PANEL = new DispatchPanel();
  private JLabel PANEL_LABEL = new JLabel();
  public RailLineController CONTROLLER;
  public ArrayList<String> AL_DESTINATIONS = new ArrayList<String>();
  RailLinePanel(){ 
    configureMainPanel();
    //the other configurators are called in the setLineColor fucntion
  }
    //component configurators
  private void configureDispatchPanel(){
      //DISPATCH_PANEL.setPreferredSize(new Dimension(400,400));
      DISPATCH_PANEL.setDestinations();
      add(DISPATCH_PANEL);
    }
  private void configureMainPanel(){
      setSize(800, 600);
      setVisible(true);
    }
  private void configurePanelLabel(){
      // PANEL_LABEL.setBounds(20, 20, 200, 40);
      PANEL_LABEL.setText(LINE_NAME+" Panel");
      add(PANEL_LABEL);
    }
  public void configurePanel(String name, RailLineController controller,String[] destinations,int size){
    CONTROLLER = controller;
    LINE_NAME = name;
    for (int i=0;i<size;i++){
      if (destinations[i]!="0"){AL_DESTINATIONS.add(destinations[i]);}
    }
    configurePanelLabel();
    configureDispatchPanel();
  }  
    
    //calls configurePanelLabel() and configureDispatchPanel()
    //CLASSES
  class DispatchPanel extends JPanel {
    //componets
    private JLabel PANEL_LABEL = new JLabel("Dispatch Train:");
    private JComboBox<String> DESTINATION_COMBOBOX = new JComboBox<>();
    private JLabel DESTINATION_COMBOBOX_LABEL = new JLabel("Destination");
    private JRadioButton DISPATCH_MODE_RADIOBUTTON = new JRadioButton("automatic");
    private JButton BUTTON = new JButton("Dispatch");
    private JTextField TIME_OF_ARRIVAL_TEXTFIELD = new JTextField("Time", 6);
    //constructor
    DispatchPanel(){
          setBorder(BorderFactory.createLineBorder(Color.black));
          configurePanelLabel();
          configureComboBoxLabel();
          configureComboBox();
          configureTimeTextField();
          configureButton();
          configureRadioButton();
      }
    //component configuration
    private void configurePanelLabel(){
          add(PANEL_LABEL);
      }
    private void configureComboBox(){
          DESTINATION_COMBOBOX.setEditable(true);
          DESTINATION_COMBOBOX.setPreferredSize(new Dimension(150,20));
          add(DESTINATION_COMBOBOX);
      }
    private void configureRadioButton(){
          add(DISPATCH_MODE_RADIOBUTTON);
      }
    private void configureComboBoxLabel(){
          add(DESTINATION_COMBOBOX_LABEL);
      }
    private void configureTimeTextField(){
          add(TIME_OF_ARRIVAL_TEXTFIELD);
      }
    private void configureButton(){
      BUTTON.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              dispatchButtonPressed();
          }
      });
      add(BUTTON);
      }
    public void setDestinations(){
         for (int i=0;i<AL_DESTINATIONS.size();i++){DESTINATION_COMBOBOX.addItem(AL_DESTINATIONS.get(i));}
      }
    //action event function
    private void dispatchButtonPressed(){
      // get gui inputs
      String destinationString = (String)DESTINATION_COMBOBOX.getSelectedItem();
      String timeString = TIME_OF_ARRIVAL_TEXTFIELD.getText();
      // format checks - display their own messages
      if ( !checkDestinationFormat(destinationString) ) {return;}
      if ( !checkTimeFormat(timeString)) {return;}
      // configuring inputs for dispatch signal
      int[] time = new int[4];
      time = parseTimeString(timeString);
      int destination = AL_DESTINATIONS.indexOf(destinationString);
      //railine line controller dispatch signal
      System.out.println(destination);
      interpretRailLineControllerSignals(CONTROLLER.dispatchSignal(destination, time));
    } 
      //errorSignals
    private void errorMessage(String message){
      JOptionPane.showMessageDialog(new JFrame(), message, "error", JOptionPane.ERROR_MESSAGE);
    }
    private void successMessage(String message){
      JOptionPane.showMessageDialog(new JFrame(), message, "success", JOptionPane.INFORMATION_MESSAGE);
    }
    //checks/input configuration - none for speed
    private boolean checkDestinationFormat(String destination){
      String message = "Invalid Destination Format";
      int destinationInt = AL_DESTINATIONS.indexOf(destination);
      if (destinationInt < 0) {
        errorMessage(message);
        return false;
      }
      return true;
    }
    private boolean checkTimeFormat(String time){
      String message = "Invalid Time Format. Please use: military time, no puncuation, specificity: minute, 4 digits";
      try {Integer.parseInt(time);} catch(Exception e){
        errorMessage(message);
        return false;
      }
      if (time.length() != 4){
        errorMessage(message);
        return false;
      }
      int timeInt = Integer.parseInt(time);
      if (timeInt < 0 || timeInt > 2359 ){
        errorMessage(message);
        return false;
      }
      char check = time.charAt(2);
      int check1 = Character.getNumericValue(check);
      if (check1 > 5){
        errorMessage(message);
        return false;
      }
      return true;
    }
    private int[] parseTimeString(String string){
      int[] time = new int[4];
      time[0] = Character.getNumericValue(string.charAt(0));
      time[1] = Character.getNumericValue(string.charAt(1));
      time[2] = Character.getNumericValue(string.charAt(2));
      time[3] = Character.getNumericValue(string.charAt(3));
      return time;
    }
    private void interpretRailLineControllerSignals(int Signal){
      switch(Signal){
        case CTCSystem.SUCCESSFULL_DISPATCH:
          successMessage("Dispatch approved. Signal Sent");
          break;
        case CTCSystem.INVALID_TIME:
          errorMessage("Time not possible");
          break;
      }
    }
  }
}










  //There will be panels for each partiton of CTC functions.
//They will be instantiated in the RailLinePanel constuctor
//They are customized first by the RailLinePanel.setDestinations(Stringx x) function
//The RailLinePanel.setDestinations(String x).setDestinatinos(String x) function then calls the
//DispatchPanel.setDestinations(String x) function to fill the JComboBox, Destintation_ComboBox









/*abstract


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
    ///private JTextField AUTHORITY_TEXTFIELD = new JTextField("Authority",6);
    //private JSlider SPEED_SLIDER = new JSlider(JSlider.HORIZONTAL,0,70,0);
    //private JLabel SPEED_SLIDER_LABEL = new JLabel("Speed");
    //private JLabel SPEED_SELECTION_LABEL = new JLabel("35 KPH");

 private boolean checkAuthorityFormat(String authority){
        String message = "Authority is out of Range. Range is 0 to "+CONTROLLER.NUMBER_0F_BLOCKS+".";
        try{ Integer.parseInt(authority);} catch (Exception e){
            errorMessage(message);
            return false;
        }
        int authorityInt = Integer.parseInt(authority);
        if (authorityInt > CONTROLLER.NUMBER_0F_BLOCKS || authorityInt < 0 ){
            errorMessage(message);
            return false;
        }
        return true;
    }

    private void configureSlider(){
        SPEED_SLIDER.setPaintTicks(true);
        SPEED_SLIDER.setMajorTickSpacing(10);
        SPEED_SLIDER.setPaintLabels(true);
        SPEED_SLIDER.setPaintTrack(true);
        class SliderListener implements ChangeListener {
            @Override
            public void stateChanged(ChangeEvent e){
                SPEED_SELECTION_LABEL.setText(SPEED_SLIDER.getValue()+" KPH");
            }
        }
        SPEED_SLIDER.addChangeListener(new SliderListener());
        add(SPEED_SLIDER);
    }


*/










/*abstract
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;  

//ECE 1140 MATT DIPAOLO 2020

/////CLASS DELCARTATION
public class GUI {
//////////////variables/////////////////
  public int numOfTrains=0; public int[] speeds = new int[50]; public int[] authorities = new int[50];
  public String lineColor;
  ////////instantiating containers///////////////
  //frame
  private JFrame frame = new JFrame("Centralize Control Center - "+lineColor+" Line");
  //////JPANELS WISH ME LUCK
//  private JPanel mainPanel = new JPanel();
  //three buttons - buttonDispatch, buttonSet, buttonFind
  private JButton buttonDispatch = new JButton("dispatch");
  private JButton buttonSet = new JButton("set");
  private JButton buttonFind = new JButton("find");
  //six text inputs - 
    // two are linked to buttonDipstatch - tfDispatchAuthority, tfDispatchSpeed
    private JTextField tfDispatchAuthority = new JTextField();
    private JTextField tfDispatchSpeed = new JTextField();
    // three are link to buttonSet - tfSetTrain , tfSetAuthorit, tfSetSpeed
    private JTextField tfSetTrain = new JTextField();
    private JTextField tfSetAuthority = new JTextField();
    private JTextField tfSetSpeed = new JTextField();
    // one is linked to buttonFind
    private JTextField tfFindTrain = new JTextField();
  //labels - _will use received server values for redudancy 
  private JLabel labelTrainCount = new JLabel();        private JLabel labelTrainCount_ = new JLabel();
  private JLabel labelDispatchAuthority = new JLabel(); private JLabel labelDispatchAuthority_ = new JLabel();
  private JLabel labelDispatchSpeed = new JLabel();     private JLabel labelDispatchSpeed_ = new JLabel();
  private JLabel labelSetTrain = new JLabel();          
  private JLabel labelSetAuthority = new JLabel();      private JLabel labelSetAuthority_ = new JLabel();
  private JLabel labelSetSpeed = new JLabel();          private JLabel labelSetSpeed_ = new JLabel();
  private JLabel labelFindTrain = new JLabel();
 // private JLabel labelFindSpeed = new JLabel();      
 // private JLabel labelFindAuthority = new JLabel();    
  //private JLabel labelFindLocation = new JLabel();
/////////configure containers//////////////////////
  private void configureFrame() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 600);
    frame.setLayout(null);
    frame.setVisible(true);
  }
  private void configureButtons() { 
    //layout
    buttonDispatch.setBounds(10, 40, 90, 30); buttonSet.setBounds(10, 130, 90, 30); buttonFind.setBounds(10, 220, 90, 30);
    frame.add(buttonDispatch);                frame.add(buttonSet);                 frame.add(buttonFind);
    // action listeners
    buttonDispatch.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        dispatchTrain();
      }
    });
    buttonSet.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        //setTrain();
      }
    });
    buttonFind.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        findTrain();
      }
    });
  }
  private void configureLabels() { 
    //train count 
    labelTrainCount.setText("Train Count  -1"); 
    labelTrainCount.setBounds(325, 20, 200, 40);
    labelTrainCount.setFont(new Font("Arial", Font.PLAIN, 20)); 
    frame.add(labelTrainCount);
    // train count_
    labelTrainCount_.setText("Train Count_ -1"); 
    labelTrainCount_.setBounds(325, 60, 200, 50);
    labelTrainCount_.setFont(new Font("Arial", Font.PLAIN, 20)); 
    frame.add(labelTrainCount_);
    //Small label (90,40)
    configureSmallLabel(labelDispatchAuthority, "Authority   -1", 120, 10);
    configureSmallLabel(labelDispatchAuthority_,"Authority_ -1", 120, 60);
    configureSmallLabel(labelDispatchSpeed, "Speed   -1", 220, 10);
    configureSmallLabel(labelDispatchSpeed_,"Speed_ -1", 220, 60);
    configureSmallLabel(labelSetTrain,"Train  -1",120,100);
    configureSmallLabel(labelSetAuthority, "Authority    -1", 220, 100);
    configureSmallLabel(labelSetAuthority_, "Authority_ -1", 220, 160);
    configureSmallLabel(labelSetSpeed, "Speed   -1",320,100);
    configureSmallLabel(labelSetSpeed_, "Speed_  -1",320,160);
    configureSmallLabel(labelFindTrain,"Train  -1",120,190);
  }
  private void configureSmallLabel(JLabel label,String title,int x,int y){
    label.setText(title);
    label.setBounds(x,y,90,40);
    label.setFont(new Font("Arial",Font.PLAIN,12));
    frame.add(label);
  }
  //private void configureLargeLabel(JLabel label, String title,int x,int y){
//
//  }
  private void configureTextFields() { 
    tfDispatchAuthority.setBounds(120,40,80,30); frame.add(tfDispatchAuthority);
    tfDispatchSpeed.setBounds(220,40,80,30);     frame.add(tfDispatchSpeed);
    tfSetTrain.setBounds(120,130,80,30);         frame.add(tfSetTrain);
    tfSetAuthority.setBounds(220,130,80,30);     frame.add(tfSetAuthority);
    tfSetSpeed.setBounds(320,130,80,30);         frame.add(tfSetSpeed);
    tfFindTrain.setBounds(120,220,80,30);        frame.add(tfFindTrain);
  }
//////////constructor////////////////////
  GUI(String x) {
    lineColor = x;
    configureFrame();
    configureLabels();
    configureButtons();
    configureTextFields();
  }
///////////methods////////////////////////
public  void dispatchTrain() {
    numOfTrains++; 
    //abelTrainCount.setText("Train Count = " + numOfTrains);
    //speeds[numOfTrains] = Integer.valueOf(tfDispatchSpeed.getText());
   // authorities[numOfTrains] = Integer.valueOf(tfDispatchAuthority.getText());
    //labelDispatchSpeed.setText("Authority = " + speeds[numOfTrains]);
   // labelDispatchAuthority.setText("Authority = " +authorities[numOfTrains]);                 
    }
public void setTrain(int train, int speed){
    //int train = Integer.valueOf(tfSetTrain.getText());
    //int speed = Integer.valueOf(tfSetSpeed.getText());
   // int authority = Integer.valueOf(tfSetAuthority.getText());
    //labelSetTrain.setText("Train  "+train);
    //labelSetSpeed.setText("Speed  "+speed); 
   // labelSetAuthority.setText("Authority  "+authority);
    speeds[train] = speed; 
    authorities[train] = speed;
  }
private void findTrain(){
    int train = Integer.valueOf(tfFindTrain.getText());
    labelFindTrain.setText("Train  "+train);

  }
public void setAuthority(int train, int authority){
    authorities[train]=authority;
 }
 public static void main(String[] args) {
   GUI gui = new GUI("Blue");
 }
}
  


*/


































/*abstract
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;  

//ECE 1140 MATT DIPAOLO 2020

/////CLASS DELCARTATION
public class GUI {
//////////////variables/////////////////
  public int numOfTrains=0; public int[] speeds = new int[50]; public int[] authorities = new int[50];
  public String lineColor;
  ////////instantiating containers///////////////
  //frame
  private JFrame frame = new JFrame("Centralize Control Center - "+lineColor+" Line");
  //////JPANELS WISH ME LUCK
//  private JPanel mainPanel = new JPanel();
  //three buttons - buttonDispatch, buttonSet, buttonFind
  private JButton buttonDispatch = new JButton("dispatch");
  private JButton buttonSet = new JButton("set");
  private JButton buttonFind = new JButton("find");
  //six text inputs - 
    // two are linked to buttonDipstatch - tfDispatchAuthority, tfDispatchSpeed
    private JTextField tfDispatchAuthority = new JTextField();
    private JTextField tfDispatchSpeed = new JTextField();
    // three are link to buttonSet - tfSetTrain , tfSetAuthorit, tfSetSpeed
    private JTextField tfSetTrain = new JTextField();
    private JTextField tfSetAuthority = new JTextField();
    private JTextField tfSetSpeed = new JTextField();
    // one is linked to buttonFind
    private JTextField tfFindTrain = new JTextField();
  //labels - _will use received server values for redudancy 
  private JLabel labelTrainCount = new JLabel();        private JLabel labelTrainCount_ = new JLabel();
  private JLabel labelDispatchAuthority = new JLabel(); private JLabel labelDispatchAuthority_ = new JLabel();
  private JLabel labelDispatchSpeed = new JLabel();     private JLabel labelDispatchSpeed_ = new JLabel();
  private JLabel labelSetTrain = new JLabel();          
  private JLabel labelSetAuthority = new JLabel();      private JLabel labelSetAuthority_ = new JLabel();
  private JLabel labelSetSpeed = new JLabel();          private JLabel labelSetSpeed_ = new JLabel();
  private JLabel labelFindTrain = new JLabel();
 // private JLabel labelFindSpeed = new JLabel();      
 // private JLabel labelFindAuthority = new JLabel();    
  //private JLabel labelFindLocation = new JLabel();
/////////configure containers//////////////////////
  private void configureFrame() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 600);
    frame.setLayout(null);
    frame.setVisible(true);
  }
  private void configureButtons() { 
    //layout
    buttonDispatch.setBounds(10, 40, 90, 30); buttonSet.setBounds(10, 130, 90, 30); buttonFind.setBounds(10, 220, 90, 30);
    frame.add(buttonDispatch);                frame.add(buttonSet);                 frame.add(buttonFind);
    // action listeners
    buttonDispatch.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        dispatchTrain();
      }
    });
    buttonSet.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        //setTrain();
      }
    });
    buttonFind.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        findTrain();
      }
    });
  }
  private void configureLabels() { 
    //train count 
    labelTrainCount.setText("Train Count  -1"); 
    labelTrainCount.setBounds(325, 20, 200, 40);
    labelTrainCount.setFont(new Font("Arial", Font.PLAIN, 20)); 
    frame.add(labelTrainCount);
    // train count_
    labelTrainCount_.setText("Train Count_ -1"); 
    labelTrainCount_.setBounds(325, 60, 200, 50);
    labelTrainCount_.setFont(new Font("Arial", Font.PLAIN, 20)); 
    frame.add(labelTrainCount_);
    //Small label (90,40)
    configureSmallLabel(labelDispatchAuthority, "Authority   -1", 120, 10);
    configureSmallLabel(labelDispatchAuthority_,"Authority_ -1", 120, 60);
    configureSmallLabel(labelDispatchSpeed, "Speed   -1", 220, 10);
    configureSmallLabel(labelDispatchSpeed_,"Speed_ -1", 220, 60);
    configureSmallLabel(labelSetTrain,"Train  -1",120,100);
    configureSmallLabel(labelSetAuthority, "Authority    -1", 220, 100);
    configureSmallLabel(labelSetAuthority_, "Authority_ -1", 220, 160);
    configureSmallLabel(labelSetSpeed, "Speed   -1",320,100);
    configureSmallLabel(labelSetSpeed_, "Speed_  -1",320,160);
    configureSmallLabel(labelFindTrain,"Train  -1",120,190);
  }
  private void configureSmallLabel(JLabel label,String title,int x,int y){
    label.setText(title);
    label.setBounds(x,y,90,40);
    label.setFont(new Font("Arial",Font.PLAIN,12));
    frame.add(label);
  }
  //private void configureLargeLabel(JLabel label, String title,int x,int y){
//
//  }
  private void configureTextFields() { 
    tfDispatchAuthority.setBounds(120,40,80,30); frame.add(tfDispatchAuthority);
    tfDispatchSpeed.setBounds(220,40,80,30);     frame.add(tfDispatchSpeed);
    tfSetTrain.setBounds(120,130,80,30);         frame.add(tfSetTrain);
    tfSetAuthority.setBounds(220,130,80,30);     frame.add(tfSetAuthority);
    tfSetSpeed.setBounds(320,130,80,30);         frame.add(tfSetSpeed);
    tfFindTrain.setBounds(120,220,80,30);        frame.add(tfFindTrain);
  }
//////////constructor////////////////////
  GUI(String x) {
    lineColor = x;
    configureFrame();
    configureLabels();
    configureButtons();
    configureTextFields();
  }
///////////methods////////////////////////
public  void dispatchTrain() {
    numOfTrains++; 
    //abelTrainCount.setText("Train Count = " + numOfTrains);
    //speeds[numOfTrains] = Integer.valueOf(tfDispatchSpeed.getText());
   // authorities[numOfTrains] = Integer.valueOf(tfDispatchAuthority.getText());
    //labelDispatchSpeed.setText("Authority = " + speeds[numOfTrains]);
   // labelDispatchAuthority.setText("Authority = " +authorities[numOfTrains]);                 
    }
public void setTrain(int train, int speed){
    //int train = Integer.valueOf(tfSetTrain.getText());
    //int speed = Integer.valueOf(tfSetSpeed.getText());
   // int authority = Integer.valueOf(tfSetAuthority.getText());
    //labelSetTrain.setText("Train  "+train);
    //labelSetSpeed.setText("Speed  "+speed); 
   // labelSetAuthority.setText("Authority  "+authority);
    speeds[train] = speed; 
    authorities[train] = speed;
  }
private void findTrain(){
    int train = Integer.valueOf(tfFindTrain.getText());
    labelFindTrain.setText("Train  "+train);

  }
public void setAuthority(int train, int authority){
    authorities[train]=authority;
 }
 public static void main(String[] args) {
   GUI gui = new GUI("Blue");
 }
}
  


*/