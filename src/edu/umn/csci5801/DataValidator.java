package edu.umn.csci5801;

import java.util.List;

import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.Professor;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.Student;
import edu.umn.csci5801.model.StudentRecord;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.Term;

/**
 * Module to validate data in a student record.
 * 
 * @author Nathan
 * 
 */
public class DataValidator {
    // private variables and objects
    private StudentRecord record = new StudentRecord();
    private DataManager dbManager = new DataManager();

    /**
     * Constructor for the DataValidator.
     */
    public DataValidator() {

    }

    /**
     * Constructor for the DataValidator with parameters
     * 
     * @param record
     * @param dbManager
     */
    public DataValidator(StudentRecord record, DataManager dbManager) {
	this.record = record;
	this.dbManager = dbManager;
    }

    /**
     * Sets the transcript to be validated by the data validator
     * 
     * @param transcript
     */
    public void setTranscript(StudentRecord transcript) {
	// set the record
	this.record = transcript;
    }

    /**
     * Check the validity of the student in the record.
     * 
     * @return true if valid, false if invalid.
     */
    public boolean studentIsValid() {
	// set the student
	Student student = record.getStudent();
	// If no student
	if (student == null) {
	    return false;
	}
	// check if each field in the student object is valid
	// check if student id is null or not in database
	if (dbManager.getStudentData(student.getId()) == null
		|| student.getId() == null) {
	    return false;
	}
	// check if student first name is valid
	if (student.getFirstName() == null) {
	    return false;
	}
	// check if student last name is valid
	if (student.getLastName() == null) {
	    return false;
	}
	// student is valid
	else {
	    return true;
	}

    }

    /**
     * Check the validity of the department in the record.
     * 
     * @return true if valid, false if invalid
     */
    public boolean departmentIsValid() {
	// set the department
	Department department = record.getDepartment();
	// check whether the department is valid
	for (Department d : Department.values()) {
	    // check each department against the department from record
	    if (d.equals(department)) {
		// return if department is found
		return true;
	    }
	}
	// department was not found
	return false;
    }

    /**
     * Check the validity of the department that is input.
     * 
     * @param departmetn
     *            the department whose validity is being checked
     * @return true if valid, false if invalid
     */
    public boolean departmentIsValid(Department department) {
	// check whether the department is valid
	for (Department d : Department.values()) {
	    // check each department against the department from record
	    if (d.equals(department)) {
		// return if department is found
		return true;
	    }
	}
	// department was not found
	return false;
    }

    /**
     * Check the validity of the degree in the record
     * 
     * @return true if valid, false if invalid
     */
    public boolean degreeIsValid() {
	// set the degree
	Degree degree = record.getDegreeSought();
	// check validity of the degrees fields
	for (Degree d : Degree.values()) {
	    // check each degree against the degree from record
	    if (d.equals(degree)) {
		// return if degree is found
		return true;
	    }
	}
	// degree is not in the degrees
	return false;
    }

    /**
     * Check the validity of the term in the record.
     * 
     * @return true if valid, false if invalid
     */
    public boolean termIsValid() {
	// set the result to return
	boolean valid = false;
	// get the term from the record
	Term term = record.getTermBegan();

	// check the term values
	// check the semester
	for (Semester s : Semester.values()) {
	    if (s.equals(term.getSemester())) {
		valid = true;
	    }
	}
	// check the year if the semester was valid
	if (valid) {
	    // check for valid year (positive 4 digit Integer)
	    if ((term.getYear().toString().length() == 4)
		    && (term.getYear().intValue() > 0)) {
		valid = true;
	    }
	    // invalid year
	    else {
		valid = false;
	    }
	}

	// return validity of term
	return valid;

    }

    /**
     * Check the validity of the professors in the record.
     * 
     * @return true if valid, false if invalid
     */
    public boolean professorsAreValid() {
	// get the professors who are advisors
	List<Professor> profAdvisor = record.getAdvisors();
	// get the professors who are on the committee
	List<Professor> profCommittee = record.getCommittee();
	// set the return boolean
	boolean valid = true;

	// check the advisors
	for (Professor p : profAdvisor) {
	    // check if professor is valid
	    if ((p.getFirstName() != null) && (p.getLastName() != null)
		    && departmentIsValid(p.getDepartment())) {
		valid = true;
	    }
	    // invalid professor ends loop and method
	    else {
		return false;
	    }
	}

	// check the committee
	for (Professor p : profCommittee) {
	    // check if the professor is valid
	    if ((p.getFirstName() != null) && (p.getLastName() != null)
		    && departmentIsValid(p.getDepartment())) {
		valid = true;
	    }
	    // invalid professor ends loop and method
	    else {
		return false;
	    }
	}
	// return valid if no false is ever caught in above tests
	return valid;

    }

    /**
     * Check the validity of the courses in the record
     * 
     * @return true if valid, false if invalid
     */
    public boolean coursesAreValid() {
	// set the courses
	List<CourseTaken> courses = record.getCoursesTaken();
	List<Course> validCourses = dbManager.getCourses();
	// set the return value
	boolean valid = true;

	// check each course
	for (CourseTaken c : courses) {
	    if ((c.getCourse() != null) && (c.getGrade() != null)
		    && (c.getTerm() != null)) {
		valid = true;
	    }
	    // TODO check against list of all courses
	    if (valid) {
		for (Course test : validCourses) {
		    if (c.getCourse().equals(test)) {
			return true;
		    }
		}
	    }
	    // course not valid or doesn't match existing course
	    else {
		return false;
	    }

	}

	// no issues found with courses
	return valid;
    }

}
