package edu.umn.csci5801;

import java.util.ArrayList;

import schema.RequirementSchema;

public class Plan {
	private String id;
	private ArrayList<RequirementSchema> requirements;
	public Plan(){
		this.id = null;
		this.requirements = null;
	}
	public Plan(String id){
		this.id = id;
		this.requirements = null;
	}
	public Plan(String id, ArrayList<RequirementSchema> requirements){
		this.id = id;
		this.requirements = requirements;
	}
}
