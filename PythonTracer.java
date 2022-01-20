/**
 * David Man 111940002 RO3
 * The Python Tracer class that implements the tracer algorithm
 *   to find the complexities of each code block
 */
import java.util.Stack;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class PythonTracer {
	// A constant that helps track of the number of indents of the line
	public static final int SPACE_COUNT = 4;
	
	/**
	 * TraceFile method to find complexities of code blocks in file
	 * @param fileName
	 *   Name of file to be traced
	 * @return
	 *   Overall complexity found from file
	 * @throws IOException
	 *   Finds invalid arguments for java stack API
	 */
	@SuppressWarnings("resource")
	public static Complexity traceFile(String fileName) throws IOException {
		// Stack of code blocks
		Stack<CodeBlock> stack = new Stack<CodeBlock>();
		ArrayList<Integer> prevBlock = new ArrayList<Integer>();
		ArrayList<Integer> currentBlock = new ArrayList<Integer>();
		
		FileInputStream fis = new FileInputStream(fileName);
		InputStreamReader inStream = new InputStreamReader(fis);
		BufferedReader reader = new BufferedReader(inStream);
		String line = reader.readLine();
		
 		while (line != null) {
 			// Checks if line is valid in Python file
			if (!(line.contains("#")) && line.length() != 0 && line != "\n"){
				int i = 0;
				int indents = 0;

				// Checks number of indents in line
				try {
					while (line.charAt(i) == ' ') {
						indents++;
						i++;
					}
				}
				catch (StringIndexOutOfBoundsException e) {
				}
				indents = indents / SPACE_COUNT;
				while (indents < stack.size()) {
					// Closes file and returns total complexity
					if (indents == 0) {
						reader.close();
						Complexity stackTop = new Complexity(
						  stack.peek().getBlockComplexity().getNPower() +
						  stack.peek().getHighestSubComplexity().getNPower(),
						  stack.peek().getBlockComplexity().getLogPower() +
						  stack.peek().getHighestSubComplexity().
						    getLogPower());
						System.out.println("	Leaving block " +
						  printBlock(currentBlock) + ".\n");
						return stackTop;
					}
					// Pops block from stack and adds highest sub-complexity
					else {
						CodeBlock oldTop = stack.pop();
						Complexity oldTopComplexity = new Complexity(
						  oldTop.getBlockComplexity().getNPower() +
						  oldTop.getHighestSubComplexity().getNPower(),
						  oldTop.getBlockComplexity().getLogPower() +
						  oldTop.getHighestSubComplexity().getLogPower());

						if (oldTopComplexity.getNPower() >=
						  stack.peek().getHighestSubComplexity().getNPower())
						{
							if (oldTopComplexity.getNPower() >
							  stack.peek().getHighestSubComplexity().
							  getNPower()) {
								stack.peek().
								  setHighestSubComplexity(oldTopComplexity);
							}
							else if (oldTopComplexity.getNPower() ==
							  stack.peek().getHighestSubComplexity().
							  getNPower()) {
								if (oldTopComplexity.getLogPower() >
								stack.peek().getHighestSubComplexity().
								getLogPower()) {
									stack.peek().setHighestSubComplexity
									  (oldTopComplexity);
								}
							}
						}
						
						//Prints block and highest sub-complexity when leaving
						prevBlock.clear();
						for (int x = 0; x < currentBlock.size(); x++) {
							prevBlock.add(currentBlock.get(x));
						}
						currentBlock.remove(currentBlock.size() - 1);

						System.out.print(
						  "    Leaving block " + printBlock(prevBlock) +
						  ", updating block " + printBlock(currentBlock) +
						  ":\n");
						System.out.print(String.format(
						  "%s%-9s",
						  "        BLOCK ", printBlock(currentBlock) + ":"));
						System.out.print(
						  printComplexity(stack.peek().getBlockComplexity(),
						  stack.peek().getHighestSubComplexity()));
					}
				}
				
				// Checks for keywords in line
				if (line.contains("def") || line.contains("for") || 
					line.contains("while") || line.contains("if") || 
					line.contains("else") || line.contains("elif")) {
					String keyword = "";
					if (line.contains("def"))
						keyword = "def";
					else if	(line.contains("for"))
						keyword = "for";
					else if (line.contains("while"))
						keyword = "while";
					else if (line.contains("if"))
						keyword = "if";
					else if (line.contains("else"))
						keyword = "else";
					else if (line.contains("elif"))
						keyword = "elif";
					// Adds 'for' code block into stack
					if (keyword == "for") {
						//Finds type of complexity in 'for' code block
						if (line.contains("log_N:")) {
							Complexity logN = new Complexity(0, 1);
							int x = line.indexOf("for ") + 4;
							String loopVariable =
							  line.substring(
							  x, x + line.substring(x).indexOf(" "));
							Complexity subComplexity = new Complexity();
							CodeBlock O =
							  new CodeBlock(
							  "for", logN, subComplexity, loopVariable);
							stack.push(O);
							currentBlock.add(1);
						}
						else if (line.contains("N:")) {
							Complexity n = new Complexity(1, 0);
							int x = line.indexOf("for ") + 4;
							String loopVariable =
							  line.substring(
							  x, x + line.substring(x).indexOf(" "));
							Complexity subComplexity = new Complexity();
							CodeBlock O = 
							  new CodeBlock(
							  "for", n, subComplexity, loopVariable);
							stack.push(O);
							currentBlock.add(1);
						}
					}
					// Adds 'while' code block into stack
					else if (keyword == "while") {
						int x = line.indexOf("while ") + 6;
						String loopVariable =
						  line.substring(
						  x, x + line.substring(x).indexOf(" "));
						Complexity o1 = new Complexity(0, 0);
						CodeBlock O =
						  new CodeBlock(
						  "while", o1, o1, loopVariable);
						stack.push(O);
						currentBlock.add(1);
					}
					// Adds empty code block into stack
					else {
						Complexity o1 = new Complexity(0, 0);
						CodeBlock O = new CodeBlock(keyword, o1, o1, "");
						stack.push(O);
						currentBlock.add(1);
					}
					
					// Prints block and highest sub-complexity when entering
					if (stack.peek() != null) {
						System.out.println(
						  "    Entering block " + printBlock(currentBlock) +
						  " '" + stack.peek().getName() + "':");
						System.out.print(String.format(
								  "%s%-9s",
								  "        BLOCK ",
								  printBlock(currentBlock) + ":"));
						System.out.print(
						printComplexity(stack.peek().getBlockComplexity(),
						  stack.peek().getHighestSubComplexity()));
					}
				}
				// Checks if 'while' block updates loop variable
				else if (stack.peek().getName().equals("while") &&
				  line.trim().substring(0, 1).
				  equals((stack.peek().getLoopVariable()))) {
					if (line.trim().substring(2, 4).equals("+=") ||
					  line.trim().substring(2, 4).equals("-="))
						stack.peek().getBlockComplexity().setNPower(1);
					if (line.trim().substring(2, 4).equals("*=") ||
					  line.trim().substring(2, 4).equals("/=")) 
						stack.peek().getBlockComplexity().setLogPower(1);
					System.out.print(
					  "    Found update statement, updating block 1:\n"); 
					System.out.print(String.format(
							  "%s%-9s",
							  "        BLOCK ",
							  printBlock(currentBlock) + ":"));
					Complexity temp = new Complexity(0, 0);
					System.out.print(
					  printComplexity(
					    stack.peek().getBlockComplexity(), temp));
				}
			}
			else {
				line = reader.readLine();
				continue;
			}
			line = reader.readLine();
		}
 		
 		// Updates highest sub-complexity until stack has 1 element
		while (stack.size() > 1) {
			CodeBlock oldTop = stack.pop();
			Complexity oldTopComplexity = new Complexity(
					  oldTop.getBlockComplexity().getNPower() +
					  oldTop.getHighestSubComplexity().getNPower(),
					  oldTop.getBlockComplexity().getLogPower() +
					  oldTop.getHighestSubComplexity().getLogPower());
			if (oldTopComplexity.getNPower() >=
			  stack.peek().getHighestSubComplexity().getNPower()) {
				if (oldTopComplexity.getNPower() >
				  stack.peek().getHighestSubComplexity().getNPower()) {
					stack.peek().setHighestSubComplexity(oldTopComplexity);
				}
				else if (oldTopComplexity.getNPower() ==
				  stack.peek().getHighestSubComplexity().getNPower()) {
					if (oldTopComplexity.getLogPower() >=
					  stack.peek().getHighestSubComplexity().getLogPower())
					{
						if (oldTopComplexity.getLogPower() >
						  stack.peek().
						  getHighestSubComplexity().getLogPower())
						{
							stack.peek().
							  setHighestSubComplexity(oldTopComplexity);
						}
					}
				}
			}
		}
		
		System.out.println("    Leaving block " +
				  printBlock(currentBlock) + ".\n");
		return stack.pop().getHighestSubComplexity();
	}
	
	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		
		// Gets file from user input
		System.out.print("Please enter a file name (or 'quit' to quit): ");
		String file = input.nextLine();
		System.out.println();

		// Runs tracer method
		Complexity totalComplex = traceFile(file);

		// Prints overall complexity of file
		String fileName = file.substring(0, file.indexOf(".py"));
		System.out.print("Overall complexity of " + fileName + ": O(");
		if (totalComplex.getLogPower() == 0 &&
		  totalComplex.getNPower() == 0)
			System.out.println("1)\n");
		else if (totalComplex.getNPower() > 0 &&
		  totalComplex.getLogPower() == 0) {
			if (totalComplex.getNPower() == 1)
				System.out.println("n)");
			else
				System.out.print("n^" + totalComplex.getNPower() + ")");
		}
		else if (totalComplex.getLogPower() > 0 &&
		  totalComplex.getNPower() == 0) {
			if (totalComplex.getLogPower() == 1)
				System.out.println("log(n))");
			else
				System.out.println(
				  "log(n^" + totalComplex.getLogPower() + "))");
		}
		else if (totalComplex.getNPower() == 1 &&
		  totalComplex.getLogPower() == 1)
			System.out.println("n * log(n))");
		else if (totalComplex.getNPower() == 1 &&
		  totalComplex.getLogPower() > 1)
			System.out.println("n * log(n^" +
		    totalComplex.getLogPower() + "))\n");
		else if (totalComplex.getNPower() > 1 &&
		  totalComplex.getLogPower() == 1)
			System.out.println(
			  "n^" + totalComplex.getNPower() + "* log(n))\n");
		else {
			System.out.println(
			  "n^" + totalComplex.getNPower() + "* log(n^" + 
			  totalComplex.getLogPower() + "))\n");
		}
	}
	
	/**
	 * printBlock method to print digits of current block
	 * @param block
	 *   ArrayList<Integer> value representing current place in block
	 * @return
	 *   String of block digits with periods between each
	 */
	public static String printBlock(ArrayList<Integer> block) {
		String s = "";
		for (int i = 0; i < block.size(); i++) {
			if (i == block.size() - 1)
				s += block.get(i);
			else
				s += block.get(i) + ".";
		}
		
		return s;
	}
	
	/**
	 * printComplexity method to print block and highest sub complexities
	 * @param blockComplex
	 *   Complexity class object that represents blockComplexity
	 * @param highestSubComplex
	 *   Complexity class object that represents highest sub-complexity
	 * @return
	 *   String of block complexity and highest sub-complexity
	 */
	public static String printComplexity(
	  Complexity blockComplex, Complexity highestSubComplex) {
		String s = "block complexity = ";
		if (blockComplex.getLogPower() == 0 &&
		  blockComplex.getNPower() == 0)
			s += (String.format("%-11s", "O(1)"));
		else if (blockComplex.getNPower() > 0 &&
		  blockComplex.getLogPower() == 0) {
			if (blockComplex.getNPower() == 1)
				s += (String.format("%-11s", "O(n)"));
			else
				s += (String.format(
				  "%-11s", "O(n^" + blockComplex.getNPower() + ")"));
		}
		else if (blockComplex.getNPower() == 0 &&
		  blockComplex.getLogPower() > 0) {
			if (blockComplex.getLogPower() == 1)
				s += (String.format("%-11s", "O(log(n))"));
			else
				s += (String.format(
				  "%-11s", "O(log(n^" + blockComplex.getLogPower() + "))"));
		}
		else if (blockComplex.getNPower() > 0 &&
		  blockComplex.getLogPower() > 0) {
			if (blockComplex.getNPower() == 1 &&
			  blockComplex.getLogPower() == 1)
				s += (String.format("%-11s", "O(n * log(n))"));
			else {
				s += (String.format(
				  "%-11s", "O(n^" + blockComplex.getNPower() + 
				  "log(n^" + blockComplex.getLogPower() + ")"));
			}
		}
		s += (String.format("%-11s", "highest sub-complexity = "));
		if (highestSubComplex.getLogPower() == 0 &&
		  highestSubComplex.getNPower() == 0)
			s += (String.format("%-11s%s", "O(1)", "\n\n"));
		else if (highestSubComplex.getNPower() > 0 &&
		  highestSubComplex.getLogPower() == 0) {
			if (highestSubComplex.getNPower() == 1)
				s += (String.format("%-11s%s", "O(n)", "\n\n"));
			else
				s += (String.format(
				  "%-11s%s",
				  "O(n^" + highestSubComplex.getNPower() + ")", "\n\n"));
		}
		else if (highestSubComplex.getNPower() == 0 &&
		  highestSubComplex.getLogPower() > 0) {
			if (highestSubComplex.getLogPower() == 1)
				s += (String.format("%-11s%s", "O(log(n))", "\n\n"));
			else
				s += (String.format(
				  "%-11s%s",
				  "O(log(n^" + highestSubComplex.getLogPower() + "))",
				  "\n\n"));
		}
		else if (highestSubComplex.getNPower() > 0 &&
		  highestSubComplex.getLogPower() > 0) {
			if (highestSubComplex.getNPower() == 1 && 
			  highestSubComplex.getLogPower() == 1)
				s += (String.format("%-11s%s", "O(n * log(n))", "\n\n"));
			else if (highestSubComplex.getNPower() == 1 &&
			  highestSubComplex.getLogPower() > 1)
				s += (String.format("%-11s%s", "O(n * log(n^" +
					  highestSubComplex.getLogPower() + "))", "\n\n"));
			else if (highestSubComplex.getNPower() > 1 &&
			  highestSubComplex.getLogPower() == 1)
				s += (String.format(
				  "%-11s%s", "O(n^" + highestSubComplex.getNPower() +
				  " * log(n))", "\n\n"));
			else {
				s += (String.format(
				  "%-11s%s", "O(n^" + highestSubComplex.getNPower() +
				  " * log(n^" + highestSubComplex.getLogPower() + ")",
				  "\n\n"));
			}
		}
		
		return s;
	}
}
