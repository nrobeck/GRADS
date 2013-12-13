package testing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;
import edu.umn.csci5801.DataManager;
import edu.umn.csci5801.DataValidator;
import edu.umn.csci5801.TranscriptHandler;
import edu.umn.csci5801.model.Student;
import edu.umn.csci5801.model.StudentRecord;
import exceptions.InvalidTranscriptException;
import exceptions.TranscriptNotPersistedException;

//tests for requirement 7
public class DataValidationTests {
    public DataManager createDefaultManager() {
	return new DataManager("data/courses.txt",
		"src/resources/studentsTest.txt", "data/progress.txt",
		"data/users.txt");
    }

    @Test
    public void testStudentIsValid() {
	DataManager testManager = createDefaultManager();
	DataValidator testValidator = new DataValidator(null, testManager);

	// Students with null fields, invalid
	StudentRecord testStudent = new StudentRecord();
	testStudent.setStudent(new Student("first", "last", null));
	testValidator.setTranscript(testStudent);

	Assert.assertFalse(testValidator.studentIsValid());

	testStudent.setStudent(new Student("first", null, "nguy0621"));
	testValidator.setTranscript(testStudent);

	Assert.assertFalse(testValidator.studentIsValid());

	testStudent.setStudent(new Student(null, "last", "nguy0621"));
	testValidator.setTranscript(testStudent);

	Assert.assertFalse(testValidator.studentIsValid());

	// Existing Student, should not be valid
	testStudent = testManager.getStudentData("nguy0621");
	testValidator.setTranscript(testStudent);

	Assert.assertTrue(testValidator.studentIsValid());
    }

    @Test
    public void testValidRecord() {
	DataManager testManager = createDefaultManager();
	DataValidator testValidator = new DataValidator(null, testManager);
	TranscriptHandler testHandler = new TranscriptHandler(testManager);
	try {
	    testHandler.updateTranscript("nguy0621",
		    testManager.getStudentData("nguy0621"));
	} catch (InvalidTranscriptException i) {
	    Assert.fail("He should have a valid transcript!");
	} catch (TranscriptNotPersistedException t) {
	    Assert.fail("He should have been able to be persisted!");
	}

	// Make it invalid
	testManager
		.setStudentRecordFileName("src/resources/studentsTestTMP.txt");
	try {
	    testHandler.updateTranscript("nguy0621", new StudentRecord());
	    Assert.fail("Should have thrown an exception");
	} catch (InvalidTranscriptException i) {

	} catch (TranscriptNotPersistedException t) {
	    Assert.fail("He should have been able to be persisted!");
	}

	// Revert state
	Path tmpPath = Paths.get("src/resources/studentsTestTMP.txt");
	try {
	    Files.deleteIfExists(tmpPath);
	} catch (IOException i) {

	}

    }
}
