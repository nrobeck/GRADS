package schema;

public class GpaSchema extends RequirementSchema{
	private float min;
	GpaSchema(){
		super(null);
		this.min = -1;
	}
	GpaSchema(String name){
		super(name);
		this.min = -1;
	}
	GpaSchema(String name, float min){
		super(name);
		this.min = min;
	}
}
