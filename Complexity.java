/**
 * David Man 111940002 RO3
 * The Complexity class that stores the complexity of the code
 */

public class Complexity {
	private int nPower;
	private int logPower;

	public Complexity() {
		nPower = 0;
		logPower = 0;
	}
	
	public Complexity(int nPower, int logPower) {
		this.setNPower(nPower);
		this.setLogPower(logPower);
	}
	
	public int getNPower() {
		return nPower;
	}
	/**
	 * NPower mutator method
	 * @param nPower
	 *   Integer that gives value of NPower
	 */
	public void setNPower(int nPower) {
		this.nPower = nPower;
	}
	
	public int getLogPower() {
		return logPower;
	}
	/**
	 * LogPower mutator method
	 * @param logPower
	 *   Integer that gives value of logPower
	 */
	public void setLogPower(int logPower) {
		this.logPower = logPower;
	}
	
	public String toString() {
		return "O(n^" + nPower + " * log(n)^" + logPower + ")";
	}
}