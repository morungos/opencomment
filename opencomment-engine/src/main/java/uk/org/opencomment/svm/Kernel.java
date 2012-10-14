package uk.org.opencomment.svm;

/**
 * The kernel interface for the Modeller.
 *
 * @author fherrmann
 * @version $Revision: 31 $
 */
public interface Kernel  {

    /**
     * Calculate the kernel value for the two vectors.
     * @param x is left vector
     * @param y is right vector
     * @return the <code>double</code> kernel value for
     * the input vectors.
     */
    double kv(double[]x, double[] y);

    /**
     * When serialising a kernel, the associated parameters
     * are required. This method returns the parameters as parseable
     * (structured) string.
     * @return a structured <code>String</code> with the parameters
     * of this kernel.
     */
    String getParams();
}
