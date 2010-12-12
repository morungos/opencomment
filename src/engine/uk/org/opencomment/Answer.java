package uk.org.opencomment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Answer {
	public StringArray concepts = new StringArray();
	public String text = "";
	public Response response = new Response();
	public StringArray suggestions = new StringArray();
	public StringArray comments = new StringArray();
	public int attempt = 1;
	private Log log = LogFactory.getLog(Answer.class);
	
	public void exit(Object value) throws AnswerValue {
		throw new AnswerValue(value);
	}
	
	public void exit() throws AnswerValue {
		throw new AnswerValue();
	}
	
	public void debug(Object value) {
		if (log.isDebugEnabled()) {
			log.debug(value);
		}
	}
	
	public boolean findConcept(String name) {
		return concepts.find(name);
	}
	
	public int positionConcept(String name) {
		return concepts.indexOf(name);
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public int getAttempt() {
		return attempt;
	}
	
	public void setAttempt(int attempt) {
		this.attempt = attempt;
	}

	public class AnswerValue extends RuntimeException {
		private static final long serialVersionUID = 7060301221701674658L;
		private Object result = null;
		public AnswerValue(Object o) {
			result = o;
		}
		public AnswerValue() {
		}
		public Object getValue() {
			return result;
		}
	}
}
