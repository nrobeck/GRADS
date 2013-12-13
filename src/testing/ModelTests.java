package testing;

import java.util.Arrays;

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

import edu.umn.csci5801.DataManager;
import edu.umn.csci5801.model.CheckResultDetails;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseArea;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Department;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.Milestone;
import edu.umn.csci5801.model.MilestoneSet;
import edu.umn.csci5801.model.Person;
import edu.umn.csci5801.model.Professor;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.Semester;
import edu.umn.csci5801.model.Student;
import edu.umn.csci5801.model.StudentRecord;
import edu.umn.csci5801.model.Term;

public class ModelTests{

	@Test
	public void testCheckResultDetails(){
		DataManager my_dm = new DataManager(null,"src/resources/studentsTest.txt", null, null);
		CheckResultDetails A_CheckResultDetails = new CheckResultDetails();
		A_CheckResultDetails.setGPA((float)3.0);
		A_CheckResultDetails.setCourses(my_dm.getStudentData("nguy0621").getCoursesTaken()) ;
		A_CheckResultDetails.setOther(Arrays.asList("data1","data2"));
		
		CheckResultDetails B_CheckResultDetails = new CheckResultDetails();
		
		CheckResultDetails C_CheckResultDetails = new CheckResultDetails();
		C_CheckResultDetails.setGPA((float)3.0);
		
		Assert.assertTrue(!A_CheckResultDetails.equals(B_CheckResultDetails));
		Assert.assertTrue(!B_CheckResultDetails.equals(A_CheckResultDetails));
		Assert.assertTrue(!A_CheckResultDetails.equals(C_CheckResultDetails));
		
		int a_hash = A_CheckResultDetails.hashCode();
		int b_hash = B_CheckResultDetails.hashCode();
	}
	
	@Test
	public void testCourse(){
		Course A_Course = new Course("Computerology", "101", "3", CourseArea.THEORY_ALGORITHMS);
		Course B_Course = new Course();
		B_Course.setName("Computernomics");
		B_Course.setId("101");
		B_Course.setNumCredits("3");
		B_Course.setCourseArea(CourseArea.THEORY_ALGORITHMS);
		Course C_Course = new Course();
		
		Assert.assertTrue(!A_Course.equals(B_Course));
		
		int a_hash = A_Course.hashCode();
		int b_hash = C_Course.hashCode();
	}
	
	@Test
	public void testCourseArea(){

	}
	
	@Test
	public void testCourseTaken(){
		Course sample_course = new Course("Computers", "101", "3", CourseArea.THEORY_ALGORITHMS);
		Term sample_term = new Term(Semester.SUMMER, 2014);
		
		CourseTaken A_CourseTaken = new CourseTaken(sample_course, sample_term, Grade.A);
		CourseTaken B_CourseTaken = new CourseTaken(sample_course, sample_term, Grade.B);
		CourseTaken C_CourseTaken = new CourseTaken();
		
		Assert.assertTrue(A_CourseTaken.equals(A_CourseTaken));
		Assert.assertTrue(!A_CourseTaken.equals(B_CourseTaken));
		
		int a_hash = A_CourseTaken.hashCode();
		int b_hash = C_CourseTaken.hashCode();
	}
	
	
	@Test
	public void testMilestoneSet(){
		MilestoneSet A_MilestoneSet = new MilestoneSet();
		A_MilestoneSet.setMilestone(Milestone.WRITTEN_PE_SUBMITTED);
		A_MilestoneSet.setTerm(new Term(Semester.SPRING, 2014));
		MilestoneSet B_MilestoneSet = new MilestoneSet(Milestone.PRELIM_COMMITTEE_APPOINTED, new Term(Semester.SPRING, 2014));
		
		Assert.assertTrue(!A_MilestoneSet.equals(B_MilestoneSet));
		int a_hash = A_MilestoneSet.hashCode();
		int b_hash = B_MilestoneSet.hashCode();
	
	}

	@Test
	public void testPerson(){
		Person A_Person = new Person();
		A_Person.setFirstName("Mark");
		A_Person.setLastName("Holmes");
		
		Person B_Person = new Person("Kyle", "Michaels");
		Person C_Person = new Person("Mark", "Wahlberg");
		Person D_Person = new Person();
		
		Assert.assertTrue(A_Person.equals(A_Person));
		Assert.assertTrue(!A_Person.equals(B_Person));
		Assert.assertTrue(!A_Person.equals(C_Person));
		
		int a_hash = A_Person.hashCode();
		int d_hash = D_Person.hashCode();
	}
	
	@Test
	public void testProfessor(){
		Professor A_Professor = new Professor("Severus","Snape");
		A_Professor.setDepartment(Department.COMPUTER_SCIENCE);
		
		Professor B_Professor = new Professor("Minerva","McGonagall",Department.MATH);
		
		Assert.assertTrue(A_Professor.equals(A_Professor));
		Assert.assertTrue(!A_Professor.equals(B_Professor));
		
		int a_hash = A_Professor.hashCode();
	}

	
	@Test
	public void testRequirementCheckResult(){
		
		RequirementCheckResult A_RequirementCheckResult = new RequirementCheckResult("A", true, Arrays.asList("err1","err2"));
		RequirementCheckResult B_RequirementCheckResult = new RequirementCheckResult("B", true, new CheckResultDetails());
	}
	
	@Test
	public void testSemester(){
		Assert.assertEquals(null,Semester.FALL.next());
		Assert.assertEquals(Semester.SUMMER, Semester.SPRING.next());
	}
	
	@Test
	public void testStudent(){
		Student A_Student = new Student("Jean", "Valjean", "24601");
		Student B_Student = new Student("Inspector", "Javert", "000");
		B_Student.setId("666");
		
		Assert.assertTrue(!B_Student.equals(A_Student));
		
		int a_hash = A_Student.hashCode(); 
		
	}
	
	@Test
	public void testStudentRecord(){
		DataManager my_dm = new DataManager(null,"src/resources/studentsTest.txt", null, null);
		StudentRecord A_StudentRecord = my_dm.getStudentData("nguy0621");
		StudentRecord B_StudentRecord = new StudentRecord();
		
		
		Assert.assertTrue(A_StudentRecord.equals(A_StudentRecord));
		int a_hash = A_StudentRecord.hashCode();
		int b_hash = B_StudentRecord.hashCode();
	}
	
	@Test
	public void testTerm(){
		Term A_Term = new Term();
		Term B_Term = new Term(Semester.SUMMER, 2014);
		Term C_Term = new Term(Semester.FALL, 2014);
		
		Assert.assertEquals(new Term(Semester.FALL, 2014),B_Term.getNext());
		Assert.assertEquals(new Term(Semester.SPRING, 2015), C_Term.getNext());
		Assert.assertEquals(new Term(Semester.FALL, 2015),B_Term.getNext(4));
		
		int a_hash = A_Term.hashCode();
		int b_hash = B_Term.hashCode();
		
	}
}
