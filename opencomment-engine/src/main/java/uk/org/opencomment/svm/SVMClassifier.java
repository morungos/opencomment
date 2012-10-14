package uk.org.opencomment.svm;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import uk.org.opencomment.classifiers.BaseClassifier;
import uk.org.opencomment.classifiers.Classifier;
import uk.org.opencomment.lang.PaiceHuskStemmer;
import uk.org.opencomment.lang.StopTermRemoval;

/**
 * <ol>
 * <li>Instantiate the database</li>
 * <li>Instantiate a repository and add some documents</li>
 * <li>Build multiple Modeller using the term frequencies as input vectors.</li>
 * <li>Train Modeller</li>
 * <li>Predict</li>
 * </ol>
 *
 * This class is quite expensive in term of memory requirements. If
 * possible, Java should be run with additional memory, i.e. use the
 * <kbd>java -Xmx2048m</kbd> command line parameter to allocate a bigger heap
 * to Java. In this example, 2 MByte are allocated.</p>
 *
 * @author fherrmann
 * @version $Revision: 38 $
 */
public class SVMClassifier extends BaseClassifier implements Classifier {

    private Logger log = LoggerFactory.getLogger(SVMClassifier.class);

    private String vocabulary = null;

    private static StopTermRemoval swr;

    private Universe universe = new Universe();

    private Modeller svm;

    /*
     * Initialisation method for the SVM classifier. We really want to do
     * as much as possible here, and in an ideal world, we would also cache
     * a fair amount of the work we do for subsequent requests. However,
     * this does have threading implications. Not too many, though: we can
     * initialise the thing once, and use in multiple threads.
     */
    public void initialize(String name, Element element) throws Exception {
        super.initialize(name, element);

        if (swr == null) {
            InputStream stopStream = SVMClassifier.class.getClassLoader().getResourceAsStream("stopwords.txt");
            if (stopStream == null) {
                throw new MissingResourceException("Missing resource", "stopwords.txt", "stopwords.txt");
            }
            swr = new StopTermRemoval(stopStream);
        }

        if (log.isDebugEnabled()) {
            log.debug("Initialising SVM classifier");
        }

        NodeList elements = element.getElementsByTagName("vocabulary");
        if (elements.getLength() != 1) {
            throw new Exception("Found invalid vocabulary elements for concept: " + name);
        }
        vocabulary = ((Element) elements.item(0)).getTextContent();

        if (log.isDebugEnabled()) {
            log.debug("Found vocabulary: " + vocabulary);
        }

        elements = element.getElementsByTagName("include");
        int docId = 0;
        for(int i = 0; i < elements.getLength(); i++) {
            Element e = (Element) elements.item(i);
            Concept c = getConcept(e.getTextContent(), "class");
            c.setId(String.valueOf(docId++));
            universe.add(c);
        }
        elements = element.getElementsByTagName("exclude");
        for(int i = 0; i < elements.getLength(); i++) {
            Element e = (Element) elements.item(i);
            Concept c = getConcept(e.getTextContent(), "other");
            c.setId(String.valueOf(docId++));
            universe.add(c);
        }

        String label = "class";
        /* Use null as value for the VectorSpace parameter forcing a new
         * VectorSpace instance to be created. Subsequently new Vectors are
         * added to the space. Note that each Modeller can be associated with
         * a single Concept. If more than one concept needs to be identified,
         * use a Modeller instance for each concept (label) and share
         * the space between them. */
        svm = new Modeller(new GaussianKernel(5), label, new VectorSpace());
        // create a vector for each document in the universe
        for(Iterator<String> i = universe.getCategorySet().iterator(); i.hasNext(); ) {
            String documentId = i.next();
            int n = 0;
            double[] x = new double[universe.getTermCount()];
            for(String j : universe.getTerms())
                x[n++] = universe.getConceptTermFrequency(j, documentId);
            // normalise support vectors
            XY sv = new XY(x, universe.getConcept(documentId).getLabel(), true);
            svm.addVector(sv);
        }
        if (log.isDebugEnabled()) {
            log.debug("Starting training...");
        }
        svm.train();
        if (log.isDebugEnabled()) {
            log.debug("Done training...");
        }
    }

    private Concept getConcept(String instance, String handle) {
        Concept rval = new Concept();
        rval.setLabel(handle);

        List<String> terms = parse(instance);
        lowercase(terms);
        stopTermRemoval(terms);
        stem(terms);
        rval.setInstance(terms);
        // rval.setId(id);
        return rval;
    }

    /**
     * Removes stop terms (word), i.e. very common words which do not
     * contribute significant information.
     * @param terms is the <code>List</code> with the terms
     * @return a <code>List</code> with terms after processing. This
     * is a convenience return in order to chain different processing steps
     * together.
     */
    public List<String> stopTermRemoval(List<String> terms) {
        swr.removeSymbolTerms(terms);
        swr.removeStopTerms(terms);
        return terms;
    }

    /**
     * Helper method to parse a string with space-separated tokens.
     * @param s the line
     * @return a <code>List</code> with tokens (terms)
     */
    public static List<String> parse(String s) {
        Vector<String> rval = new Vector<String>();
        StringTokenizer tkn = new StringTokenizer(s);
        while (tkn.hasMoreTokens())
            rval.add(tkn.nextToken());
        return rval;
    }

    /**
     * Turns all terms in the list into lower case.
     * @param terms a <code>List</code> of terms.
     * @return a <code>List</code> of terms as convenience for chaining.
     */
    public List<String> lowercase(List<String> terms) {
        for(String i : terms)
            i.toLowerCase();
        return terms;
    }

    /**
     * A helper method to stem the terms in a <code>List</code>. The
     * method returns the reference, so various helper methods, e.g.
     * stop-word removal can be chained together.
     * @param terms the <code>List</code> of terms
     * @return the reference to the processed list of terms.
     */
    public List<String> stem(List<String> terms) {
        PaiceHuskStemmer stemmer = null;
        try {
            stemmer = PaiceHuskStemmer.getInstance(SVMClassifier.class.getClassLoader().getResource("stemrules.txt").openStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        stemmer.setPreStrip(false);
        for (int i = 0; i < terms.size(); i++) {
            String w = terms.get(i);
            terms.set(i, stemmer.stripAffixes(w));
        }
        return terms;
    }

    public boolean classify(String segment) {
        Concept test = getConcept(segment, "test");
        int n = 0;
        double[] x = new double[universe.getTermCount()];
        for(String j : universe.getTerms())
            x[n++] = test.getTermFrequency(j);
        XY sv = new XY(x, "test", true);
        boolean outcome = svm.predict(sv);
        if (log.isDebugEnabled()) {
            log.debug("Classifier \"" + getName() + "\" " + ((outcome) ? "matched" : "failed to match") +
                      " " + segment);
        }
        return outcome;
    }
}
