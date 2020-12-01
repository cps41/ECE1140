import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
public class CTCSystem {
    public static JFrame MAIN_FRAME = new JFrame("Centralized Traffic Control");
    public static JPanel HEADER_PANEL = new JPanel();
    public static JLabel TIME_LABEL = new JLabel("waiting for server");
    public static JLabel TF_SPINNER_LABEL = new JLabel("Time Factor");
    public static JSpinner TF_SPINNER = new JSpinner(new SpinnerNumberModel(1,1,Clock.TIME_FACTOR_LIMIT,1));
    public static JTabbedPane TABBED_PANES = new JTabbedPane();
    public static Model RED_MODEL = new Model(false);
    public static Model GREEN_MODEL = new Model(true);
    public static Controller RED_CONTROLLER = new Controller();;
    public static Controller GREEN_CONTROLLER = new Controller();
    public static View RED_VIEW = new View();
    public static View GREEN_VIEW = new View();
    public static ArrayList<ArrayList<Object>> TRAINS = new ArrayList<ArrayList<Object>>();
    public static int dispatchCount = 0;

    public static void main(String[] args){
        configureSystem();
        while (MAIN_FRAME.isShowing())
            updateSystem();
    }
    private static void configureSystem(){
        CTCOfficeClient.configure();
        MAIN_FRAME.add(HEADER_PANEL);
        MAIN_FRAME.add(TABBED_PANES);
        configureFrame();
        configureSpinner();
        configureHeader();
        configureTabbedPane();
        GREEN_MODEL.configure();
        GREEN_CONTROLLER.configure(GREEN_MODEL,GREEN_VIEW);
        GREEN_VIEW.configure(GREEN_CONTROLLER);
        RED_MODEL.configure();
        RED_CONTROLLER.configure(RED_MODEL,RED_VIEW);
        RED_VIEW.configure(RED_CONTROLLER);
    }
    private static void configureFrame(){
        MAIN_FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MAIN_FRAME.setSize(1000,650);
        MAIN_FRAME.setResizable(false);
        MAIN_FRAME.setLayout(null);
        MAIN_FRAME.setVisible(true);
    }  
    private static void configureSpinner(){
        TF_SPINNER.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Clock.timeFactor = (int)TF_SPINNER.getValue();
            }
        });
    }
    private static void configureHeader(){
        HEADER_PANEL.setBounds(0,0,950,30);
        HEADER_PANEL.add(TIME_LABEL);
        HEADER_PANEL.add(TF_SPINNER_LABEL);
        HEADER_PANEL.add(TF_SPINNER);
    }
    private static void configureTabbedPane(){
        TABBED_PANES.setBounds(10,30,950,560);
        TABBED_PANES.add("Green",GREEN_VIEW.MAIN_PANEL);
        TABBED_PANES.add("Red",RED_VIEW.MAIN_PANEL);
    }

    private static void updateSystem(){     
        CTCOfficeClient.update();
        TIME_LABEL.setText(Clock.getDate());
        Clock.update();

    }
    
}
