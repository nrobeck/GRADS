package schema;

import edu.umn.csci5801.model.Department;

/**
 * Schema for retrieving users from JSON, this can easily be converted into a
 * User Was made because GSON can handle importing this class seamlessly
 * 
 * @author markholmes
 * 
 */
public class UserSchema {
    IdNameSchema user;
    String role;
    Department department;

    /**
     * Blank constructor
     */
    public UserSchema() {
    }

    /**
     * Constructor with all fields
     * 
     * @param idNameSchema
     *            The Id, first, and last name
     * @param role
     * @param department
     */
    public UserSchema(IdNameSchema idNameSchema, String role,
	    Department department) {
	this.user = idNameSchema;
	this.role = role;
	this.department = department;
    }

    /**
     * Getter for the IdNameSchema
     * 
     * @return IdNameSchema
     */
    public IdNameSchema getID() {
	return this.user;
    }

    /**
     * Getter for the user's role
     * 
     * @return user's role
     */
    public String getRole() {
	return this.role;
    }

    /**
     * Getter for the user's department
     * 
     * @return user's department
     */
    public Department getDepartment() {
	return this.department;
    }
}
