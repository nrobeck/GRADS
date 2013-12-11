package exceptions;

/**
 * An exception to be thrown when a user attempts to do something they are not allowed to.
 * @author Nathan
 *
 */
public class UserNotAllowedException extends Exception{
    
    //stores the ID of the current user of the system at the time this exception is thrown
    private String userID;
    
    
    /**
     * The constructor for the exception
     * @param userId the X500 of the user whose file is being operated on.
     */
    public UserNotAllowedException(String userId) {
        //continue where super left off at time of exception throwing 
        super();
        //store the X500 of the user whose file is being operated on
        userID = userId;
    }
    
    /**
     * Returns an error message explaining the cause of the exception.
     * @return message Error message explaining the exception cause
     */
    public String errorMessage() {
        //create the message
        String message = "Only GPCs or the user " + userID + " are allowed to perform that command with this data.";
        //return the message
        return message;
    }
}
