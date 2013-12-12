package edu.umn.csci5801;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.ListIterator;

import org.apache.commons.io.FileUtils;

import schema.IdNameSchema;
import schema.UserSchema;

import edu.umn.csci5801.model.CheckResultDetails;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.MilestoneSet;
import edu.umn.csci5801.model.Professor;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.StudentRecord;
import edu.umn.csci5801.model.RequirementCheckResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

/**
 * The DataManager class is used to retrieve and store information contained in
 * the databases
 *
 * @author Mark Holmes
 *
 */
public class DataManager {
    public static final boolean DEBUG = false;
    // PRIVATE VARIABLES
    // instance of gson for json conversion
    private Gson gson;

    // arraylists for storing information
    private ArrayList<Course> courses;
    private ArrayList<StudentRecord> studentRecords;
    private ArrayList<ProgressSummary> progressSummaries;
    private ArrayList<User> users;

    // string storage of database file names
    private String coursesFileName;
    private String studentRecordFileName;
    private String progressSummaryFileName;
    private String userFileName;

    private boolean init;

    // END PRIVATE VARIABLES

    /**
     * Constructor of the data manager with no inputs.
     */
    public DataManager() {
        // initialize local variables
        GsonBuilder gsonBuilder = new GsonBuilder();
        this.gson = gsonBuilder.create();

        this.coursesFileName = null;
        this.studentRecordFileName = null;
        this.progressSummaryFileName = null;
        this.userFileName = null;
        this.init = false;
    }

    /**
     * Constructor of the data manager with 4 database inputs.
     *
     * @param coursesFileName
     *            string representation of the relative path to the courses data
     * @param studentRecordFileName
     *            string representation of the relative path to the students
     *            data
     * @param progressSummaryFileName
     *            string representation of the relative path to the progress
     *            summary data
     * @param userFileName
     *            string representation of the relative path to the user data
     */
    public DataManager(String coursesFileName, String studentRecordFileName,
            String progressSummaryFileName, String userFileName) {
        // initialize local variables
        GsonBuilder gsonBuilder = new GsonBuilder();
        // gsonBuilder.registerTypeAdapter(RequirementSchema.class, new
        // RequirementSchemaDeserializer());
        this.gson = gsonBuilder.create();

        this.coursesFileName = coursesFileName;
        this.studentRecordFileName = studentRecordFileName;
        this.progressSummaryFileName = progressSummaryFileName;
        this.userFileName = userFileName;
        this.init = false;
    }

    // Loads courses, records, summaries, users, from files
    /**
     * Init loads database information from the data files into local arraylists
     */
    public void init() {
        // check for null courses
        if (coursesFileName == null) {
            courses = null;
        } else {
            // load the courses from the database
            courses = getCourses();
        }
        // check for null student records
        if (studentRecordFileName == null) {
            studentRecords = null;
        } else {
            // load the student records from the database
            studentRecords = getStudentRecords();
        }
        // check for null progress summaries
        if (progressSummaryFileName == null) {
            progressSummaries = null;
        }
        // load the progress summaries from the database
        else {
            progressSummaries = getProgressSummaries();
        }
        // check for null users
        if (userFileName == null) {
            users = null;
        }
        // load the users information from the database
        else {
            users = getUsers();
        }
        // set init to say the data has been initialized
        this.init = true;
    }

    /**
     * Initializes the data manager if it has not been already
     */
    public void checkInit() {
        // check if data manager has not been initialized
        if (!init) {
            // initialize data manager
            this.init();
        }
    }

    // To print a course
    /**
     * Prints the course data out to the screen.
     *
     * @param course
     *            the course object for which you want the data to be printed
     */
    public void printCourse(Course course) {
        // print course name
        System.out.println("Course: " + course.getName());
        // print course id
        System.out.println("\tID: " + course.getId());
        // print course credits
        System.out.println("\tCredits: " + course.getNumCredits());
        // print area of course
        System.out.println("\tArea: " + course.getCourseArea());
    }

    /**
     * Print when the when the course was taken and what grade was received in
     * addition to course data from printCourse.
     *
     * @param courseTaken
     *            the course for which the data is being requested
     */
    public void printCourseTaken(CourseTaken courseTaken) {
        // get the course object from the courseTaken object, and print the
        // information for it
        this.printCourse(courseTaken.getCourse());
        // print when the course was taken
        System.out.println("\t\tTerm: " + courseTaken.getTerm().getSemester()
                + " " + courseTaken.getTerm().getYear());
        // print what grade the student recieved in the course
        System.out.println("\t\tGrade: " + courseTaken.getGrade());
    }

    // To print a student record
    /**
     * Print the given student record out to the screen.
     *
     * @param studentRecord
     *            the student record to be printed
     */
    public void printStudentRecord(StudentRecord studentRecord) {
        // print the name of the student
        System.out.println("Student Name: "
                + studentRecord.getStudent().getFirstName() + " "
                + studentRecord.getStudent().getLastName());
        // print the department the student belongs to
        System.out.println("\tDepartment: " + studentRecord.getDepartment());
        // print the degree the student is seeking
        System.out.println("\tDegree: " + studentRecord.getDegreeSought());

        // Term Began
        // check if the student has started yet
        if (studentRecord.getTermBegan() != null) {
            // print the term the student began at
            System.out.println("\tTerm Began: "
                    + studentRecord.getTermBegan().getSemester() + " "
                    + studentRecord.getTermBegan().getYear());
        } else {
            // print that student has not started
            System.out.println("\tNo Term Began");
        }

        // Advisors
        // check that the students advisors are set
        if (studentRecord.getAdvisors() != null) {
            // print the students advisors
            System.out.println("\tAdvisors: ");
            // loop through the advisors, printing each
            for (Professor advisor : studentRecord.getAdvisors()) {
                System.out.println("\t\t" + advisor.getLastName() + ", ");
            }
        } else {
            // print that there are no advisors for this student
            System.out.println("\tNo Advisors");
        }

        // Committee
        // check that the student committee is set
        if (studentRecord.getCommittee() != null) {
            // print the committee
            System.out.println("\tCommittee: ");
            // loop through committee members, printing each
            for (Professor member : studentRecord.getCommittee()) {
                System.out.println("\t\t" + member.getLastName());
            }
        } else {
            // print that there are no committee members
            System.out.println("\tNo Committee");
        }

        // Courses Taken
        // check that the student has taken courses
        if (studentRecord.getCoursesTaken() != null) {
            // print the courses taken
            System.out.println("\tCourses Taken: ");
            // loop through the course the student has taken, printing each
            for (CourseTaken courseTaken : studentRecord.getCoursesTaken()) {
                this.printCourseTaken(courseTaken);
            }
        } else {
            // print that no courses have been taken
            System.out.println("\tNo Courses Taken");
        }

        // Print Milestones
        // check that milestones have been set
        if (studentRecord.getMilestonesSet() != null) {
            // print milestones
            System.out.println("\tMilestones Set: ");
            // loop through milestones, printing each
            for (MilestoneSet milestoneSet : studentRecord.getMilestonesSet()) {
                System.out.println("\t\t" + milestoneSet.getMilestone() + " "
                        + milestoneSet.getTerm().getYear() + " "
                        + milestoneSet.getTerm().getSemester());
            }
        } else {
            // print that there are no milestones
            System.out.println("\tNo Milestones Set");
        }

        // Notes
        // check if there are notes to print
        if (studentRecord.getNotes() != null) {
            // print the notes
            System.out.println("\tNotes:");
            // loop through notes, printing each
            for (String note : studentRecord.getNotes()) {
                System.out.println("Note: " + note);
            }
        } else {
            // print that there are no notes
            System.out.println("\t No Notes");
        }
    }

    /**
     * Print the results from checking the requirements onto the screen.
     *
     * @param requirementCheckResult
     *            the result of checking the requirements, to be printed
     */
    public void printRequirementCheckResult(
            RequirementCheckResult requirementCheckResult) {
        // print requirement name and whether it was passed or not
        System.out.println("Requirement Result: ");
        System.out.println("\tName: " + requirementCheckResult.getName());
        System.out.println("\tPassed?: " + requirementCheckResult.isPassed());

        // Error Messages
        if (requirementCheckResult.getErrorMsgs() != null) {
            System.out.println("\tError Messages: ");
            // loop through error messages, printing each
            for (String errorMsg : requirementCheckResult.getErrorMsgs()) {
                System.out.println("\t\t" + errorMsg);
            }
        } else {
            // print if there were no errors
            System.out.println("\tNo Error Messages");
        }

        // Result Details
        // check that the details were not null
        if (requirementCheckResult.getDetails() != null) {
            // print the gpa and courses taken
            System.out.println("\tDetails:");
            System.out.println("\t\tGPA: "
                    + requirementCheckResult.getDetails().getGPA());
            System.out.println("\t\tCourses Taken:");
            // loop through courses taken, printing each
            if (requirementCheckResult.getDetails().getCourses() != null) {
                for (CourseTaken course : requirementCheckResult.getDetails()
                        .getCourses()) {
                    this.printCourseTaken(course);
                }
            } else {
                // print that no courses were taken
                System.out.println("No Courses Taken:");
            }
            // check for other details to print
            if (requirementCheckResult.getDetails().getOther() != null) {
                // print other details
                System.out.println("\t\tOther:");
                // loop through other details, printing each
                for (String other : requirementCheckResult.getDetails()
                        .getOther()) {
                    System.out.println("\t\t\tOther Detail: " + other);
                }
            } else {
                // print that there were no other details
                System.out.println("\t\tNo Other Details");
            }
        } else {
            // print that there were no details
            System.out.println("\tNo Details");
        }

    }

    /**
     * Print a progress summary onto the screen.
     *
     * @param progressSummary
     *            the progress summary, to be printed
     */
    public void printProgressSummary(ProgressSummary progressSummary) {
        // print the student name, department, and degree from the progress
        // summary
        System.out.println("Student Name: "
                + progressSummary.getStudent().getFirstName() + " "
                + progressSummary.getStudent().getLastName());
        System.out.println("\tDepartment: " + progressSummary.getDepartment());
        System.out.println("\tDegree: " + progressSummary.getDegreeSought());

        // Term Began
        // check if the term began is null
        if (progressSummary.getTermBegan() != null) {
            // print the term began
            System.out.println("\tTerm Began: "
                    + progressSummary.getTermBegan().getSemester() + " "
                    + progressSummary.getTermBegan().getYear());
        } else {
            // print that there is no term began
            System.out.println("\tNo Term Began");
        }

        // Advisors
        // check for advisors
        if (progressSummary.getAdvisors() != null) {
            // print the advisors
            System.out.println("\tAdvisors: ");
            // loop through advisors, printing each
            for (Professor advisor : progressSummary.getAdvisors()) {
                System.out.println("\t\t" + advisor.getLastName() + ", ");
            }
        } else {
            // print that there were no advisors
            System.out.println("\tNo Advisors");
        }

        // Committee
        // check for committee
        if (progressSummary.getCommittee() != null) {
            // print the committee
            System.out.println("\tCommittee: ");
            // loop through committee members, printing each
            for (Professor member : progressSummary.getCommittee()) {
                System.out.println("\t\t" + member.getLastName());
            }
        } else {
            // print that there is no committee
            System.out.println("\tNo Committee");
        }

        // Notes
        // check for notes
        if (progressSummary.getNotes() != null) {
            // print notes
            System.out.println("\tNotes:");
            // loop through notes, printing each
            for (String note : progressSummary.getNotes()) {
                System.out.println("Note: " + note);
            }
        } else {
            // print that there are no notes
            System.out.println("\tNo Notes");
        }

        // check if there are requirements check results
        if (progressSummary.getRequirementCheckResults() != null) {
            // print requirements check results
            System.out.println("\tRequirement Check Results:");
            // loop through results, printing each
            for (RequirementCheckResult requirementsResult : progressSummary
                    .getRequirementCheckResults()) {
                this.printRequirementCheckResult(requirementsResult);
            }
        } else {
            // print that there are no requirements results
            System.out.println("\tNo Requirement Results");
        }
    }

    /**
     * Print the input user onto the screen.
     *
     * @param user
     *            the user object from which the data will be printed onto the
     *            screen.
     */
    public void printUser(User user) {
        // print the user id, role, and department of the given user
        System.out.println("User Id: " + user.getID());
        System.out.println("\tRole: " + user.getRole());
        System.out.println("\tDepartment: " + user.getDepartment());
    }

    // Getters from JSON--------------------------

    /**
     * Get the courses from the JSON file coursesFileName
     *
     * @return an arraylist of course objects
     */
    public ArrayList<Course> getCourses() {
        // set the course collection type
        Type courseCollectionType = new TypeToken<ArrayList<Course>>() {
        }.getType(); // https://sites.google.com/site/gson/gson-user-guide#TOC-Collections-Examples
        ArrayList<Course> courses;

        // Course file > JSON > Course Object Array
        File courseFile = new File(coursesFileName);
        String courseJson = null;

        try {
            courseJson = FileUtils.readFileToString(courseFile);
        } catch (IOException e) {
            System.err.println("Error parsing course JSON file");
            e.printStackTrace();
            return null;
        }

        // convert the json into course objects
        try {
            courses = this.gson.fromJson(courseJson, courseCollectionType);
        } catch (JsonParseException e) {
            System.err.println("Error parsing course JSON!");
            e.printStackTrace();
            return null;
        }

        // print the courses
        if(this.DEBUG){
            for (Course course : courses) {
                this.printCourse(course);
            }
        }
        // return the array of courses
        return courses;
    }

    // Needs blank constructors for all sub-classes for this to work
    // Gets student records from JSON

    /**
     * Get the student records from the JSON file studentRecordFileName
     *
     * @return an arraylist of the student records
     */
    public ArrayList<StudentRecord> getStudentRecords() {
        // set the type of the record
        Type studentRecordCollectionType = new TypeToken<ArrayList<StudentRecord>>() {
        }.getType(); // https://sites.google.com/site/gson/gson-user-guide#TOC-Collections-Examples
        ArrayList<StudentRecord> studentRecords;

        // Course file > JSON > Course Object Array
        File studentRecordFile = new File(studentRecordFileName);
        String studentRecordJson = null;

        try {
            // read in the JSON
            studentRecordJson = FileUtils.readFileToString(studentRecordFile);
        } catch (IOException e) {
            System.err.println(e);
        }
        try {
            // convert the JSON into object array
            studentRecords = this.gson.fromJson(studentRecordJson,
                    studentRecordCollectionType);
        } catch (JsonParseException e) {
            System.err.println("Error parsing course JSON!");
            e.printStackTrace();
            return null;
        }
        if(this.DEBUG){
            // print the student records
            for (StudentRecord studentRecord : studentRecords) {
                this.printStudentRecord(studentRecord);
            }
        }
        this.studentRecords = studentRecords;
        // return the student record array
        return studentRecords;
    }

    /**
     * Get the progress summaries from JSON file progressSummaryFileName
     *
     * @return an arraylist of the progress summaries
     */
    public ArrayList<ProgressSummary> getProgressSummaries() {
        // set the type for the progress summaries
        Type progressSummaryCollectionType = new TypeToken<ArrayList<ProgressSummary>>() {
        }.getType(); // https://sites.google.com/site/gson/gson-user-guide#TOC-Collections-Examples
        ArrayList<ProgressSummary> progressSummaries;

        // Course file > JSON > Course Object Array
        File progressSummaryFile = new File(progressSummaryFileName);
        String progressSummaryJson = null;

        try {
            // read in the JSON
            progressSummaryJson = FileUtils
                    .readFileToString(progressSummaryFile);
        } catch (IOException e) {
            System.err.println(e);
        }
        try {
            // convert the JSON into objects
            progressSummaries = this.gson.fromJson(progressSummaryJson,
                    progressSummaryCollectionType);
        } catch (JsonParseException e) {
            System.err.println("Error parsing course JSON!");
            e.printStackTrace();
            return null;
        }
        if(this.DEBUG){
            // print the progress summaries
            for (ProgressSummary progressSummary : progressSummaries) {
                this.printProgressSummary(progressSummary);
            }
        }
        this.progressSummaries = progressSummaries;
        // return the array of progress summaries
        return progressSummaries;
    }

    /**
     * Get the users from the JSON file userFileName
     *
     * @return an arraylist of the users
     */
    public ArrayList<User> getUsers() {
        // set the type for the users
        Type userSchemaCollectionType = new TypeToken<ArrayList<UserSchema>>() {
        }.getType(); // https://sites.google.com/site/gson/gson-user-guide#TOC-Collections-Examples
        ArrayList<UserSchema> userSchemas;
        ArrayList<User> users = new ArrayList<User>();

        // Course file > JSON > Course Object Array
        File userFile = new File(userFileName);
        String userJson = null;

        try {
            // import the JSON
            userJson = FileUtils.readFileToString(userFile);
        } catch (IOException e) {
            System.err.println(e);
        }
        try {
            // convert the JSON to users array
            userSchemas = this.gson
                    .fromJson(userJson, userSchemaCollectionType);
        } catch (JsonParseException e) {
            System.err.println("Error parsing course JSON!");
            e.printStackTrace();
            return null;
        }

        // print the users
        for (UserSchema userSchema : userSchemas) {
            User user = new User(userSchema.getID().getID(),
                    userSchema.getRole(), userSchema.getDepartment());
            users.add(user);
            if(this.DEBUG){
                printUser(user);
            }
        }
        this.users = users;
        // return the users arraylist
        return users;

    }

    // Interface Methods--------------------------

    /**
     * Get the user to whom the given id belongs.
     *
     * @param userId
     * @return user object with given id, null if invalid
     */
    public User getUserByID(String userId) {
        // initialize data if necessary
        checkInit();
        // create user to run comparisons with
        User searchUser = new User(userId, null, null);
        // find the index of the user who meets the search criteria
        int userIndex = this.users.indexOf(searchUser);
        // check that a user was found
        if (userIndex == -1) {
            return null;
        } else {
            // return the found user
            return this.users.get(userIndex);
        }
    }

    /**
     * Get the ids of all the students in the given department.
     *
     * @param department
     *            department from which all student ids are to be collected
     * @return an arraylist of the student ids as strings
     */
    public ArrayList<String> getStudentIDList(Department department) {
        // initialize if not already
        checkInit();
        // create the arraylist
        ArrayList<String> studentIds = new ArrayList<String>();

        // add the student id of each student in the department to the arraylist
        for (StudentRecord student : this.studentRecords) {
            if (student.getDepartment() == department) {
                studentIds.add(student.getStudent().getId());
            }
            // add any students not in any department to the arraylist
            else if (student.getDepartment() == null) {
                studentIds.add(student.getStudent().getId());
            }
        }

        // return the arraylist of student ids
        return studentIds;
    }

    /**
     * Get a student record based on the given id.
     *
     * @param studentId
     *            the id of the student whose record is needed
     * @return the student record, null if none found
     */
    public StudentRecord getStudentData(String studentId) {
        // initialize list iterator
        ListIterator<StudentRecord> i = this.studentRecords.listIterator();
        // create student record for storage
        StudentRecord studentRecord = null;
        // iterate through the records
        while (i.hasNext()) {
            // move to next record
            studentRecord = i.next();
            try{
                String studentRecordId = studentRecord.getStudent().getId();
                // check if record is one requested
                if (studentRecordId.equals(studentId)) {
                    // return the requested record
                    return studentRecord;
                }
            }
            catch(NullPointerException e){
                return null;
            }
        }
        // return null if no record is found
        return null;
    }

    /**
     * Write out the now modified transcript to the storage file.
     *
     * @return true if successful, false if unsuccessful
     */
    public boolean writeTranscript() {
        // create string for storage
        String newTranscriptJson = this.gson.toJson(this.studentRecords);
        // open student records file
        File studentRecordFile = new File(this.studentRecordFileName);
        try {
            // write out string to file
            FileUtils.writeStringToFile(studentRecordFile, newTranscriptJson,
                    false);
            return true;
        } catch (IOException e) {
            // catch error, report failure
            System.err.println(e);
            return false;
        }
    }

    /**
     * Store a student's new record in the transcript file and update the model.
     *
     * @param studentId
     *            the student ID of the student whose record will be updated
     * @param newRecord
     *            the new record for the student with student ID.
     * @return true if successful, false if unsuccessful
     */
    public boolean storeTranscript(String studentId, StudentRecord newRecord) {
        // create the list iterator
        ListIterator<StudentRecord> i = this.studentRecords.listIterator();
        // create student record for storage
        StudentRecord studentRecord = null;
        int index = -1;
        // loop through the student records until desired record is found
        while (i.hasNext()) {
            studentRecord = i.next();
            try{
                String studentRecordId = studentRecord.getStudent().getId();
                // check if record id is desired student id
                if (studentRecordId.equals(studentId)) {
                    index = i.nextIndex() - 1;
                    // This record is the one we want to change
                    break;
                }
            }
            //If null do nothing
            catch(NullPointerException e){
                continue;
            }
        }
        // If the student is there, overwrite
        if (index >= 0) {
            // Set the model
            this.studentRecords.set(index, newRecord);
            // write the record out to the storage file
            if (writeTranscript()) {
                // print that write was successful
                System.out.println("Write Success!");
                return true;
            } else {
                // print failure message
                System.err.println("Write Failed!");
                // reset the model
                this.studentRecords.set(index, studentRecord);
                return false;
            }
        }
        // Otherwise add a new record
        else {
            // Set the model
            this.studentRecords.add(newRecord);
            // write the record out to the storage file
            if (writeTranscript()) {
                // print that write was successful
                System.out.println("Write Success!");
                return true;
            } else {
                // print failure message
                System.err.println("Write Failed!");
                // reset the model
                this.studentRecords.remove(newRecord);
                return false;
            }
        }
    }

}
