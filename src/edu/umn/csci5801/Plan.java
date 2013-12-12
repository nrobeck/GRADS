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
    /**
     * Compares the input object to the current plan for equality.
     * If the IDs are the same the object and user are considered equal.
     * ASSUME NO ID DUPLICATES
     * @param o the object that the plan is to be compared to
     */
    @Override
    public boolean equals(Object o){
        //check if the object is actually the user
        if (this == o)
            return true;
        //check if the object is null...cannot be equal
        if (o == null)
            return false;
        //declare user object
        Plan plan;
        //check if object is in fact a user object
        if(o.getClass() != this.getClass()){
            return false;
        }
        else{
            //cast the object as a user
            plan = (Plan) o;
        }
        if(this.getID() == plan.getID()){
            return true;
        }
        else{
            return false;
        }
    }
}
