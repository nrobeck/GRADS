package testing;

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

import edu.umn.csci5801.DataManager;
import edu.umn.csci5801.model.CheckResultDetails;
import edu.umn.csci5801.model.MilestoneSet;

//tests for requirement 11
public class ModelTests{

	@Test
	public void testCheckResultDetails(){
		DataManager my_dm = new DataManager(null,"src/resources/studentsTest.txt", null, null);
		CheckResultDetails A_CheckResultDetails = new CheckResultDetails();
		A_CheckResultDetails.setGPA((float)3.0);
		A_CheckResultDetails.setCourses(my_dm.getStudentData("nguy0621").getCoursesTaken()) ;
		
		CheckResultDetails B_CheckResultDetails = new CheckResultDetails();
		B_CheckResultDetails.setGPA((float)3.0);
		B_CheckResultDetails.setCourses(my_dm.getStudentData("nguy0621").getCoursesTaken()) ;
		
		CheckResultDetails C_CheckResultDetails = new CheckResultDetails();
		
		CheckResultDetails D_CheckResultDetails = new CheckResultDetails();
		D_CheckResultDetails.setGPA((float)3.0);
		
		boolean result = A_CheckResultDetails.equals(B_CheckResultDetails);
		Assert.assertTrue(!C_CheckResultDetails.equals(A_CheckResultDetails));
		Assert.assertTrue(!D_CheckResultDetails.equals(A_CheckResultDetails));
		
		int a = A_CheckResultDetails.hashCode();
		int c = C_CheckResultDetails.hashCode();
	}
	

	@Test
	public void testMilestoneSet(){
		MilestoneSet myMilestoneSet = new MilestoneSet();
		int x = myMilestoneSet.hashCode();
	}

}
