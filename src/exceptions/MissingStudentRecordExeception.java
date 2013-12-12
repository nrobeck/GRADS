package exceptions;

public class MissingStudentRecordExeception extends Exception{

    //stores the ID missing student record the exception
    private String studentID;

    /**
     * The constructor for the exception.
     * @param invalidId the X500 causing the exception
     */
    public MissingStudentRecordExeception(String id) {
        //continue where super left off at time of exception throwing
        super();
        //store the X500 causing the exception
        studentID = id;
    }

    /**
     * Returns a error message explaining the cause of the exception.
     * @return message An error message explaining the exception.
     */
    public String errorMessage() {
        //create the message
        String message = "The student record for student  ID " + studentID + " is not in the database";
        //return the message
        return message;
    }
}
