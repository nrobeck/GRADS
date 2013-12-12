package testing;

import java.util.List;

import org.junit.Assert;
import org.junit.After;
import org.junit.Test;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.ComparisonFailure;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Rule;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.RequirementCheckResult;

//tests for requirement 9

public class ProgressSummaryTests {
	@Test
	public void makeAllRequirementsCheck(){
		GRADS grads = new GRADS("src/resources/studentsTest001.txt", "src/resources/courses.txt", "src/resources/usersTest.txt");
		
		try {
			grads.setUser("student001");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ProgressSummary summary = new ProgressSummary();
		try {
			summary = grads.generateProgressSummary("student001");
			
			// check that all requirements check results were created
			String[] reqList = new String[]{"BREADTH_REQUIREMENT_PHD", "THESIS_PHD", "COLLOQUIUM", "OUT_OF_DEPARTMENT", "INTRO_TO_RESEARCH", "TOTAL_CREDITS", "OVERALL_GPA_PHD", "IN_PROGRAM_GPA_PHD",
											"PRELIM_COMMITTEE_APPOINTED", "WRITTEN_PE_SUBMITTED", "ORAL_PE_PASSED", "DPF_SUBMITTED", "DPF_APPROVED", "THESIS_COMMITTEE_APPOINTED", "PROPOSAL_PASSED", 
											"GRADUATION_PACKET_REQUESTED", "THESIS_SUBMITTED", "THESIS_APPROVED", "DEFENSE_PASSED"};
			for (String s: reqList){
				Assert.assertNotNull("PHD Summary requires requirement: " + s, getRequirement(summary, s));
				Assert.assertFalse("Requirement, " + s + ", Should not be passed", getRequirement(summary, s).isPassed());
			}
			
			// check for MS A requirements
			grads.setUser("studentMSA000");
			summary = grads.generateProgressSummary("studentMSA000");
			
			// check that all requirements check results were created
			reqList = new String[]{"BREADTH_REQUIREMENT_MS", "THESIS_MS", "COLLOQUIUM", "PHD_LEVEL_COURSES", "TOTAL_CREDITS", "COURSE_CREDITS", "OVERALL_GPA_MS", "IN_PROGRAM_GPA_MS",
											"DPF_SUBMITTED", "DPF_APPROVED", "THESIS_COMMITTEE_APPOINTED",
											"GRADUATION_PACKET_REQUESTED", "THESIS_SUBMITTED", "THESIS_APPROVED", "DEFENSE_PASSED"};
			for (String s: reqList){
				Assert.assertNotNull("MS_A Summary requires requirement: " + s, getRequirement(summary, s));
				Assert.assertFalse("Requirement, " + s + ", Should not be passed", getRequirement(summary, s).isPassed());
			}
			
			// check for MS B requirements
			grads.setUser("studentMSB000");
			summary = grads.generateProgressSummary("studentMSB000");
			
			// check that all requirements check results were created
			reqList = new String[]{"BREADTH_REQUIREMENT_MS", "PLAN_B_PROJECT", "COLLOQUIUM", "PHD_LEVEL_COURSES", "TOTAL_CREDITS", "OVERALL_GPA_MS", "IN_PROGRAM_GPA_MS",
											"DPF_SUBMITTED", "DPF_APPROVED", "PROJECT_COMMITTEE_APPOINTED", "GRADUATION_PACKET_REQUESTED", "DEFENSE_PASSED"};
			for (String s: reqList){
				Assert.assertNotNull("MS_B Summary requires requirement: " + s, getRequirement(summary, s));
				Assert.assertFalse("Requirement, " + s + ", Should not be passed", getRequirement(summary, s).isPassed());
			}
	
			// check for MS C requirements
			grads.setUser("studentMSC000");
			summary = grads.generateProgressSummary("studentMSC000");
			
			// check that all requirements check results were created
			reqList = new String[]{"BREADTH_REQUIREMENT_MS", "COLLOQUIUM", "PHD_LEVEL_COURSES_PLANC", "TOTAL_CREDITS", "OVERALL_GPA_MS", "IN_PROGRAM_GPA_MS",
											"DPF_SUBMITTED", "DPF_APPROVED", "TRACKING_FORM_APPROVED", "TRACKING_FORM_SUBMITTED", "GRADUATION_PACKET_REQUESTED"};
			for (String s: reqList){
				Assert.assertNotNull("MS_C Summary requires requirement: " + s, getRequirement(summary, s));
				Assert.assertFalse("Requirement, " + s + ", Should not be passed", getRequirement(summary, s).isPassed());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Find a requirement in a summary
	 * @param summary
	 * @param name
	 * @return
	 */
	private RequirementCheckResult getRequirement(ProgressSummary summary, String name){
		List<RequirementCheckResult> list = summary.getRequirementCheckResults();
		
		for (RequirementCheckResult r: list){
			if(name.equals(r.getName())){
				return r;
			}
		}
		
		return null;
	}
}
