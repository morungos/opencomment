package uk.org.opencomment.svm;

/**
 * Receives information about the lengthy optimisation process.
 * @author fherrmann
 * @version $Revision: 31 $
 */
public interface ProgressListener {
	/** Solver converged constant. */
	public static final int CONVERGED = 0;
	/**
	 * Solving.
	 */
	public static final int ITERATING = 1;

	/**
	 * Takes a number of predefined integer constants stating
	 * the status of the algorithm.
	 * @param e a <code>StatusChangeEvent</code>
	 */
	public void statusChanged(StatusChangeEvent e);

}
