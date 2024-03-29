package testing;

import org.junit.Assert;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.model.StudentRecord;
import exceptions.InvalidTranscriptException;
import exceptions.UserNotGPCException;

/**
 * tests for requirement 6 Tests the updateTranscript function of Grads
 */
public class EditRecordTests {
    GRADS grads = new GRADS("src/resources/studentsTest001.txt",
	    "src/resources/courses.txt", "src/resources/usersTest.txt");

    /**
     * Test if a student is rejected from updating a transcript
     */
    @Test
    public void studentAttempt() {

	try {
	    grads.setUser("studentPHD");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	boolean passed = false;

	StudentRecord record = new StudentRecord();

	try {
	    grads.updateTranscript("studentPHD", record);
	} catch (UserNotGPCException e) {
	    passed = true;
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	Assert.assertTrue(
		"A Student attempting to update a transcipt must cause a UserNotGPCException",
		passed);
    }

    /**
     * Test if a gpc can update a transcript
     */
    @Test
    public void gpcAttempt() {

	try {
	    grads.setUser("gpc001");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	boolean passed = true;

	StudentRecord record;
	try {
	    record = grads.getTranscript("studentPHD");
	    try {
		grads.updateTranscript("studentPHD", record);
	    } catch (InvalidTranscriptException i) {

	    } catch (Exception e) {
		passed = false;
		e.printStackTrace();
	    }
	} catch (Exception e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	Assert.assertTrue("A GPC must be allowed to update the transcript",
		passed);
    }
}
