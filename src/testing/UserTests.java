package testing;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import edu.umn.csci5801.User;

public class UserTests {

	@Test
	public void testEqualsCoverage() {
		User self = new User(null, null, null);
		Assert.assertTrue(self.equals(self));	//Tests if equal to self
		Assert.assertFalse(self.equals(null));	//Tests if not equal to null
		Assert.assertFalse(self.equals("LOL"));	//Tests if it is a different type of object
	}

}
