package project;

import java.util.*;
import javax.swing.*;

public class PLCInterpreter extends trackController {
	
	//declare variables
	private static JFileChooser fileChooser = new JFileChooser();
	private static StringBuilder sb = new StringBuilder();
	private static java.io.File file1;
	private static java.io.File file2;
	private static String fileName1;
	private static String fileName2;
	private static Scanner compare1;
	private static Scanner compare2;
	private static Scanner input;
	
	private static String line;
	private static ArrayList<Integer> allSections = new ArrayList<Integer>();
	private static int section;
	private static String condition;
	private static String action;
	
	//first file opening method
	public void openFirstFile() throws Exception {
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			//get file
			file1 = fileChooser.getSelectedFile();
			fileName1 = file1.getName();
			
			//create scanners
			compare1 = new Scanner(file1);
			input = new Scanner(file1);
		}
	}
	
	//second file opening method
	public void openSecondFile() throws Exception {
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			//get file
			file2 = fileChooser.getSelectedFile();
			fileName2 = file2.getName();
			
			//create scanner
			compare2 = new Scanner(file2);
			
		}
	}

	//get file extension
	public String getFileType() {
		return fileName1.substring(fileName1.length()-3);
	}
	
	//create perfect voter to ensure files match
	public boolean compareFiles() {
		//if (!fileName1.equals(fileName2)) return false;
		while (compare1.hasNext() && compare2.hasNext())
			if (!compare1.nextLine().equals(compare2.nextLine())) {
				compare1.close();
				compare2.close();
				return false; 
			}
		compare1.close();
		compare2.close();
		return true;
	}
	
	//convert PLC to running java code
	public void runPLC() {
		while (input.hasNext()) {
			//get next line to interpret
			String command = input.nextLine();
			
			//set line and section numbers PLC is running on 
			if (command.contains("line =")) {
				int index = command.indexOf('=');
				line = command.substring(index+2);
			}
			if (command.contains("section =")) {
				Scanner intScanner = new Scanner(command);
				int index = command.indexOf('=');
				while (intScanner.hasNextInt())
					allSections.add(intScanner.nextInt());
			}
			
			//set condition and action
			if (command.contains("IF") && command.contains("THEN")) {
				int startIndex = command.indexOf("IF") + 3;
				int endIndex = command.indexOf("THEN") - 1;
				condition = command.substring(startIndex, endIndex);
				action = command.substring(endIndex + 6);
			}
		} 
		input.close();
			
			//execute actions
		while (condition.equals("trainPresent")) {
			for (int i=0; i<allSections.size(); i++) {
				section = allSections.get(i);
				
				//execute crossing action
				if (action.equals("activateCrossing")) {
					while (super.getTrainPresence(line, section) || super.getTrainPresence(line, section+1) || super.getTrainPresence(line, section-1))
						super.setCrossingStatus(line, section, true);
					super.setCrossingStatus(line, section, false);
				}
				
				//execute switching action
				if (action.equals("activateSwitch")) 
					if (super.getTrainPresence(line, section))
						super.setSwitchPosition(line, section, true);
			}
		}

	}
}
