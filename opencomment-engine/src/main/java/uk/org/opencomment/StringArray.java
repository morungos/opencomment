package uk.org.opencomment;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An array of strings, designed for accessibility from JavaScript
 * @author stuart
 */
final public class StringArray extends ArrayList<String> {

    /**
     * A serialization identifier
     */
    private static final long serialVersionUID = 9101907582918775913L;

    /**
     * The logger
     */
    private Logger log = LoggerFactory.getLogger(StringArray.class);

    /**
     * @param name the value to find
     * @return true if found
     */
    final public boolean find(String name) {
        return indexOf(name) != -1;
    }

    /**
     * Adds a string
     * @param name the value to add
     */
    final public void push(String name) {
        if (log.isDebugEnabled()) {
            log.debug("Added new item: " + name);
        }
        add(name);
    }

    /**
     * Adds a string if it isn't already there
     * @param name the value to add
     */
    final public void pushNew(String name) {
        if (indexOf(name) == -1) {
            add(name);
        }
    }

    /**
     * Concatenates two string arrays
     * @param other the second string array
     * @return a new array containing the concatenated contents of both
     */
    final public StringArray concat(StringArray other) {
        if (log.isDebugEnabled()) {
            log.debug("Concatenating items");
        }
        StringArray result = new StringArray();
        result.addAll(this);
        result.addAll(other);
        return result;
    }
}
