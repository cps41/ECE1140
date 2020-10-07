package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TrainModelGUI extends JPanel{
    private GridBagConstraints c = new GridBagConstraints();
    private Container panel;
    private JLabel power;
    private JLabel velocity;

    public TrainModelGUI() {
		power.setFont(new Font("Serif", Font.PLAIN, 35));
		velocity.setFont(new Font("Serif", Font.PLAIN, 35));
        panel.setLayout(new GridBagLayout());

        c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(power, c);

        c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(velocity, c);
    }
    
}
