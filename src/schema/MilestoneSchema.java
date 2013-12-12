package schema;

import edu.umn.csci5801.model.Milestone;

public class MilestoneSchema extends RequirementSchema{
	MilestoneSchema(){
		this.name = null;
	}
	MilestoneSchema(String name){
		//Convert string > milestone
		this.name = Milestone.valueOf(name);
	}
	MilestoneSchema(Milestone name){
		this.name = name;
	}
}
