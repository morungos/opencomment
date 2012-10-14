package uk.org.opencomment.classifiers;

import org.w3c.dom.Element;

public abstract class BaseClassifier {

    private String name = null;

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     * @param element
     * @throws Exception
     */
    public void initialize(String name, Element element) throws Exception {
        initialize(name, element.getTextContent());
    }

    /**
     * @param name
     * @param element
     * @throws Exception
     */
    public void initialize(String name, String element) throws Exception {
        this.name = name;
    }
}
