package uk.org.opencomment.svm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The universe holding all concepts by their representation
 * instances and the associated handles. Universe provides methods
 * to get term frequencies, document frequencies for a term and handles
 * for a document identifier. By default, any new concept will contribute
 * its terms to the vocabulary. This behaviour can be switched off by
 * using the expand flag in the {@link Universe#add(Concept, boolean)} method.
 * Terms can be added or removed manually as keys to the Universe instance.
 * @author fherrmann
 * @version $Revision: 34 $
 */
public class Universe /* extends TreeMap<String, List<String>> */ {
    private static final long serialVersionUID = 8632444594009838736L;
    /**
     * Look up the handle for a concept instance identified by its ID.
     */
    private Map<String, Concept> categories;

    /**
     * The internal storage for the universe information.
     */
    private Map<String, List<String>> data;

    /**
     * Creates an instance of an universe.
     */
    public Universe() {
        super();
        categories = new HashMap<String, Concept>();
        data = new HashMap<String, List<String>>();
    }

    public Set<String> getTerms() {
        return data.keySet();
    }

    public int getTermCount() {
        return data.size();
    }

    /**
     * Look up the handle for a concept with the specified ID.
     * @param conceptId is the unique ID of the stored concept.
     * @return the handle <code>String</code>.
     */
    public Concept getConcept(String conceptId) {
        return categories.get(conceptId);
    }

    /**
     * Get the set of identifiers of concepts in this universe.
     * @return a <code>Set</code> of identifiers for concepts
     *  in this universe.
     */
    public Set<String> getCategorySet() {
        return categories.keySet();
    }

    /**
     * Add an document to the repository. Terms in the document term list are
     * added to the vocabulary.
     * @param concept is the <code>SampleDocument</code> instance to be added.
     * @param expand specifies if terms which are new in this concept instance
     * will be added to the vocabulary. This will be true during the learning.
     *
     */
    public void add(Concept concept) {
        String conceptId = concept.getId();
        assert conceptId != null;
        for(String term : concept.getInstance()) {
            if(data.containsKey(term)) {
                data.get(term).add(conceptId);
            } else {
                List<String> concepts = new ArrayList<String>();
                concepts.add(conceptId);
                data.put(term, concepts);
            }
        }
        categories.put(conceptId, concept);
    }

    /**
     * How frequent is a term in a document. This does not depend on any
     * items being added
     * @param term is a <code>String</code> term.
     * @param conceptId is the document.
     * @return the frequency of the term in the specified document.
     */
    public int getConceptTermFrequency(String term, String conceptId) {
        return getConcept(conceptId).getTermFrequency(term);
    }
}
