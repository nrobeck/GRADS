package edu.umn.csci5801;
// SummaryBuilder.java
//

import edu.umn.csci5801.model.CheckResultDetails;
import edu.umn.csci5801.model.CourseTaken;
import edu.umn.csci5801.model.Grade;
import edu.umn.csci5801.model.MilestoneSet;
import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.RequirementCheckResult;
import edu.umn.csci5801.model.StudentRecord;


import java.lang.String;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.Detail;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/** TODO: copyright */

/**
 *
 * Module of GRADS used to create student summary.
 * @author Kyle
 *
 */
public class SummaryBuilder {
    DataManager dbManager;
    TranscriptHandler transcriptHandler;

    /**
     * Constructor method for the Summary Builder
     */
    public SummaryBuilder() {

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
        StudentRecord record = new StudentRecord();
        record = transcriptHandler.getTranscript(studentID);
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
        summary.setRequirementCheckResults(checkPlanRequirements(plan, courses, record.getMilestonesSet()));

        return summary;
    }

    /**
     * Creates a list of RequirementCheckResults for the summary.
     * @param plan - the plan that will be checked
     * @param courses - the courses that the student has taken
     * @param milestones - a list of milestones of the student
     * @return a list of Requirement Check Results based on the plan/student progress
     */
    private List<RequirementCheckResult> checkPlanRequirements(JsonObject plan, List<CourseTaken> courses, List<MilestoneSet> milestones) {
        //create the needed elements
        JsonElement reqs;
        List<RequirementCheckResult> retVal = new ArrayList<RequirementCheckResult>() {
        };

        /* TODO ?
        if (plan.get("requirements") == null || !plan.get("requirements").getClass().equals()){
            // throw a error, no requirements
            return null;
        }
        */

        //get the requirements for the plan
        reqs = plan.get("requirements");

        //collect the requirements as strings
        for( JsonElement e : reqs.getAsJsonArray()){
            JsonObject req = e.getAsJsonObject();
            String name = req.get("name").getAsString();
            RequirementCheckResult result = new RequirementCheckResult(name, false);
            CheckResultDetails details = new CheckResultDetails();

            // check a milestone
            if(req.get("type").getAsString().equals("milestone")){
                // compare all completed milestone to the current one
                for(MilestoneSet m : milestones){
                    if(m.getMilestone().toString().equals(name)){
                        result.setPassed(true);
                    }
                }
            }

            // check overall gpa
            else if(req.get("type").getAsString().equals("overall_gpa")){
                double min = req.get("min_gpa").getAsDouble();
                double currentGPA = calculateGPA(courses);

                if (currentGPA >= min){
                    result.setPassed(true);
                }


            }

            // check in course gpa
            else if(req.get("type").getAsString().equals("incourse_gpa")){

            }

            // check if certain courses were passed
            else if(req.get("type").getAsString().equals("courses_passed")){
                boolean PassFail = false;
                List<String> c = new ArrayList<String>();
                if (req.getAsJsonObject("PassFail").getAsBoolean()) {
                    PassFail = true;
                }



            }

            // check credit type requirements
            else if(req.get("type").getAsString().equals("credits")){
                // find any other courses
                // TODO: fill in
            }

            // check Breadth requirements
            else if(req.get("type").getAsString().equals("breadth")){
                //TODO
            }

            else {
                // TODO: decide what to do, error?
                result.addErrorMsg("Requirement in plan is unhandled!");
            }

            retVal.add(result);
        }

        return null;
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

        //divide sum of gpa totals by number fo credits taken to get gpa and then return it
        return sum/credits;
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
