package exceptions;

import edu.umn.csci5801.model.StudentRecord;

public class TranscriptNotPersistedException extends Exception{
	private StudentRecord transcript;
	
    public TranscriptNotPersistedException(StudentRecord transcript) {
        //continue where super left off at time of exception throwing
        super();
        this.transcript = transcript;
    }

    public String errorMessage() {
    	String message;
        //create the message
    	if(transcript.getStudent() != null || transcript.getStudent().getId() != null){
    		message = "Transcript with ID: " + transcript.getStudent().getId() + " was not persisted";
    	}
    	else{
    		message = "Transcript with null ID was not persisted";
    	}
        //return the message
        return message;
    }
}
