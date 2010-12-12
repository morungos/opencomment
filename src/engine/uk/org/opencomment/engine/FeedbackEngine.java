package uk.org.opencomment.engine;

public interface FeedbackEngine {
	public void initialiseEngine() throws Exception;
	public String getFeedback(String theQuestion, String descriptionUrl, 
			                  String theAnswer, int theAttempt) throws Exception;
}
