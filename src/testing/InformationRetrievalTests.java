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

//tests for requirement 1
public class InformationRetrievalTests {
    //variables for testing
    GRADS testGrads = new GRADS("resources/studentsTest.txt", "resources/courses.txt", "resources/usersTest.txt");
    StudentRecord testRecord = new StudentRecord();


    //test 1.1
    public void retrieveAStudentTranscriptTest() {
        try {
            testGrads.setUser("tolas9999");
        } catch (Exception e) {
            System.out.println("X500 invalid");
            e.printStackTrace();
        }
        try {
            testRecord = testGrads.getTranscript("nguy0621");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assert.assertEquals(testRecord.getStudent().getId() , "nguy0621");
    }


}
