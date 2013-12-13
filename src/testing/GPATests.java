package testing;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.RequirementCheckResult;

//tests for requirement 10
public class GPATests {
    GRADS grads = new GRADS("src/resources/studentsTestGPA.txt",
	    "src/resources/courses.txt", "src/resources/usersTest.txt");

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

    @Test
    public void GPATest() {
	try {
	    grads.setUser("gpc001");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	ProgressSummary summary40 = new ProgressSummary();
	ProgressSummary summary35 = new ProgressSummary();
	ProgressSummary summary33 = new ProgressSummary();
	try {
	    summary40 = grads.generateProgressSummary("studentMSA");
	    summary35 = grads.generateProgressSummary("studentMSB");
	    summary33 = grads.generateProgressSummary("studentMSC");

	    Assert.assertTrue(4.0 == getRequirement(summary40, "OVERALL_GPA_MS")
		    .getDetails().getGPA());
	    Assert.assertTrue(3.0 <= getRequirement(summary35, "OVERALL_GPA_MS")
		    .getDetails().getGPA());
	    Assert.assertTrue(3.0 <= getRequirement(summary33, "OVERALL_GPA_MS")
		    .getDetails().getGPA());
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}
