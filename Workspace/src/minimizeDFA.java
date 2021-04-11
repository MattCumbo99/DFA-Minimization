import java.io.File;

public class minimizeDFA {

	public static void main(String[] args) {
		// Has to accept one command line argument
		if (args.length < 1) {
			System.out.println("Usage: java minimizeDFA filename.dfa");
			return;
		}
		
		File source = new File(args[0]);
		if (source.exists()) {
			DFA theDFA = new DFA(source);
			theDFA.output();
		}
		else {
			System.err.println("Error: File " + source.getAbsolutePath() + " does not exist.");
		}
		
	}

}
