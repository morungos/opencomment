package uk.org.opencomment.svm;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Individual representation (<code>double[]</code>) and handle
 * (<code>String</code>). This class breaks encapsulation principles to
 * simplify its handling as a union data type.
 * @author fherrmann
 * @version $Revision: 26 $
 */
public class XY {
    /**
     *
     */
    public double[] x;
    /**
     * the handle, target, category, class, label ... whatever
     * you want to call it.
     */
    public String y;

    /**
     * Creates a new instance
     * @param x the x vector of <code>double[]</code>.
     * @param y the <code>String</code> handle.
     * @param normalise a <code>boolean</code> which if true
     * forces the p2 vector norm to be one.
     */
    public XY(double[] x, String y, boolean normalise) {
        this.y = y;
        if(normalise) {
            double min = x[0];
            double max = x[0];
            for(int i = 1; i < x.length; i++) {
                if(x[i] > max) max = x[i];
                if(x[i] < min) min = x[i];
            }
            for(int i = 0; i < x.length; i++)
                x[i] = (x[i] - min) / (max - min);
        }
        this.x = x;
    }

    /**
     * Returns the handles and the first few values
     * of the representation.
     * @return a <code>String</code> representing this instance.
     */
    @Override
    public String toString() {
        String rval = "";
        NumberFormat df = new DecimalFormat("0.000");
        rval += "<" + y + "> ";
        int i = 0;
        for(; i < x.length - 1; i++)
            rval += df.format(x[i]) + ", ";
        rval += df.format(x[i]);
        return rval;
    }


}
