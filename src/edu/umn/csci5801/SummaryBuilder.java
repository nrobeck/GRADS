package edu.umn.csci5801;
// SummaryBuilder.java
//

import edu.umn.csci5801.model.CheckResultDetails;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseArea;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.Milestone;
import edu.umn.csci5801.model.MilestoneSet;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.StudentRecord;







import java.lang.String;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.soap.Detail;







import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/** TODO: copyright */

/**
 *
 * Module of GRADS used to create student summary.
 * @author Kyle
 *
 */
public class SummaryBuilder {
    DataManager dbManager;
    TranscriptHandler transcriptHandler;

    /**
     * Constructor method for the Summary Builder
     */
    public SummaryBuilder() {

    }

    /**
     * Creates a graduate progress summary based on the student's transcript and courses to simulate.
     * @param studentID The id of the student
     * @param simCourses A list of courses to simulate
     * @return A completed summary of a student's progress towards his/her graduate degree
     */
    public ProgressSummary createStudentSummary(String studentID, List<CourseTaken> simCourses) {
        //initialize data objects
        ProgressSummary summary = new ProgressSummary();
        //get the student record from transcriptHandler
        StudentRecord record = new StudentRecord();
        record = transcriptHandler.getTranscript(studentID);
        JsonObject plan = new JsonObject();

        //set data for the summary
        summary.setStudent(record.getStudent());
        summary.setDepartment(record.getDepartment());
        summary.setDegreeSought(record.getDegreeSought());
        summary.setTermBegan(record.getTermBegan());
        summary.setAdvisors(record.getAdvisors());
        summary.setCommittee(record.getCommittee());
        summary.setNotes(record.getNotes());

        //get the courses the student has taken from their record
        List<CourseTaken> courses = record.getCoursesTaken();
        
        //check the requirements and set results
        summary.setRequirementCheckResults(checkPlanRequirements(plan, courses, record.getMilestonesSet(), record.getDegreeSought()));

        return summary;
    }

    /**
     * Creates a list of RequirementCheckResults for the summary.
     * @param plan - the plan that will be checked
     * @param courses - the courses that the student has taken
     * @param milestones - a list of milestones of the student
     * @return a list of Requirement Check Results based on the plan/student progress
     */
    private List<RequirementCheckResult> checkPlanRequirements(JsonObject plan, List<CourseTaken> courses, List<MilestoneSet> milestones, Degree degree) {
        //create the needed elements
        List<RequirementCheckResult> retVal = new ArrayList<RequirementCheckResult>() {};
        RequirementCheckResult result;
        CheckResultDetails details;
        // TODO: determine if I should break these out into individual methods
        // probably should
        
        // check overall and in-course GPAs requirements
        double minGPA;
        
        if(degree == Degree.PHD) {
        	minGPA = 3.45;
        	result = new RequirementCheckResult("OVERALL_GPA_PHD");
        }
        else {
        	minGPA = 3.25;
        	result = new RequirementCheckResult("OVERALL_GPA_MS");
        }
        
        // determine overall GPA
        double gpa = 0;
        double totalSum = 0;
        double incourseSum = 0;
        double credits = 0;
        for (CourseTaken c: courses){
        	// only check A-F courses
        	if(c.getGrade().ordinal() <= Grade.F.ordinal()){
        		totalSum += c.getGrade().numericValue();
        		credits += Integer.parseInt(c.getCourse().getNumCredits());
        		if(isInCourse(c.getCourse(), "csci")){
        			incourseSum += c.getGrade().numericValue();
        		}
        	}
        }
        
        // compare GPA with minimum
        gpa = totalSum/credits;
        if (gpa < minGPA){
        	result.setPassed(false);
        	result.addErrorMsg("Overall GPA is bellow the minimum");
        }
        
        // create a new result for in course gpa
        if(degree == Degree.PHD) {
        	result = new RequirementCheckResult("INCOURSE_GPA_PHD");
        }
        else {
        	result = new RequirementCheckResult("IN_PROGRAM_GPA_MS");
        }
        
        // compare GPA with minimum
        gpa = incourseSum/credits;
        if (gpa < minGPA){
        	result.setPassed(false);
        	result.addErrorMsg("In course  GPA is bellow the minimum");
        }
        
        // add to result list
        retVal.add(result);
        
        /*----------------------------------------------------------------------------*/
        
        // determine credit requirements
        
        /*----------------------------------------------------------------------------*/
        
        // course specific requirements
        
        /*----------------------------------------------------------------------------*/
        
        /*----------------------------------------------------------------------------*/
        
        // Check For Breadth Courses
        int breadthCourseTotal;
        if(degree == Degree.PHD) {
        	breadthCourseTotal = 5;
        }
        else {
        	breadthCourseTotal = 3;
        }
        
        /*
        String[] temp = new String[]{"csci5302", "csci5304", "csci5421", "csci55421", "csci5525"};
        temp = new String[]{"csci5103", "csci5104", "csci5105", "csci5106", "csci5161",
				"csci5204", "csci5211", "csci5221", "csci5231", "csci5451",
				"csci5461", "csci5708", "csci5801", "csci5802"};
        temp = new String[]{"csci5115", "csci5125", "csci5271", "csci5471", "csci5481",
				"csci5511", "csci5512", "csci5521", "csci5523", "csci5551",
				"csci5561", "csci5607", "csci5608", "csci5"};
				*/
        
        // lists of all courses, sorted by area
        List<CourseTaken> appCoursesTaken = new ArrayList<CourseTaken>();
        List<CourseTaken> archCoursesTaken = new ArrayList<CourseTaken>();
        List<CourseTaken> theoryCoursesTaken = new ArrayList<CourseTaken>();
        
        List<String> appCourseList = new ArrayList<String>();
        List<String> archCourseList = new ArrayList<String>();
        List<String> theoryCourseList = new ArrayList<String>();
        
        List<Course> courseList = dbManager.getCourses();
        
        // get lists of all courses that statisfy breadth requirements
        for(Course c: courseList){
        	switch(c.getCourseArea()){
        	case APPLICATIONS:
        		appCourseList.add(c.getId());
        		break;
        	case ARCHITECTURE_SYSTEMS_SOFTWARE:
        		archCourseList.add(c.getId());
        		break;
        	case THEORY_ALGORITHMS:
        		theoryCourseList.add(c.getId());
        		break;
    		default:
        	}
        }
        
        // check each course to see if they fall into an Area
        for(CourseTaken c: courses){
        	// only add courses that have been passed
        	if(passedCourse(c, false)){
        		// add to appropriate area list
        		if(appCourseList.contains(c.getCourse().getId())){
        			appCoursesTaken.add(c);
        		}
        		else if (archCourseList.contains(c.getCourse().getId())){
        			archCoursesTaken.add(c);
        		}
        		else if (theoryCourseList.contains(c.getCourse().getId())){
        			theoryCoursesTaken.add(c);
        		}
        	}
        }
        
        
        // Check that two addition courses have been taken
        if(degree == Degree.PHD){
        	
        }
        
        
        /*------------------------------------------------------------------------------*/
        
        // Determine which milestones have been passed
        Milestone[] planMilestones;
        
        // determine which degree milestones to use
        switch (degree){
        case PHD: 
        	planMilestones = new Milestone[]{		Milestone.PRELIM_COMMITTEE_APPOINTED, Milestone.WRITTEN_PE_SUBMITTED, Milestone.WRITTEN_PE_APPROVED, Milestone.ORAL_PE_PASSED, 
        											Milestone.DPF_SUBMITTED, Milestone.DPF_APPROVED, Milestone.THESIS_COMMITTEE_APPOINTED, Milestone.PROPOSAL_PASSED, Milestone.GRADUATION_PACKET_REQUESTED, 
        											Milestone.THESIS_SUBMITTED, Milestone.THESIS_APPROVED, Milestone.DEFENSE_PASSED};
        	break;
        case MS_A:
        	planMilestones = new Milestone[]{		Milestone.DPF_SUBMITTED, Milestone.DPF_APPROVED, Milestone.THESIS_COMMITTEE_APPOINTED, 
													Milestone.GRADUATION_PACKET_REQUESTED, Milestone.THESIS_SUBMITTED, Milestone.THESIS_APPROVED, Milestone.DEFENSE_PASSED};
        	break;
        case MS_B:
        	planMilestones = new Milestone[]{		Milestone.DPF_SUBMITTED, Milestone.DPF_APPROVED, Milestone.PROJECT_COMMITTEE_APPOINTED, 
													Milestone.GRADUATION_PACKET_REQUESTED, Milestone.DEFENSE_PASSED};
        	break;
        case MS_C:
        	planMilestones = new Milestone[]{		Milestone.DPF_SUBMITTED, Milestone.DPF_APPROVED, Milestone.TRACKING_FORM_APPROVED, Milestone.TRACKING_FORM_SUBMITTED, 
													Milestone.GRADUATION_PACKET_REQUESTED, Milestone.THESIS_SUBMITTED};
        	break;
        default: planMilestones = new Milestone[0];
        }
           
        // run through each milestone and check if the student have completed it
        for (Milestone s: planMilestones){
        	result = new RequirementCheckResult(s.name(), false);
            details = new CheckResultDetails();

            // check if a student has passed this milestone
        	for (MilestoneSet m: milestones){
        		if(s.equals(m.getMilestone())){
        			result.setPassed(true);
        		}
        	}
        	
        	// add result to the result list
        	retVal.add(result);
        }
        
        return retVal;

    }

    /**
     * Calculate the GPA of a list of courses taken.
     * @param courses - the list of courses to derive a GPA from
     * @return the GPA
     */
    private double calculateGPA(List<CourseTaken> courses) {
        //initialize parameters
        int credits = 0;
        int sum = 0;
        int temp;

        //loop through each course taken
        for (CourseTaken c: courses){
            // check if the course is not a S/N or a sim course
            if(c.getGrade().ordinal() <= Grade.F.ordinal() ){
                //get the credits the course was worth
                temp = Integer.parseInt(c.getCourse().getNumCredits());
                //add the credits to the running total
                credits += temp;
                //add the gpa integer value for the credits to the sum
                sum += temp * c.getGrade().numericValue();
            }
        }

        //divide sum of gpa totals by number for credits taken to get gpa and then return it
        return sum/credits;
    }
    
    /**
     * Determines if a course was passed
     * @param course - the course that a student has taken
     * @param passFail - tells if S grades are allowable
     * @return true if the course was passed, false if not
     */
    private boolean passedCourse(CourseTaken course, boolean passFail){
    	// check if C or greater
    	if (course.getGrade().numericValue() >= Grade.C.numericValue()){
    		return true;
    	}
    	
    	if (course.getGrade() == Grade._){
    		return true;
    	}
    	
    	// check if course was passed and passFail is OK
    	if (passFail){
    		return (course.getGrade() == Grade.S);
    	}
    	
    	
    	// course was not passed
    	return false;
    }
    
    private boolean isInCourse(Course course, String department){
    	if (course.getId().contains(department)){	// check that the course Id contains the department string
    		int level = Integer.parseInt(course.getId().substring(department.length()-1, department.length()));
    		if (level >= 5){	// check that the level is 5000+
    			return true;
    		}
    	}
    	
    	return false;
    }

    
    /**
     * Return the progress summary based solely upon a student's transcript and no simulated courses.
     * Calls createStudentSummary with null for simulated courses
     * @see createStudentSummary method used to perform all creation
     * @param userId user id of the student whose progress summary is to be generated.
     * @return progress summary for the student.
     */
    public ProgressSummary getStudentSummary(String userId) {
        //create the progress summary
        ProgressSummary progress = createStudentSummary(userId, null);
        //return the progress summary
        return progress;
    }
    
}
