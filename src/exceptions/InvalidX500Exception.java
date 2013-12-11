package exceptions;

/**
 * An exception to be thrown when an invalid X500 is input to a method call.
 * @author Nathan
 *
 */
public class InvalidX500Exception extends Exception{
    
    //stores the ID causing the exception
    private String invalidID;

    /**
     * The constructor for the exception.
     * @param invalidId the X500 causing the exception
     */
    public InvalidX500Exception(String invalidId) {
        //continue where super left off at time of exception throwing 
        super();
        //store the X500 causing the exception
        invalidID = invalidId;
    }
        
    /**
     * Returns a error message explaining the cause of the exception.
     * @return message An error message explaining the exception.
     */
    public String errorMessage() {
        //create the message
        String message = "The user ID " + invalidID + " is not a valid ID in this system";
        //return the message
        return message;
    }
}
