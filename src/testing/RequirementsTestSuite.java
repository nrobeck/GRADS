//created following sample found at: http://www.tutorialspoint.com/junit/junit_suite_test.htm

package testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

//master test suite

@RunWith(Suite.class)
@SuiteClasses({
    BestClassesTests.class,
    DataValidationTests.class,
    DifferentiateCoursesTests.class,
    DifferentiateUsersTests.class,
    EditRecordTests.class,
    GPATests.class,
    GPCViewsTranscriptTests.class,
    GraduationReccomendationTests.class,
    HypotheticalClassesTests.class,
    InformationRetrievalTests.class,
    MilestonesTests.class,
    PersistentDataTests.class,
    ProgressSummaryTests.class,
    TimestampsTests.class,
    DataManagerTests.class,
    SchemaTests.class,
    UserTests.class
})
public class RequirementsTestSuite {

}
