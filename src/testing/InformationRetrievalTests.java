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
import edu.umn.csci5801.User;
import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.DataManager;
import edu.umn.csci5801.SummaryBuilder;
import edu.umn.csci5801.TranscriptHandler;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.StudentRecord;
import exceptions.InvalidX500Exception;

/**
 * Tests for requirement 1 from the requirements document
 * 
 * @author Nathan
 * 
 */
public class InformationRetrievalTests {
    // variables for testing
    GRADS testGrads = new GRADS("src/resources/students.txt",
	    "src/resources/courses.txt", "src/resources/users.txt");
    StudentRecord testRecord = new StudentRecord();
    DataManager dbManager = new DataManager("src/resources/courses.txt",
	    "src/resources/students.txt", "src/resources/progress.txt",
	    "src/resources/users.txt");

    /**
     * Test successful retrieval of student transcript.
     * 
     * @see x Requirements Documnet Test 1.1
     */
    @Test
    public void retrieveAStudentTranscriptTest() {
	try {
	    testGrads.setUser("tolas9999");
	} catch (Exception e) {
	    System.out.println(((InvalidX500Exception) e).errorMessage());
	    e.printStackTrace();
	}

	try {
	    testRecord = testGrads.getTranscript("nguy0621");
	} catch (Exception e) {
	    System.out.println(((InvalidX500Exception) e).errorMessage());
	    e.printStackTrace();
	}
	Assert.assertEquals(testRecord.getStudent().getId(), "nguy0621");
    }

    /**
     * Test retrieval of record that does not exist.
     * 
     * @see x Requirements Document Test 1.2
     */
    @Test
    public void retrieveNonExistentRecordTest() {
	String message = "";
	try {
	    testGrads.setUser("tolas9999");
	} catch (Exception e) {
	    System.out.println(((InvalidX500Exception) e).errorMessage());
	    e.printStackTrace();
	}
	try {
	    testRecord = testGrads.getTranscript("nguy0622");
	} catch (Exception e) {
	    message = ((InvalidX500Exception) e).errorMessage();
	}
	Assert.assertTrue(message
		.equals("The user ID nguy0622 is not a valid ID in this system"));

    }

    /**
     * Test successful retrieval of list of graduate students
     * 
     * @see x Tequirements Document Test 1.3
     */
    @Test
    public void retrieveGradStudentsTest() {
	List<String> s = null;
	try {
	    testGrads.setUser("tolas9999");
	} catch (Exception e) {
	    System.out.println(((InvalidX500Exception) e).errorMessage());
	}
	try {
	    s = testGrads.getStudentIDs();
	} catch (Exception e) {
	    System.out.println(((InvalidX500Exception) e).errorMessage());
	}

	User tolas = dbManager.getUserByID("tolas9999");
	try {
	    Assert.assertTrue(s.equals(dbManager.getStudentIDList(tolas
		    .getDepartment())));
	} catch (NullPointerException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Test successful retrieval of courses for degree
     * 
     * @see x Requirements Document Test 1.5
     */
    @Test
    public void retriveCoursesTest() {

    }

    /**
     * Test successful retrieval of notes for student record
     * 
     * @see x Requirements Document Test 1.6
     */
    @Test
    public void retrieveNotesTest() {

    }

    /**
     * Test successful retrieval of milestones
     * 
     * @see x Requirements Document Test 1.7
     */
    @Test
    public void retrieveMilestonesTest() {

    }

    /**
     * Test data not available messages
     * 
     * @see x Requirements Document Test 1.8
     */
    @Test
    public void dataNotAvailableTest() {

    }
}
