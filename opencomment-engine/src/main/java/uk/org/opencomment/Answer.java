package uk.org.opencomment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Answer {
    public StringArray concepts = new StringArray();
    public String text = "";
    public Response response = new Response();
    public StringArray suggestions = new StringArray();
    public StringArray comments = new StringArray();
    public int attempt = 1;
    private Logger log = LoggerFactory.getLogger(Answer.class);

    /**
     * @param value
     * @throws AnswerValue
     */
    final public void exit(Object value) {
        throw new AnswerValue(value);
    }

    /**
     * @throws AnswerValue
     */
    final public void exit() {
        throw new AnswerValue();
    }

    /**
     * @param value
     */
    final public void debug(Object value) {
        if (log.isDebugEnabled()) {
            log.debug(value.toString());
        }
    }

    /**
     * @param name
     * @return
     */
    final public boolean findConcept(String name) {
        return concepts.find(name);
    }

    /**
     * @param name
     * @return
     */
    final public int positionConcept(String name) {
        return concepts.indexOf(name);
    }

    /**
     * @return
     */
    final public String getText() {
        return text;
    }

    /**
     * @param text
     */
    final public void setText(String text) {
        this.text = text;
    }

    /**
     * @return
     */
    final public int getAttempt() {
        return attempt;
    }

    /**
     * @param attempt
     */
    final public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public class AnswerValue extends RuntimeException {
        private static final long serialVersionUID = 7060301221701674658L;
        private Object result = null;
        
        /**
         * @param o
         */
        public AnswerValue(Object o) {
            result = o;
        }
        
        public AnswerValue() {
        }
        
        /**
         * @return
         */
        public Object getValue() {
            return result;
        }
    }
}
