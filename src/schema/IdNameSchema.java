package schema;

/**
 * Id Name Schema, is equivalent to student model class
 * 
 * @author markholmes
 * 
 */
public class IdNameSchema {
    String id;
    String firstName;
    String lastName;

    /**
     * Blank constructor
     */
    public IdNameSchema() {
    }

    /**
     * Parameterized constructor
     * 
     * @param id
     * @param firstName
     * @param lastName
     */
    public IdNameSchema(String id, String firstName, String lastName) {
	this.id = id;
	this.firstName = firstName;
	this.lastName = lastName;
    }

    /**
     * Getter for the user's ID
     * 
     * @return user's ID
     */
    public String getID() {
	return this.id;
    }

    /**
     * Getter for the user's first name
     * 
     * @return user's first name
     */
    public String getFirstName() {
	return this.firstName;
    }

    /**
     * Getter for user's last name
     * 
     * @return user's last name
     */
    public String getLastName() {
	return this.lastName;
    }
}
