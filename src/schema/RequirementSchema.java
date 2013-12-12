package schema;

public abstract class RequirementSchema {
    protected Object name;	//Can be string or milestone
    RequirementSchema(){
        this.name = null;
    }
    RequirementSchema(String name){
        this.name = name;
    }
}
