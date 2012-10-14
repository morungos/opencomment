package uk.org.opencomment.engine;

public interface FeedbackEngine {
	
    /**
     * @throws Exception
     */
    public void initialiseEngine() throws Exception;
    
    /**
     * @param theQuestion
     * @param descriptionUrl
     * @param theAnswer
     * @param theAttempt
     * @return
     * @throws Exception
     */
    public String getFeedback(String theQuestion, String descriptionUrl,
                              String theAnswer, int theAttempt) throws Exception;
}
