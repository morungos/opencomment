package uk.org.opencomment.svm;

import java.util.ArrayList;
import java.util.List;

/**
 * The Concept instance contains  a definition and a representation of a concept.
 * The idea is that the representation is used to identify unknown concepts and the
 * appropriate definition is flagged, i.e. for the purpose of generating
 * a response.
 *
 * @author fherrmann
 * @version $Revision: 10 $
 */
public class Concept {
    private static final long serialVersionUID = -9159622080270881767L;
    /**
     * Concept representation, i.e. a collection of terms, i.e.
     * word, number or symbol <code>String</code>s.
     */
    private List<String> instance = new ArrayList<String>();
    /**
     * Name of the concept, usually as a abbreviation. This
     * abbreviation or name may be mapped into a explanation or
     * response.
     */
    private String label;
    private String id;

    /**
     * Returns the representation of this concept as a <ode>List</code>
     * of terms, i.e. word and number strings.
     * @return a <code>List</code> of terms.
     */
    public List<String> getInstance() {
        return instance;
    }

    /**
     * Set the representation of a concept as a list of terms (words).
     * @param name is a <code>List</code> of terms.
     */
    public void setInstance(List<String> name) {
        this.instance = name;
    }

    /**
     * Gets a handle for this concept. The handle may be a category or
     * simply an identifier for this concept. The handle is concerned with
     * the concept type rather than any instance of the concept.
     * @return a <code>String</code> handle
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the handle and the first 20 terms in the concept
     * representation.
     * @return a <code>String</code> short description of the concept
     * consisting o the handle and the concept instances (first terms).
     */
    @Override
    public String toString() {
        return label + " : " +
            instance.subList(0, Math.min(instance.size(), 20));
    }

    /**
     * Set the handle for this concept.
     * @param label the handle to be set.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Set the identifier for this concept.
     * @param id is a identifier <code>String</code>.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * The concept instance identifier.
     * @return the identifier <code>Strin</code>.
     */
    public String getId() {
        return id;
    }

    public int getTermFrequency(String term) {
        return instance.contains(term) ? 1 : 0;
    }

}
