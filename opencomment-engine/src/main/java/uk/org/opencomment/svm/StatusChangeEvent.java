package uk.org.opencomment.svm;

import java.util.EventObject;

/**
 * Event encapsulating status information from the solver.
 * @author fherrmann
 * @version $Revision: 31 $
 */
public class StatusChangeEvent extends EventObject {
    /**
     *
     */
    private static final long serialVersionUID = 551302603864305106L;
    /**
     * The status constant.
     */
    private int status;
    /**
     * General purpose to publish a general message.
     */
    private String message;

    /**
     * Constructs an event.
     * @param source is the <code>Object</code> creating the event.
     */
    public StatusChangeEvent(Object source) {
        super(source);
    }

    /**
     * Create a <code>StatusChangeEvent</code> instance.
     * @param source is the <code>Object</code> creating the event.
     * @param status is the status as defined in <code>StatusChangeEvent</code>.
     */
    public StatusChangeEvent(Object source, int status) {
        this(source);
        this.status = status;
    }

    /**
     * @param source is the <code>Object</code> creating the event.
     * @param status is the status as defined in <code>StatusChangeEvent</code>.
     * @param message is a message <code>String</code>
     * @see com.fherrmann.oc.core.svm.StatusChangeEvent
     */
    public StatusChangeEvent(Object source, int status, String message) {
        this(source, status);
        setMessage(message);
    }

    /**
     * Get the status information.
     * @return the <code>int</code> status code.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Set the status information.
     * @param status the <code>int</code> status code.
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Get the message.
     * @return the message <code>String</code>
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message.
     * @param message a message <code>String</code>.
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
