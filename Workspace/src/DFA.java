import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Calculates a minimized DFA.
 * @author Matthew Cumbo
 * @since  2021-03-31
 */
public class DFA {
	
	/* Initial DFA variables */
	// Filename of the .dfa
	private final String dfa_filename;
	// Array of sigma chars
	private ArrayList<Character> sigma;
	// DFA data
	private ArrayList<ArrayList<Integer>> theDfa;
	// Initial state
	private int initialState;
	// Accepting states
	private ArrayList<Integer> exitStates;
	// List of input strings
	private ArrayList<String> inStrings;
	
	/* Minimized DFA variables */
	// Minimized DFA data
	private ArrayList<ArrayList<Integer>> minDfa;
	// Exit states
	private ArrayList<Integer> acceptingStates;
	
	/**
	 * Creates a new DFA graph.
	 * @param source DFA file to read from
	 */
	public DFA(File source) {
		dfa_filename = source.getName();
		theDfa = new ArrayList<>();
		inStrings = new ArrayList<>();
		sigma = new ArrayList<>();
		
		try {
			Scanner input = new Scanner(source);
			String data;
			String[] nums;
			int index = 0;
			int totalStates = 0;
			
			// Read file data
			while (input.hasNextLine()) {
				data = input.nextLine();
				
				if (index == 0) { // Reading first line (total states)
					totalStates = Integer.parseInt(data);
				}
				else if (index == 1) { // Reading sigma
					data = data.substring(6); // Remove the "Sigma:"
					data = data.replaceAll("\\s+", " "); // Remove excess spaces
					data = data.trim();
					String[] vals = data.split(" ");
					
					// Instantiate the sigma chars
					for (int i = 0; i < vals.length; i++) {
						sigma.add(vals[i].charAt(0));
					}
				}
				else if (data.contains("---")) {
					// Do nothing
				}
				else if (index-3 < totalStates) { // Initiate the DFA table
					data = data.substring(6); // Remove the sigma identifier
					data = data.replaceAll("\\s+", " "); // Remove excess spaces
					data = data.trim();
					nums = data.split(" ");
					theDfa.add(new ArrayList<>());
					
					for (String s: nums) {
						theDfa.get(index-3).add(Integer.parseInt(s));
					}
				}
				else if (data.contains("Initial State")) {
					// Remove the initial state text
					data = data.replace(": Initial State", "");
					initialState = Integer.parseInt(data);
				}
				else if (data.contains("Accepting State(s)")) {
					// Remove the accepting state text
					data = data.replace(": Accepting State(s)", "");
					nums = data.split(",");
					exitStates = new ArrayList<>();
					
					for (String s: nums) {
						exitStates.add(Integer.parseInt(s));
					}
				}
				else if (inStrings.isEmpty() && data.isEmpty()) {
					inStrings.add("");
				}
				else if (!data.isEmpty()) { // Read input strings
					inStrings.add(data);
				}
				index++;
			}
			input.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error: File " + source.getAbsolutePath() + " does not exist.");
		}
	}
	
	/**
	 * Calculates the minimized DFA.
	 */
	private void calculateMinimized() {
		// TODO: Calculate minimized DFA
		minDfa = new ArrayList<>();
		acceptingStates = new ArrayList<>();
	}
	
	/**
	 * Tests if a string can pass through a DFA.
	 * @param dfa DFA graph to parse through
	 * @param grammar String to parse
	 * @return true if grammar ends on an exit state
	 */
	private boolean parseString(ArrayList<ArrayList<Integer>> dfa, String grammar) {
		// TODO: Parse a string on provided DFA
		return false;
	}
	
	/**
	 * Outputs the results to the console.
	 */
	public void output() {
		System.out.println("\nParsing results of " + dfa_filename + 
				" on strings attached in " + dfa_filename + ":");
		
		// Parse provided strings on provided DFA
		// XXX: This code is reused twice. Put into a method.
		ArrayList<ArrayList<Boolean>> strResults = new ArrayList<>();
		int index = 0;
		
		strResults.add(new ArrayList<>());
		
		for (String s: inStrings) {
			// If the results pass 15, make a new line
			if (strResults.get(index).size() == 15) {
				index++;
				strResults.add(new ArrayList<>());
			}
			// Add the result to the list
			strResults.get(index).add(parseString(theDfa, s));
		}
		
		int numYes = 0;
		int numNo  = 0;
		
		// Output the results of the parsed string
		for (int i = 0; i < strResults.size(); i++) {
			System.out.print(" ");
			for (int j = 0; j < strResults.get(i).size(); j++) {
				// Display Yes
				if (strResults.get(i).get(j) == true) {
					System.out.print("Yes  ");
					numYes++;
				}
				// Display No
				else {
					System.out.print("No   ");
					numNo++;
				}
			}
			System.out.println();
		}
		System.out.println("\nYes:" + numYes + " No:" + numNo);
		/* End of reused code */
		
		// Display minimized DFA header
		System.out.println("\nMinimized DFA from " + dfa_filename + ":");
		System.out.print(" Sigma:");
		
		for (int i = 0; i < sigma.size(); i++) {
			System.out.format("%5c", sigma.get(i));
		}
		
		// Display line separator
		System.out.print("\n ------");
		for (int i = 0; i < sigma.size(); i++) {
			System.out.print("-----");
		}
		System.out.println();
		
		calculateMinimized();
		
		// Display the actual minimized DFA
		if (!minDfa.isEmpty()) {
			for (int i = 0; i < minDfa.size(); i++) {
				System.out.format("%6d:", i);
				for (int j = 0; j < minDfa.get(i).size(); j++) {
					System.out.format("%5d", minDfa.get(i).get(j));
				}
				System.out.println();
			}
		}
		
		// Display line separator
		System.out.print(" ------");
		for (int i = 0; i < sigma.size(); i++) {
			System.out.print("-----");
		}
		System.out.println();
		
		// Display minimized DFA closers
		// Initial state
		// XXX: Always assumes initial state is 0!
		System.out.println(" 0: Initial State");
		
		// Display accepting states
		if (!acceptingStates.isEmpty()) {
			System.out.print(" " + acceptingStates.get(0));
			for (int i = 1; i < acceptingStates.size(); i++) {
				System.out.print("," + acceptingStates.get(i));
			}
			System.out.println(": Accepting State(s)");
		}
		else {
			System.out.println(" No accepting states");
		}
		System.out.println();
		
		// Parse provided strings on minimized DFA
		System.out.println("Parsing results of minimized " + dfa_filename + " on same set of strings:");
		
		// Parse strings on minimized DFA
		index = 0;
		strResults.clear();
		strResults.add(new ArrayList<>());
		for (String s: inStrings) {
			// If the results pass 15, make a new line
			if (strResults.get(index).size() == 15) {
				index++;
				strResults.add(new ArrayList<>());
			}
			// Add the result to the list
			strResults.get(index).add(parseString(minDfa, s));
		}
		numYes = 0;
		numNo  = 0;
		
		// Output the results of the parsed string
		for (int i = 0; i < strResults.size(); i++) {
			System.out.print(" ");
			for (int j = 0; j < strResults.get(i).size(); j++) {
				// Display Yes
				if (strResults.get(i).get(j) == true) {
					System.out.print("Yes  ");
					numYes++;
				}
				// Display No
				else {
					System.out.print("No   ");
					numNo++;
				}
			}
			System.out.println();
		}
		System.out.println("\nYes:" + numYes + " No:" + numNo);
	}
}









