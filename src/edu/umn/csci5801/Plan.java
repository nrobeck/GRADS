package edu.umn.csci5801;

import java.util.ArrayList;

import edu.umn.csci5801.model.Degree;

import schema.RequirementSchema;

public class Plan {
	private Degree id;
	private ArrayList<RequirementSchema> requirements;
	public Plan(){
		this.id = null;
		this.requirements = null;
	}
	public Plan(Degree id){
		this.id = id;
		this.requirements = null;
	}
	
	public Plan(String id){
		this.id = Degree.valueOf(id);
		this.requirements = null;
	}
	
	public Plan(Degree id, ArrayList<RequirementSchema> requirements){
		this.id = id;
		this.requirements = requirements;
	}
	
	public Plan(String id, ArrayList<RequirementSchema> requirements){
		this.id = Degree.valueOf(id);
		this.requirements = requirements;
	}
	
	public Degree getID(){
		return this.id;
	}
	
	public ArrayList<RequirementSchema> getRequirements(){
		return this.requirements;
	}
}
