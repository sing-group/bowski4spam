import org.bowssf.util.IniConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IniConfigTest {
    public IniConfig iniConfig = null;
    public final int READ_LEVELS = 3;
    public final boolean FORCE_JEDIS = false;
    public final boolean FORCE_BABEL = false;
    @Before
    public void setUp(){
        iniConfig = IniConfig.getInstance();
    }

    @Test
    public void readTest(){
        iniConfig.load();
        Assert.assertEquals(iniConfig.getPreloadingLevels(),READ_LEVELS);
        Assert.assertEquals(iniConfig.isForceJedis(),FORCE_JEDIS);
        Assert.assertEquals(iniConfig.isForceBabelNet(),FORCE_BABEL);
    }
}
