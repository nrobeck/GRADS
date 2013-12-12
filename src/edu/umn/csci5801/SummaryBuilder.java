package edu.umn.csci5801;
// SummaryBuilder.java
//

import edu.umn.csci5801.model.CheckResultDetails;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Degree;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.Milestone;
import edu.umn.csci5801.model.MilestoneSet;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.StudentRecord;

import java.lang.String;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.Detail;

import com.google.gson.JsonObject;

/** TODO: copyright */

/**
 *
 * Module of GRADS used to create and simulate student summaries.
 * @author Kyle
 *
 */
public class SummaryBuilder {
    private DataManager dbManager;
    private TranscriptHandler transcriptHandler;

    /**
     * Constructor method for the Summary Builder
     */
    public SummaryBuilder(DataManager d, TranscriptHandler t) {
    	this.dbManager = d;
    	this.transcriptHandler = t;
    }

    /**
     * Creates a graduate progress summary based on the student's transcript and courses to simulate.
     * @param studentID The id of the student
     * @param simCourses A list of courses to simulate
     * @return A completed summary of a student's progress towards his/her graduate degree
     */
    public ProgressSummary createStudentSummary(String studentID, List<CourseTaken> simCourses) {
        //initialize data objects
        ProgressSummary summary = new ProgressSummary();
        //get the student record from transcriptHandler
        StudentRecord record = transcriptHandler.getTranscript(studentID);
        JsonObject plan = new JsonObject();

        //set data for the summary
        summary.setStudent(record.getStudent());
        summary.setDepartment(record.getDepartment());
        summary.setDegreeSought(record.getDegreeSought());
        summary.setTermBegan(record.getTermBegan());
        summary.setAdvisors(record.getAdvisors());
        summary.setCommittee(record.getCommittee());
        summary.setNotes(record.getNotes());

        //get the courses the student has taken from their record
        List<CourseTaken> courses = record.getCoursesTaken();
        
        //check the requirements and set results
        summary.setRequirementCheckResults(checkPlanRequirements(plan, courses, record.getMilestonesSet(), record.getDegreeSought()));

        return summary;
    }

    /**
     * Creates a list of RequirementCheckResults for the summary.
     * @param plan - the plan that will be checked
     * @param courses - the courses that the student has taken
     * @param milestones - a list of milestones of the student
     * @return a list of Requirement Check Results based on the plan/student progress
     */
    private List<RequirementCheckResult> checkPlanRequirements(JsonObject plan, List<CourseTaken> courses, List<MilestoneSet> milestones, Degree degree) {
        //create the needed elements
        List<RequirementCheckResult> retVal = new ArrayList<RequirementCheckResult>();
        RequirementCheckResult result;
        CheckResultDetails details;
        List<CourseTaken> tempCourseList;
        // TODO: determine if I should break these out into individual methods
        // probably should
        
        // check overall and in-course GPAs requirements
        double minGPA;
        
        if(degree == Degree.PHD) {
        	minGPA = 3.45;
        	result = new RequirementCheckResult("OVERALL_GPA_PHD");
        }
        
        else {
        	minGPA = 3.25;
        	result = new RequirementCheckResult("OVERALL_GPA_MS");
        }
        
        // determine overall GPA
        double gpa = 0;
        double totalSum = 0;
        double incourseSum = 0;
        double credits = 0;
        tempCourseList = new ArrayList<CourseTaken>();

        // copy the course list
        for (CourseTaken c: courses){
        	tempCourseList.add(c);
        }
        // trim it
        tempCourseList = removeDuplicateCourses(tempCourseList);
        
        // look through all courses
        for (CourseTaken c: tempCourseList){
        	// only check A-F courses
        	if(c.getGrade().ordinal() <= Grade.F.ordinal()){
        		totalSum += c.getGrade().numericValue();
        		credits += Integer.parseInt(c.getCourse().getNumCredits());
        		if(isGraduteLevel(c.getCourse(), "csci")){
        			incourseSum += c.getGrade().numericValue();
        		}
        	}
        }
        
        // compare GPA with minimum
        gpa = totalSum/credits;
        if (gpa < minGPA){
        	result.setPassed(false);
        	result.addErrorMsg("Overall GPA is bellow the minimum");
        }
        
        // create a new result for in course gpa
        if(degree == Degree.PHD) {
        	result = new RequirementCheckResult("INCOURSE_GPA_PHD");
        }
        else {
        	result = new RequirementCheckResult("IN_PROGRAM_GPA_MS");
        }
        
        // compare GPA with minimum
        gpa = incourseSum/credits;
        if (gpa < minGPA){
        	result.setPassed(false);
        	result.addErrorMsg("In course  GPA is bellow the minimum");
        }
        
        // add to result list
        retVal.add(result);
        
        /*============================================================================*/
        /*----------------------------------------------------------------------------*/
        
        // credit requirements
        
        result = new RequirementCheckResult("TOTAL_CREDITS", false);
        tempCourseList = new ArrayList<CourseTaken>();
        details = new CheckResultDetails();
        double csciCredits = 0;
        double totalCredits = 0;
        
        // Total credits:

        // check each taken course
        for (CourseTaken c: courses){
        	// avoid thesis courses
        	if (!(c.getCourse().getId().equals("csci8888") || c.getCourse().getId().equals("csci8777"))){
        		// make sure that the course was passed
        		if (passedCourse(c, false)) {
        			// run tests on csci courses
        			if(c.getCourse().getId().contains("csci")){
        				// add it only if at graduate level
        				if(isGraduteLevel(c.getCourse(), "csci")){
        					tempCourseList.add(c);
        					csciCredits += Integer.parseInt(c.getCourse().getNumCredits());
        					totalCredits += Integer.parseInt(c.getCourse().getNumCredits());
        				}
        			}
        			// Any C or higher non-csci course counts for total credits
        			else {
        				tempCourseList.add(c);
        				totalCredits += Integer.parseInt(c.getCourse().getNumCredits());
        			}
        		}
        	}
        }
        
        // check that we have enough credits
        if(totalCredits >= 31){
        	
        }
        else {
        	result.setPassed(false);
        	result.addErrorMsg("Not enough credits.  Need 31.");
        }
        // MS A does not need to check csci credits here
        if(degree == Degree.MS_A) {
        	// do nothing
        }
        else if (csciCredits >= 16){
        	
        }
        else {
        	result.setPassed(false);
        	result.addErrorMsg("Not enough csci 5000+ credits.  Need 16.");
        }
        
        details.setCourses(tempCourseList);
        result.setDetails(details);
        
        retVal.add(result);
        /*----------------------------------------------------------------------------*/
        
        // out of department: PHD
      
        if(degree == Degree.PHD){
        	double tempCredits = 0;
        	
	        // check each taken course
	        for (CourseTaken c: courses){
	        	// find non csci courses
	        	if (!(c.getCourse().getId().contains("csci"))){
	        		// course must be passed C or better
	        		if (passedCourse(c, false)) {
        				// must be gradute level
        				if(isGraduteLevel(c.getCourse(), "other")){
        					tempCourseList.add(c);
        					tempCredits += Integer.parseInt(c.getCourse().getNumCredits());
        				}
	        		}
	        	}
	        }
	        
	        // check that we have enough credits
	        if(tempCredits >= 6){
	        	
	        }
	        else {
	        	result.setPassed(false);
	        	result.addErrorMsg("Not enough out of department credits.  Need 6.");
	        }
	        
	        details.setCourses(tempCourseList);
	        result.setDetails(details);
	        
	        retVal.add(result);
        }
        

        /*----------------------------------------------------------------------------*/
        
        // Course Credits: BS A 
        // TODO: Learn what course credits means
        if(degree == Degree.MS_A) {
        	result = new RequirementCheckResult("COURSE_CREDITS", false);
            tempCourseList = new ArrayList<CourseTaken>();
            details = new CheckResultDetails();
            csciCredits = 0;
            double courseCredits = 0;
            
        	// check each taken course
            for (CourseTaken c: courses){
            	// avoid thesis courses
            	if (!(c.getCourse().getId().equals("csci8888") || c.getCourse().getId().equals("csci8777"))){
            		// make sure that the course was passed
            		if (passedCourse(c, false)) {
            			// run tests on csci courses
            			if(c.getCourse().getId().contains("csci")){
            				// add it only if at graduate level
            				if(isGraduteLevel(c.getCourse(), "csci")){
            					tempCourseList.add(c);
            					csciCredits += Integer.parseInt(c.getCourse().getNumCredits());
            					courseCredits += Integer.parseInt(c.getCourse().getNumCredits());
            				}
            			}
            			// Any C or higher non-csci course counts for total credits
            			else {
            				tempCourseList.add(c);
            				courseCredits += Integer.parseInt(c.getCourse().getNumCredits());
            			}
            		}
            	}
            }
            
            // check that we have enough credits
            if(courseCredits >= 22){
            	
            }
            else {
            	result.setPassed(false);
            	result.addErrorMsg("Not enough course credits.  Need 22.");
            }
            if (csciCredits >= 16){
            }
            else {
            	result.setPassed(false);
            	result.addErrorMsg("Not enough csci 5000+ credits.  Need 16.");
            }
            
            details.setCourses(tempCourseList);
            result.setDetails(details);
            
            retVal.add(result);
        }
        
        /*----------------------------------------------------------------------------*/
        
        // check for various specific requirements
        
        // colloqium
        result = new RequirementCheckResult("COLLOQUIUM", false);
        tempCourseList = new ArrayList<CourseTaken>();
        details = new CheckResultDetails();
        
        for (CourseTaken c: courses){
        	if (c.getCourse().getId().equals("csci8970")){
        		if (passedCourse(c, true)){
        			tempCourseList.add(c);
        			result.setPassed(true);
        		}
        	}
        }
        if(!result.isPassed()){
        	result.addErrorMsg("Course csci8970, Computer Science Colloquium, was not taken/passed.");
        }
        
        details.setCourses(tempCourseList);
        result.setDetails(details);
        retVal.add(result);
        
        // Thesis_phd credits
        result = new RequirementCheckResult("THESIS_PHD", false);
        tempCourseList = new ArrayList<CourseTaken>();
        details = new CheckResultDetails();
        int tempSum = 0;
        
        // look for the required course
        for (CourseTaken c: courses){
        	if (c.getCourse().getId().equals("csci8888")){
        		if (passedCourse(c, true)){
        			tempCourseList.add(c);
        			tempSum += Integer.getInteger(c.getCourse().getNumCredits());	// add number of credits to total
        		}
        	}
        }
        
        // check if enough thesis credits have been taken
        if(tempSum >= 24){
        	result.setPassed(true);
        }
        else {
        	result.addErrorMsg("Less than 24 credits have been taken from csci8888, Thesis Credits: Doctorial.");
        }
        
        details.setCourses(tempCourseList);
        result.setDetails(details);
        retVal.add(result);
        
        // Thesis_MS_A credits
        result = new RequirementCheckResult("THESIS_MS", false);
        tempCourseList = new ArrayList<CourseTaken>();
        details = new CheckResultDetails();
        tempSum = 0;
        
        // look for the required course
        for (CourseTaken c: courses){
        	if (c.getCourse().getId().equals("csci8777")){
        		if (passedCourse(c, true)){
        			tempCourseList.add(c);
        			tempSum += Integer.getInteger(c.getCourse().getNumCredits());	// add number of credits to total
        		}
        	}
        }
        
        // check if enough thesis credits have been taken
        if(tempSum >= 10){
        	result.setPassed(true);
        }
        else {
        	result.addErrorMsg("Less than 10 credits have been taken from csci8888, Thesis Credits: Masters.");
        }
        
        // MS B project
        
    	if(degree == Degree.MS_B){
            result = new RequirementCheckResult("PLAN_B_PROJECT");
            tempCourseList = new ArrayList<CourseTaken>();
            details = new CheckResultDetails();
            
            for (CourseTaken c: courses){
            	if (c.getCourse().getId().equals("csci8760")){
            		if (passedCourse(c, true)){
            			tempCourseList.add(c);
            			result.setPassed(true);
            		}
            	}
            }
            // set the error message
            if(!result.isPassed()){
            	result.addErrorMsg("Course csci8760, Plan B Project, has not taken/passed.");
            }
            
            details.setCourses(tempCourseList);
            result.setDetails(details);
            retVal.add(result);
    	}
    
        // phd level courses MS A and B
    	if(degree != Degree.PHD){
            result = new RequirementCheckResult("PHD_LEVEL_COURSES");
            tempCourseList = new ArrayList<CourseTaken>();
            details = new CheckResultDetails();
            
            for (CourseTaken c: courses){
            	if (c.getCourse().getId().contains("csci8")){
            		if (passedCourse(c, false)){
            			tempCourseList.add(c);
            			result.setPassed(true);
            		}
            	}
            }
            // set the error message
            if(!result.isPassed()){
            	result.addErrorMsg("A phd level course, csci 8000+, has not been taken/passed");
            }
            
            details.setCourses(tempCourseList);
            result.setDetails(details);
            retVal.add(result);
    	}
    	
    	// phd level courses MS C
    	if(degree != Degree.PHD){
            result = new RequirementCheckResult("PHD_LEVEL_COURSES_PLANC");
            tempCourseList = new ArrayList<CourseTaken>();
            details = new CheckResultDetails();
            
            for (CourseTaken c: courses){
            	if (c.getCourse().getId().contains("csci8")){
            		if (passedCourse(c, false)){
            			tempCourseList.add(c);
            		}
            	}
            }
            
            // check if at least 2 phd courses have been completed
            if(tempCourseList.size() >= 2){
            	result.setPassed(true);
            }
            else{
            	result.addErrorMsg("At least two phd level courses, csci 8000+, has not been taken/passed");
            }
            
            details.setCourses(tempCourseList);
            result.setDetails(details);
            retVal.add(result);
    	}
        
        /*----------------------------------------------------------------------------*/
        
        // Check For Breadth Courses
        tempCourseList = new ArrayList<CourseTaken>();
        details = new CheckResultDetails();
        int breadthCourseTotal;
        
        if(degree == Degree.PHD) {
        	result = new RequirementCheckResult("BREADTH_REQUIREMENT_PHD", false);
        	breadthCourseTotal = 5;
        }
        else {
        	result = new RequirementCheckResult("BREADTH_REQUIREMENT_MS", false);
        	breadthCourseTotal = 3;
        }
        
        // lists of all courses, sorted by area
        List<CourseTaken> appCoursesTaken = new ArrayList<CourseTaken>();
        List<CourseTaken> archCoursesTaken = new ArrayList<CourseTaken>();
        List<CourseTaken> theoryCoursesTaken = new ArrayList<CourseTaken>();
        
        List<String> appCourseList = new ArrayList<String>();
        List<String> archCourseList = new ArrayList<String>();
        List<String> theoryCourseList = new ArrayList<String>();
        
        List<Course> courseList = dbManager.getCourses();
        
        // get lists of all courses that statisfy breadth requirements
        for(Course c: courseList){
        	if(c.getCourseArea() != null){
	        	switch(c.getCourseArea()){
	        	case APPLICATIONS:
	        		appCourseList.add(c.getId());
	        		break;
	        	case ARCHITECTURE_SYSTEMS_SOFTWARE:
	        		archCourseList.add(c.getId());
	        		break;
	        	case THEORY_ALGORITHMS:
	        		theoryCourseList.add(c.getId());
	        		break;
	    		default:
	        	}
        	}
        }
        
        // check each course to see if they fall into an Area
        for(CourseTaken c: courses){
        	// only add courses that have been passed
        	if(passedCourse(c, false)){
        		// add to the appropriate area list
        		if(appCourseList.contains(c.getCourse().getId())){
        			appCoursesTaken.add(c);
        		}
        		else if (archCourseList.contains(c.getCourse().getId())){
        			archCoursesTaken.add(c);
        		}
        		else if (theoryCourseList.contains(c.getCourse().getId())){
        			theoryCoursesTaken.add(c);
        		}
        	}
        }
        
        // trim the coursesTaken
        appCoursesTaken = sortByGrade(removeDuplicateCourses(appCoursesTaken));
        archCoursesTaken = sortByGrade(removeDuplicateCourses(archCoursesTaken));
        theoryCoursesTaken = sortByGrade(removeDuplicateCourses(theoryCoursesTaken));
        
        if(appCoursesTaken.isEmpty()){
        	result.addErrorMsg("An Application course has not been completed witha C- or better");
        	result.setPassed(false);
        }
        else {
        	tempCourseList.add(appCoursesTaken.remove(0));
        }
        if(archCoursesTaken.isEmpty()){
        	result.addErrorMsg("An ARCHITECTURE, SYSTEMS, and SOFTWARE course has not been completed witha C- or better");
        	result.setPassed(false);
        }
        else {
        	tempCourseList.add(archCoursesTaken.remove(0));
        }
        if (theoryCoursesTaken.isEmpty()){
        	result.addErrorMsg("A Theory and Algorithms course has not been completed witha C- or better");
        	result.setPassed(false);
        }
        else {
        	tempCourseList.add(theoryCoursesTaken.remove(0));
        }
        // Check that two addition courses have been taken
        if(degree == Degree.PHD) {
        	// TODO: fill this in
        }
        else {
        	
        }
        
        // TODO: make sure GPA is done
        retVal.add(result);
        
        /*------------------------------------------------------------------------------*/
        
        // Determine which milestones have been passed
        Milestone[] planMilestones;
        
        // determine which degree milestones to use
        switch (degree){
        case PHD: 
        	planMilestones = new Milestone[]{		Milestone.PRELIM_COMMITTEE_APPOINTED, Milestone.WRITTEN_PE_SUBMITTED, Milestone.WRITTEN_PE_APPROVED, Milestone.ORAL_PE_PASSED, 
        											Milestone.DPF_SUBMITTED, Milestone.DPF_APPROVED, Milestone.THESIS_COMMITTEE_APPOINTED, Milestone.PROPOSAL_PASSED, Milestone.GRADUATION_PACKET_REQUESTED, 
        											Milestone.THESIS_SUBMITTED, Milestone.THESIS_APPROVED, Milestone.DEFENSE_PASSED};
        	break;
        case MS_A:
        	planMilestones = new Milestone[]{		Milestone.DPF_SUBMITTED, Milestone.DPF_APPROVED, Milestone.THESIS_COMMITTEE_APPOINTED, 
													Milestone.GRADUATION_PACKET_REQUESTED, Milestone.THESIS_SUBMITTED, Milestone.THESIS_APPROVED, Milestone.DEFENSE_PASSED};
        	break;
        case MS_B:
        	planMilestones = new Milestone[]{		Milestone.DPF_SUBMITTED, Milestone.DPF_APPROVED, Milestone.PROJECT_COMMITTEE_APPOINTED, 
													Milestone.GRADUATION_PACKET_REQUESTED, Milestone.DEFENSE_PASSED};
        	break;
        case MS_C:
        	planMilestones = new Milestone[]{		Milestone.DPF_SUBMITTED, Milestone.DPF_APPROVED, Milestone.TRACKING_FORM_APPROVED, Milestone.TRACKING_FORM_SUBMITTED, 
													Milestone.GRADUATION_PACKET_REQUESTED, Milestone.THESIS_SUBMITTED};
        	break;
        default: planMilestones = new Milestone[0];
        }
           
        // run through each milestone and check if the student have completed it
        for (Milestone s: planMilestones){
        	result = new RequirementCheckResult(s.name(), false);
            details = new CheckResultDetails();

            // check if a student has passed this milestone
        	for (MilestoneSet m: milestones){
        		if(s.equals(m.getMilestone())){
        			result.setPassed(true);
        		}
        	}
        	
        	// add result to the result list
        	retVal.add(result);
        }
        return retVal;
    }

    /**
     * Calculate the GPA of a list of courses taken.
     * @param courses - the list of courses to derive a GPA from
     * @return the GPA
     */
    private double calculateGPA(List<CourseTaken> courses) {
        //initialize parameters
        int credits = 0;
        int sum = 0;
        int temp;

        //loop through each course taken
        for (CourseTaken c: courses){
            // check if the course is not a S/N or a sim course
            if(c.getGrade().ordinal() <= Grade.F.ordinal() ){
                //get the credits the course was worth
                temp = Integer.parseInt(c.getCourse().getNumCredits());
                //add the credits to the running total
                credits += temp;
                //add the gpa integer value for the credits to the sum
                sum += temp * c.getGrade().numericValue();
            }
        }

        //divide sum of gpa totals by number for credits taken to get gpa and then return it
        return sum/credits;
    }
    
    /**
     * Determines if a course was passed
     * @param course - the course that a student has taken
     * @param passFail - tells if S grades are allowable
     * @return true if the course was passed, false if not
     */
    private boolean passedCourse(CourseTaken course, boolean passFail){
    	// check if C or greater
    	if (course.getGrade().numericValue() >= Grade.C.numericValue()){
    		return true;
    	}
    	
    	if (course.getGrade() == Grade._){
    		return true;
    	}
    	
    	// check if course was passed and passFail is OK
    	if (passFail){
    		return (course.getGrade() == Grade.S);
    	}
    	
    	
    	// course was not passed
    	return false;
    }
    
    private boolean isGraduteLevel(Course course, String department){
    	// check that the course Id contains the department string
    	if (course.getId().length() > 4 && (department.equals("other") || course.getId().contains(department))){
    		int level = Integer.parseInt(course.getId().substring(4, 5));
    		if (level >= 5){	// check that the level is 5000+
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Utility function that removes duplicate courses from a lists
     * @param coursesTaken - list of courses
     * @return a trimmed version of coursesTaken
     */
    private List<CourseTaken> removeDuplicateCourses(List<CourseTaken> coursesTaken){
    	List<CourseTaken> retVal = new ArrayList<CourseTaken>();
    	
    	for(CourseTaken c: coursesTaken){
    		boolean addOK = true;
    		for(CourseTaken d: retVal){
    			// if two course that are not exactly the same are found
    			if(c.equals(d)){
    				addOK = false;
    			}
    			else if((c.getCourse().getId().equals(d.getCourse().getId()))){
    				if(c.getGrade().numericValue() >= d.getGrade().numericValue()){
    					retVal.remove(d);	// remove the old best grade
    				}
    				else {
    					addOK = false;		// There already exists a better grade course
    				}
    			}
    		}
    		
    		// add the course if it is OK
    		if(addOK){
    			retVal.add(c);
    		}
    	}
    	
    	return retVal;
    }
    
    /**
     * Sorts a list of CourseTakens by grade (A, B, C, x/other)
     * @param courses
     * @return
     */
    private List<CourseTaken> sortByGrade(List<CourseTaken> courses){
    	List<CourseTaken> listA = new ArrayList<CourseTaken>();
    	List<CourseTaken> listB = new ArrayList<CourseTaken>();
    	List<CourseTaken> listC = new ArrayList<CourseTaken>();
    	List<CourseTaken> listX = new ArrayList<CourseTaken>();
    	
    	for(CourseTaken c: courses){
    		switch(c.getGrade()){
    		case A:
    			listA.add(c);
    			break;
    		case B:
    			listB.add(c);
    			break;
    		case C:
    			listC.add(c);
    			break;
    		default:
    			listX.add(c);
    		}
    	}
    	
    	// combine all lists and return them
    	listA.addAll(listB);
    	listA.addAll(listC);
    	listA.addAll(listX);
    	
    	return listA;
    }

    
    /**
     * Return the progress summary based solely upon a student's transcript and no simulated courses.
     * Calls createStudentSummary with null for simulated courses
     * @see createStudentSummary method used to perform all creation
     * @param userId user id of the student whose progress summary is to be generated.
     * @return progress summary for the student.
     */
    public ProgressSummary getStudentSummary(String userId) {
        //create the progress summary
        ProgressSummary progress = createStudentSummary(userId, null);
        //return the progress summary
        return progress;
    }
    
}
