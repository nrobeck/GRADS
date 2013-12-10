// User.java
//
/** TODO: copyright */

import java.lang.String;

import edu.umn.csci5801.model.Department;

/**
 * 
 * @author Kyle
 *
 * Storage class for user information
 * This is used for user validation and specifying departments
 * Information is only allowed to be set on creation
 * This is to reduce mistakes in overriding data;
 *
 */
public class User {
	/** the x500 of the user? */
	private String id;
	/** GPC or student only*/
	private String role;
	/** the department of the user */
	private Department department;
	
	public User(String id, String role, Department department){
		this.id = id;
		this.role = role;
		this.department = department;
	}
	
	public String getID(){
		return id;
	}
	
	public String getRole(){
		return role;
	}
	
	public Department getDepartment(){
		return department;
	}
	
	//If IDs are equal Users are equal ASSUMES NO ID DUPLICATES
	@Override
	public boolean equals(Object o){
		if (this == o)
			return true;
		if (o == null)
			return false;
		User user;
		if(o.getClass() != this.getClass()){
			return false;
		}
		else{
			user = (User)o;
		}
		if(this.getID() == user.getID()){
			return true;
		}
		else{
			return false;
		}
	}
}
