package testing;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import schema.UserSchema;
import schema.IdNameSchema;
import edu.umn.csci5801.model.Department;

//Unit tests for Schema constructors
public class SchemaTests {
    // User Schema tests
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

    // IdNameSchema tests
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
