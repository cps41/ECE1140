import java.io.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.ScrollPane;

public class TrackModelGUI
{
	private static JFrame frame;
	private static JPanel panel;
	private static JTable table;
	
	public TrackModelGUI()
	{
		String[][] data = new String[250][11];
		String[] columnNames = {"Section", "Block", "Block Length", "Block Grade", "Speed Limit", "Below Ground", "Direction", "Switch #", "Crossing", "Station Name", "Door Side"};
		
		for(int i = 0; i < Client.greenSize; i++)
		{
			data[i][0] = Client.greenSections[i+1];
			data[i][1] = Integer.toString(i+1);
			data[i][2] = Double.toString(Client.greenBlockLength[i+1]);
			data[i][3] = Double.toString(Client.greenBlockGrade[i+1]);
			data[i][4] = Integer.toString(Client.greenSpeedLimit[i+1]);
			data[i][5] = Boolean.toString(Client.greenBelowGround[i+1]);
			if(Client.greenDirection[i+1] == 0)
				data[i][6] = "Forward";
			else if(Client.greenDirection[i+1] == 1)
				data[i][6] = "Backward";
			else if(Client.greenDirection[i+1] == 2)
				data[i][6] = "Both";
			if(Client.greenSwitch[i+1] != 0)
				data[i][7] = Integer.toString(Client.greenSwitch[i+1]);
			if(Client.greenCrossing[i+1] == true)
				data[i][8] = "Crossing";
			
			boolean isGStation = !(Client.greenStation[i+1].length() == 1);
			if(isGStation)
			{
				data[i][9] = Client.greenStation[i+1];
				if(Client.greenSide[i+1] == 0)
					data[i][10] = "Left";
				else if(Client.greenSide[i+1] == 1)
					data[i][10] = "Right";
				else if(Client.greenSide[i+1] == 2)
					data[i][10] = "Both";
			}
		}
		
		
		table = new JTable(data, columnNames)
		{@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) 
			{
		        Component comp = super.prepareRenderer(renderer, row, col);
		        Object value = getModel().getValueAt(row, col);
	            if (value.equals(false)) 
	            {
	                comp.setBackground(Color.red);
	            } else if (value.equals(true)) 
	            {
	                comp.setBackground(Color.green);
	            } else {
	                comp.setBackground(Color.white);
	            }
		        return comp;
		    }
		};
		
		table.setBounds(20,20,200,400);
		
		
		JScrollPane sp = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setPreferredSize(new Dimension(800, 800));

		frame = new JFrame();
		frame.getContentPane().add(sp, BorderLayout.WEST);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1200,700));
		frame.setTitle("Track Model");
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
}