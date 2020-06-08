
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)

@Suite.SuiteClasses({
        BabelTest.class,
        JedisSynsetCacheTest.class,
        IniConfigTest.class,
        TreeUtilsTest.class,
        ModelConstructorTest.class
})


public class TestSuite {

}
