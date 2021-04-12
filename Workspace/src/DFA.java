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
	// Initial state
	private int initialState;
	// Filename of the .dfa
	private final String dfa_filename;
	// List of input strings
	private ArrayList<String> inStrings;
	// Exit states
	private ArrayList<Integer> exitStates;
	// Array of sigma chars
	private ArrayList<Character> sigma;
	// DFA data
	private ArrayList<ArrayList<Integer>> theDfa;
	
	/* Minimized DFA variables */
	// Minimized DFA data
	private ArrayList<ArrayList<Integer>> minDfa;
	// Accepting states
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
	 * Removes any unreachable states from the DFA.
	 */
	@SuppressWarnings("unchecked")
	private void removeUnreachables() {
		ArrayList<Integer> visited = new ArrayList<>();
		
		visited.add(initialState);
		getReachables(initialState, visited);
		
		// Only need to do the remove operation when there actually 
		// are unreachable states
		if (visited.size() < theDfa.size()) {
			visited.sort(null);
			
			ArrayList<ArrayList<Integer>> betterList = new ArrayList<>();
			ArrayList<Integer> betterExits = new ArrayList<>();
			
			// Traverse each visited part
			for (int i = 0; i < visited.size(); i++) {
				// Add the state that was reached
				betterList.add((ArrayList<Integer>) theDfa.get(visited.get(i)).clone());
				// Adjust path directions
				for (int j = 0; j < sigma.size(); j++) {
					int adjPath = betterList.get(i).get(j);
					if (exitStates.contains(adjPath)) {
						betterExits.add(visited.indexOf(adjPath));
					}
					betterList.get(i).set(j, visited.indexOf(adjPath));
				}
			}
			theDfa = (ArrayList<ArrayList<Integer>>) betterList.clone();
			exitStates = (ArrayList<Integer>) betterExits.clone();
			exitStates.sort(null);
		}
		
	}
	
	/**
	 * Helper method for removeUnreachables().
	 * @param state Starting state to test visits
	 * @param visits ArrayList of each visited state
	 */
	private void getReachables(int state, ArrayList<Integer> visits) {
		// Traverse each sigma char
		for (int i = 0; i < sigma.size(); i++) {
			int curState = theDfa.get(state).get(i);
			if (!visits.contains(curState)) {
				visits.add(curState);
				getReachables(curState, visits);
			}
		}
	}
	
	/**
	 * Determines if two states are in the same set.
	 * @param state1 First state to compare
	 * @param state2 Second state to compare
	 * @param set The set of states to check with
	 * @return true if both states go to the same sets with all sigma characters
	 */
	private boolean isSameSet(int state1, int state2, ArrayList<ArrayList<Integer>> set) {
		int connector1, connector2;
		
		// Check each sigma character
		for (int i = 0; i < sigma.size(); i++) {
			connector1 = theDfa.get(state1).get(i);
			connector2 = theDfa.get(state2).get(i);
			
			// They are guaranteed to be in the same set if they are the same
			if (connector1 != connector2) {
				// Check each set
				for (int j = 0; j < set.size(); j++) {
					// When there is a discrepency, they are not in the same set
					if (set.get(j).contains(connector1) && !set.get(j).contains(connector2)) {
						return false;
					}
					if (set.get(j).contains(connector2) && !set.get(j).contains(connector1)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Sorts a partitioned set.
	 * @param set Partition to sort
	 * @return sorted partition
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<ArrayList<Integer>> sortSet(ArrayList<ArrayList<Integer>> set) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<>();
		ArrayList<ArrayList<Integer>> temp = (ArrayList<ArrayList<Integer>>) set.clone();
		
		int minVal;
		int minIndex;
		while (!temp.isEmpty()) {
			minVal = temp.get(0).get(0);
			minIndex = 0;
			for (int i = 1; i < temp.size(); i++) {
				if (temp.get(i).get(0) < minVal) {
					minIndex = i;
					minVal = temp.get(i).get(0);
				}
			}
			result.add((ArrayList<Integer>) temp.get(minIndex).clone());
			temp.remove(minIndex);
		}
		
		return result;
	}
	
	/**
	 * Calculates the minimized DFA.
	 */
	@SuppressWarnings("unchecked")
	private void calculateMinimized() {
		// Remove any unreachable states
		removeUnreachables();
		
		// Initialize array lists
		minDfa 			= new ArrayList<>();
		acceptingStates = new ArrayList<>();
		
		// Get default states
		ArrayList<Integer> regStates = new ArrayList<>();
		for (int i = 0; i < theDfa.size(); i++) {
			if (!exitStates.contains(i)) {
				regStates.add(i);
			}
		}
		
		// Initialize the first partition (exit states and regular states)
		ArrayList<ArrayList<ArrayList<Integer>>> pset = new ArrayList<>();
		pset.add(new ArrayList<>());
		pset.get(0).add((ArrayList<Integer>) exitStates.clone());
		pset.get(0).add((ArrayList<Integer>) regStates.clone());
		
		int index = 0;
		boolean isFinished = false;
		
		// Main operation
		while (!isFinished) {
			// Initialize the next set of sets of states
			pset.add(new ArrayList<>());
			
			// Check every set in the current index
			for (int i = 0; i < pset.get(index).size(); i++) {
				// Put the first state into its own set
				int initSize = pset.get(index+1).size();
				pset.get(index+1).add(new ArrayList<>());
				pset.get(index+1).get(initSize).add(pset.get(index).get(i).get(0));
				
				// Size of the current set
				int curSize = pset.get(index).get(i).size();
				// First state of the set
				int curState = pset.get(index+1).get(initSize).get(0);
				
				// Check each state in the current set
				for (int j = 1; j < curSize; j++) {
					int secondState = pset.get(index).get(i).get(j);
					
					// When they are in the same set, combine them in the next set of sets
					if (isSameSet(curState, secondState, pset.get(index))) {
						pset.get(index+1).get(initSize).add(secondState);
					}
					else {
						// When they are not in the same set, check the first state 
						// of each added set until they are, otherwise add a new set
						boolean good = true;
						for (int k = 0; k < pset.get(index+1).size(); k++) {
							// Check if the first state of this set fits the second state
							if (isSameSet(pset.get(index+1).get(k).get(0), secondState, pset.get(index))) {
								// Make sure both of the sets are either exits or not
								if (exitStates.contains(pset.get(index+1).get(k).get(0)) == 
									exitStates.contains(secondState)) {
									pset.get(index+1).get(k).add(secondState);
									good = false;
								}
							}
						}
						if (good) {
							// Add a new set
							pset.get(index+1).add(new ArrayList<>());
							pset.get(index+1).get(pset.get(index+1).size()-1).add(secondState);
						}
					}
				}
			}
			
			// If the two last sets are similar, then we are done minimizing
			if (pset.get(index).equals(pset.get(index+1))) {
				//System.out.println("Result: " + sortSet(pset.get(index)).toString());
				isFinished = true;
			}
			else {
				index++;
			}
		}
		
		// Partitioned sets for the minimized DFA
		// Sorts the partitions based on the first number
		ArrayList<ArrayList<Integer>> miniDfa = (ArrayList<ArrayList<Integer>>) sortSet(pset.get(index)).clone();
		
		// Write to the minimum DFA data:
		// Check each partitioned state
		for (int i = 0; i < miniDfa.size(); i++) {
			// First state of the current partition
			int curIndex = miniDfa.get(i).get(0);
			int lastSpot = minDfa.size();
			
			// Determine if the current state is an accepting state
			if (exitStates.contains(curIndex)) {
				acceptingStates.add(i);
			}
			
			minDfa.add(new ArrayList<>());
			
			// Travel through each sigma char
			for (int j = 0; j < sigma.size(); j++) {
				int resultPath = theDfa.get(curIndex).get(j);
				
				// Check each partition until it finds the containing state
				for (int k = 0; k < miniDfa.size(); k++) {
					if (miniDfa.get(k).contains(resultPath)) {
						minDfa.get(lastSpot).add(k);
					}
				}
			}
		}
	}
	
	/**
	 * Tests if a string can pass through a DFA.
	 * @param dfa DFA graph to parse through
	 * @param isMinimized Put true if the provided DFA is minimized
	 * @param grammar String to parse
	 * @return true if grammar ends on an exit state
	 */
	private boolean parseString(ArrayList<ArrayList<Integer>> dfa, boolean isMinimized, String grammar) {
		int curState;
		char curChar;
		
		if (isMinimized) 
			curState = 0;
		else 
			curState = initialState;
		
		// Traverse each character
		for (int i = 0; i < grammar.length(); i++) {
			curChar = grammar.charAt(i);
			// Found an invalid character
			if (!sigma.contains(curChar)) {
				return false;
			}
			
			// Traverse the regular DFA if it isn't minimized
			if (!isMinimized) 
				curState = theDfa.get(curState).get(sigma.indexOf(curChar));
			else
				curState = minDfa.get(curState).get(sigma.indexOf(curChar));
		}
		
		if (!isMinimized)
			return exitStates.contains(curState);
		else
			return acceptingStates.contains(curState);
	}
	
	/**
	 * Outputs the results to the console.
	 */
	public void output() {
		System.out.println("\nParsing results of " + dfa_filename + 
				" on strings attached in " + dfa_filename + ":");
		
		// Parse provided strings on provided DFA
		// TODO: This code is reused twice. Put into a method.
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
			strResults.get(index).add(parseString(theDfa, false, s));
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
		System.out.println();
		
		// Display line separator
		System.out.print(" ------");
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
			if (!minDfa.isEmpty())
				strResults.get(index).add(parseString(minDfa, true, s));
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









