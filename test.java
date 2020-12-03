import java.io.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;


import java.awt.*;
import javax.swing.*;

public class test {
    public static void main(String[] args) {
        Icon intOff = new ImageIcon(new File("img/intOff.png").getAbsolutePath());
        JFrame f = new JFrame();
        f.setVisible(true);

        ImageIcon icon = new ImageIcon("img/intOff.png", "My ImageIcon");

        if (icon != null) {
            JLabel picture = new JLabel("HERE IS ICON", icon, JLabel.CENTER);
            picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
            picture.setHorizontalAlignment(JLabel.CENTER);
        }
    }
}