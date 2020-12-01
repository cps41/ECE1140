import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
public class View {
    public JPanel MAIN_PANEL = new JPanel();
    public DispatchPanel DISPATCH_PANEL = new DispatchPanel();
    public Controller CONTROLLER;
    View(){}
    public void configure(Controller control){
        CONTROLLER = control;
        if (CONTROLLER == null)
            System.out.println("this shit makes no sense");
        DISPATCH_PANEL.configure();
        MAIN_PANEL.add(DISPATCH_PANEL);
    }
    private boolean checkTimeFormat(String time){
        String message = "Invalid Time Format. Please use: military time, no puncuation, specificity: minute, 4 digits";
        try{
            Integer.parseInt(time);
        }
        catch(Exception e){
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
        int[] time = new int[2];
        time[0] = ( Character.getNumericValue(string.charAt(0)) ) * 10 + Character.getNumericValue(string.charAt(1));
        time[1] = ( Character.getNumericValue(string.charAt(2)) ) * 10 + Character.getNumericValue(string.charAt(3));
        return time;
      }
    private void errorMessage(String message){
        JOptionPane.showMessageDialog(new JFrame(), message, "error", JOptionPane.ERROR_MESSAGE);
      }
    private void successMessage(String message){
        JOptionPane.showMessageDialog(new JFrame(), message, "success", JOptionPane.INFORMATION_MESSAGE);
    }
    class DispatchPanel extends JPanel {
        //componets
        JLabel PANEL_LABEL = new JLabel("Dispatch Train:");
        JComboBox<String> DESTINATION_COMBOBOX = new JComboBox<>();
        JLabel DESTINATION_BLOCK = new JLabel();
        JRadioButton DISPATCH_MODE_RADIOBUTTON = new JRadioButton("automatic");
        JButton BUTTON = new JButton("Dispatch");
        JTextField TIME_ARRIVAL_TEXTFIELD = new JTextField("Arrival Time",7);
        JTextField TIME_DEPARTURE_TEXTFIELD = new JTextField("Departure Time",9);
        //constructor - sets border color, calls component configurators, and adds components to line panel
        DispatchPanel(){
              setBorder(BorderFactory.createLineBorder(Color.black));
              configureButton();
              configureDestinationComboBox();
              add(PANEL_LABEL);
              add(DESTINATION_COMBOBOX);
              add(DESTINATION_BLOCK);
              add(TIME_DEPARTURE_TEXTFIELD);
              add(TIME_ARRIVAL_TEXTFIELD);
              add(BUTTON);
              add(DISPATCH_MODE_RADIOBUTTON);
        }
        //component configuration - called in the constructor
        private void configureDestinationComboBox(){
            DESTINATION_COMBOBOX.setPreferredSize(new Dimension(150,20));
            DESTINATION_COMBOBOX.setEditable(false);
            DESTINATION_COMBOBOX.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    int destinationBlock = CONTROLLER.MODEL.destinationBlock(DESTINATION_COMBOBOX.getSelectedItem().toString());
                    String section = CONTROLLER.MODEL.BLOCKS[destinationBlock].SECTION;
                    DESTINATION_BLOCK.setText(Integer.toString(destinationBlock)+", "+section);
                }
            });
        }
        private void configureButton(){
            // user action, checks format and parses text inputs for Controller method call.
            BUTTON.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String destinationString = DESTINATION_COMBOBOX.getSelectedItem().toString();
                    String arrivalString = TIME_ARRIVAL_TEXTFIELD.getText();
                    String departureString = TIME_DEPARTURE_TEXTFIELD.getText();
                    if ( !checkTimeFormat(arrivalString)) {return;}
                    if ( !checkTimeFormat(departureString)) {return;}
                    int[] arrivalTime = parseTimeString(arrivalString);
                    int[] departureTime = parseTimeString(departureString);
                    int destination = CONTROLLER.destinationBlock(destinationString);
                    CONTROLLER.MODEL.dispatchSignal(departureTime,arrivalTime,destination);
                }
          });
        }
        //post construction configuration - sets the destinations in the combo box
        public void configure(){
            if (CONTROLLER.MODEL == null)
                System.out.println("shit");
            for (int i=0;i<CONTROLLER.MODEL.DESTINATIONS.size();i++)
                DESTINATION_COMBOBOX.addItem(CONTROLLER.MODEL.DESTINATIONS.get(i));
        }
    }

}