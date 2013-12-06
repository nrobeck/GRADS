package schema;

public class IdNameSchema{
	String id;
	String firstName;
	String lastName;
	public IdNameSchema(){}
	public IdNameSchema(String id, String firstName, String lastName){
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public String getID(){
		return this.id;
	}
	public String getFirstName(){
		return this.firstName;
	}
	
	public String getLastName(){
		return this.lastName;
	}
}
