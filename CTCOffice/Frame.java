import java.util.ArrayList;
import javax.swing.*;

public class Frame extends JFrame { 
    //components
    public static JTabbedPane tp = new JTabbedPane();

    //constructor - calls all configurators
    Frame(){
        super("Centralized Traffic Control");
        configureFrame();
        configureTabbedPane();
    }
    //component configurators
    private void configureFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,650);
        setResizable(false);
        setLayout(null);
        setVisible(true);
    }
    private void configureTabbedPane(){
      tp.setBounds(10,10,950,560);
      //tp.add("green",CTCSystem.GREEN_LINE_CONTROLLER.PANEL);
      //tp.add("red",CTCSystem.RED_LINE_CONTROLLER.PANEL);
      add(tp);
    }
}