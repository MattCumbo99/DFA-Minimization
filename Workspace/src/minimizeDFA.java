import java.io.File;

public class minimizeDFA {

	public static void main(String[] args) {
		// Has to accept one command line argument
		if (args.length < 1) {
			System.out.println("Usage: java minimizeDFA filename.dfa");
			return;
		}
		
		File source = new File(args[0]);
		DFA theDFA = new DFA(source);
		
		theDFA.output();
	}

}
