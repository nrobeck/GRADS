//source: http://www.tutorialspoint.com/junit/junit_suite_test.htm
package testing;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

//runs the test suite "RequirementsTestSuite"

public class TestSuiteRunner {
    public static void main(String[] args) {
	Result result = JUnitCore.runClasses(RequirementsTestSuite.class);
	for (Failure failure : result.getFailures()) {
	    System.out.println(failure.toString());
	}
	System.out.println(result.wasSuccessful());
    }
}
