package exceptions;

import edu.umn.csci5801.model.StudentRecord;

public class InvalidTranscriptException extends Exception{
	private StudentRecord transcript;
	
    public InvalidTranscriptException(StudentRecord transcript) {
        //continue where super left off at time of exception throwing
        super();
        this.transcript = transcript;
    }

    public String errorMessage() {
    	String message;
        //create the message
    	if(transcript.getStudent() != null || transcript.getStudent().getId() != null){
    		message = "Transcript with ID: " + transcript.getStudent().getId() + " is invalid";
    	}
    	else{
    		message = "Transcript with null ID is invalid";
    	}
        //return the message
        return message;
    }
}
