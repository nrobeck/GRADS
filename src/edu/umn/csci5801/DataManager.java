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
import com.google.gson.reflect.TypeToken;


/**
 * The DataManager class is used to retrieve and store
 * information contained in the databases
 * @author Mark Holmes
 *
 */
public class DataManager {
    //PRIVATE VARIABLES
    //instance of gson for json conversion
    private Gson gson;

    //arraylists for storing information
    private ArrayList<Course> courses;
    private ArrayList<StudentRecord> studentRecords;
    private ArrayList<ProgressSummary> progressSummaries;
    private ArrayList<User> users;

    //string storage of database file names
    private String coursesFileName;
    private String studentRecordFileName;
    private String progressSummaryFileName;
    private String userFileName;

    private boolean init;

    //END PRIVATE VARIABLES

    /**
     * Constructor of the data manager with no inputs.
     */
    public DataManager(){
        //initialize local variables
        this.gson = new Gson();
        this.coursesFileName = null;
        this.studentRecordFileName = null;
        this.progressSummaryFileName = null;
        this.userFileName = null;
        this.init = false;
    }

    /**
     * Constructor of the data manager with 4 database inputs.
     * @param coursesFileName string representation of the relative path to the courses data
     * @param studentRecordFileName string representation of the relative path to the students data
     * @param progressSummaryFileName string representation of the relative path to the progress summary data
     * @param userFileName string representation of the relative path to the user data
     */
    public DataManager(String coursesFileName, String studentRecordFileName, String progressSummaryFileName, String userFileName){
        //initialize local variables
        this.gson = new Gson();
        this.coursesFileName = coursesFileName;
        this.studentRecordFileName = studentRecordFileName;
        this.progressSummaryFileName = progressSummaryFileName;
        this.userFileName = userFileName;
        this.init = false;
    }

    //Loads courses, records, summaries, users, from files
    /**
     * Init loads database information from the data files into local arraylists
     */
    public void init(){
        //check for null courses
        if(coursesFileName == null){
            courses = null;
        }
        else{
            //load the courses from the database
            courses = getCourses();
        }
        //check for null student records
        if(studentRecordFileName == null){
            studentRecords = null;
        }
        else{
            //load the student records from the database
            studentRecords = getStudentRecords();
        }
        //check for null progress summaries
        if(progressSummaryFileName == null){
            progressSummaries = null;
        }
        //load the progress summaries from the database
        else{
            progressSummaries = getProgressSummaries();
        }
        //check for null users
        if(userFileName == null){
            users = null;
        }
        //load the users information from the database
        else{
            users = getUsers();
        }
        //set init to say the data has been initialized
        this.init = true;
    }

    /**
     * Initializes the data manager if it has not been already
     */
    public void checkInit(){
        //check if data manager has not been initialized
        if(!init){
            //initialize data manager
            this.init();
        }
    }

    //To print a course
    /**
     * Prints the course data out to the screen.
     * @param course the course object for which you want the data to be printed
     */
    public void printCourse(Course course){
        //print course name
        System.out.println("Course: " + course.getName());
        //print course id
        System.out.println("\tID: " + course.getId());
        //print course credits
        System.out.println("\tCredits: " + course.getNumCredits());
        //print area of course
        System.out.println("\tArea: " + course.getCourseArea());
    }

    /**
     * Print when the when the course was taken and what grade was received in addition to course data from printCourse.
     * @param courseTaken the course for which the data is being requested
     */
    public void printCourseTaken(CourseTaken courseTaken){
        //get the course object from the courseTaken object, and print the information for it
        this.printCourse(courseTaken.getCourse());
        //print when the course was taken
        System.out.println("\t\tTerm: " + courseTaken.getTerm().getSemester() + " " + courseTaken.getTerm().getYear());
        //print what grade the student recieved in the course
        System.out.println("\t\tGrade: " + courseTaken.getGrade());
    }

    //To print a student record
    /**
     * Print the given student record out to the screen.
     * @param studentRecord the student record to be printed
     */
    public void printStudentRecord(StudentRecord studentRecord){
        //print the name of the student
        System.out.println("Student Name: " + studentRecord.getStudent().getFirstName() + " " + studentRecord.getStudent().getLastName());
        //print the department the student belongs to
        System.out.println("\tDepartment: " + studentRecord.getDepartment());
        //print the degree the student is seeking
        System.out.println("\tDegree: " + studentRecord.getDegreeSought());
        //TODO System.out.println("\tProgram: " + studentRecord.getProgram());

        //Term Began
        //check if the student has started yet
        if(studentRecord.getTermBegan() != null){
            //print the term the student began at
            System.out.println("\tTerm Began: " + studentRecord.getTermBegan().getSemester() + " " + studentRecord.getTermBegan().getYear());
        }
        else{
            //print that student has not started
            System.out.println("\tNo Term Began");
        }

        //Advisors
        //check that the students advisors are set
        if(studentRecord.getAdvisors() != null){
            //print the students advisors
            System.out.println("\tAdvisors: ");
            //loop through the advisors, printing each
            for(Professor advisor : studentRecord.getAdvisors()){
                System.out.println("\t\t" + advisor.getLastName() + ", ");
            }
        }
        else{
            //print that there are no advisors for this student
            System.out.println("\tNo Advisors");
        }

        //Committee
        //check that the student committee is set
        if(studentRecord.getCommittee() != null){
            //print the committee
            System.out.println("\tCommittee: ");
            //loop through committee members, printing each
            for(Professor member : studentRecord.getCommittee()){
                System.out.println("\t\t" + member.getLastName());
            }
        }
        else{
            //print that there are no committee members
            System.out.println("\tNo Committee");
        }

        //Courses Taken
        //check that the student has taken courses
        if(studentRecord.getCoursesTaken() != null){
            //print the courses taken
            System.out.println("\tCourses Taken: ");
            //loop through the course the student has taken, printing each
            for(CourseTaken courseTaken : studentRecord.getCoursesTaken()){
                this.printCourseTaken(courseTaken);
            }
        }
        else{
            //print that no courses have been taken
            System.out.println("\tNo Courses Taken");
        }

        //Print Milestones
        //check that milestones have been set
        if(studentRecord.getMilestonesSet() != null){
            //print milestones
            System.out.println("\tMilestones Set: ");
            //loop through milestones, printing each
            for(MilestoneSet milestoneSet : studentRecord.getMilestonesSet()){
                System.out.println("\t\t" + milestoneSet.getMilestone() + " " + milestoneSet.getTerm().getYear() + " " + milestoneSet.getTerm().getSemester());
            }
        }
        else{
            //print that there are no milestones
            System.out.println("\tNo Milestones Set");
        }

        //Notes
        //check if there are notes to print
        if(studentRecord.getNotes() != null){
            //print the notes
            System.out.println("\tNotes:");
            //loop through notes, printing each
            for(String note : studentRecord.getNotes()){
                System.out.println("Note: " + note);
            }
        }
        else{
            //print that there are no notes
            System.out.println("\t No Notes");
        }
    }


    public void printRequirementCheckResult(RequirementCheckResult requirementCheckResult){
        //print requirement name and whether it was passed or not
        System.out.println("Requirement Result: ");
        System.out.println("\tName: " + requirementCheckResult.getName());
        System.out.println("\tPassed?: " + requirementCheckResult.isPassed());

        //Error Messages
        if(requirementCheckResult.getErrorMsgs() != null){
            System.out.println("\tError Messages: ");
            for(String errorMsg : requirementCheckResult.getErrorMsgs()){
                System.out.println("\t\t" + errorMsg);
            }
        }
        else{
            System.out.println("\tNo Error Messages");
        }
        //Result Details
                if(requirementCheckResult.getDetails() != null){
                    System.out.println("\tDetails:");
                    System.out.println("\t\tGPA: " + requirementCheckResult.getDetails().getGPA());
                    System.out.println("\t\tCourses Taken:");
                    if(requirementCheckResult.getDetails().getCourses() != null){
                        for(CourseTaken course : requirementCheckResult.getDetails().getCourses()){
                            this.printCourseTaken(course);
                        }
                    }
                    else{
                        System.out.println("No Courses Taken:");
                    }
                    if(requirementCheckResult.getDetails().getOther() != null){
                        System.out.println("\t\tOther:");
                        for(String other : requirementCheckResult.getDetails().getOther()){
                            System.out.println("\t\t\tOther Detail: " + other);
                        }
                    }
                    else{
                        System.out.println("\t\tNo Other Details");
                    }
                }
                else{
                    System.out.println("\tNo Details");
                }

    }

    //To print a progress summary
    public void printProgressSummary(ProgressSummary progressSummary){
        System.out.println("Student Name: " + progressSummary.getStudent().getFirstName() + " " + progressSummary.getStudent().getLastName());
        System.out.println("\tDepartment: " + progressSummary.getDepartment());
        System.out.println("\tDegree: " + progressSummary.getDegreeSought());
        //Term Began
        if(progressSummary.getTermBegan() != null){
            System.out.println("\tTerm Began: " + progressSummary.getTermBegan().getSemester() + " " + progressSummary.getTermBegan().getYear());
        }
        else{
            System.out.println("\tNo Term Began");
        }
        //Advisors
        if(progressSummary.getAdvisors() != null){
            System.out.println("\tAdvisors: ");
            for(Professor advisor : progressSummary.getAdvisors()){
                System.out.println("\t\t" + advisor.getLastName() + ", ");
            }
        }
        else{
            System.out.println("\tNo Advisors");
        }
        //Committee
        if(progressSummary.getCommittee() != null){
            System.out.println("\tCommittee: ");
            for(Professor member : progressSummary.getCommittee()){
                System.out.println("\t\t" + member.getLastName());
            }
        }
        else{
            System.out.println("\tNo Committee");
        }
        //Notes
        if(progressSummary.getNotes() != null){
            System.out.println("\tNotes:");
            for(String note : progressSummary.getNotes()){
                System.out.println("Note: " + note);
            }
        }
        else{
            System.out.println("\tNo Notes");
        }
        if(progressSummary.getRequirementCheckResults() != null){
            System.out.println("\tRequirement Check Results:");
            for(RequirementCheckResult requirementsResult : progressSummary.getRequirementCheckResults()){
                this.printRequirementCheckResult(requirementsResult);
            }
        }
        else{
            System.out.println("\tNo Requirement Results");
        }
    }

    public void printUser(User user){
        System.out.println("User Id: " + user.getID());
        System.out.println("\tRole: " + user.getRole());
        System.out.println("\tDepartment: " + user.getDepartment());
    }

    //Getters from JSON--------------------------
    //Gets courses from JSON
    public ArrayList<Course> getCourses(){
        Type courseCollectionType = new TypeToken<ArrayList<Course>>(){}.getType();	//https://sites.google.com/site/gson/gson-user-guide#TOC-Collections-Examples
        ArrayList<Course> courses;

        //Course file > JSON > Course Object Array
        File courseFile = new File(coursesFileName);
        String courseJson = null;

        try{
            courseJson = FileUtils.readFileToString(courseFile);
        }
        catch(IOException e){
            System.err.println(e);
        }

        courses = this.gson.fromJson(courseJson, courseCollectionType);

        for(Course course : courses){
            this.printCourse(course);
        }
        return courses;
    }

    //Needs blank constructors for all sub-classes for this to work
    //Gets student records from JSON
    public ArrayList<StudentRecord> getStudentRecords(){
        Type studentRecordCollectionType = new TypeToken<ArrayList<StudentRecord>>(){}.getType();	//https://sites.google.com/site/gson/gson-user-guide#TOC-Collections-Examples
        ArrayList<StudentRecord> studentRecords;

        //Course file > JSON > Course Object Array
        File studentRecordFile = new File(studentRecordFileName);
        String studentRecordJson = null;

        try{
            studentRecordJson = FileUtils.readFileToString(studentRecordFile);
        }
        catch(IOException e){
            System.err.println(e);
        }

        studentRecords = this.gson.fromJson(studentRecordJson, studentRecordCollectionType);

        for(StudentRecord studentRecord : studentRecords){
            this.printStudentRecord(studentRecord);
        }
        this.studentRecords = studentRecords;
        return studentRecords;
    }

    //Gets Progress Summaries from JSON
    public ArrayList<ProgressSummary> getProgressSummaries(){
        Type progressSummaryCollectionType = new TypeToken<ArrayList<ProgressSummary>>(){}.getType();	//https://sites.google.com/site/gson/gson-user-guide#TOC-Collections-Examples
        ArrayList<ProgressSummary> progressSummaries;

        //Course file > JSON > Course Object Array
        File progressSummaryFile = new File(progressSummaryFileName);
        String progressSummaryJson = null;

        try{
            progressSummaryJson = FileUtils.readFileToString(progressSummaryFile);
        }
        catch(IOException e){
            System.err.println(e);
        }

        progressSummaries = this.gson.fromJson(progressSummaryJson, progressSummaryCollectionType);

        for(ProgressSummary progressSummary : progressSummaries){
            this.printProgressSummary(progressSummary);
        }
        this.progressSummaries = progressSummaries;
        return progressSummaries;
    }
    //Gets Users from JSON
    public ArrayList<User> getUsers(){
        Type userSchemaCollectionType = new TypeToken<ArrayList<UserSchema>>(){}.getType();	//https://sites.google.com/site/gson/gson-user-guide#TOC-Collections-Examples
        ArrayList<UserSchema> userSchemas;
        ArrayList<User> users = new ArrayList<User>();

        //Course file > JSON > Course Object Array
        File userFile = new File(userFileName);
        String userJson = null;

        try{
            userJson = FileUtils.readFileToString(userFile);
        }
        catch(IOException e){
            System.err.println(e);
        }

        userSchemas = this.gson.fromJson(userJson, userSchemaCollectionType);

        for(UserSchema userSchema : userSchemas){
            User user = new User(userSchema.getID().getID(), userSchema.getRole(), userSchema.getDepartment());
            users.add(user);
            printUser(user);
        }
        this.users = users;
        return users;

    }

    //Interface Methods--------------------------
    //Return null if invalid
    public User getUserByID(String userId){
        checkInit();
        User searchUser = new User(userId, null, null);
        int userIndex = this.users.indexOf(searchUser);
        if(userIndex == -1){
            return null;
        }
        else{
            return this.users.get(userIndex);
        }
    }

    //Get all IDs of Students in department
    public ArrayList<String> getStudentIDList(Department department){
        checkInit();
        ArrayList<String> studentIds = new ArrayList<String>();

        for(StudentRecord student : this.studentRecords){
            if(student.getDepartment() == department){
                studentIds.add(student.getStudent().getId());
            }
            //QUESTION: Can any GPC see student's without a department? I think yes.
            else if(student.getDepartment() == null){
                studentIds.add(student.getStudent().getId());
            }
        }

        return studentIds;
    }

    //Gets student record based on ID returns null if none can be found
    public StudentRecord getStudentData(String studentId){
        ListIterator<StudentRecord> i = this.studentRecords.listIterator();
        StudentRecord studentRecord = null;
        while(i.hasNext()){
            studentRecord = i.next();
            if(studentRecord.getStudent().getId() == studentId){
                return studentRecord;
            }
        }
        return null;
    }


    //What is a plan???
    public void getPlan(Degree degree){

    }

    //Writes transcript model to file
    public boolean writeTranscript(){
        String newTranscriptJson = this.gson.toJson(this.studentRecords);
        File studentRecordFile = new File(this.studentRecordFileName);
        try{
            FileUtils.writeStringToFile(studentRecordFile, newTranscriptJson, false);
            return true;
        }
        catch(IOException e){
            System.err.println(e);
            return false;
        }
    }

    public boolean storeTranscript(String studentId, StudentRecord newRecord){
        ListIterator<StudentRecord> i = this.studentRecords.listIterator();
        StudentRecord studentRecord = null;
        int index = -1;
        while(i.hasNext()){
            studentRecord = i.next();
            if(studentRecord.getStudent().getId() == studentId){
                index = i.nextIndex() - 1;
                break;	//This record is the one we want to change
            }
        }
        if(index >= 0){
            this.studentRecords.set(index, newRecord);	//Set the model
            if(writeTranscript()){	//Write Out
                System.out.println("Write Success!");
                return true;
            }
            else{
                System.err.println("Write Failed!");
                this.studentRecords.set(index, studentRecord);	//Reset model
                return false;
            }
        }
        else{
            return false;
        }
    }

    //This should be moved into testing
    public static void main(String[] args){
        String courseFile = "data/courses.txt";
        String studentsFile = "data/students.txt";
        String progressFile = "data/progress.txt";
        String usersFile = "data/users.txt";
        String plansFile = "data/plans.txt";

        DataManager dataManager = new DataManager(courseFile, studentsFile, progressFile, usersFile);
        dataManager.init();
    }

}
