import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DFATester {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private DFA theDfa;
	
	@BeforeEach
	void initDFA() {
		theDfa = new DFA(new File("./src/Xnfa.dfa"));
		System.setOut(new PrintStream(outContent));
	}
	
	@Test
	void testOutput() {
		theDfa.output();
		assertEquals("\n"
				+ "Parsing results of Xnfa.dfa on strings attached in Xnfa.dfa:\n"
				+ " Yes  Yes  Yes  Yes  No  Yes  No   No  Yes  No   Yes  No   Yes  No  No\n"
				+ " Yes  No   No   No   No  Yes  Yes  No  No   Yes  Yes  Yes  No   No  No\n"
				+ "\n"
				+ "Yes:14 No:16\n"
				+ "\n"
				+ "Minimized DFA from Xnfa.dfa:\n"
				+ " Sigma:    a    b    c    d\n"
				+ " --------------------------\n"
				+ "     0:    1    2    0    3\n"
				+ "     1:    1    3    1    1\n"
				+ "     2:    1    3    2    3\n"
				+ "     3:    3    3    2    3\n"
				+ " --------------------------\n"
				+ " 0: Initial State\n"
				+ " 0,2,3: Accepting State(s)\n"
				+ "\n"
				+ "Parsing results of minimized Xnfa.dfa on same set of strings:\n"
				+ " Yes  Yes  Yes  Yes  No  Yes  No   No  Yes  No   Yes  No   Yes  No  No\n"
				+ " Yes  No   No   No   No  Yes  Yes  No  No   Yes  Yes  Yes  No   No  No\n"
				+ "\n"
				+ "Yes:14 No:16\n", outContent.toString());
		
	}
	
	@After
	void restoreStreams() {
		System.setOut(originalOut);
	}

}
