package schema;

public class UserSchema{
	IdNameSchema user;
	String role;
	String department;
	public UserSchema(){}
	public UserSchema(IdNameSchema idNameSchema, String role, String department){
		this.user = idNameSchema;
		this.role = role;
		this.department = department;
	}
	public IdNameSchema getID(){
		return this.user;
	}
	
	public String getRole(){
		return this.role;
	}
	
	public String getDepartment(){
		return this.department;
	}
}