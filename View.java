import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.filechooser.*; 
import java.io.*;
import java.nio.charset.StandardCharsets; 
import java.nio.file.*;
import java.util.*;  
public class View {
    public JPanel MAIN_PANEL = new JPanel();
    public JLabel TRAINS_DISPATCHED = new JLabel("Trains Dispatched:  ");
    public DispatchPanel DISPATCH_PANEL = new DispatchPanel();
    public DrawCanvas MAP = new DrawCanvas();
    public SchedulePanel SCHEDULE_PANEL = new  SchedulePanel();
    public BlockMaintenancePanel BLOCK_MAINTENANCE_PANEL = new BlockMaintenancePanel();
    public SwitchMaintenancePanel SWITCH_MAINTENANCE_PANEL = new SwitchMaintenancePanel();
    public PresencePanel PRESENCE_PANEL = new PresencePanel();
    public OccupancyPanel OCCUPANCY_PANEL = new OccupancyPanel();
    public ThroughputPanel THROUGHPUT_PANEL = new ThroughputPanel();
    public DispatchLog DISPATCH_LOG = new DispatchLog();
    
    public Controller CONTROLLER;
    View(){
        MAIN_PANEL.add(TRAINS_DISPATCHED);
        MAIN_PANEL.add(DISPATCH_PANEL);
        MAIN_PANEL.add(BLOCK_MAINTENANCE_PANEL);
        MAIN_PANEL.add(SWITCH_MAINTENANCE_PANEL);
        MAIN_PANEL.add(MAP);
        MAIN_PANEL.add(PRESENCE_PANEL);
        MAIN_PANEL.add(SCHEDULE_PANEL);
        MAIN_PANEL.add(THROUGHPUT_PANEL);
        MAIN_PANEL.add(OCCUPANCY_PANEL);
        MAIN_PANEL.add(DISPATCH_LOG);
    }
    public void configure(Controller control){
        CONTROLLER = control;
        DISPATCH_PANEL.configure();
        BLOCK_MAINTENANCE_PANEL.configure();
        SWITCH_MAINTENANCE_PANEL.configure();
    }
    private boolean checkTimeFormat(String time){
        String message = "Invalid Time Format. Please use: military time, no puncuation, specificity: minute, 4 digits";
        try{
            Integer.parseInt(time);
        }
        catch(Exception e){
            CTCSystem.errorMessage(message);
            return false;
        }
        if (Integer.parseInt(time) == -1) 
            return true;
        if (time.length() != 4){
            CTCSystem.errorMessage(message);
            return false;
        }
        int timeInt = Integer.parseInt(time);
        if (timeInt < 0 || timeInt > 2359 ){
            CTCSystem.errorMessage(message);
            return false;
        }
        char check = time.charAt(2);
        int check1 = Character.getNumericValue(check);
        if (check1 > 5){
            CTCSystem.errorMessage(message);
            return false;
        }
        return true;
    }
    private int[] parseTimeString(String string){
        int[] time = new int[2];
        if (Integer.parseInt(string) == -1){
            time[0] = -1;
            time[1] = 0;
            return time;}
        time[0] = ( Character.getNumericValue(string.charAt(0)) ) * 10 + Character.getNumericValue(string.charAt(1));
        time[1] = ( Character.getNumericValue(string.charAt(2)) ) * 10 + Character.getNumericValue(string.charAt(3));
        return time;
      }
    public void update(){
        TRAINS_DISPATCHED.setText("Trains Disptach: "+CONTROLLER.MODEL.trainsDispatched);
        BLOCK_MAINTENANCE_PANEL.update();
        SWITCH_MAINTENANCE_PANEL.update();
        if (!CTCSystem.LOCAL_MODE){
            PRESENCE_PANEL.update();
            OCCUPANCY_PANEL.update();
            THROUGHPUT_PANEL.update();
        }
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
        DispatchPanel(){
              setBorder(BorderFactory.createLineBorder(Color.black));
              configureButton();
              configureComboBox();
              configureRadioButton();
              add(PANEL_LABEL);
              add(DESTINATION_COMBOBOX);
              add(DESTINATION_BLOCK);
              add(TIME_DEPARTURE_TEXTFIELD);
              add(TIME_ARRIVAL_TEXTFIELD);
              add(BUTTON);
              add(DISPATCH_MODE_RADIOBUTTON);
        }
        private void configureRadioButton(){
            DISPATCH_MODE_RADIOBUTTON.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if (DISPATCH_MODE_RADIOBUTTON.isSelected() && CONTROLLER.MODEL.validSchedule){
                        BUTTON.setEnabled(false);
                        CONTROLLER.MODEL.enterAutomaticMode();
                        CONTROLLER.MODEL.schedule.clear();
                    }
                    else{
                        BUTTON.setEnabled(true);
                        CONTROLLER.MODEL.automaticMode = false;
                        CONTROLLER.MODEL.schedule.clear();
                        DISPATCH_LOG.update();
                    }

                }
            });

        }
        private void configureComboBox(){
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
                    DISPATCH_LOG.update();
                }
          });
        }
        public void configure(){
            for (int i=0;i<CONTROLLER.MODEL.DESTINATIONS.size();i++)
                DESTINATION_COMBOBOX.addItem(CONTROLLER.MODEL.DESTINATIONS.get(i));
        }
    }
    class DrawCanvas extends JPanel{
        public void paintComponent(Graphics g) {
            super.paintComponent(g);     // paint parent's background
            setBackground(Color.BLACK);  // set background color for this JPanel
    
            // Your custom painting codes. For example,
            // Drawing primitive shapes
            g.setColor(Color.YELLOW);    // set the drawing color
            g.drawLine(30, 40, 100, 200);
            g.drawOval(150, 180, 10, 10);
            g.drawRect(200, 210, 20, 30);
            g.setColor(Color.RED);       // change the drawing color
            g.fillOval(300, 310, 30, 50);
            g.fillRect(400, 350, 60, 50);
            // Printing texts
            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g.drawString("Testing custom drawing ...", 10, 20);
         }
    }
    class BlockMaintenancePanel extends JPanel{
        JLabel PANEL_LABEL = new JLabel("Block Maintenance");
        JComboBox<String> BLOCK_COMBOBOX = new JComboBox<>();
        JButton CLOSE_BUTTON = new JButton("Close Block");
        JButton OPEN_BUTTON = new JButton("Open Block");
        JLabel DOWN_BLOCK = new JLabel("Downed Blocks: ");
        JLabel BLOCKS_DOWN = new JLabel(" ");
        JLabel MAINTENANCE_BLOCK = new JLabel("Maintenance Blocks: ");
        JLabel BLOCKS_MAINTENANCE = new JLabel();

        BlockMaintenancePanel(){
            setBorder(BorderFactory.createLineBorder(Color.black));
            configureButtons();
            configureComboBox();
            add(PANEL_LABEL);
            add(BLOCK_COMBOBOX);
            add(CLOSE_BUTTON);
            add(OPEN_BUTTON);
            add(DOWN_BLOCK);
            add(BLOCKS_DOWN);
            add(MAINTENANCE_BLOCK);
            add(BLOCKS_MAINTENANCE);
        }
        private void configureButtons(){
            CLOSE_BUTTON.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Integer i = Integer.valueOf((String)BLOCK_COMBOBOX.getSelectedItem());
                    CONTROLLER.MODEL.maintenance[i] = true;
                }
            });
            OPEN_BUTTON.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Integer i = Integer.valueOf((String)BLOCK_COMBOBOX.getSelectedItem());
                    CONTROLLER.MODEL.maintenance[i] = false;
                    CONTROLLER.MODEL.maintenanceBlocks.remove((Object)i);
                }
            });
        }
        private void configureComboBox(){
            BLOCK_COMBOBOX.setPreferredSize(new Dimension(60,20));
            BLOCK_COMBOBOX.setEditable(false);
        }
        private void configure(){
            int size;
            if (CONTROLLER.MODEL.isGreen)
                size = CONTROLLER.MODEL.SIZE + 2;
            else
                size = CONTROLLER.MODEL.SIZE + 1;
            for (int i=1;i<=size;i++)
                BLOCK_COMBOBOX.addItem(Integer.toString(i));
        }
        public void update(){
            if (!CONTROLLER.MODEL.downedBlocks.isEmpty()){
                String string = "";
                for (int i=0;i<CONTROLLER.MODEL.downedBlocks.size();i++)
                    string = string + CONTROLLER.MODEL.downedBlocks.get(i) + " ";  
                BLOCKS_DOWN.setText(string);                
            }
            else
                BLOCKS_DOWN.setText("");  
            if (!CONTROLLER.MODEL.maintenanceBlocks.isEmpty()){
                String string = "";
                for (int i=0;i<CONTROLLER.MODEL.maintenanceBlocks.size();i++)
                    string = string + CONTROLLER.MODEL.maintenanceBlocks.get(i) + " ";
                BLOCKS_MAINTENANCE.setText(string);                      
            }
            else
                BLOCKS_MAINTENANCE.setText("");
        }
    }
    class SwitchMaintenancePanel extends JPanel{
        JLabel PANEL_LABEL = new JLabel("Switch Maintenance");
        JComboBox<String> SWITCH_COMBOBOX = new JComboBox<>();
        JButton TOGGLE_BUTTON = new JButton("Toggle Switch");
        JLabel SWITCH_POSITION = new JLabel(" ");

        SwitchMaintenancePanel(){
            setBorder(BorderFactory.createLineBorder(Color.black));
            configureButton();
            configureComboBox();
            add(PANEL_LABEL);
            add(SWITCH_COMBOBOX);
            add(TOGGLE_BUTTON);
            add(SWITCH_POSITION);
        }
        public void configure(){
            for (int i=1;i<=CONTROLLER.MODEL.SIZE;i++){
                if (CONTROLLER.MODEL.BLOCKS[i].SWITCH > 0)
                    SWITCH_COMBOBOX.addItem(Integer.toString(i));
            }
        }
        public void configureButton(){
            TOGGLE_BUTTON.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Integer i = Integer.valueOf((String)SWITCH_COMBOBOX.getSelectedItem());
                    CONTROLLER.MODEL.switchPositionLocal[i] = !CONTROLLER.MODEL.switchPosition[i];
                    if (CONTROLLER.MODEL.BLOCKS[i].TYPE){
                        if (CONTROLLER.MODEL.switchPositionLocal[i]){
                            if (CONTROLLER.MODEL.BLOCKS[i].CONNECTS[1].NUMBER > CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER)
                                SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[1].NUMBER));
                            else
                                SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER));
                        }
                        else{
                            if (CONTROLLER.MODEL.BLOCKS[i].CONNECTS[1].NUMBER > CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER)
                                SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER));
                            else
                                SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[1].NUMBER));   
                        }
                    }
                    else{
                        if (CONTROLLER.MODEL.switchPositionLocal[i]){
                            if (CONTROLLER.MODEL.BLOCKS[i].CONNECTS[0].NUMBER > CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER)
                                SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[0].NUMBER));
                            else
                                SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER));
                        }
                        else{
                            if (CONTROLLER.MODEL.BLOCKS[i].CONNECTS[0].NUMBER > CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER)
                                SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER));
                            else
                                SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[0].NUMBER));   
                        }
                    }
                }
            });
        }
        public void configureComboBox(){
            SWITCH_COMBOBOX.setPreferredSize(new Dimension(60,20));
            SWITCH_COMBOBOX.setEditable(false);
        }
        public void update(){
            Integer i = Integer.valueOf((String)SWITCH_COMBOBOX.getSelectedItem());
            if (!CONTROLLER.MODEL.maintenance[i])
                TOGGLE_BUTTON.setEnabled(false);
            else   
                TOGGLE_BUTTON.setEnabled(true);

            if (CONTROLLER.MODEL.BLOCKS[i].TYPE){
                if (CONTROLLER.MODEL.switchPosition[i]){
                    if (CONTROLLER.MODEL.BLOCKS[i].CONNECTS[1].NUMBER > CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER)
                        SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[1].NUMBER));
                    else
                        SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER));
                }
                else{
                    if (CONTROLLER.MODEL.BLOCKS[i].CONNECTS[1].NUMBER > CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER)
                        SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER));
                    else
                        SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[1].NUMBER));
                }
            }
            else{
                if (CONTROLLER.MODEL.switchPosition[i]){
                    if (CONTROLLER.MODEL.BLOCKS[i].CONNECTS[0].NUMBER > CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER)
                        SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[0].NUMBER));
                    else
                        SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER));
                }
                else{
                    if (CONTROLLER.MODEL.BLOCKS[i].CONNECTS[0].NUMBER > CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER)
                        SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[2].NUMBER));
                    else
                        SWITCH_POSITION.setText(Integer.toString(CONTROLLER.MODEL.BLOCKS[i].CONNECTS[0].NUMBER));
                }
            } 
        }
    }
    class SchedulePanel extends JPanel{
        JButton CHOOSE_SCHEDULE_BUTTON = new JButton("Choose Schedule");
        JButton USE_SCHEDULE_BUTTON = new JButton("Use Schedule");
        JLabel FILE_SELECTED_LABEL = new JLabel("No Schedule Selected");
        JFileChooser SCHEDULE_CHOOSER = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        SchedulePanel(){
            setBorder(BorderFactory.createLineBorder(Color.black));
            configureButtons();
            configureFileChooser();
            add(CHOOSE_SCHEDULE_BUTTON);
            add(FILE_SELECTED_LABEL);
        }
        public void configureButtons(){
            CHOOSE_SCHEDULE_BUTTON.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { 
                    int F = SCHEDULE_CHOOSER.showOpenDialog(MAIN_PANEL); 
                    String filename = "No Schedule Selected";
                    if (F == JFileChooser.APPROVE_OPTION)
                        filename = SCHEDULE_CHOOSER.getSelectedFile().getAbsolutePath();
                    if(CONTROLLER.MODEL.parseSchedule(filename)){
                        CONTROLLER.MODEL.validSchedule = true;
                        FILE_SELECTED_LABEL.setText(filename);
                        DISPATCH_LOG.update();
                    }
                    else{
                        CTCSystem.errorMessage("Only .xlsx files extensions are valid. Ensure file format is correct. See Canvas for correct file Format");
                        CONTROLLER.MODEL.validSchedule = false;
                    }
                }
            });

        }
        public void configureFileChooser(){
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT File", "txt");
            SCHEDULE_CHOOSER.setFileFilter(filter);
        }
    }
    class PresencePanel extends JPanel{
        JLabel PANEL_LABEL = new JLabel("Block Presence: ");
        JLabel BLOCKS_PRESENT = new JLabel(" ");
        PresencePanel(){
            setBorder(BorderFactory.createLineBorder(Color.black));
            add(PANEL_LABEL);
            add(BLOCKS_PRESENT);
        }
        public void update(){
            int size;
            if (CONTROLLER.MODEL.isGreen)
                size = CONTROLLER.MODEL.SIZE + 2;
            else
                size = CONTROLLER.MODEL.SIZE + 1;
            String string = "";
            for (int i=1;i<=size;i++){
                if ( CONTROLLER.MODEL.status[i][0] )
                    string = string + CONTROLLER.MODEL.BLOCKS[i].NUMBER;
            }
            BLOCKS_PRESENT.setText(string);
        }

    }
    class OccupancyPanel extends JPanel{
        JLabel PANEL_LABEL = new JLabel("Train Occupancy: ");
        ArrayList<JPanel> TRAIN_PASSENGER_COUNTS = new ArrayList<JPanel>();
        OccupancyPanel(){
            setBorder(BorderFactory.createLineBorder(Color.black));
            add(PANEL_LABEL);
        }
        public void update(){
            removeAll();
            add(PANEL_LABEL);
            if (CTCSystem.TRAINS_INFO != null) {
                if (!CTCSystem.TRAINS_INFO.isEmpty()){
                for (int i=0;i<CTCSystem.TRAINS_INFO.size();i++){
                    if (  !(boolean)CTCSystem.TRAINS_INFO.get(i).get(4)  && (boolean) CTCSystem.TRAINS_INFO.get(i).get(8) == CONTROLLER.MODEL.isGreen )
                        add(new JLabel("Train: "+ i + " Passenger Count: "+ CTCSystem.TRAINS_INFO.get(i).get(3)+", "));
                        CONTROLLER.MODEL.passengerCount = CONTROLLER.MODEL.passengerCount + (int)CTCSystem.TRAINS_INFO.get(i).get(3);
                }
            }}
            repaint();
            revalidate();
        }
    }
    class ThroughputPanel extends JPanel{
        JLabel PANEL_LABEL = new JLabel("Throughput: ");
        JLabel THROUGHPUT = new JLabel();
        ThroughputPanel(){
            setBorder(BorderFactory.createLineBorder(Color.black));
            add(PANEL_LABEL);
            add(THROUGHPUT);
        }
        public void update(){
            THROUGHPUT.setText(Float.toString(CONTROLLER.MODEL.throughput));
        }
    }
    class DispatchLog extends JPanel{
        JLabel PANEL_LABEL = new JLabel ("Dispatch Log");
        JTextArea LOG = new JTextArea();
        DispatchLog(){
            setBorder(BorderFactory.createLineBorder(Color.black));
            add(PANEL_LABEL);
            add(LOG);
        }
        public void update(){
                String string = "";
                if (!CONTROLLER.MODEL.schedule.isEmpty()){
                for (int i=0;i<CONTROLLER.MODEL.schedule.size();i++){
                    int[] departure = (int[]) CONTROLLER.MODEL.schedule.get(i);
                    if (departure[1] < 10){
                        String temp = "0"+Integer.toString(departure[1]);
                        string = string + " Departure:" +departure[0]+":"+temp+", Destination: ALL STATIONS, \n";
                    }
                    else
                        string = string + " Departure:" +departure[0]+":"+departure[1]+", Destination: ALL STATIONS, \n";   
                }}
                LOG.setText(string);

        
        }
    }

} 