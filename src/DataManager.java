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
 * 
 */

/**
 * @author Mark Holmes
 *
 */
public class DataManager {
	private Gson gson;
	
	private ArrayList<Course> courses;
	private ArrayList<StudentRecord> studentRecords;
	private ArrayList<ProgressSummary> progressSummaries;
	private ArrayList<User> users;
	
	private String coursesFileName;
	private String studentRecordFileName;
	private String progressSummaryFileName;
	private String userFileName;
	
	private boolean init;
	
	public DataManager(){
		this.gson = new Gson();
		this.coursesFileName = null;
		this.studentRecordFileName = null;
		this.progressSummaryFileName = null;
		this.userFileName = null;
		this.init = false;
	}
	
	public DataManager(String coursesFileName, String studentRecordFileName, String progressSummaryFileName, String userFileName){
		this.gson = new Gson();
		this.coursesFileName = coursesFileName;
		this.studentRecordFileName = studentRecordFileName;
		this.progressSummaryFileName = progressSummaryFileName;
		this.userFileName = userFileName;
		this.init = false;
	}
	
	//Loads courses, records, summaries, users, from files
	public void init(){
		if(coursesFileName == null){
			courses = null;
		}
		else{
			courses = getCourses();
		}
		if(studentRecordFileName == null){
			studentRecords = null;
		}
		else{
			studentRecords = getStudentRecords();
		}
		if(progressSummaryFileName == null){
			progressSummaries = null;
		}
		else{
			progressSummaries = getProgressSummaries();
		}
		if(userFileName == null){
			users = null;
		}
		else{
			users = getUsers();
		}
		this.init = true;
	}
	
	//Initializes if not already
	public void checkInit(){
		if(!init){
			this.init();
		}
	}
	
	//To print a course
	public void printCourse(Course course){
		System.out.println("Course: " + course.getName());
		System.out.println("\tID: " + course.getId());
		System.out.println("\tCredits: " + course.getNumCredits());
		System.out.println("\tArea: " + course.getCourseArea());
	}
	
	public void printCourseTaken(CourseTaken courseTaken){
		this.printCourse(courseTaken.getCourse());
		System.out.println("\t\tTerm: " + courseTaken.getTerm().getSemester() + " " + courseTaken.getTerm().getYear());
		System.out.println("\t\tGrade: " + courseTaken.getGrade());
	}
	
	//To print a student record
	public void printStudentRecord(StudentRecord studentRecord){
		System.out.println("Student Name: " + studentRecord.getStudent().getFirstName() + " " + studentRecord.getStudent().getLastName());
		System.out.println("\tDepartment: " + studentRecord.getDepartment());
		System.out.println("\tDegree: " + studentRecord.getDegreeSought());
		//System.out.println("\tProgram: " + studentRecord.getProgram());
		//Term Began
		if(studentRecord.getTermBegan() != null){
			System.out.println("\tTerm Began: " + studentRecord.getTermBegan().getSemester() + " " + studentRecord.getTermBegan().getYear());
		}
		else{
			System.out.println("\tNo Term Began");
		}
		//Advisors
		if(studentRecord.getAdvisors() != null){
			System.out.println("\tAdvisors: ");
			for(Professor advisor : studentRecord.getAdvisors()){
				System.out.println("\t\t" + advisor.getLastName() + ", ");
			}
		}
		else{
			System.out.println("\tNo Advisors");
		}
		//Committee
		if(studentRecord.getCommittee() != null){
			System.out.println("\tCommittee: ");
			for(Professor member : studentRecord.getCommittee()){
				System.out.println("\t\t" + member.getLastName());
			}
		}
		else{
			System.out.println("\tNo Committee");
		}
		//Courses Taken
		if(studentRecord.getCoursesTaken() != null){
			System.out.println("\tCourses Taken: ");
			for(CourseTaken courseTaken : studentRecord.getCoursesTaken()){
				this.printCourseTaken(courseTaken);
			}
		}
		else{
			System.out.println("\tNo Courses Taken");
		}
		//Set Milestones
		if(studentRecord.getMilestonesSet() != null){
			System.out.println("\tMilestones Set: ");
			for(MilestoneSet milestoneSet : studentRecord.getMilestonesSet()){
				System.out.println("\t\t" + milestoneSet.getMilestone() + " " + milestoneSet.getTerm().getYear() + " " + milestoneSet.getTerm().getSemester());
			}
		}
		else{
			System.out.println("\tNo Milestones Set");
		}
		//Notes
		if(studentRecord.getNotes() != null){
			System.out.println("\tNotes:");
			for(String note : studentRecord.getNotes()){
				System.out.println("Note: " + note);
			}
		}
		else{
			System.out.println("\t No Notes");
		}
	}

	public void printRequirementCheckResult(RequirementCheckResult requirementCheckResult){
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
		String courseFile = "src/resources/courses.txt";
		String studentsFile = "src/resources/students.txt";
		String progressFile = "src/resources/progress.txt";
		String usersFile = "src/resources/users.txt";
		DataManager dataManager = new DataManager(courseFile, studentsFile, progressFile, usersFile);
		dataManager.init();
	}
	
}
