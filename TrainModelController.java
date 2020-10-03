import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TrainModelController {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;

    public TrainModelController() {
        prepareGUI();
    }

    public static void main(String[] args) {
        TrainModelController swingControlDemo = new TrainModelController();
        swingControlDemo.showEventDemo();
    }
    private void prepareGUI(){
        mainFrame = new JFrame("Welcome");
        mainFrame.setSize(400,400);
        mainFrame.setLayout(new GridLayout(3, 1));

        headerLabel = new JLabel("",JLabel.CENTER );
        statusLabel = new JLabel("",JLabel.CENTER);
        statusLabel.setSize(350,100);
      
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
            }        
        });
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);  
    }
   private void showEventDemo(){
        headerLabel.setText("Control in action: Button"); 

        JButton loginButton = new JButton("LOG IN");

        loginButton.setActionCommand("LOG IN");

        loginButton.addActionListener(new ButtonClickListener());

        controlPanel.add(loginButton);       

        mainFrame.setVisible(true);  
    }
    private class ButtonClickListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();  
        
            if( command.equals("LOG IN"))  {
                statusLabel.setText("Ok Button clicked.");
            }		
        }
    }
}
