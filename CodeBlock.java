/**
 * David Man 111940002 RO3
 * The CodeBlock class that creates the code block found in file
 */

public class CodeBlock {
	// An array to store all types of code blocks
	public static final String[] BLOCK_TYPES =
	  {"def", "for", "while", "if", "else", "elif"};
	public static final int DEF = 0;
	public static final int FOR = 1;
	public static final int WHILE = 2;
	public static final int IF = 3;
	public static final int ELIF = 4;
	public static final int ELSE = 5;
	// Name of code block (etc: "FOR", "WHILE", etc...)
	private String name;
	// Complexity class object to store complexity of current block
	private Complexity blockComplexity;
	// Complexity class object to store highest-sub complexity of block
	private Complexity highestSubComplexity;
	// String that represents the loopVariable used in the code block
	private String loopVariable;
	
	public CodeBlock() {
		this.setName("");
		this.setBlockComplexity(null);
		this.setHighestSubComplexity(null);
		this.setLoopVariable("");
	}
	
	public CodeBlock(String name, Complexity blockComplexity,
	  Complexity highestSubComplexity, String loopVariable) {
		this.setName(name);
		this.setBlockComplexity(blockComplexity);
		this.setHighestSubComplexity(highestSubComplexity);
		this.setLoopVariable(loopVariable);
	}

	public String getName() {
		return name;
	}
	/**
	 * Code block name mutator method
	 * @param name
	 *   String that represents the name for the code block
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Complexity getBlockComplexity() {
		return blockComplexity;
	}
	/**
	 * Code block complexity mutator method
	 * @param blockComplexity
	 *   Complexity class object that represents the block complexity
	 */
	public void setBlockComplexity(Complexity blockComplexity) {
		this.blockComplexity = blockComplexity;
	}

	public Complexity getHighestSubComplexity() {
		return highestSubComplexity;
	}
	/**
	 * Code block highest-sub complexity mutator method
	 * @param highestSubComplexity
	 *   Complexity class object that represents the highest-sub complexity
	 */
	public void setHighestSubComplexity(Complexity highestSubComplexity) {
		this.highestSubComplexity = highestSubComplexity;
	}

	public String getLoopVariable() {
		return loopVariable;
	}
	/**
	 * Code block loop variable mutator method
	 * @param loopVariable
	 *   String that represents the loop variable of the code block
	 */
	public void setLoopVariable(String loopVariable) {
		this.loopVariable = loopVariable;
	}
}
