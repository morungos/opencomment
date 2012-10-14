package uk.org.opencomment.svm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the Support Vector Machine (Modeller). Various kernels can be plugged
 * in. It provides the methods add(...) to add new data vectors, train(String)
 * to train for a definition and predict() to see if the definition is met
 * by the representation.
 * @author fherrmann
 * @see com.fherrmann.oc.core.svm.Kernel
 * @version $Revision: 26 $
 */
public class Modeller implements ProgressListener {
    private static final long serialVersionUID = 784142118295858837L;
    /* A vector space that may be shared between different modellers. */
    private VectorSpace space;
    private double[] alpha;
    private int[] y;
    private Kernel kernelImpl;
    private double b;
    private String handle;
    private QuadProg solver;

    private Logger log = LoggerFactory.getLogger(Modeller.class);

    /**
     * Instantiate the Modeller with a kernel instance.
     * @param kernel a kernel instance.
     * @param handle is the <code>String</code> handle for which this
     * model is trained.
     * @param space is the vector space. If <code>null</code> a new vector
     * space is created to which <code>XY</code> vectors can be added. To
     * obtain a reference to an existing space use the addVector method provided.
     */
    public Modeller(Kernel kernel, String handle, VectorSpace space) {
        this.kernelImpl = kernel;
        this.space = space;
        // Make sure the space exists, otherwise create a new instance.
        if(this.space == null)
            this.space = new VectorSpace();
        this.handle = handle;
    }

    /**
     * Train the Modeller.
     */
    public void train() {
        double[][] kernel = new double[space.size()][space.size()];
        for(int i = 0; i < space.size() ; i++)
            for(int j = 0; j < space.size(); j++)
                kernel[i][j] = kernelImpl.kv(space.get(i).x, space.get(j).x);
        y = new int[space.size()];
        for(int i = 0; i < space.size(); i++) {
            y[i] = handle.equals(space.get(i).y) ? +1 : -1;
        }
        solver = new QuadProg(kernel, y, handle);
        solver.setParams(2, 1e-3);
        solver.addProgressListener(this);
        // start the solver thread
        solver.run();
    }

    /**
     * Wait to die.
     */
    private void estimateB() {
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        double[] w = mmhp();
        for(int i = 0; i < space.size(); i++) {
            double t = kernelImpl.kv(w, space.get(i).x);
            if((y[i] == -1) && (t > max))
                max = t;
            if((y[i] == +1) && (t < min))
                min = t;
        }
        b = - (max + min) / 2;
    }

    /**
     * Compute the maximal margin hyper-plane
     * @return a <code>double[]</code>
     */
    private double[] mmhp() {
        double[] rval = new double[space.get(0).x.length];
        for(int j = 0; j < rval.length; j++) {
            for(int i = 0; i < space.size(); i++) {
                rval[j] += alpha[i] * y[i] * space.get(i).x[j];
            }
        }
        return rval;
    }

    /**
     * The prediction tool to be used after the Modeller is trained.
     * @param x a <code>double</code> representation.
     * @return a <code>boolean</code> true if the instance
     * matches the internalised representation in the Modeller model.
     *
     */
    public boolean predict(XY x) {
        double f = 0.0;
        for(int i = 0; i < space.size(); i++)
            f += alpha[i] * y[i] * kernelImpl.kv(space.get(i).x, x.x);
        return (f + b) > 0;
    }

    /**
     * Add a new vector to the space underpinning the this modeller.
     * @param xy is a <code>XY</code> vector to be added.
     */
    public void addVector(XY xy) {
        space.add(xy);
    }

    /**
     * @return the <code>String</code> with the handle for which
     * this Modeller is trained. The handle will be </cod>null</code>
     * if the model has not yet been trained.
     */
    public String getHandle() {
        return handle;
    }

    /**
     * y vector
     * @return a <code>double[]</code> y vector.
     */
    public int[] getY() {
        return y;
    }

    /**
     * Get the weight vector alpha. Alpha masks the input vectors in a way
     * that vectors weighted by non-zero alpha values are support vectors.
     * @return the <code>double[]</code> alpha.
     */
    public double[] getAlpha() {
        return alpha;
    }

    /**
     * Get the kernel implementation for this Modeller.
     * @return the <code>Kernel</code> instance.
     */
    public Kernel getKernel() {
        return kernelImpl;
    }

    /**
     * Return the VectorSpace defining the world for this modeller.
     * @return a <code>VectorSpace</code> vector space underpinning
     * this modeller.
     */
    public VectorSpace getVectorSpace() {
        return space;
    }

    /**
     * Get the bias b for the model.
     * @return a <code>double</code> bias.
     */
    public double getB() {
        return b;
    }

    /**
     * @param e a <code>StatusChangeEvent</code> generated.
     * @see com.fherrmann.oc.core.svm.ProgressListener#statusChanged(com.fherrmann.oc.core.svm.StatusChangeEvent)
     */
    public void statusChanged(StatusChangeEvent e) {
        if(e.getStatus() == ProgressListener.CONVERGED) {
            alpha = solver.getAlpha();
            estimateB();
        }
        String[] status = new String[] {"converged", "iterating"};
        if (log.isDebugEnabled()) {
            log.debug("Status: " + status[e.getStatus()]);
        }
    }
}
