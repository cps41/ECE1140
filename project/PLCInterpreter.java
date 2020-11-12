package project;

import java.util.*;
import javax.swing.*;

public class PLCInterpreter extends trackController {
	
	//declare variables
	private static JFileChooser fileChooser1 = new JFileChooser();
	private static JFileChooser fileChooser2 = new JFileChooser();
	public static java.io.File file1;
	public static java.io.File file2;
	private static String fileName1;
	private static String fileName2;
	private static Scanner compare1;
	private static Scanner compare2;
	private static Scanner input;
	
	private static String line;
	private static int section;
	private static int source;
	private static int higher;
	private static int lower;
	private static String condition;
	private static String action;
	
	//first file opening method
	public void openFirstFile() {
		if (fileChooser1.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			//get file
			file1 = fileChooser1.getSelectedFile();
			fileName1 = file1.getName();
			
			try {
				//create scanner
				compare1 = new Scanner(file1);
			} catch (Exception e) {
				System.out.println("cancelled");
			}
		}
	}
	
	//second file opening method
	public void openSecondFile() {
		if (fileChooser2.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			//get file
			file2 = fileChooser2.getSelectedFile();
			fileName2 = file2.getName();
			
			try {
				//create scanner
				compare2 = new Scanner(file2);
			} catch (Exception e) {
				System.out.println("cancelled");
			}
			
		}
	}

	//get file extension
	public String getFileType() {
		return fileName1.substring(fileName1.length()-3);
	}
	
	//create perfect voter to ensure files match
	public boolean compareFiles() {
		while (compare1.hasNext() && compare2.hasNext())
			if (!compare1.nextLine().equals(compare2.nextLine())) {
				compare1.close();
				compare2.close();
				return false; 
			}
		return true;
	}
	
	//convert PLC to running java code
	public void runPLC(java.io.File file) {
		ArrayList<Integer> allSections = new ArrayList<Integer>();
		try {
			//create scanner
			input = new Scanner(file);
		} catch (Exception e) {
			System.out.println("cancelled");
		}
		while (input.hasNext()) {
			//get next line to interpret
			String command = input.nextLine();
			
			//set line and section numbers PLC is running on 
			if (command.contains("line =")) {
				int index = command.indexOf('=');
				line = command.substring(index+2);
			}
			if (command.contains("section =")) {
				int index = command.indexOf('=');
				String trackSections = command.substring(index+2);
				Scanner intScanner = new Scanner(trackSections);
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
		
		//execute crossing action
		if (action.equals("activateCrossing")) {
			if (condition.equals("trainPresent")) {
				for (int i=0; i<allSections.size(); i++) {
					section = allSections.get(i);
					while (super.getTrainPresence(line, section) || super.getTrainPresence(line, section+1) || super.getTrainPresence(line, section-1)) 
						super.setCrossingStatus(line, section, true);
					super.setCrossingStatus(line, section, false);
				}
			}
		}
		
		//execute switching action
		if (action.equals("activateSwitch")) {
			if (condition.equals("trainPresent")) {
				for (int i=0; i<allSections.size(); i+=3) {
					source = allSections.get(i);
					higher = allSections.get(i+1);
					lower = allSections.get(i+2);
					if (super.getTrainPresence(line, source) || super.getTrainPresence(line, higher) || super.getTrainPresence(line, lower)) {
						Boolean nextPosition = super.redQueue.get(source).poll();
						if (nextPosition != null) super.setSwitchPosition(line, source, nextPosition);
					}
				}
			}
		}
	}
}
