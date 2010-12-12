package uk.org.opencomment.classifiers;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RegexClassifier extends BaseClassifier implements Classifier {
	
	private Pattern pattern = null;
	private Log log = LogFactory.getLog(RegexClassifier.class);

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
