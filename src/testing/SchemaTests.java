package testing;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.junit.Test;

import schema.IdNameSchema;
import schema.UserSchema;
import edu.umn.csci5801.model.Department;

/**
 * Unit Tests for schema constructors
 * 
 * @author markholmes
 * 
 */
public class SchemaTests {
    /**
     * Tests user schema creation
     */
    @Test
    public void testUserSchemaCreation() {
	try {
	    UserSchema user = new UserSchema(new IdNameSchema(), "GPC",
		    Department.MATH);
	    Assert.assertTrue(user.getRole().equals("GPC"));
	    Assert.assertTrue(user.getDepartment() == Department.MATH);
	} catch (Exception e) {
	    fail("Exception!");
	}
    }

    /**
     * Tests IDNameSchema creation
     */
    @Test
    public void testIdNameSchemaCreation() {
	try {
	    IdNameSchema idName = new IdNameSchema("id", "firstName",
		    "lastName");
	    Assert.assertTrue(idName.getID().equals("id"));
	    Assert.assertTrue(idName.getFirstName().equals("firstName"));
	    Assert.assertTrue(idName.getLastName().equals("lastName"));
	} catch (Exception e) {
	    fail("Exception!");
	}
    }

}
