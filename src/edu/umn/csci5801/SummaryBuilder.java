package edu.umn.csci5801;
// SummaryBuilder.java
//

import edu.umn.csci5801.model.CheckResultDetails;
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
        JsonElement reqs;
        List<RequirementCheckResult> retVal = new ArrayList<RequirementCheckResult>() {};
        RequirementCheckResult result;
        
        
        // determine overall and in-course GPAs
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
        double sum = 0;
        double credits = 0;
        for (CourseTaken c: courses){
        	// only check A-F courses
        	if(c.getGrade().ordinal() <= Grade.F.ordinal()){
        		sum += c.getGrade().numericValue();
        		credits += Integer.parseInt(c.getCourse().getNumCredits());
        	}
        }
        
        // compare GPA with minimum
        gpa = sum/credits;
        if (gpa < minGPA){
        	result.setPassed(false);
        	result.addErrorMsg("Overall GPA is bellow the minimum");;
        }
        
        // add to result list
        retVal.add(result);
        
        
        // determine which milestones have been passed
        Milestone[] planMilestones;
        
        // determine which degree
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
            CheckResultDetails details = new CheckResultDetails();

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
     * @return
     */
    private boolean passedCourse(CourseTaken course, boolean passFail){
    	// check if C or greater
    	if (course.getGrade().numericValue() >= Grade.C.numericValue()){
    		return true;
    	}
    	
    	// check if course was passed and passFail is OK
    	if (passFail){
    		return (course.getGrade() == Grade.S);
    	}
    	
    	// course was not passed
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
