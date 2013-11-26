// User.java
//
/** TODO: copyright */

import java.lang.String;

/**
 * 
 * @author Kyle
 *
 * Storage class for user information
 * This is used for user validation and specifying departments
 * Information is only allowed to be set on creation
 * This is to prevent mistakes in overriding data;
 *
 */
public class User {
	/** the x500 of the user? */
	private String id;
	/** GPC or student only*/
	private String role;
	/** the department of the user */
	private String department;
	
	public User(String id, String role, String department){
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
	
	public String getDepartment(){
		return department;
	}
}
