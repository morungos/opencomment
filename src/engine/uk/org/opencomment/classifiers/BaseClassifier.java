package uk.org.opencomment.classifiers;

import org.w3c.dom.Element;

public abstract class BaseClassifier {
	
	private String name = null;
	
	public String getName() {
		return name;
	}
	
	public void initialize(String name, Element element) throws Exception {
		initialize(name, element.getTextContent());
	}
	
	public void initialize(String name, String element) throws Exception {
		this.name = name;
	}
}
