package uk.org.opencomment.classifiers;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A classifier that uses a regular expression to determine whether or
 * not to match a category. These are typically used for manually
 * constructed and simple categories.
 *
 * @author stuart
 */
public class RegexClassifier extends BaseClassifier implements Classifier {

    /**
     * The pattern.
     */
    private Pattern pattern = null;

    /**
     * The logger.
     */
    private Logger log = LoggerFactory.getLogger(RegexClassifier.class);

    public boolean classify(String segment) {
        boolean result = pattern.matcher(segment).find();
        if (log.isDebugEnabled()) {
            log.debug("Classifier \"" + getName() + "\" " + ((result) ? "matched" : "failed to match") +
                      " " + segment);
        }
        return result;
    }

    public void initialize(String name, String element) throws Exception {
        super.initialize(name, element);
        element = element.trim();
        if (element.startsWith("/") && element.endsWith("/")) {
            element = element.substring(1, element.length() - 1);
        } else {
            throw new Exception("Failed to initialise classifier: syntax error for pattern");
        }
        pattern = Pattern.compile(element);
        if (log.isDebugEnabled()) {
            log.debug("Compiled pattern: " + element);
        }
    }
}
