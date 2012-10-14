package uk.org.opencomment.classifiers;

import org.w3c.dom.Element;

/**
 * The main classifier interface.
 * @author stuart
 */
public interface Classifier {

    /**
     * Initializes the classifier with the data from the configuration.
     * @param name the name of the classifier
     * @param element the body of the initialization data
     * @throws Exception any error
     */
    void initialize(String name, String element) throws Exception;

    /**
     * Initializes the classifier with the data from the configuration.
     * @param name the name of the classifier
     * @param element the DOM element to use to initialize
     * @throws Exception any error
     */
    void initialize(String name, Element element) throws Exception;

    /**
     * Tests a segment against the classifier.
     * @param segment the segment to be classified
     * @return the classification result
     */
    boolean classify(String segment);

    /**
     * Returns the name of the classifier.
     * @return the name of the classifier
     */
    String getName();
}
