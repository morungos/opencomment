package uk.org.opencomment.svm;

/**
 * Implementation of a Gaussian kernel.
 * @author fherrmann
 * @version $Revision: 24 $
 */
public class GaussianKernel implements Kernel {
    private double sigma;

    /**
     * Construct a kernel instance.
     * @param sigma is the standard deviation of the kernel which
     * is defined as the difference between two concept instances
     * in terms of their bag of) term representation.
     */
    public GaussianKernel(double sigma) {
        this.sigma = sigma;
    }

    /**
     * Create a Gaussian kernel from a parameter <code>String</code>. The
     * only parameter is sigma for the standard deviation of this kernel.
     * @param params is the parameter <code>String</code> for this kernel.
     */
    public GaussianKernel(String params) {
        this(Double.parseDouble(params));
    }

    /**
     * Return the kernel value for a Gaussian kernel.
     * @param x left <code>double[]</code> vector
     * @param y right <code>double[]</code> vector
     * @return the kernel value <code>double</code>
     */
    public double kv(double[] x, double[] y) {
        return Math.exp( - pNorm2Diff(x, y) / (sigma * sigma) );
    }

    /**
     * p-norm (p = 2) Euclidean norm of the difference
     * @param x a vector
     * @param y a vector
     * @return
     */
    private double pNorm2Diff(double[] x, double[] y) {
        double rval = 0.0;
        for(int i = 0; i < x.length; i++)
            for(int j = 0; j < y.length; j++)
                rval += (x[i] - y[j]) * (x[i] - y[i]);
        return rval;
    }

    /**
     * Return the parameter for this kernel.
     * @return the parameter <code>String</code>.
     */
    public String getParams() {
        return Double.toString(sigma);
    }
}
