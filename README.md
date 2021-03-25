# IT 328, Introduction to the Theory of Computation Programming Assignment 3, Minimize DFA

**Due date:** April 11, 2021, Sunday, 11:55 PM, 50 points (40 on programs, 10 on report)
Write a Java program to minimize any given DFA to an optimal DFA. The DFA is prepared in a text
file as the input of your program. Your program should parse the strings appended at the end of the DFA
description (see below), minimize the DFA, and parse the same strings again with the optimal DFA. Your
programs will be compiled, run, and graded on our Unix system.
DFA file format: Xnfa.dfa
6
Sigma: a b c d
------------------------------
0: 1 2 0 3
1: 4 3 4 4
2: 1 5 2 3
3: 5 3 2 5
4: 4 3 4 4
5: 5 5 2 5
------------------------------
0: Initial State
0,2,3,5: Accepting State(s)
bacbcdacacbcaabcaabdcadbcaabacacbaccabdcadbdcadbdcaba
abcaabacacbcadbcadbcacbacddcaabcaab
...
• The first line contains the number of states of the DFA; in this case, there are 6 states.
• The second line Sigma: a b c d indicates Σ = {a, b, c, d}.
• Lines between two dashed lines are the transition table, in which every line is a state and its transitions. The number to the left of the colon is the label of the state. After the colon, each column
indicates the next state with input on the corresponding alphabet in the Sigma line. For example,
δ(q0, a) = q1, δ(q0, b) = q2, δ(q0, c) = q0, and δ(q1, d) = q3.
• After the transitions table (i.e, the 2nd dashed line), there are two lines for initial state, q0, and the
set of accepting states, {q0, q2, q3, q5}, respectively.
• After the line for accepting states, there are 30 lines of strings for this DFA to parse before and after
minimization. Of course, if your program is correct, you should get the same results. Note that,
the first string is always the empty string as indicated by a blank line. Here we only show 3 strings
(including the empty string) and use ... to denote the rest.
What to do
1. Make a new directory ∼/IT328/dfa on your unix account for this assignment. If you need any
programs developed from the previous assignment, copy them all to this directory. Don’t put any
of your programs in any package.
IT 328 Programming Assignment 3
2. Copy all files from my public directory: /home/ad.ilstu.edu/cli2/Public/IT328/dfa to your
∼/IT328/dfa. There are Anfa.dfa, Bnfa.dfa, Cnfa.dfa, Dnfa.dfa, Enfa.dfa, Fnfa.dfa, Xnfa.dfa,
and AX.dfa.
Those files are DFA files using the format described above. I will not test your program on Fnfa.dfa,
and AX.dfa is optional for extra credit (10 points). Except AX.dfa, all DFA’s are obtained from the
NFA’s used in the previous assignment. For example, Anfa.dfa is obtained from A.nfa, and that’s
why I name it Anfa.dfa. Same to others. Don’t use the results of your program for the previous
assignment, since your program may be incorrect.
3. Name your main program as minimizeDFA.java. I will compile and run your program from the Unix
command line as follows:
javac minimizeDFA.java
java minimizeDFA Xnfa.dfa
Your program should print out the following text:
Parsing results of Xnfa.dfa on strings attached in Xnfa.dfa:
Yes Yes Yes Yes No Yes No No Yes No Yes No Yes No No
Yes No No No No Yes Yes No No Yes Yes Yes No No No
Yes:14 No:16
Minimized DFA from Xnfa.dfa:
Sigma: a b c d
------------------------------
0: 1 2 0 3
1: 1 3 1 1
2: 1 3 2 3
3: 3 3 2 3
------------------------------
0: Initial State
0,2,3: Accepting State(s)
Parsing results of minimized Xnfa.dfa on same set of strings:
Yes Yes Yes Yes No Yes No No Yes No Yes No Yes No No
Yes No No No No Yes Yes No No Yes Yes Yes No No No
Yes:14 No:16
As described earlier, the 30 strings can be found after the DFA description in each DFA file. We
expect the parsing results are the same before and after minimization, otherwise your program is
incorrect. Let each line has 15 yes or no. You should count how many yes (accepted) and no (rejected)
and print out the counts.
4. Extra credit: 10 points. As mentioned earlier, except AX.dfa, all DFA’s are obtained from the
NFA’s using the algorithm we learned. Thus, every state in the DFA is reachable, i.e, every state
can be reached from the initial state through some input string. Therefore, we don’t have to remove
the unreachable states before minimization. However, AX.dfa is not the case. There are some
April 11, 2021, Sunday, 11:55 PM 
c Chung-Chih Li P-2/3
IT 328 Programming Assignment 3
unreachable sates. If we directly minimize AX.dfa, we will not get an optimal DFA. If your program
can take care of this problem, i.e., remove unreachable states before minimization to guarantee an
optimal version of Ax.dfa, you will have this 10 extra points.
5. Final Step on Unix Account. After you finish the project, or has decided that what you have is
the final version for me to grade, collect or transfer all needed program in the corresponding student’s
Unix account and select a secret name, say “peekapoo” as an example (you should chose your own),
and that will be the secret directory, and should not give to anyone except the grader (that’s the
instructor). Run the following bash script program from corresponding student’s Unix account:
bash /home/ad.ilstu.edu/cli2/Public/IT328/submit328.sh peekapoo
Note that your programs have to be in the required directory ∼/IT328/dfa/ as described in step 1
in order to let this script program correctly copy your programs into the secret directory, where your
programs will be tested.
6. Report. You have to write up a report and submit a copy of the report through ReggieNet. The
report should include:
• A cover page that contains assignment number, your name, ULID (not student ID) and the
name of the secret directory.
• A brief summary of the methods, algorithms and data structures, and the difficulties, if any,
the project has faced and how to solve them.
• The direct output and program code are not required.
The score is based on the correctness and documentation of your program, 50 points (40 on programs,
10 on report).
