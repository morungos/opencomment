package uk.org.opencomment.svm;

/**
 * Receives information about the lengthy optimisation process.
 * @author fherrmann
 * @version $Revision: 31 $
 */
public interface ProgressListener {

    /** Solver converged constant. */
    int CONVERGED = 0;

    /**
     * Solving.
     */
    int ITERATING = 1;

    /**
     * Takes a number of predefined integer constants stating
     * the status of the algorithm.
     * @param e a <code>StatusChangeEvent</code>
     */
    void statusChanged(StatusChangeEvent e);
}
