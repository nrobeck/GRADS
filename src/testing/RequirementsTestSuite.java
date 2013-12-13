//created following sample found at: http://www.tutorialspoint.com/junit/junit_suite_test.htm

package testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

//master test suite

@RunWith(Suite.class)
@SuiteClasses({
    DataValidationTests.class,
    EditRecordTests.class,
    GPATests.class,
    GPCViewsTranscriptTests.class,
    InformationRetrievalTests.class,
    ProgressSummaryTests.class,
    AddNoteTests.class,
    DataManagerTests.class,
    SchemaTests.class,
    UserTests.class,
    ModelTests.class
})
public class RequirementsTestSuite {

}
