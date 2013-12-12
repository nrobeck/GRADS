package testing;

import static org.junit.Assert.*;
import edu.umn.csci5801.DataManager;
import org.junit.Test;

public class DataManagerTests {

	@Test
	public void test() {
		try{
			String courseFile = "data/courses.txt";
			String studentsFile = "data/students.txt";
			String progressFile = "data/progress.txt";
			String usersFile = "data/users.txt";
			String plansFile = "data/plans1.txt";
			
			DataManager dataManager = new DataManager(courseFile, studentsFile, progressFile, usersFile, plansFile);
			dataManager.init();
		}
		catch(Exception e){
			e.printStackTrace();
			fail("Exception! ");
		}
	}

}
