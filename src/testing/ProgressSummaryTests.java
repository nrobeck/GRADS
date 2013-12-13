package testing;

import java.util.ArrayList;
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
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.StudentRecord;

//tests for requirement 9

public class ProgressSummaryTests {
	@Test
	public void makeAllRequirementsCheck() {
		GRADS grads = new GRADS("src/resources/studentsTest001.txt",
				"src/resources/courses.txt", "src/resources/usersTest.txt");

		try {
			grads.setUser("studentPHD");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ProgressSummary summary = new ProgressSummary();
		try {
			summary = grads.generateProgressSummary("studentPHD");

			// check that all requirements check results were created
			String[] reqList = new String[] { "BREADTH_REQUIREMENT_PHD",
					"THESIS_PHD", "COLLOQUIUM", "OUT_OF_DEPARTMENT",
					"INTRO_TO_RESEARCH", "TOTAL_CREDITS", "OVERALL_GPA_PHD",
					"IN_PROGRAM_GPA_PHD", "PRELIM_COMMITTEE_APPOINTED",
					"WRITTEN_PE_SUBMITTED", "ORAL_PE_PASSED", "DPF_SUBMITTED",
					"DPF_APPROVED", "THESIS_COMMITTEE_APPOINTED",
					"PROPOSAL_PASSED", "GRADUATION_PACKET_REQUESTED",
					"THESIS_SUBMITTED", "THESIS_APPROVED", "DEFENSE_PASSED" };
			for (String s : reqList) {
				Assert.assertNotNull("PHD Summary requires requirement: " + s,
						getRequirement(summary, s));
				Assert.assertFalse("Requirement, " + s
						+ ", Should not be passed", getRequirement(summary, s)
						.isPassed());
			}

			// check for MS A requirements
			grads.setUser("studentMSA");
			summary = grads.generateProgressSummary("studentMSA");

			// check that all requirements check results were created
			reqList = new String[] { "BREADTH_REQUIREMENT_MS", "THESIS_MS",
					"COLLOQUIUM", "PHD_LEVEL_COURSES", "TOTAL_CREDITS",
					"COURSE_CREDITS", "OVERALL_GPA_MS", "IN_PROGRAM_GPA_MS",
					"DPF_SUBMITTED", "DPF_APPROVED",
					"THESIS_COMMITTEE_APPOINTED",
					"GRADUATION_PACKET_REQUESTED", "THESIS_SUBMITTED",
					"THESIS_APPROVED", "DEFENSE_PASSED" };
			for (String s : reqList) {
				Assert.assertNotNull("MS_A Summary requires requirement: " + s,
						getRequirement(summary, s));
				Assert.assertFalse("Requirement, " + s
						+ ", Should not be passed", getRequirement(summary, s)
						.isPassed());
			}

			// check for MS B requirements
			grads.setUser("studentMSB");
			summary = grads.generateProgressSummary("studentMSB");

			// check that all requirements check results were created
			reqList = new String[] { "BREADTH_REQUIREMENT_MS",
					"PLAN_B_PROJECT", "COLLOQUIUM", "PHD_LEVEL_COURSES",
					"TOTAL_CREDITS", "OVERALL_GPA_MS", "IN_PROGRAM_GPA_MS",
					"DPF_SUBMITTED", "DPF_APPROVED",
					"PROJECT_COMMITTEE_APPOINTED",
					"GRADUATION_PACKET_REQUESTED", "DEFENSE_PASSED" };
			for (String s : reqList) {
				Assert.assertNotNull("MS_B Summary requires requirement: " + s,
						getRequirement(summary, s));
				Assert.assertFalse("Requirement, " + s
						+ ", Should not be passed", getRequirement(summary, s)
						.isPassed());
			}

			// check for MS C requirements
			grads.setUser("studentMSC");
			summary = grads.generateProgressSummary("studentMSC");

			// check that all requirements check results were created
			reqList = new String[] { "BREADTH_REQUIREMENT_MS", "COLLOQUIUM",
					"PHD_LEVEL_COURSES_PLANC", "TOTAL_CREDITS",
					"OVERALL_GPA_MS", "IN_PROGRAM_GPA_MS", "DPF_SUBMITTED",
					"DPF_APPROVED", "TRACKING_FORM_APPROVED",
					"TRACKING_FORM_SUBMITTED", "GRADUATION_PACKET_REQUESTED" };
			for (String s : reqList) {
				Assert.assertNotNull("MS_C Summary requires requirement: " + s,
						getRequirement(summary, s));
				Assert.assertFalse("Requirement, " + s
						+ ", Should not be passed", getRequirement(summary, s)
						.isPassed());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void checkCompletedSummaries() {
		GRADS grads = new GRADS("src/resources/studentsTest111.txt",
				"src/resources/courses.txt", "src/resources/usersTest.txt");

		try {
			grads.setUser("studentPHD");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ProgressSummary summary = new ProgressSummary();
		try {
			summary = grads.generateProgressSummary("studentPHD");

			// check that all requirements check results were passed
			for (RequirementCheckResult r : summary
					.getRequirementCheckResults()) {
				if (!r.isPassed()) {
					System.out.println(r.getErrorMsgs().get(0)); // TODO: remove
				}
				Assert.assertTrue("PHD Requirement, " + r.getName()
						+ ", Should be passed", r.isPassed());
			}

			// check for MS A requirements
			grads.setUser("studentMSA");
			summary = grads.generateProgressSummary("studentMSA");

			// check that all requirements check results were passed
			for (RequirementCheckResult r : summary
					.getRequirementCheckResults()) {
				Assert.assertTrue("MS A Requirement, " + r.getName()
						+ ", Should be passed", r.isPassed());
			}

			// check for MS B requirements
			grads.setUser("studentMSB");
			summary = grads.generateProgressSummary("studentMSB");

			// check that all requirements check results were passed
			for (RequirementCheckResult r : summary
					.getRequirementCheckResults()) {
				Assert.assertTrue("MS B Requirement, " + r.getName()
						+ ", Should be passed", r.isPassed());
			}

			// check for MS C requirements
			grads.setUser("studentMSC");
			summary = grads.generateProgressSummary("studentMSC");

			// check that all requirements check results were passed
			for (RequirementCheckResult r : summary
					.getRequirementCheckResults()) {
				Assert.assertTrue("MS C Requirement, " + r.getName()
						+ ", Should be passed", r.isPassed());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Run tests on the simulate course function
	 */
	@Test
	public void simulateTest(){
		GRADS grads = new GRADS("src/resources/studentsTest001.txt",
				"src/resources/courses.txt", "src/resources/usersTest.txt");
		List<CourseTaken> list = new ArrayList<CourseTaken>();
		
		list.add(new CourseTaken(new Course("Colloquium", "csci8970", "1", null), null, Grade._));
		
		try {
			grads.setUser("gpc001");
		} catch (Exception e) {
			Assert.fail("setUser should work");
		}
		
		try {
			ProgressSummary summary = grads.simulateCourses("studentPHD", list);
			Assert.assertTrue("Simiulate course must successfully allow COLLOQUIUM to pass", getRequirement(summary, "COLLOQUIUM").isPassed());
		} catch (Exception e) {
			Assert.fail("");
		}
		

		
		
		
	}

	/**
	 * Find a requirement in a progress summary
	 * 
	 * @param summary
	 *            - a progress summary
	 * @param name
	 *            - name of the requirement
	 * @return the desired requirement by name
	 */
	private RequirementCheckResult getRequirement(ProgressSummary summary,
			String name) {
		List<RequirementCheckResult> list = summary
				.getRequirementCheckResults();

		for (RequirementCheckResult r : list) {
			if (name.equals(r.getName())) {
				return r;
			}
		}

		return null;
	}

}
