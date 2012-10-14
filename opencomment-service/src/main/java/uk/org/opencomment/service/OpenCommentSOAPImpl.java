package uk.org.opencomment.service;

import java.rmi.RemoteException;

import uk.org.opencomment.engine.Engine;

public class OpenCommentSOAPImpl extends OpenCommentSkeleton implements OpenCommentSkeletonInterface {

	private Engine engine = new Engine();
	
    public String initializeEngine(String in) throws RemoteException {
        try {
			engine.initialiseEngine();
		} catch (Exception e) {
			throw new RemoteException(e.toString());
		}
		return "DONE";
    }

	public String getFeedback(String questionid, String descriptorUrl, String submission, int attempt) throws RemoteException {
		String result = "ERROR";
		try {
			result = engine.getFeedback(questionid, descriptorUrl, submission, attempt);
		} catch (Exception e) {
			throw new RemoteException(e.toString());
		}
		return result;
	}
}
