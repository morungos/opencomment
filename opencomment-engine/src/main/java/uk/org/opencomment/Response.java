package uk.org.opencomment;

/**
 * A representation of a response, designed to be accessible to
 * JavaScript through Rhino.
 *
 * @author stuart
 */
public class Response {

    /**
     * The response text.
     */
    private String text = "";

    /**
     * @param value a paragraph
     */
    public final void addParagraph(final String value) {
        text = text + "<p>" + value + "</p>";
    }

    /**
     * @param values a bulleted list
     */
    public final void addList(final StringArray values) {
        String value = "<ul>\n";
        for (String o : values) {
            value = value + "<li>" + o.toString() + "</li>\n";
        }
        value = value + "</ul>";
        text = text + value;
    }

    /**
     * @return the response text
     */
    public final String getText() {
        return text;
    }

    /**
     * @param body the new response text
     */
    public final void setText(final String body) {
        text = body;
    }
}
