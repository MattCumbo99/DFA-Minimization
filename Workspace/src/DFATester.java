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
		theDfa = new DFA(new File("Xnfa.dfa"));
		System.setOut(new PrintStream(outContent));
	}
	
	@Test
	void testOutput() {
		theDfa.output();
		assertEquals("\n"
				+ "Parsing results of Xnfa.dfa on strings attached in Xnfa.dfa:\r\n"
				+ " Yes  Yes  Yes  Yes  No   Yes  No   No   Yes  No   Yes  No   Yes  No   No   \r\n"
				+ " Yes  No   No   No   No   Yes  Yes  No   No   Yes  Yes  Yes  No   No   No   \r\n"
				+ "\n"
				+ "Yes:14 No:16\r\n"
				+ "\n"
				+ "Minimized DFA from Xnfa.dfa:\r\n"
				+ " Sigma:    a    b    c    d\r\n"
				+ " --------------------------\r\n"
				+ "     0:    1    2    0    3\r\n"
				+ "     1:    1    3    1    1\r\n"
				+ "     2:    1    3    2    3\r\n"
				+ "     3:    3    3    2    3\r\n"
				+ " --------------------------\r\n"
				+ " 0: Initial State\r\n"
				+ " 0,2,3: Accepting State(s)\r\n"
				+ "\r\n"
				+ "Parsing results of minimized Xnfa.dfa on same set of strings:\r\n"
				+ " Yes  Yes  Yes  Yes  No   Yes  No   No   Yes  No   Yes  No   Yes  No   No   \r\n"
				+ " Yes  No   No   No   No   Yes  Yes  No   No   Yes  Yes  Yes  No   No   No   \r\n"
				+ "\n"
				+ "Yes:14 No:16\r\n", outContent.toString());
		
	}
	
	@After
	void restoreStreams() {
		System.setOut(originalOut);
	}

}
