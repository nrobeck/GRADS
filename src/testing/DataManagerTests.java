package testing;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import edu.umn.csci5801.DataManager;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.Student;
import edu.umn.csci5801.model.StudentRecord;

/**
 * Covers requirements 1 and 2 first six tests are unit tests for the various
 * class members. The next four tests are for requirement 1. The next test is
 * for requirement 2.
 * 
 * @author markholmes
 * 
 */
public class DataManagerTests {

    private String courseFile;
    private String studentsFile;
    private String progressFile;
    private String usersFile;

    /**
     * Blank constructor assigning file names
     */
    public DataManagerTests() {
	super();
	this.courseFile = "data/courses.txt";
	this.studentsFile = "src/resources/studentsTest.txt"; // Uses test file
	this.progressFile = "data/progress.txt";
	this.usersFile = "data/users.txt";
    }

    /**
     * Creates a DataManager with the filenames given
     * 
     * @return DataManager retrieving from the databases at these filenames
     */
    public DataManager createDefaultManager() {
	return new DataManager(courseFile, studentsFile, progressFile,
		usersFile);
    }

    /**
     * Tests the constructor of DataManager
     */
    @Test
    public void testCreation() {
	try {
	    assertNotNull(new DataManager());
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Exception! ");
	}
    }

    /**
     * Tests that courses can be retrieved
     */
    @Test
    public void testGetCourses() {
	try {
	    DataManager dataManager = createDefaultManager();
	    assertNotNull(dataManager.getCourses());

	    DataManager nullDataManager = new DataManager();
	    assertNull(nullDataManager.getCourses());
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Exception! ");
	}
    }

    /**
     * Tests that student records can be retrieved
     */
    @Test
    public void testGetStudentRecords() {
	try {
	    DataManager dataManager = createDefaultManager();
	    assertNotNull(dataManager.getStudentRecords());

	    DataManager nullDataManager = new DataManager();
	    assertNull(nullDataManager.getStudentRecords());
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Exception! ");
	}
    }

    /**
     * Tests that progress summaries can be retrieved
     */
    @Test
    public void testGetProgressSummaries() {
	try {
	    DataManager dataManager = createDefaultManager();
	    assertNotNull(dataManager.getProgressSummaries());

	    DataManager nullDataManager = new DataManager();
	    assertNull(nullDataManager.getProgressSummaries());
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Exception! ");
	}
    }

    /**
     * Tests that users can be retrieved
     */
    @Test
    public void testGetUsers() {
	try {
	    DataManager dataManager = createDefaultManager();
	    assertNotNull(dataManager.getUsers());

	    DataManager nullDataManager = new DataManager();
	    assertNull(nullDataManager.getUsers());
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Exception! ");
	}
    }

    /**
     * Tests database initialization
     */
    @Test
    public void testInit() {
	try {
	    DataManager dataManager = createDefaultManager();
	    dataManager.init();

	    DataManager nullDataManager = new DataManager();
	    nullDataManager.init();

	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Exception! ");
	}
    }

    /*
     * Tests the retrieval of a list of student IDs available in a department
     */
    @Test
    public void testGetStudentIDList() {
	try {
	    DataManager dataManager = createDefaultManager();
	    Assert.assertNotEquals("Empty?", new ArrayList<String>(),
		    dataManager.getStudentIDList(Department.COMPUTER_SCIENCE));

	    DataManager nullDataManager = new DataManager();
	    Assert.assertEquals("Not Empty?", new ArrayList<String>(),
		    nullDataManager
			    .getStudentIDList(Department.COMPUTER_SCIENCE));

	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Exception! ");
	}
    }

    /**
     * Tests the retrieval of the data in a student record
     */
    @Test
    public void testGetStudentData() {
	try {
	    DataManager dataManager = createDefaultManager();
	    // Check if good record is returned
	    Assert.assertNotEquals("Empty?", null,
		    dataManager.getStudentData("nguy0621"));

	    // Check if null is returned
	    Assert.assertNull(dataManager.getStudentData("not an id"));

	    // There should be nothing in a null data manager
	    DataManager nullDataManager = new DataManager();
	    Assert.assertEquals("Not Empty?", null,
		    nullDataManager.getStudentData("nguy0621"));

	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Exception! ");
	}
    }

    /**
     * Tests the retrieval of a user object by the user's ID
     */
    @Test
    public void testGetUserByID() {
	try {
	    DataManager dataManager = createDefaultManager();
	    // Check if good record is returned for a student
	    Assert.assertNotEquals("Empty?", null,
		    dataManager.getUserByID("nguy0621"));

	    // Check if good record is returned for a GPC
	    Assert.assertNotEquals("Empty?", null,
		    dataManager.getUserByID("tolas9999"));

	    // Check if null is returned
	    Assert.assertNull(dataManager.getStudentData("not an id"));

	    // There should be nothing in a null data manager
	    DataManager nullDataManager = new DataManager();
	    Assert.assertEquals("Not Empty?", null,
		    nullDataManager.getUserByID("nguy0621"));

	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Exception! ");
	}
    }

    /**
     * Tests the retrieval of the data for each available course
     */
    @Test
    public void testGetCoursesData() {
	try {
	    DataManager dataManager = createDefaultManager();
	    // Check if courses are returned
	    Assert.assertNotEquals("Empty?", null, dataManager.getCoursesData());

	    // There should be nothing in a null data manager
	    DataManager nullDataManager = new DataManager();
	    Assert.assertEquals("Not Empty?", null,
		    nullDataManager.getCoursesData());

	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Exception! ");
	}
    }

    /**
     * Tests whether data can be persisted to the database
     */
    @Test
    public void testStoreTranscript() {
	try {
	    // Check if write transcript is consistent
	    // Old records
	    DataManager dataManager = createDefaultManager();
	    ArrayList<StudentRecord> originalRecords = dataManager
		    .getStudentRecords();
	    dataManager
		    .setStudentRecordFileName("src/resources/studentsTestTMP.txt");
	    dataManager.writeTranscript();

	    // New records
	    DataManager newDataManager = createDefaultManager();
	    newDataManager
		    .setStudentRecordFileName("src/resources/studentsTestTMP.txt");
	    ArrayList<StudentRecord> newRecords = newDataManager
		    .getStudentRecords();

	    // Compare
	    Assert.assertEquals(originalRecords, newRecords);
	    // Check adding a new student
	    DataManager stuDataManager = createDefaultManager();
	    stuDataManager
		    .setStudentRecordFileName("src/resources/studentsTestTMP.txt");

	    StudentRecord stu = new StudentRecord();
	    stu.setStudent(new Student("Stu", "Student", "stu1337"));
	    String stuId = stu.getStudent().getId();
	    stuDataManager.storeTranscript(stuId, stu);
	    ArrayList<String> hasStu = stuDataManager
		    .getStudentIDList(Department.MATH);

	    Assert.assertTrue("Stu is not here!", hasStu.contains(stuId));

	    // Modifying a student
	    stu.setDepartment(Department.MATH);
	    stuDataManager.storeTranscript(stuId, stu);
	    ArrayList<String> newStu = stuDataManager
		    .getStudentIDList(Department.MATH);

	    Assert.assertTrue("Array lengths not the same, new stu added!",
		    hasStu.size() == newStu.size());
	    Assert.assertTrue("Stu should be a math student", stuDataManager
		    .getStudentData(stuId).getDepartment() == Department.MATH);

	    // Revert state
	    Path tmpPath = Paths.get("src/resources/studentsTestTMP.txt");
	    Files.deleteIfExists(tmpPath);

	    // A null data manager should not write and not have errors
	    DataManager nullDataManager = new DataManager();
	    Assert.assertTrue("How did this write?",
		    !nullDataManager.storeTranscript("stu1337", stu));

	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Exception! ");
	}
    }

}
