package testing;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import edu.umn.csci5801.DataManager;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.StudentRecord;

import org.junit.Assert;
import org.junit.Test;

public class DataManagerTests {

    private String courseFile;
    private String studentsFile;
    private String progressFile;
    private String usersFile;

    public DataManagerTests(){
        super();
        this.courseFile = "data/courses.txt";
        this.studentsFile = "src/resources/studentsTest.txt";	//Uses test file
        this.progressFile = "data/progress.txt";
        this.usersFile = "data/users.txt";
    }

    public DataManager createDefaultManager(){
        return new DataManager(courseFile, studentsFile, progressFile, usersFile);
    }
    @Test
    public void testCreation() {
        try{
            assertNotNull(new DataManager());
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception! ");
        }
    }
    @Test
    public void testGetCourses(){
        try{
            DataManager dataManager = createDefaultManager();
            dataManager.setDebug(true);
            assertNotNull(dataManager.getCourses());
            
            DataManager nullDataManager = new DataManager();
            assertNull(nullDataManager.getCourses());
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception! ");
        }
    }
    @Test
    public void testGetStudentRecords(){
        try{
            DataManager dataManager = createDefaultManager();
            dataManager.setDebug(true);
            assertNotNull(dataManager.getStudentRecords());
            
            DataManager nullDataManager = new DataManager();
            assertNull(nullDataManager.getStudentRecords());
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception! ");
        }
    }
    @Test
    public void testGetProgressSummaries(){
        try{
            DataManager dataManager = createDefaultManager();
            dataManager.setDebug(true);
            assertNotNull(dataManager.getProgressSummaries());
            
            DataManager nullDataManager = new DataManager();
            assertNull(nullDataManager.getProgressSummaries());
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception! ");
        }
    }
    @Test
    public void testGetUsers(){
        try{
            DataManager dataManager = createDefaultManager();
            dataManager.setDebug(true);
            assertNotNull(dataManager.getUsers());
            
            DataManager nullDataManager = new DataManager();
            assertNull(nullDataManager.getUsers());
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception! ");
        }
    }
    @Test
    public void testInit(){
        try{
            DataManager dataManager = createDefaultManager();
            dataManager.init();
            
            DataManager nullDataManager = new DataManager();
            nullDataManager.init();
            
            
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception! ");
        }
    }
      @Test
    public void testGetStudentIDList(){
        try{
            DataManager dataManager = createDefaultManager();
            Assert.assertNotEquals("Empty?",new ArrayList<String>(), dataManager.getStudentIDList(Department.COMPUTER_SCIENCE));
            
            DataManager nullDataManager = new DataManager();
            Assert.assertEquals("Not Empty?", new ArrayList<String>(), nullDataManager.getStudentIDList(Department.COMPUTER_SCIENCE));
            
            
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception! ");
        }
    }
    @Test
    public void testGetStudentData(){
        try{
        	DataManager dataManager = createDefaultManager();
        	//Check if good record is returned
            Assert.assertNotEquals("Empty?", null, dataManager.getStudentData("nguy0621"));
            
            //Check if null is returned
            Assert.assertNull(dataManager.getStudentData("not an id"));
            
            //There should be nothing in a null data manager
            DataManager nullDataManager = new DataManager();
            Assert.assertEquals("Not Empty?", null, nullDataManager.getStudentData("nguy0621"));
            
            
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception! ");
        }
    }
    @Test
    public void testGetUserByID(){
        try{
        	DataManager dataManager = createDefaultManager();
        	//Check if good record is returned for a student
            Assert.assertNotEquals("Empty?", null, dataManager.getUserByID("nguy0621"));
            
          //Check if good record is returned for a GPC
            Assert.assertNotEquals("Empty?", null, dataManager.getUserByID("tolas9999"));
            
            //Check if null is returned
            Assert.assertNull(dataManager.getStudentData("not an id"));
            
            //There should be nothing in a null data manager
            DataManager nullDataManager = new DataManager();
            Assert.assertEquals("Not Empty?", null, nullDataManager.getUserByID("nguy0621"));
            
            
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception! ");
        }
    }
    @Test
    public void testGetCoursesData(){
        try{
        	DataManager dataManager = createDefaultManager();
        	//Check if courses are returned
            Assert.assertNotEquals("Empty?", null, dataManager.getCoursesData());
            
            //There should be nothing in a null data manager
            DataManager nullDataManager = new DataManager();
            Assert.assertEquals("Not Empty?", null, nullDataManager.getCoursesData());
            
            
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception! ");
        }
    }
    @Test
    public void testStoreTranscript(){
        try{
        	DataManager dataManager = createDefaultManager();
        	ArrayList<StudentRecord> originalRecords= dataManager.getStudentRecords();
        	dataManager.setStudentRecordFileName("src/resources/studentsTestTMP.txt");
        	dataManager.writeTranscript();
        	
        	//Check if it is consistent
        	DataManager newDataManager = createDefaultManager();
        	newDataManager.setStudentRecordFileName("src/resources/studentsTestTMP.txt");
        	ArrayList<StudentRecord> newRecords= newDataManager.getStudentRecords();
        	
        	Assert.assertEquals(originalRecords, newRecords);
        	
        	Path tmpPath = Paths.get("src/resources/studentsTestTMP.txt");
        	Files.deleteIfExists(tmpPath);
            
            //There should be nothing in a null data manager
            //DataManager nullDataManager = new DataManager();
            //Assert.assertEquals("Not Empty?", null, nullDataManager.getCoursesData());
            
            
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception! ");
        }
    }
    
}
