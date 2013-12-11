package edu.umn.csci5801;

import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.Student;
import edu.umn.csci5801.model.StudentRecord;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.Term;

/**
 * Module to validate data in a student record.
 * @author Nathan
 *
 */
public class DataValidator {
    //private variables and objects
    private StudentRecord record = new StudentRecord();
    private DataManager dbManager = new DataManager();

    /**
     * Constructor for the DataValidator.
     */
    public DataValidator() {

    }

    /**
     * Sets the transcript to be validated by the data validator
     * @param transcript
     */
    public void setTranscript(StudentRecord transcript) {
        //set the record
        this.record = transcript;
    }

    /**
     * Check the validity of the student in the record.
     * @return true if valid, false if invalid.
     */
    public boolean studentIsValid() {
        //set the student
        Student student = record.getStudent();
        //check if each field in the student object is valid
        //check if student id is null or not in database
        if(dbManager.getStudentData(student.getId())!= null || student.getId() == null) {
            return false;
        }
        //check if student first name is valid
        if(student.getFirstName() == null) {
            return false;
        }
        //check if student last name is valid
        if(student.getLastName() == null) {
            return false;
        }
        //student is valid
        else {
            return true;
        }

    }

    /**
     * Check the validity of the department in the record.
     * @return true if valid, false if invalid
     */
    public boolean departmentIsValid() {
        //check whether the department is valid
        if((record.getDepartment() == Department.COMPUTER_SCIENCE) ||
           (record.getDepartment() == Department.MATH)) {
            return true;
        }
        else {
            return false;
        }

    }

    /**
     * Check the validity of the degree in the record
     * @return true if valid, false if invalid
     */
    public boolean degreeIsValid() {
        //set the degree
        Degree degree = record.getDegreeSought();
        //check validity of the degrees fields
        for (Degree d : Degree.values()) {
            //check each degree against the degree from record
            if (d.name().equals(degree)) {
                //return if degree is found
                return true;
            }
        }
        //degree is not in the degrees
         return false;
    }

    /**
     *
     * @return true if valid, false if invalid
     */
    public boolean termIsValid() {
        //get the term from the record
        Term term = record.getTermBegan();
        //check the term against valid terms

        //term not found
        return false;

    }

    /**
     *
     * @return true if valid, false if invalid
     */
    public boolean professorIsValid() {
        // TODO Auto-generated method stub

    }

    /**
     *
     * @return true if valid, false if invalid
     */
    public boolean courseIsValid() {
        // TODO Auto-generated method stub

    }

}
