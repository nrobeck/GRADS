package testing;

import org.junit.Assert;
import org.junit.Test;

import edu.umn.csci5801.GRADS;
import exceptions.UserNotGPCException;
import exceptions.InvalidX500Exception;

//tests for requirement 5
/**
 * Tests the addNotes function of GRADS
 */
public class AddNoteTests {
    GRADS grads = new GRADS("src/resources/studentsTest001.txt",
	    "src/resources/courses.txt", "src/resources/usersTest.txt");

    @Test
    public void refuseStudent() {
	try {
	    grads.setUser("studentPHD");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	boolean passed = false;

	try {
	    grads.addNote("studentPHD", "Test Note");
	} catch (UserNotGPCException e) {
	    passed = true;
	} catch (Exception e) {
	    e.printStackTrace();
	}

	Assert.assertTrue("Students are not allowed to add notes", passed);
    }

    @Test
    public void allowGPC() {
	try {
	    grads.setUser("gpc001");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	boolean passed = false;

	try {
	    grads.addNote("nobody", "Test Note");
	} catch (InvalidX500Exception e) {
	    passed = true;

	} catch (Exception e) {
	    e.printStackTrace();
	}

	Assert.assertTrue(
		"Return a InvalidX500Exception if student record is missing",
		passed);

	passed = false;
	try {
	    grads.addNote("studentPHD", "Test Note");
	    passed = true;
	} catch (UserNotGPCException e) {

	} catch (Exception e) {
	    e.printStackTrace();
	}

	Assert.assertTrue("GPC must be allowed to add notes", passed);
    }
}
