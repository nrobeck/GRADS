// SummaryBuilder.java
//

import edu.umn.csci5801.model.*;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/** TODO: copyright */

/**
 *
 * Module of GRADS used to create student summary
 * @author Kyle
 *
 */
public class SummaryBuilder {
	private int dbmanPtr; // TODO: replace with an actual pointer
	private int transPtr; // TODO: get pointer to transcript handler
	
	/**
	 * Constructor method for the Summary Builder
	 */
	public SummaryBuilder() {
		
	}
	
	/**
	 * Creates a graduate progress summary based on the student's transcript
	 * @param studentID The id of the student
	 * @param simCourses A list of courses to simulate
	 * @return A completed summary of a student's progress towards his/her graduate degree
	 */
	public ProgressSummary createStudentSummary(String studentID, List<Course> simCourses) {
		ProgressSummary summary = new ProgressSummary();
		StudentRecord record = new StudentRecord();	// TODO get this from transcript handler
		JsonObject plan = new JsonObject();
		
		summary.setStudent(record.getStudent());
		summary.setDepartment(record.getDepartment());
		summary.setDegreeSought(record.getDegreeSought());
		summary.setTermBegan(record.getTermBegan());
		summary.setAdvisors(record.getAdvisors());
		summary.setCommittee(record.getCommittee());
		summary.setNotes(record.getNotes());
		
		List<CourseTaken> courses = record.getCoursesTaken();
		
		summary.setRequirementCheckResults(checkPlanRequirements(plan, courses, record.getMilestonesSet()));
		
		return null;
	}
	
	/**
	 * Creates a list of RequirementCheckResults for the summary
	 * @param plan - the plan that will be checked
	 * @param courses - the courses that the student has taken
	 * @param milestones - a list of milestones of the student
	 * @return a list of Requirement Check Results based on the plan/student progress
	 */
	private List<RequirementCheckResult> checkPlanRequirements(JsonObject plan, List<CourseTaken> courses, List<MilestoneSet> milestones) {
		String[] requirements = new String[1];
		JsonElement reqs;
		List<RequirementCheckResult> retVal = new ArrayList<RequirementCheckResult>() {
		};
		/*
		if (plan.get("requirements") == null || !plan.get("requirements").getClass().equals()){
			// throw a error, no requirements
			return null;
		}
		*/
		
		reqs = plan.get("requirements");
		
		for( JsonElement e : reqs.getAsJsonArray()){
			JsonObject req = e.getAsJsonObject();
			String name = req.get("name").getAsString();
			RequirementCheckResult result = new RequirementCheckResult(name, false);
			
			if(req.get("type").getAsString().equals("milestone"))
			{
				for(MilestoneSet m : milestones){
					if(m.getMilestone().toString().equals(name)){
						result.setPassed(true);
					}
				}
			}
			else {
				
			}
			
			retVal.add(result);
		}
		
		return null;
	}
	
	
	/**
	 * calculate the GPA of a list of courses
	 * @param courses - the list of courses to derive a GPA from
	 * @return the GPA
	 */
	private double calculateGPA(List<CourseTaken> courses) {
		double sum = 0;
		
		for(CourseTaken course : courses){
			sum += course.getGrade().numericValue();
		}
		
		return (sum/courses.size());
	}
}
