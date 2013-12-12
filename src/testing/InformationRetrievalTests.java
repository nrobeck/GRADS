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
import edu.umn.csci5801.User;
import edu.umn.csci5801.GRADS;
import edu.umn.csci5801.DataManager;
import edu.umn.csci5801.SummaryBuilder;
import edu.umn.csci5801.TranscriptHandler;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.StudentRecord;
import exceptions.InvalidX500Exception;

//tests for requirement 1
public class InformationRetrievalTests {
    //variables for testing
    GRADS testGrads = new GRADS("resources/studentsTest.txt", "resources/courses.txt", "resources/usersTest.txt");
    StudentRecord testRecord = new StudentRecord();


    /**
     * Test successful retrieval of student transcript.
     * Test 1.1
     */
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
        Assert.assertEquals(testRecord.getStudent().getId() , "nguy0621");
    }

    /**
     * Test retrieval of record that does not exist.
     * Test 1.2
     */
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
        Assert.assertTrue(message == "The user ID nguy0622 is not a valid ID in this system");

    }


}
