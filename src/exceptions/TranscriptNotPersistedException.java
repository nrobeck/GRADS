package exceptions;

import edu.umn.csci5801.model.StudentRecord;

/**
 * If the transcript was written to the model but not persisted to the database,
 * this is thrown
 * 
 * @author markholmes
 * 
 */
public class TranscriptNotPersistedException extends Exception {
    private StudentRecord transcript; // Transcript in question

    /**
     * Constructor with transcript in question
     * 
     * @param transcript
     */
    public TranscriptNotPersistedException(StudentRecord transcript) {
	// continue where super left off at time of exception throwing
	super();
	this.transcript = transcript;
    }

    /**
     * Message about which transcript was not persisted
     * 
     * @return message about which transcript was not persisted
     */
    public String errorMessage() {
	String message;
	// create the message
	if (transcript.getStudent() != null
		|| transcript.getStudent().getId() != null) {
	    message = "Transcript with ID: " + transcript.getStudent().getId()
		    + " was not persisted";
	} else {
	    message = "Transcript with null ID was not persisted";
	}
	// return the message
	return message;
    }
}
