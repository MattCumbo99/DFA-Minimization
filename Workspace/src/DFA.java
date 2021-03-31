import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DFA {
	
	// Filename of the .dfa
	private final String dfa_filename;
	// Array of sigma chars
	private char[] sigma;
	// DFA data
	private ArrayList<ArrayList<Integer>> theDfa;
	// Initial state
	private int initialState;
	// Accepting states
	private ArrayList<Integer> exitStates;
	// List of input strings
	private ArrayList<String> inStrings;
	
	/**
	 * Creates a new DFA graph.
	 * @param source DFA file to read from
	 */
	public DFA(File source) {
		dfa_filename = source.getName();
		theDfa = new ArrayList<>();
		inStrings = new ArrayList<>();
		
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
					String[] vals = data.split("    ");
					sigma = new char[vals.length];
					
					// Instantiate the sigma chars
					for (int i = 0; i < vals.length; i++) {
						sigma[i] = vals[i].charAt(0);
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
	 * Outputs the results to the console.
	 */
	public void output() {
		System.out.println("\nParsing results of " + dfa_filename + " on strings attached in " + dfa_filename + ":");
	}
}
