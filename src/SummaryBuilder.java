// SummaryBuilder.java
//

import edu.umn.csci5801.model.ProgressSummary;
import edu.umn.csci5801.model.*;
import java.lang.String;
import java.util.ArrayList;
import com.google.gson.JsonObject;

/** TODO: copyright */

/**
 *
 * Module of GRADS used to create student summary
 * @author Kyle
 *
 */
public class SummaryBuilder {
	private int dbmanPtr; // TODO: replace with an actual pointer
	
	/**
	 * Constructor method for the Summary Builder
	 */
	public SummaryBuilder() {
		
	}
	
	/**
	 * Creates a graduate progress summary based on the student's transcript
	 * @param studentID The id of the student
	 * @return A completed summary of a student's progress towards his/her graduate degree
	 */
	public ProgressSummary createStudentSummary(String studentID) {
		ProgressSummary summary = new ProgressSummary();
		return null;
	}
	
	/**
	 * Creates a list of RequirementCheckResults for the summary
	 */
	private void checkPlanRequirements() {
	}
}
