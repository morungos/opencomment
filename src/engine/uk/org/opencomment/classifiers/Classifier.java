package uk.org.opencomment.classifiers;

import org.w3c.dom.Element;

public interface Classifier {
	public void initialize(String name, String element) throws Exception;
	public void initialize(String name, Element element) throws Exception;
	public boolean classify(String segment);
	public String getName();
}
