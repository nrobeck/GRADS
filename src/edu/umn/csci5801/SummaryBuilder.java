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
        
        boolean simulate;
        if(simCourses == null){
        	simulate = false;
        }
        else{
        	simulate = true;
        }
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
        summary.setRequirementCheckResults(checkPlanRequirements(record.getDegreeSought(), courses, record.getMilestonesSet(), simulate));

        return summary;
    }

    /**
     * Creates a list of RequirementCheckResults for the summary.
     * @param plan - the plan that will be checked
     * @param courses - the courses that the student has taken
     * @param milestones - a list of milestones of the student
     * @return a list of Requirement Check Results based on the plan/student progress
     */
    private List<RequirementCheckResult> checkPlanRequirements(Degree degree, List<CourseTaken> courses, List<MilestoneSet> milestones, boolean simulate) {
        //create the needed elements
        List<RequirementCheckResult> retVal = new ArrayList<RequirementCheckResult>();

        // TODO: determine if I should break these out into individual methods
        // probably should
        
        // Check For Breadth Courses
        retVal.add(checkBreadthRequirements(courses, degree, simulate));
        
        // Thesis_phd credits
        if(degree == Degree.PHD || degree == Degree.MS_A){
        	retVal.add(checkThesisRequirements(courses, degree, simulate));
        }

        // Colloqium passed
        retVal.add(checkColloquium(courses, degree, simulate));
        
        // phd level courses MS plans
        if(degree != Degree.PHD){
        	retVal.add(checkPHDLevelCsciCourses(courses, degree, simulate));
        }
        
    	// PHD research intro courses
    	if (degree == Degree.PHD){
    		retVal.add(checkResearchIntro(courses, degree, simulate));
    	}
        
        // credit requirements
        retVal.addAll(checkCreditRequirements(courses, degree, simulate));
        
        // MS B project
    	if(degree == Degree.MS_B){
            retVal.add(checkPlanBProject(courses, degree, simulate));
    	}
        
        // check overall and in-course GPAs requirements
        retVal.addAll(checkGPARequirementCheckResults(courses, degree));
        
        
        // Check and add Milestones
        retVal.addAll(checkMilestones(milestones, degree));
        
        return retVal;
    }
    
    private RequirementCheckResult checkColloquium(List<CourseTaken> courses, Degree degree, boolean simulate) {
    	RequirementCheckResult result = new RequirementCheckResult("COLLOQUIUM", false);
        CheckResultDetails details = new CheckResultDetails();
        List<CourseTaken> tempCourseList = new ArrayList<CourseTaken>();
        
        for (CourseTaken c: courses){
        	if (c.getCourse().getId().equals("csci8970")){
        		if (passedCourse(c, true, simulate)){
        			tempCourseList.add(c);
        			result.setPassed(true);
        		}
        	}
        }
        
        // add an error message
        if(!result.isPassed()){
        	result.addErrorMsg("Course csci8970, Computer Science Colloquium, was not taken/passed.");
        }
        
        details.setCourses(tempCourseList);
        result.setDetails(details);
        
        return result;
    }
    /**
     * Checks if a student has passed the plan B project
     * @param courses
     * @param degree
     * @return a RequirementCheckResult
     */
    private RequirementCheckResult checkPlanBProject(List<CourseTaken> courses, Degree degree, boolean simulate){
    	RequirementCheckResult result = new RequirementCheckResult("INTRO_TO_RESEARCH");
        CheckResultDetails details = new CheckResultDetails();
        List<CourseTaken> tempCourseList = new ArrayList<CourseTaken>();
        
        // leave if not MS_B
        if(degree != Degree.MS_B){
        	return null;
        }
        
        // look through all courses for csci8760
        for (CourseTaken c: courses){
        	if (c.getCourse().getId().equals("csci8760")){
        		if (passedCourse(c, true, simulate)){
        			tempCourseList.add(c);
        			result.setPassed(true);
        		}
        	}
        }
        
        // set the error message
        if(!result.isPassed()){
        	result.addErrorMsg("Course csci8760, Plan B Project, has not taken/passed.");
        }

        // add it all up and return
        details.setCourses(tempCourseList);
        result.setDetails(details);
        return result;
    }
    
    /**
     * check that a PHD student has taken csci8001 and csci8002
     * @param courses
     * @param degree
     * @return
     */
    private RequirementCheckResult checkResearchIntro(List<CourseTaken> courses, Degree degree, boolean simulate){
    	RequirementCheckResult result = new RequirementCheckResult("INTRO_TO_RESEARCH");
        CheckResultDetails details = new CheckResultDetails();
        List<CourseTaken> tempCourseList = new ArrayList<CourseTaken>();
    	
        boolean passed8001 = false;
        boolean passed8002 = false;
        
    	if (degree != Degree.PHD){
    		return null;
    	}
    	
    	// look through all courses for csci8001 and csci 8002
        for (CourseTaken c: courses){
    		if (passedCourse(c, true, simulate)){
    			if(c.getCourse().getId().equals("csci8001")) {
    				passed8001 = true;
    				tempCourseList.add(c);
    			}
    			else if (c.getCourse().getId().equals("csci8002")){
    				passed8002 = true;
    				tempCourseList.add(c);
    			}
    		}
        }
        
        // check that both courses are passed
        if (passed8001) {
        }
        else {
        	result.setPassed(false);
        	result.addErrorMsg("Course csci8001 has not taken/passed.");
        }
        if (passed8002) {
        }
        else {
        	result.setPassed(false);
        	result.addErrorMsg("Course csci8002 has not taken/passed.");
        }

        
        details.setCourses(tempCourseList);
        result.setDetails(details);
    	
    	return result;
    }
    
    /**
     * 
     * @param courses
     * @param degree
     * @return
     */
    private List<RequirementCheckResult> checkGPARequirementCheckResults(List<CourseTaken> courses, Degree degree){
    	//create the needed elements
        List<RequirementCheckResult> retVal = new ArrayList<RequirementCheckResult>();
        RequirementCheckResult result;
        CheckResultDetails details = new CheckResultDetails();
        List<CourseTaken> tempCourseList;
        
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
        

        tempCourseList = new ArrayList<CourseTaken>();
        List<CourseTaken> afCourses = new ArrayList<CourseTaken>();
        List<CourseTaken> csciCourseList = new ArrayList<CourseTaken>();
        
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
        		afCourses.add(c);
        		// add to csci list if taken at grad level
        		if(isGraduteLevel(c.getCourse(), "csci")){
        			csciCourseList.add(c);
        		}
        	}
        }
        
        // compare GPA with minimum
     // compare GPA with minimum
        float gpa = calculateGPA(afCourses);
        if (gpa < minGPA){
        	result.setPassed(false);
        	result.addErrorMsg("Overall GPA is bellow the minimum");
        }
        
        // package up results and add it
        details.setGPA(gpa);
        result.setDetails(details);
        retVal.add(result);
        
        
        // create a new result and details for in course gpa
        if(degree == Degree.PHD) {
        	result = new RequirementCheckResult("INCOURSE_GPA_PHD");
        }
        else {
        	result = new RequirementCheckResult("IN_PROGRAM_GPA_MS");
        }
        details = new CheckResultDetails();
        
        // compare GPA with minimum
        gpa = calculateGPA(csciCourseList);
        
        if (gpa >= minGPA){
        }
        else {
        	result.setPassed(false);
        	result.addErrorMsg("In course GPA is bellow the minimum");
        }
        
        // package up results and add it
        details.setGPA(gpa);
        result.setDetails(details);
        retVal.add(result);
        
        return retVal;
    }
    
    /**
     * Checks requirements related to credits
     * @param courses
     * @param degree
     * @return
     */
    
    private List<RequirementCheckResult> checkCreditRequirements(List<CourseTaken> courses, Degree degree, boolean simulate){
    	List<RequirementCheckResult>retVal = new ArrayList<RequirementCheckResult>();
    	RequirementCheckResult result = new RequirementCheckResult("TOTAL_CREDITS");
        CheckResultDetails details = new CheckResultDetails();
        
        List<CourseTaken>tempCourseList = new ArrayList<CourseTaken>();
        double csciCredits = 0;
        double totalCredits = 0;
        
        // check each taken course
        for (CourseTaken c: courses){
        	// avoid thesis courses if on PHD plan
        	if (!(degree == Degree.PHD) && (c.getCourse().getId().equals("csci8888") || c.getCourse().getId().equals("csci8777"))){
        		// make sure that the course was passed
        		if (passedCourse(c, false, simulate)) {
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
            result = new RequirementCheckResult("OUT_OF_DEPARTMENT");
            tempCourseList = new ArrayList<CourseTaken>();
            details = new CheckResultDetails();
        	double tempCredits = 0;
        	
	        // check each taken course
	        for (CourseTaken c: courses){
	        	// find non csci courses
	        	if (!(c.getCourse().getId().contains("csci"))){
	        		// course must be passed C or better
	        		if (passedCourse(c, false, simulate)) {
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
        
        // Course Credits: MS A 
        // No thesis courses
        if(degree == Degree.MS_A) {
        	result = new RequirementCheckResult("COURSE_CREDITS");
            tempCourseList = new ArrayList<CourseTaken>();
            details = new CheckResultDetails();
            csciCredits = 0;
            double courseCredits = 0;
            
        	// check each taken course
            for (CourseTaken c: courses){
            	// avoid thesis courses
            	if (!(c.getCourse().getId().equals("csci8888") || c.getCourse().getId().equals("csci8777"))){
            		// make sure that the course was passed
            		if (passedCourse(c, false, simulate)) {
        				// check if course is a csci 5000+ course
        				if(isGraduteLevel(c.getCourse(), "csci")){
        					
        					csciCredits += Integer.parseInt(c.getCourse().getNumCredits());
        					
        				}
        					tempCourseList.add(c);
        					courseCredits += Integer.parseInt(c.getCourse().getNumCredits());
        			}
        			// Any C or higher non-csci course counts for total credits
        			else {
        				tempCourseList.add(c);
        				courseCredits += Integer.parseInt(c.getCourse().getNumCredits());
        			}
        		}
        	}
            
            // check that the student have enough credits
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
        
        return retVal;
    }
    
    /**
     * Check that a PHD or MSA student has taken enough thesis credits
     * @param courses
     * @param degree - the degree of the student
     * @return
     */
    private RequirementCheckResult checkThesisRequirements(List<CourseTaken> courses, Degree degree, boolean simulate){
    	RequirementCheckResult result;
    	List<CourseTaken>tempCourseList = new ArrayList<CourseTaken>();
        CheckResultDetails details = new CheckResultDetails();
        
        if (degree == Degree.PHD){
	        result = new RequirementCheckResult("THESIS_PHD");
	        tempCourseList = new ArrayList<CourseTaken>();
	        details = new CheckResultDetails();
	        int tempSum = 0;
	        
	        // look for the required course
	        for (CourseTaken c: courses){
	        	if (c.getCourse().getId().equals("csci8888")){
	        		if (passedCourse(c, true, simulate)){
	        			tempCourseList.add(c);
	        			tempSum += Integer.parseInt(c.getCourse().getNumCredits());	// add number of credits to total
	        		}
	        	}
	        }
	        
	        // check if enough thesis credits have been taken
	        if(tempSum >= 24){
	        }
	        else {
	        	result.setPassed(false);
	        	result.addErrorMsg("Less than 24 credits have been taken from csci8888, Thesis Credits: Doctorial.");
	        }
	        
	        details.setCourses(tempCourseList);
	        result.setDetails(details);
	        return result;
        }
        
        // Thesis_MS_A credits
        else if (degree == Degree.MS_A) {
	        result = new RequirementCheckResult("THESIS_MS");
	        tempCourseList = new ArrayList<CourseTaken>();
	        details = new CheckResultDetails();
	        int tempSum = 0;
	        
	        // look for the required course
	        for (CourseTaken c: courses){
	        	if (c.getCourse().getId().equals("csci8777")){
	        		if (passedCourse(c, true, simulate)){
	        			//if there are credits to check
	        			Integer temp = Integer.parseInt(c.getCourse().getNumCredits());
	        			if(temp != null){
		        			tempCourseList.add(c);
		        			tempSum += temp;	// add number of credits to total
	        			}
	        		}
	        	}
	        }
	        
	        // check if enough thesis credits have been taken
	        if(tempSum >= 10){
	        }
	        else {
	        	result.setPassed(false);
	        	result.addErrorMsg("Less than 10 credits have been taken from csci8888, Thesis Credits: Masters.");
	        }
	        
	        details.setCourses(tempCourseList);
	        result.setDetails(details);
	        return result;
        }
        else {
        	return null;
        }
    }
    
    /**
     * Check that MS students have passed PHD courses
     * @param courses
     * @param degree
     * @return
     */
    private RequirementCheckResult checkPHDLevelCsciCourses(List<CourseTaken> courses, Degree degree, boolean simulate){
    	RequirementCheckResult result;
    	List<CourseTaken>tempCourseList = new ArrayList<CourseTaken>();
        CheckResultDetails details = new CheckResultDetails();
        
        
    	// PHD student don't need this requirement
        // Set appropriate result name
    	if(degree == Degree.PHD){
    		return null;
    	}
    	else if(degree == Degree.MS_C){
    		result = new RequirementCheckResult("PHD_LEVEL_COURSES_PLANC");
    	}
    	else {
    		result = new RequirementCheckResult("PHD_LEVEL_COURSES");
    	}
    	
    	// find all PHD level courses
        for (CourseTaken c: courses){
        	if (c.getCourse().getId().contains("csci8")){
        		if (passedCourse(c, false, simulate)){
        			tempCourseList.add(c);
        		}
        	}
        }
        
        tempCourseList = removeDuplicateCourses(tempCourseList);
        
        // check if the student has taken enough PHD courses
        if (degree == Degree.MS_C) {
        	if(tempCourseList.size() >= 2) {
            }
            else {
            	result.setPassed(false);
            	result.addErrorMsg("At least two phd level courses, csci 8000+, has not been taken/passed");
            }
        }
        else {
        	if(tempCourseList.size() >= 1) {
        	}
        	else {
        		result.setPassed(false);
        		result.addErrorMsg("A phd level course, csci 8000+, has not been taken/passed");
        	}
        	
        }

        
        details.setCourses(tempCourseList);
        result.setDetails(details);
        return result;
    }
    
    /**
     * check the breadth requirement
     * @param courses
     * @param degree
     * @return
     */
    private RequirementCheckResult checkBreadthRequirements(List<CourseTaken> courses, Degree degree, boolean simulate){
    	RequirementCheckResult result;
    	CheckResultDetails details= new CheckResultDetails();;
    	List<CourseTaken>tempCourseList = new ArrayList<CourseTaken>();
        int breadthCourseTotal;
        float minGPA, gpa;
        
        // determine which version to use
        if(degree == Degree.PHD) {
        	result = new RequirementCheckResult("BREADTH_REQUIREMENT_PHD");
        	breadthCourseTotal = 5;
        	minGPA = (float) 3.45;
        }
        else {
        	result = new RequirementCheckResult("BREADTH_REQUIREMENT_MS");
        	breadthCourseTotal = 3;
        	minGPA = (float) 3.25;
        }
        
        // lists of all courses, sorted by area
        List<CourseTaken> appCoursesTaken = new ArrayList<CourseTaken>();
        List<CourseTaken> archCoursesTaken = new ArrayList<CourseTaken>();
        List<CourseTaken> theoryCoursesTaken = new ArrayList<CourseTaken>();
        
        List<String> appCourseList = new ArrayList<String>();
        List<String> archCourseList = new ArrayList<String>();
        List<String> theoryCourseList = new ArrayList<String>();
        
        List<Course> courseList = dbManager.getCourses();
        
        // get lists of all courses that satisfy breadth requirements
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
        	if(passedCourse(c, false, simulate)){
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
        
        // check that each area has at least course
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
        	// combine the rest of the courses into one list
        	appCoursesTaken.addAll(archCoursesTaken);
        	appCoursesTaken.addAll(theoryCoursesTaken);
        	// sort the list
        	appCoursesTaken = sortByGrade(appCoursesTaken);
        	// check if two more courses have been taken
        	if(appCoursesTaken.size() >= 2){
        		// add the two best courses to the list
        		tempCourseList.add(appCoursesTaken.remove(0));
        		tempCourseList.add(appCoursesTaken.remove(0));
        	}
        	else {
        		// the possible one course that could have been taken
        		tempCourseList.addAll(appCoursesTaken);
        		result.setPassed(false);
        		result.addErrorMsg("Less than five breadth courses have been taken");
        	}
        }

        // check the GPA
    	gpa = calculateGPA(tempCourseList);
    	if(gpa >= minGPA){
    	}
    	else {
    		result.setPassed(false);
    		result.addErrorMsg("Breadth GPA is below the required minimum: " + minGPA);
    	}
        
        // TODO: make sure GPA is done right
        details.setGPA(gpa);
        result.setDetails(details);
    	return result;
    }
    
    /**
     * Creates a list of RequirmentCheckResults based on what milestones have been completed
     * @param milestones
     * @param degree
     * @return
     */
    private List<RequirementCheckResult> checkMilestones(List<MilestoneSet> milestones, Degree degree){
    	List<RequirementCheckResult> retVal = new ArrayList<RequirementCheckResult>();
    	RequirementCheckResult result;
    	CheckResultDetails details;
    	
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
        	
        	if(result.isPassed()){
        		
        	}
        	else {
        		result.addErrorMsg("The milestone has not been completed.");
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
    private float calculateGPA(List<CourseTaken> courses) {
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

        // avoid dvi 0 errors
        if(credits == 0){
        	return 0;
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
    private boolean passedCourse(CourseTaken course, boolean passFail, boolean simulate){
    	// check if C or greater
    	if (course.getGrade().numericValue() >= Grade.C.numericValue()){
    		return true;
    	}
    	
    	if ((simulate) && (course.getGrade() == Grade._)){
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
    	
    	// sort into grade categories
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
