package testing;

import static org.junit.Assert.*;
import edu.umn.csci5801.DataManager;
import org.junit.Test;

public class DataManagerTests {
	
	private String courseFile = "data/courses.txt";
	private String studentsFile = "data/students.txt";
	private String progressFile = "data/progress.txt";
	private String usersFile = "data/users.txt";
	private String plansFile = "data/plans1.txt";
	
	public DataManagerTests(){
		super();
		this.courseFile = "data/courses.txt";
		this.studentsFile = "data/students.txt";
		this.progressFile = "data/progress.txt";
		this.usersFile = "data/users.txt";
		this.plansFile = "data/plans1.txt";
	}
	
	public DataManager createDefaultManager(){
		return new DataManager(courseFile, studentsFile, progressFile, usersFile, plansFile);
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
			assertNotNull(dataManager.getCourses());
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
			assertNotNull(dataManager.getStudentRecords());
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
			assertNotNull(dataManager.getProgressSummaries());
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
			assertNotNull(dataManager.getUsers());
		}
		catch(Exception e){
			e.printStackTrace();
			fail("Exception! ");
		}
	}
	@Test
	public void testGetPlans(){
		try{
			DataManager dataManager = createDefaultManager();
			assertNotNull(dataManager.getPlans());
		}
		catch(Exception e){
			e.printStackTrace();
			fail("Exception! ");
		}
	}

}
