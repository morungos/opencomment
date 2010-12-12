package uk.org.opencomment.svm;

import java.util.Vector;

/**
 * VectorSpace defines the world in which one or more concepts live. VectorSpace
 * instances are shared between Modeller's as they can model different Concept's
 * that live in the same VectorSpace instance.
 * @author f.herrmann
 * @version 
 *
 */
public class VectorSpace {

	private Vector<XY> space;
	
	
	/**
	 * Create a new VectorSpace instance.
	 */
	public VectorSpace() {
		space = new Vector<XY>();
	}
	
	/**
	 * Add a new vector (dimension) to the VectorSpace instance.
	 * @param xy is a <code>XY</code> vector to be added.
	 */
	public void add(XY xy) {
		space.add(xy);
	}
	
	
	/**
	 * Remove an existing vector (dimension) from the VectorSpace instance.
	 * @param xy is a <code>XY</code> vector to be removed.
	 */
	public void remove(XY xy) {
		space.remove(xy);
	}
	
	/**
	 * Return the dimensionality of the vector space.
	 * @return a <code>int</code> of the dimensionality of the space.
	 */
	public int size() {
		return space.size();
	}
	
	/**
	 * Return the vector
	 * @param index is the <code>int</code> index of the vector.
	 * @return
	 */
	public XY get(int index) {
		return space.get(index);
	}
	
}
