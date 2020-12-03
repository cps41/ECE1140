import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
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
	public TrackModelGUI()
	{
		//create data array to give to JTable
		String[][] greenData = new String[200][24];
		String[][] redData = new String[200][24];
		String[] columnNames = {"Section", "Block", "Length(m)", "Grade(%)", "Elevation(m)", "Speed Limit(km/h)", "Underground", "Direction", "Switch", "Position", "Crossing", 
				               "Lights",  "Station Name", "Door Side", "BeaconData", "Waiting", "Disembark", "Presence", "Closed", "Rail", "Circuit", "Power", "Heater", "Temp.(F)"};
		
		//fill greenData
		for(int i = 0; i < Client.greenSize; i++)
		{
			greenData[i][0] = Client.greenSections[i+1];
			greenData[i][1] = Integer.toString(i+1);
			greenData[i][2] = Double.toString(Client.greenBlockLength[i+1]);
			greenData[i][3] = Double.toString(Client.greenBlockGrade[i+1]);
			greenData[i][4] = String.format("%.3g%n", Client.greenBlockElevation[i+1]);
			greenData[i][5] = Integer.toString(Client.greenSpeedLimit[i+1]);
			if(Client.greenBelowGround[i+1] == true)
				greenData[i][6] = "True";
			else
				greenData[i][6] = "False";
			if(Client.greenDirection[i+1] == 0)
				greenData[i][7] = "Forward";
			else if(Client.greenDirection[i+1] == 1)
				greenData[i][7] = "Backward";
			else if(Client.greenDirection[i+1] == 2)
				greenData[i][7] = "Both";
			if(Client.greenSwitch[i+1] != 0)
				greenData[i][8] = Integer.toString(Client.greenSwitch[i+1]);
			if(Client.greenCrossing[i+1] == true)
				greenData[i][10] = "True";
			greenData[i][11] = "";
			boolean isGStation = !(Client.greenStation[i+1].length() == 1);
			if(isGStation)
			{
				greenData[i][12] = Client.greenStation[i+1];
				if(Client.greenSide[i+1] == 0)
				{
					greenData[i][13] = "Left";
					greenData[i][14] = "0" + Client.greenStation[i+1];
				}
				else if(Client.greenSide[i+1] == 1)
				{
					greenData[i][13] = "Right";
					greenData[i][14] = "1" + Client.greenStation[i+1];
				}
				else if(Client.greenSide[i+1] == 2)
				{
					greenData[i][13] = "Both";
					greenData[i][14] = "2" + Client.greenStation[i+1];
				}
			}
			greenData[i][15] = "";
			greenData[i][16] = "";
			greenData[i][17] = "";
			greenData[i][18] = "";
			greenData[i][19] = "";
			greenData[i][20] = "";
			greenData[i][21] = "";
			greenData[i][22] = "";
			greenData[i][23] = "70";
		}
		//yard blocks
		if(Client.greenDirection[Client.greenSize+1] == Client.greenDirection[Client.greenSize+2] && Client.greenSwitch[Client.greenSize+1] == Client.greenSwitch[Client.greenSize+2]) //if represent same section
			greenData[Client.greenSize][0] = "Yard"; //both in and out
		else
			greenData[Client.greenSize][0] = "InYard";
		greenData[Client.greenSize][1] = Integer.toString(Client.greenSize + 1);
		if(Client.greenDirection[Client.greenSize+1] == 0)
			greenData[Client.greenSize][7] = "Forward";
		else
			greenData[Client.greenSize][7] = "Both";
		greenData[Client.greenSize][8] = Integer.toString(Client.greenSwitch[Client.greenSize+1]);
		if(Client.greenDirection[Client.greenSize+1] != Client.greenDirection[Client.greenSize+2] && Client.greenSwitch[Client.greenSize+1] != Client.greenSwitch[Client.greenSize+2])
		{
			greenData[Client.greenSize+1][0] = "OutYard";
			greenData[Client.greenSize+1][1] = Integer.toString(Client.greenSize + 2);
			if(Client.greenDirection[Client.greenSize+2] == 1)  //would be == 0, but I never want to display backwards for block out of yard - even though it is internally
				greenData[Client.greenSize+1][7] = "Forward";
			else
				greenData[Client.greenSize+1][7] = "Both";
			greenData[Client.greenSize+1][8] = Integer.toString(Client.greenSwitch[Client.greenSize+2]);
		}
		
		//fill redData
		for(int i = 0; i < Client.redSize; i++)
		{
			redData[i][0] = Client.redSections[i+1];
			redData[i][1] = Integer.toString(i+1);
			redData[i][2] = Double.toString(Client.redBlockLength[i+1]);
			redData[i][3] = Double.toString(Client.redBlockGrade[i+1]);
			redData[i][4] = String.format("%.3g%n", Client.redBlockElevation[i+1]);//Double.toString(Client.redBlockElevation[i+1]);
			redData[i][5] = Integer.toString(Client.redSpeedLimit[i+1]);
			if(Client.redBelowGround[i+1] == true)
				redData[i][6] = "True";
			else
				redData[i][6] = "False";
			if(Client.redDirection[i+1] == 0)
				redData[i][7] = "Forward";
			else if(Client.redDirection[i+1] == 1)
				redData[i][7] = "Backward";
			else if(Client.redDirection[i+1] == 2)
				redData[i][7] = "Both";
			if(Client.redSwitch[i+1] != 0)
				redData[i][8] = Integer.toString(Client.redSwitch[i+1]);
			if(Client.redCrossing[i+1] == true)
				redData[i][10] = "True";
			redData[i][11] = "";
			boolean isRStation = !(Client.redStation[i+1].length() == 1);
			if(isRStation)
			{
				redData[i][12] = Client.redStation[i+1];
				if(Client.redSide[i+1] == 0)
				{
					redData[i][13] = "Left";
					redData[i][14] = "0" + Client.redStation[i+1];
				}
				else if(Client.redSide[i+1] == 1)
				{
					redData[i][13] = "Right";
					redData[i][14] = "1" + Client.redStation[i+1];
				}
				else if(Client.redSide[i+1] == 2)
				{
					redData[i][13] = "Both";
					redData[i][14] = "2" + Client.redStation[i+1];
				}
			}
			redData[i][15] = "";
			redData[i][16] = "";
			redData[i][17] = "";
			redData[i][18] = "";
			redData[i][19] = "";
			redData[i][20] = "";
			redData[i][21] = "";
			redData[i][22] = "";
			redData[i][23] = "70";
		}
		//yard blocks
		if(Client.redDirection[Client.redSize+1] == Client.redDirection[Client.redSize+2] && Client.redSwitch[Client.redSize+1] == Client.redSwitch[Client.redSize+2]) //if represent same section
			redData[Client.redSize][0] = "Yard"; //both in and out
		else
			redData[Client.redSize][0] = "InYard";
		redData[Client.redSize][1] = Integer.toString(Client.redSize + 1);
		if(Client.redDirection[Client.redSize+1] == 0)
			redData[Client.redSize][7] = "Forward";
		else
			redData[Client.redSize][7] = "Both";
		redData[Client.redSize][8] = Integer.toString(Client.redSwitch[Client.redSize+1]);
		if(Client.redDirection[Client.redSize+1] != Client.redDirection[Client.redSize+2] && Client.redSwitch[Client.redSize+1] != Client.redSwitch[Client.redSize+2])
		{
			redData[Client.redSize+1][0] = "OutYard";
			redData[Client.redSize+1][1] = Integer.toString(Client.redSize + 2);
			if(Client.redDirection[Client.redSize+2] == 1)  //would be == 0, but I never want to display backwards for block out of yard - even though it is internally
				redData[Client.redSize+1][7] = "Forward";
			else
				redData[Client.redSize+1][7] = "Both";
			redData[Client.redSize+1][8] = Integer.toString(Client.redSwitch[Client.redSize+2]);
		}
		
		//create JTable and specify column widths
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		//create green line data table
		JTable greenTable = new JTable(greenData, columnNames);
		greenTable.setBounds(20,20,1495,800);
		greenTable.getColumnModel().getColumn(0).setPreferredWidth(20);
		greenTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(1).setPreferredWidth(15);
		greenTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(2).setPreferredWidth(35);
		greenTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(3).setPreferredWidth(30);
		greenTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(4).setPreferredWidth(50);
		greenTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(5).setPreferredWidth(80);
		greenTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(6).setPreferredWidth(55);
		greenTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(7).setPreferredWidth(50);
		greenTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(8).setPreferredWidth(20);
		greenTable.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(9).setPreferredWidth(25);
		greenTable.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(10).setPreferredWidth(35);
		greenTable.getColumnModel().getColumn(10).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(11).setPreferredWidth(20);
		greenTable.getColumnModel().getColumn(11).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(12).setPreferredWidth(80);
		greenTable.getColumnModel().getColumn(12).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(13).setPreferredWidth(35);
		greenTable.getColumnModel().getColumn(13).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(14).setPreferredWidth(60);
		greenTable.getColumnModel().getColumn(14).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(15).setPreferredWidth(20);
		greenTable.getColumnModel().getColumn(15).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(16).setPreferredWidth(45);
		greenTable.getColumnModel().getColumn(16).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(17).setPreferredWidth(35);
		greenTable.getColumnModel().getColumn(17).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(18).setPreferredWidth(20);
		greenTable.getColumnModel().getColumn(18).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(19).setPreferredWidth(15);
		greenTable.getColumnModel().getColumn(19).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(20).setPreferredWidth(20);
		greenTable.getColumnModel().getColumn(20).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(21).setPreferredWidth(20);
		greenTable.getColumnModel().getColumn(21).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(22).setPreferredWidth(25);
		greenTable.getColumnModel().getColumn(22).setCellRenderer(centerRenderer);
		greenTable.getColumnModel().getColumn(23).setPreferredWidth(30);
		greenTable.getColumnModel().getColumn(23).setCellRenderer(centerRenderer);
		
		//create red line data table
		JTable redTable = new JTable(redData, columnNames);
		redTable.setBounds(20,20,1495,800);
		redTable.getColumnModel().getColumn(0).setPreferredWidth(20);
		redTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(1).setPreferredWidth(15);
		redTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(2).setPreferredWidth(35);
		redTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(3).setPreferredWidth(30);
		redTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(4).setPreferredWidth(50);
		redTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(5).setPreferredWidth(80);
		redTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(6).setPreferredWidth(55);
		redTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(7).setPreferredWidth(50);
		redTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(8).setPreferredWidth(20);
		redTable.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(9).setPreferredWidth(25);
		redTable.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(10).setPreferredWidth(35);
		redTable.getColumnModel().getColumn(10).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(11).setPreferredWidth(20);
		redTable.getColumnModel().getColumn(11).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(12).setPreferredWidth(80);
		redTable.getColumnModel().getColumn(12).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(13).setPreferredWidth(35);
		redTable.getColumnModel().getColumn(13).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(14).setPreferredWidth(60);
		redTable.getColumnModel().getColumn(14).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(15).setPreferredWidth(20);
		redTable.getColumnModel().getColumn(15).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(16).setPreferredWidth(45);
		redTable.getColumnModel().getColumn(16).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(17).setPreferredWidth(35);
		redTable.getColumnModel().getColumn(17).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(18).setPreferredWidth(20);
		redTable.getColumnModel().getColumn(18).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(19).setPreferredWidth(15);
		redTable.getColumnModel().getColumn(19).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(20).setPreferredWidth(20);
		redTable.getColumnModel().getColumn(20).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(21).setPreferredWidth(20);
		redTable.getColumnModel().getColumn(21).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(22).setPreferredWidth(25);
		redTable.getColumnModel().getColumn(22).setCellRenderer(centerRenderer);
		redTable.getColumnModel().getColumn(23).setPreferredWidth(30);
		redTable.getColumnModel().getColumn(23).setCellRenderer(centerRenderer);
		
		//create JScrollPane to house green JTable
		JScrollPane greenScrollpane = new JScrollPane(greenTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		greenScrollpane.setPreferredSize(new Dimension(1495, 800));
		
		//create JScrollPane to house red JTable
		JScrollPane redScrollpane = new JScrollPane(redTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		redScrollpane.setPreferredSize(new Dimension(1495, 800));
		
		//create green line panel to house buttons
		JPanel greenPanel = new JPanel();
		greenPanel.setLayout(null); //use absolute layout (able to position buttons)
		
		//create red line panel to house buttons
		JPanel redPanel = new JPanel();
		redPanel.setLayout(null); //use absolute layout (able to position buttons)
		
		//add update button to green line panel
		JButton greenUpdate = new JButton("Update"); 
		greenPanel.add(greenUpdate);
		greenUpdate.setBounds(10,10,150,60);
		greenUpdate.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = 1; i <= Client.greenSize; i++) //when press update
				{
					if(Client.greenStation[i].length() > 1) //if station
					{
						String numPass = Integer.toString(Client.greenPassengerCount[i]);
						greenTable.getModel().setValueAt(numPass , i-1, 15); //update waiting passenger count at every station
						if(Client.TrainModelGreenPassCountObj != null)
							greenTable.getModel().setValueAt(Integer.toString(Client.TrainModelGreenPassCount[i]), i-1, 16); //update disembark passenger count if not null
					}
					if(Client.greenStationStatus != null && Client.greenStation[i].length() > 1) //display when at station
					{
						if(Client.greenStationStatus[i] == true)
							greenTable.getModel().setValueAt("*"+Client.greenStation[i]+"*", i-1, 12);
						else
							greenTable.getModel().setValueAt(Client.greenStation[i], i-1, 12);
					}
					if(Client.greenCrossingStatus[i] == true)  //if train on crossing block, show crossing active
						greenTable.getModel().setValueAt("*ACTIVE*", i-1, 10);
					else if(Client.greenCrossing[i] == true)
						greenTable.getModel().setValueAt("True", i-1, 10);
					if(Client.greenLight[i] == true)    //if train is on crossing block or switch block, show lights active
						greenTable.getModel().setValueAt("ON", i-1, 11);
					else
						greenTable.getModel().setValueAt("", i-1, 11);
					if(Client.greenMaintenance[i] != null)
					{
						if(Client.greenMaintenance[i] == true) //if block closed, display true
							greenTable.getModel().setValueAt("True", i-1, 18);
						else
							greenTable.getModel().setValueAt("", i-1, 18);
					}
					if(Client.greenTrainPresence[i] != null) //if received train presence
					{
						if(Client.greenTrainPresence[i] == true)
							greenTable.getModel().setValueAt("True", i-1, 17); //update every train presence value
						else
							greenTable.getModel().setValueAt("", i-1, 17);
					}
					if(Client.greenSwitch[i] > 0) //if block contains switch source
					{
						int lowestBlock = Client.greenSize+3; //higher than yard blocks numbers
						int highestBlock = 0;
						int extend = 2; //number of yard blocks to account for (defaults to 2)
						if(Client.greenSwitch[Client.greenSize + 1] == Client.greenSwitch[Client.greenSize + 2] && Client.greenDirection[Client.greenSize + 1] == Client.greenDirection[Client.greenSize + 2] || Client.greenDirection[Client.greenSize + 2] == -1) //if only 1 yard block
							extend = 1;
						for(int j = 1; j <= Client.greenSize + extend; j++) //find highest and lowest   //+extend to account for yard blocks(can only be sinks = no switch on yard blocks = i iterates 1 -> size)
						{
							if(Client.greenSwitch[j] == Client.greenSwitch[i] * -1) //if sink block for corresponding source block
							{
								if(j > highestBlock)
									highestBlock = j;
								if(j < lowestBlock)
									lowestBlock = j;
							}
						}
						if(Client.greenSwitchPosition[i] != null)//if switch positions are set
						{
							if(Client.greenSwitchPosition[i] == false) //if switch is set to false/retracted (point to lower block)
							{
								greenTable.getModel().setValueAt(Integer.toString(lowestBlock), i-1, 9); //source block switch position
								greenTable.getModel().setValueAt(Integer.toString(i), lowestBlock-1, 9); //lowest sink block switch position (connected)
								greenTable.getModel().setValueAt("None", highestBlock-1, 9); 			//highest sink block switch position (disconnected)
							}
							else //if switch is set to true/extended (greenSwitchPosition[i] == true) (point to higher block)
							{
								greenTable.getModel().setValueAt(Integer.toString(highestBlock), i-1, 9); //source block switch position
								greenTable.getModel().setValueAt("None", lowestBlock-1, 9); 			  //lowest sink block switch position (disconnected)
								greenTable.getModel().setValueAt(Integer.toString(i), highestBlock-1, 9); //highest sink block switch position (connected)
							}
						}
					}
				}
			}
		});
		
		//add update button to red line panel
		JButton redUpdate = new JButton("Update"); 
		redPanel.add(redUpdate);
		redUpdate.setBounds(10,10,150,60);
		redUpdate.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = 1; i <= Client.redSize; i++) //when press update
				{
					if(Client.redStation[i].length() > 1) //if station
					{
						String numPass = Integer.toString(Client.redPassengerCount[i]);
						redTable.getModel().setValueAt(numPass , i-1, 15); //update waiting passenger count at every station
						if(Client.TrainModelRedPassCountObj != null)
							redTable.getModel().setValueAt(Integer.toString(Client.TrainModelRedPassCount[i]), i-1, 16); //update disembark passenger count if not null
					}
					if(Client.redStationStatus != null && Client.redStation[i].length() > 1) //display when at station
					{
						if(Client.redStationStatus[i] == true)
							redTable.getModel().setValueAt("*"+Client.redStation[i]+"*", i-1, 12);
						else
							redTable.getModel().setValueAt(Client.redStation[i], i-1, 12);
					}
					if(Client.redCrossingStatus[i] == true)  //if train on crossing block, show crossing active
						redTable.getModel().setValueAt("*ACTIVE*", i-1, 10);
					else if(Client.redCrossing[i] == true)
						redTable.getModel().setValueAt("True", i-1, 10);
					if(Client.redLight[i] == true)    //if train is on crossing block or switch block, show lights active
						redTable.getModel().setValueAt("ON", i-1, 11);
					else
						redTable.getModel().setValueAt("", i-1, 11);
					if(Client.redMaintenance[i] != null)
					{
						if(Client.redMaintenance[i] == true) //if block closed, display true
							redTable.getModel().setValueAt("True", i-1, 18);
						else
							redTable.getModel().setValueAt("", i-1, 18);
					}
					if(Client.redTrainPresence[i] != null) //if received train presence
					{
						if(Client.redTrainPresence[i] == true)
							redTable.getModel().setValueAt("True", i-1, 17); //update every train presence value
						else
							redTable.getModel().setValueAt("", i-1, 17);
					}
					if(Client.redSwitch[i] > 0) //if block contains switch source
					{
						int lowestBlock = Client.redSize+3; //higher than yard blocks numbers
						int highestBlock = 0;
						int extend = 2; //number of yard blocks to account for (defaults to 2)
						if(Client.redSwitch[Client.redSize + 1] == Client.redSwitch[Client.redSize + 2] && Client.redDirection[Client.redSize + 1] == Client.redDirection[Client.redSize + 2] || Client.redDirection[Client.redSize + 2] == -1) //if only 1 yard block
							extend = 1;
						for(int j = 1; j <= Client.redSize + extend; j++) //find highest and lowest   //+extend to account for yard blocks(can only be sinks = no switch on yard blocks = i iterates 1 -> size)
						{
							if(Client.redSwitch[j] == Client.redSwitch[i] * -1) //if sink block for corresponding source block
							{
								if(j > highestBlock)
									highestBlock = j;
								if(j < lowestBlock)
									lowestBlock = j;
							}
						}
						if(Client.redSwitchPosition[i] != null)//if switch positions are set
						{
							if(Client.redSwitchPosition[i] == false) //if switch is set to false/retracted (point to lower block)
							{
								redTable.getModel().setValueAt(Integer.toString(lowestBlock), i-1, 9); //source block switch position
								redTable.getModel().setValueAt(Integer.toString(i), lowestBlock-1, 9); //lowest sink block switch position (connected)
								redTable.getModel().setValueAt("None", highestBlock-1, 9); 			//highest sink block switch position (disconnected)
							}
							else //if switch is set to true/extended (redSwitchPosition[i] == true) (point to higher block)
							{
								redTable.getModel().setValueAt(Integer.toString(highestBlock), i-1, 9); //source block switch position
								redTable.getModel().setValueAt("None", lowestBlock-1, 9); 			  //lowest sink block switch position (disconnected)
								redTable.getModel().setValueAt(Integer.toString(i), highestBlock-1, 9); //highest sink block switch position (connected)
							}
						}
					}
				}
			}
		});
		
		String twoLines = "Generate\nPassengers";
		Random rand = new Random();
		//add generate passengers button to green line panel
		JButton greenGenerator = new JButton("<html>" + twoLines.replaceAll("\\n", "<br>") + "</html>");
		greenPanel.add(greenGenerator);
		greenGenerator.setBounds(10, 110, 150, 60);
		greenGenerator.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = 1; i <= Client.greenSize; i++)
				{
					if(Client.greenStation[i].length() > 1 && Client.greenPassengerCount[i] < 1) //if 0 passengers waiting at station
						Client.greenPassengerCount[i] = rand.nextInt(14) + 1;  //generate new passengers
				}
			}
		});
		
		//add generate passengers button to red line panel
		JButton redGenerator = new JButton("<html>" + twoLines.replaceAll("\\n", "<br>") + "</html>");
		redPanel.add(redGenerator);
		redGenerator.setBounds(10, 110, 150, 60);
		redGenerator.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = 1; i <= Client.redSize; i++)
				{
					if(Client.redStation[i].length() > 1 && Client.redPassengerCount[i] < 1) //if 0 passengers waiting at station
						Client.redPassengerCount[i] = rand.nextInt(14) + 1;  //generate new passengers
				}
			}
		});
		
		//add block JLabel to green line panel
		JLabel greenBlock = new JLabel("Block:");
		greenBlock.setBounds(10,200,50,15);
		greenPanel.add(greenBlock);
		
		//add block JLabel to red line panel
		JLabel redBlock = new JLabel("Block:");
		redBlock.setBounds(10,200,50,15);
		redPanel.add(redBlock);
		
		//add block number drop down box for green line panel
		JComboBox greenSelect = new JComboBox();
		greenPanel.add(greenSelect);
		greenSelect.setBounds(10,220,150,20);
		for(int i = 1; i <= Client.greenSize; i++)
		{
			greenSelect.addItem(Integer.toString(i));
		}
		
		//add block number drop down box for red line panel
		JComboBox redSelect = new JComboBox();
		redPanel.add(redSelect);
		redSelect.setBounds(10,220,150,20);
		for(int i = 1; i <= Client.redSize; i++)
		{
			redSelect.addItem(Integer.toString(i));
		}

		//add heat toggle button to green line panel
		JButton greenHeat = new JButton("Toggle Track Heater");
		greenPanel.add(greenHeat);
		greenHeat.setBounds(10, 270, 150, 50);
		greenHeat.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = Integer.parseInt((String)greenSelect.getSelectedItem()) - 1; //pull selected value from drop down box to use for track heater
				if(greenTable.getModel().getValueAt(value, 22) == "")					 //select: 1 -> size | value: 0 -> size-1
					greenTable.getModel().setValueAt("ON", value, 22);
				else if(greenTable.getModel().getValueAt(value, 22) == "ON")
					greenTable.getModel().setValueAt("", value, 22);
			}
		});
		
		//add heat toggle button to red line panel
		JButton redHeat = new JButton("Toggle Track Heater");
		redPanel.add(redHeat);
		redHeat.setBounds(10, 270, 150, 50);
		redHeat.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = Integer.parseInt((String)redSelect.getSelectedItem()) - 1; //pull selected value from drop down box to use for track heater
				if(redTable.getModel().getValueAt(value, 22) == "")					   //select: 1 -> size | value: 0 -> size-1
					redTable.getModel().setValueAt("ON", value, 22);
				else if(redTable.getModel().getValueAt(value, 22) == "ON")
					redTable.getModel().setValueAt("", value, 22);
			}
		});
		
		//add murphy's break track button to green line panel
		JButton greenTrack = new JButton("Rail Failure"); 
		greenPanel.add(greenTrack);
		greenTrack.setBounds(10, 350, 150, 50);
		greenTrack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = Integer.parseInt((String)greenSelect.getSelectedItem()) - 1; //pull selected value from drop down box to use for broken track
				if(greenTable.getModel().getValueAt(value, 19) == "")					 //select: 1 -> size | value: 0 -> size-1
				{													
					greenTable.getModel().setValueAt("Fail", value, 19);
					Client.greenTrackStatus[value+1] = true;
				}
				else if(greenTable.getModel().getValueAt(value, 19) == "Fail")
				{
					greenTable.getModel().setValueAt("", value, 19);
					Client.greenTrackStatus[value+1] = false;
				}
			}
		});
		
		//add murphy's break track button to red line panel
		JButton redTrack = new JButton("Rail Failure"); 
		redPanel.add(redTrack);
		redTrack.setBounds(10, 350, 150, 50);
		redTrack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = Integer.parseInt((String)redSelect.getSelectedItem()) - 1; //pull selected value from drop down box to use for broken track
				if(redTable.getModel().getValueAt(value, 19) == "")			  		   //select: 1 -> size | value: 0 -> size-1
				{
					redTable.getModel().setValueAt("Fail", value, 19);
					Client.redTrackStatus[value+1] = true;
				}
				else if(redTable.getModel().getValueAt(value, 19) == "Fail")
				{
					redTable.getModel().setValueAt("", value, 19);
					Client.redTrackStatus[value+1] = false;
				}
			}
		});
		
		//add murphy's break track circuit button to green line panel
		JButton greenCircuit = new JButton("Circuit Failure"); 
		greenPanel.add(greenCircuit);
		greenCircuit.setBounds(10, 410, 150, 50);
		greenCircuit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = Integer.parseInt((String)greenSelect.getSelectedItem()) - 1; //pull selected value from drop down box to use for broken track
				if(greenTable.getModel().getValueAt(value, 20) == "")					 //select: 1 -> size | value: 0 -> size-1
				{													
					greenTable.getModel().setValueAt("Fail", value, 20);
					Client.greenCircuitStatus[value+1] = true;
				}
				else if(greenTable.getModel().getValueAt(value, 20) == "Fail")
				{
					greenTable.getModel().setValueAt("", value, 20);
					Client.greenCircuitStatus[value+1] = false;
				}
			}
		});
		
		//add murphy's break track circuit button to red line panel
		JButton redCircuit = new JButton("Circuit Failure"); 
		redPanel.add(redCircuit);
		redCircuit.setBounds(10, 410, 150, 50);
		redCircuit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = Integer.parseInt((String)redSelect.getSelectedItem()) - 1; //pull selected value from drop down box to use for broken track
				if(redTable.getModel().getValueAt(value, 20) == "")					 //select: 1 -> size | value: 0 -> size-1
				{													
					redTable.getModel().setValueAt("Fail", value, 20);
					Client.redCircuitStatus[value+1] = true;
				}
				else if(redTable.getModel().getValueAt(value, 20) == "Fail")
				{
					redTable.getModel().setValueAt("", value, 20);
					Client.redCircuitStatus[value+1] = false;
				}
			}
		});
		
		//add murphy's power failure button to green line panel
		JButton greenPower = new JButton("Power Failure"); 
		greenPanel.add(greenPower);
		greenPower.setBounds(10, 470, 150, 50);
		greenPower.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = Integer.parseInt((String)greenSelect.getSelectedItem()) - 1; //pull selected value from drop down box to use for broken track
				if(greenTable.getModel().getValueAt(value, 21) == "")					 //select: 1 -> size | value: 0 -> size-1
				{													
					greenTable.getModel().setValueAt("Fail", value, 21);
					Client.greenPowerStatus[value+1] = true;
				}
				else if(greenTable.getModel().getValueAt(value, 21) == "Fail")
				{
					greenTable.getModel().setValueAt("", value, 21);
					Client.greenPowerStatus[value+1] = false;
				}
			}
		});
		
		//add murphy's power failure button to red line panel
		JButton redPower = new JButton("Power Failure"); 
		redPanel.add(redPower);
		redPower.setBounds(10, 470, 150, 50);
		redPower.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = Integer.parseInt((String)redSelect.getSelectedItem()) - 1; //pull selected value from drop down box to use for broken track
				if(redTable.getModel().getValueAt(value, 21) == "")					 //select: 1 -> size | value: 0 -> size-1
				{													
					redTable.getModel().setValueAt("Fail", value, 21);
					Client.redPowerStatus[value+1] = true;
				}
				else if(redTable.getModel().getValueAt(value, 21) == "Fail")
				{
					redTable.getModel().setValueAt("", value, 21);
					Client.redPowerStatus[value+1] = false;
				}
			}
		});
		
		//add block JLabel to green line panel
		JLabel greenTemp = new JLabel("Temperature:");
		greenTemp.setBounds(10,570,80,15);
		greenPanel.add(greenTemp);
		
		//add block JLabel to red line panel
		JLabel redTemp = new JLabel("Temperature:");
		redTemp.setBounds(10,570,80,15);
		redPanel.add(redTemp);
		
		//add temperature drop down box for green line panel
		JComboBox greenSelectTemp = new JComboBox();
		greenPanel.add(greenSelectTemp);
		greenSelectTemp.setBounds(10,590,150,20);
		for(int i = 0; i <= 120; i++)
		{
			greenSelectTemp.addItem(Integer.toString(i));
		}
		greenSelectTemp.setSelectedIndex(70); //default drop down to 70
		
		//add temperature drop down box for red line panel
		JComboBox redSelectTemp = new JComboBox();
		redPanel.add(redSelectTemp);
		redSelectTemp.setBounds(10,590,150,20);
		for(int i = 0; i <= 120; i++)
		{
			redSelectTemp.addItem(Integer.toString(i));
		}
		redSelectTemp.setSelectedIndex(70); //default drop down to 70
		
		//add set temperature button to green line panel
		JButton greenSetTemp = new JButton("Set Temperature"); 
		greenPanel.add(greenSetTemp);
		greenSetTemp.setBounds(10,640,150,50);
		greenSetTemp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = Integer.parseInt((String)greenSelect.getSelectedItem()) - 1; //block number select
				String temp = (String)greenSelectTemp.getSelectedItem();  //temperature select
				greenTable.getModel().setValueAt(temp, value, 23); 
			}
		});
		
		//add set temperature button to red line panel
		JButton redSetTemp = new JButton("Set Temperature"); 
		redPanel.add(redSetTemp);
		redSetTemp.setBounds(10,640,150,50);
		redSetTemp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int value = Integer.parseInt((String)redSelect.getSelectedItem()) - 1; //block number select
				String temp = (String)redSelectTemp.getSelectedItem();  //temperature select
				redTable.getModel().setValueAt(temp, value, 23); 
			}
		});

		//add green split pane (scroll pane on left, control panel on right)
		JSplitPane greenSplit = new JSplitPane(SwingConstants.VERTICAL, greenScrollpane, greenPanel);
		
		//add red split pane (scroll pane on left, control panel on right)
		JSplitPane redSplit = new JSplitPane(SwingConstants.VERTICAL, redScrollpane, redPanel);
		
		//add tabbed pane to combine green line and red line
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Green Line", greenSplit);
		tabbedPane.addTab("Red Line", redSplit);

		//create final frame and add tabbed pane
		JFrame frame = new JFrame();
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1695,800));
		frame.setTitle("Track Model");
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
			
	}
}