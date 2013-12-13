package exceptions;

import edu.umn.csci5801.model.StudentRecord;

/**
 * Exception thrown if a transcript is invalid
 * 
 * @author markholmes
 * 
 */
public class InvalidTranscriptException extends Exception {
    private StudentRecord transcript; // Transcript in question

    /**
     * Constructor for exception
     * 
     * @param transcript
     */
    public InvalidTranscriptException(StudentRecord transcript) {
	// continue where super left off at time of exception throwing
	super();
	this.transcript = transcript;
    }

    /**
     * Error message references the transcript
     * 
     * @return message about the transcript that was invalid
     */
    public String errorMessage() {
	String message;
	// create the message
	if (transcript.getStudent() != null
		|| transcript.getStudent().getId() != null) {
	    message = "Transcript with ID: " + transcript.getStudent().getId()
		    + " is invalid";
	} else {
	    message = "Transcript with null ID is invalid";
	}
	// return the message
	return message;
    }
}
