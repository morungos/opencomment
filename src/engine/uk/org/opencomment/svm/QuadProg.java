package uk.org.opencomment.svm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * <strong>Sequential Minimal Optimisation</strong>
 * Implementation of Fan, Chen and Lin's "Working Set Selection using second order information
 * for training support vector machines" Journal of Machine Learning Research Volume 6 (2005)
 * pp. 1889 - 918.
 * Solving:
 * \min_{\alpha} \frac{1}{2} \alpha^{t} Q \alpha - e^{t} \alpha
 * subject to
 * 0 \leq alpha_{i} \leq c
 * y{t}\alpha = 0
 * with e being an vector with all 1.
 * @author fherrmann
 * @version $Revision: 31 $
 */
public class QuadProg {
	/**
	 * ProgressListeners are introduced because for the multi-threading of
	 * the application.
	 */
	private List<ProgressListener> progressListener;
	
	/**
	 * y_{i} is in {+1, -1} for ith instance
	 */
	private int[] y;
	
	/**
	 * matrix must be a positive semi-definite matrix
	 */
	private double[][] matrix;
	/**
	 * Tolerance below which the algorithm terminates.
	 */
	private double eps = 1e-6;
	/**
	 * A non-zero double initialisation value.
	 */
	private double tau = 1e-12;
	/**
	 * Capacity, i.e. norm of the support vectors.
	 */
	private double cap = 2;
	private double[] alpha;
	private double[] gradient;
	private int len;
	
	/**
	 * Instantiate the quadratic programming solver.
	 * @param kernel a instance of a kernel.
	 * @param y the <code>int[]</code> vector with y[i] = 1
	 * if the vector xy[i] matches the handle to be learned and
	 * y[i] = -1 otherwise.
	 * @param name is the thread name <code>String</code>.
	 */
	public QuadProg(double[][] kernel, int[] y, String name) {
		init(kernel, y);
		progressListener = new ArrayList<ProgressListener>();
	}
	
	/**
	 * Method is factored out, so sub-classes can override it.
	 * @param kernel a <code>double[][]</code> kernel matrix.
	 * @param y a <code>int[]</code> vector with y[i] \in {-1, 1}
	 * '-1' if the associated vector is not member and '+1' otherwise.
	 */
	protected void init(double[][] kernel, int[] y) {
		len = y.length;
		this.y = y;
		// initialise kernel, alpha and gradient
		matrix = new double[len][len];
		for(int i = 0; i < len; i++)
			for(int j = 0; j < len; j++)
				matrix[i][j] = y[i] * y[j] * kernel[i][j];
		// initialise alpha and gradient
		alpha = new double[len];
		gradient = new double[len];
		Arrays.fill(alpha, 0);
		Arrays.fill(gradient, -1);
	}
	
	/**
	 * Solve the problem.
	 */
	public void run() {
		while(true) {
			int[] index = select();
			int i = index[0];
			int j = index[1];
			if(j == -1) {
				fireStatusChangeEvent( new StatusChangeEvent(this,
						ProgressListener.CONVERGED));
				break;
			}
			double a = matrix[i][i] + matrix[j][j] - 2 * y[i] * y[j] * matrix[i][j];
			if(a <= 0) a = tau;
			double b = - y[i] * gradient[i] + y[j] * gradient[j];
			// update alpha
			double[] old = new double[] { alpha[i], alpha[j] };
			alpha[i] += y[i] * b / a;
			alpha[j] -= y[j] * b / a;
			double sum = y[i] * old[0] + y[j] * old[1];
			alpha[i] = y[i] * (sum - y[j] * alpha[j]);
			alpha[j] = y[j] * (sum - y[i] * alpha[i]);
			// apply set of constraints
			if(alpha[i] > cap) alpha[i] = cap;
			if(alpha[i] < 0) alpha[i] = 0;
			if(alpha[j] > cap) alpha[j] = cap;
			if(alpha[j] < 0) alpha[j] = 0;
			// update gradient
			double[] dAlpha = new double[] { alpha[i] - old[0], alpha[j] - old[1] };
			for(int t = 0; t < len; t++)
				gradient[t] += matrix[t][i] * dAlpha[0] + matrix[t][j] * dAlpha[1];
			fireStatusChangeEvent(new StatusChangeEvent(this,
					ProgressListener.ITERATING, "Iterating "));
		}
	}
	
	/**
	 * Implement the <code>ProgressListener</code> interface.
	 * @param e is a <code>StatusChangeEvent</code> instance.
	 */
	private void fireStatusChangeEvent(StatusChangeEvent e) {
		for(Iterator<ProgressListener> i = progressListener.iterator(); i.hasNext(); ) {
			i.next().statusChanged(e);
		}
	}

	/**
	 * Solve the problem by using non-default parameters for the solver.
	 * @param cap i a <code>double</code> for the capacity, typically somewhere
	 * between 0 and 2 for normalised vectors. The default value is 2.
	 * @param eps is a <code>double</code> for the tolerance for which
	 * the algorithm terminated. it should a be a small number typically between
	 * 1e-6 and 1e-2. The default value is 1e-3.
	 */
	public void setParams(double cap, double eps) {
		this.cap = cap;
		this.eps = eps;
	}
	
	private int[] select() {
		int i = -1, j = -1;
		double gmax = Double.NEGATIVE_INFINITY;
		double gmin = Double.POSITIVE_INFINITY;
		for(int t = 0; t < len; t++) {
			if(((y[t] == +1) && (alpha[t] < cap)) || ((y[t] == -1) && (alpha[t] > 0))) {
				if(- y[t] * gradient[t] >= gmax) {
					i = t;
					gmax = - y[t] * gradient[t];
				}
			}
		}
		double objmin = Double.POSITIVE_INFINITY;
		for(int t = 0; t < len; t++) {
			if(((y[t] == +1) && (alpha[t] > 0)) || ((y[t] == -1) && (alpha[t] < cap))) {
				double b = gmax + y[t] * gradient[t];
				if(- y[t] * gradient[t] <= gmin) gmin = - y[t] * gradient[t];
				if(b > 0) {
					double a = matrix[i][i] + matrix[t][t] -
						2 * y[i] * y[t] * matrix[i][t];
					if(a <= 0)  a = tau;
					if(- b * b / a <= objmin) {
						j = t;
						objmin = - b * b / a;
					}
				}
			}
		}
		if(gmax - gmin < eps) return new int[] { -1, -1 };
		return new int[] {i, j};
	}
	
	/**
	 * @return the alpha
	 */
	public double[] getAlpha() {
		return alpha;
	}

	/**
	 * Remove a progress listener.
	 * @param listener a <code>ProgressListener</code>.
	 */
	public void removeProgressListener(ProgressListener listener) {
		progressListener.remove(listener);
	}
	
	/**
	 * Add a progress listener.
	 * @param listener a <code>ProgressListener</code>.
	 */
	public void addProgressListener(ProgressListener listener) {
		progressListener.add(listener);
	}
	
}
