package edu.umn.csci5801;

//TODO FileNotFound Exceptions

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
	public boolean debug;
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
        this.gson = gsonBuilder.setPrettyPrinting().create();

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
        this.gson = gsonBuilder.setPrettyPrinting().create();

        this.coursesFileName = coursesFileName;
        this.studentRecordFileName = studentRecordFileName;
        this.progressSummaryFileName = progressSummaryFileName;
        this.userFileName = userFileName;
        this.init = false;
    }
    
    /**
     * 
     * @return  the current student record file name
     */
    public String getStudentRecordFileName(){
    	return this.studentRecordFileName;
    }
    
    /**
     * 
     * @param studentRecordFileName
     * @return the current student record file name
     */
    public String setStudentRecordFileName(String studentRecordFileName){
    	this.studentRecordFileName = studentRecordFileName;
    	return this.studentRecordFileName;
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

    // Getters from JSON--------------------------

    /**
     * Get the courses from the JSON file coursesFileName
     *
     * @return an arraylist of course objects
     */
    public ArrayList<Course> getCourses() {
    	if(coursesFileName == null){
        	return null;
        }
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
    	if(studentRecordFileName == null){
        	return null;
        }
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
    	if(progressSummaryFileName == null){
        	return null;
        }
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
    	if(userFileName == null){
        	return null;
        }
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
        //If there are null users
        if(this.users == null){
        	return null;
        }
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
     * @return an arraylist of the student ids as strings, empty list if none are found
     */
    public ArrayList<String> getStudentIDList(Department department) {
        // initialize if not already
        checkInit();
    	//If there are no students
    	if(this.studentRecords == null){
    		return new ArrayList<String>();
    	}
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
    	checkInit();
    	if(this.studentRecords == null){
    		return null;
    	}
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
     * Get courses data, checking if the object is initialized
     * 
     * @return the list of courses or null if none are found
     */
    public ArrayList<Course> getCoursesData(){
    	checkInit();
    	return this.courses;
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
    	checkInit();
    	if(this.studentRecords == null){
    		return false;
    	}
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
                // System.out.println("Write Success!");
                return true;
            } else {
                // print failure message
                // System.err.println("Write Failed!");
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
                // System.out.println("Write Success!");
                return true;
            } else {
                // print failure message
                // System.err.println("Write Failed!");
                // reset the model
                this.studentRecords.remove(newRecord);
                return false;
            }
        }
    }

}
