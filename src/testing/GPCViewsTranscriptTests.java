package testing;

import org.junit.Assert;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.model.StudentRecord;
import exceptions.UserNotAllowedException;
import exceptions.UserNotGPCException;

/**
 * Requirement 4. Tests If a GPC can access a Transcript
 * 
 * @author Kyle
 * 
 */
public class GPCViewsTranscriptTests {
    GRADS grads = new GRADS("src/resources/studentsTest001.txt",
	    "src/resources/courses.txt", "src/resources/usersTest.txt");

    /**
     * Tests if a student tries to access a transcript Test if a GPC of the same
     * Department tries to get a transcript Test if a GPC from a different
     * department tries to get the transcript
     */
    @Test
    public void gpcAccessTests() {
	StudentRecord transcript;

	// check if a student tries to get the transcript
	try {
	    grads.setUser("studentPHD");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	boolean temp = false;
	try {
	    transcript = grads.getTranscript("studentPHD");
	} catch (UserNotGPCException e) {
	    // successful error message
	    temp = true;
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	}
	Assert.assertTrue("A UserNotGPC execption must be thrown", temp);

	// check that a CSCI GPC can get a CSCI transcript
	try {
	    grads.setUser("gpc001");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	transcript = null;

	try {
	    transcript = grads.getTranscript("studentPHD");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	Assert.assertNotNull(
		"A csci GPC must be able to get a csci student's transcript",
		transcript);

	// check that a non-csci gpc cannot get a csci student's transcript
	try {
	    grads.setUser("mathGPC001");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	Assert.assertTrue("A UserNotGPC execption must be thrown", temp);

	transcript = null;
	temp = false;
	try {
	    transcript = grads.getTranscript("studentPHD");
	} catch (UserNotAllowedException e) {
	    // correct error response
	    temp = true;
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	Assert.assertNull("A math GPC can not get a csci student's transcript",
		transcript);
    }
}
