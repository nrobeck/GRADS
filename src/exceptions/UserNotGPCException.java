package exceptions;

/**
 * An exception to be thrown when a non GPC user attempts a GPC only command.
 * @author Nathan
 *
 */
public class UserNotGPCException extends Exception{

    //stores the ID of the current user of the system at the time this exception is thrown
    private String invalidID;

    /**
     * The constructor of the exception.
     * @param userId the X500 of the current user of the system at the time the exception is thrown
     */
    public UserNotGPCException(String userId) {
        //continue where super left off at time of exception throwing
        super();
        //store the user of the system at the time of the exception
        invalidID = userId;
    }

    /**
     * Return an error message explaining why this exception was thrown.
     * @return message a message explaining the cause of the exception
     */
    public String errorMessage() {
        //create the message
        String message = "Only a GPC may use perform this action. The user ID " + invalidID + " is not a valid ID for a GPC in this system.";
        //return the message
        return message;
    }

}
