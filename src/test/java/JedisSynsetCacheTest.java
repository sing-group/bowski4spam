import it.uniroma1.lcl.babelnet.InvalidSynsetIDException;
import org.bowssf.util.BabelUtils;
import org.bowssf.util.JedisSynsetCache;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JedisSynsetCacheTest {

    JedisSynsetCache jedisSynsetCache = null;
    public final String START_SYNSET = "bn:00031027n";
    public final String NON_EXISTING = "bn:23323n";

    @Before
    public void setUp(){
        jedisSynsetCache = JedisSynsetCache.getInstance();
        jedisSynsetCache.setForceBabelnet(false);
    }

    public void setForcebabelNet(){
        jedisSynsetCache.setForceBabelnet(true);
        jedisSynsetCache.setForceJedis(false);
    }

    @Test
    public void getChildrenTest(){
        Assert.assertEquals(jedisSynsetCache.getChildNodes(START_SYNSET), BabelUtils.getChildNodes(START_SYNSET));
    }

    @Test
    public void getWordTest(){
        Assert.assertEquals(jedisSynsetCache.getWordFromSynset(START_SYNSET), BabelUtils.getWord(START_SYNSET));
    }


    @Test
    public void getGlossTest(){
        Assert.assertEquals(jedisSynsetCache.getGlossFromSynset(START_SYNSET), BabelUtils.getGloss(START_SYNSET));
    }

    @Test
    public void getChildrenForceBabelTest(){
        this.setForcebabelNet();
        Assert.assertEquals(jedisSynsetCache.getChildNodes(START_SYNSET), BabelUtils.getChildNodes(START_SYNSET));
    }

    @Test
    public void getWordForceBabelTest(){
        this.setForcebabelNet();
        Assert.assertEquals(jedisSynsetCache.getWordFromSynset(START_SYNSET), BabelUtils.getWord(START_SYNSET));
    }

    @Test
    public void getGlossForceBabelTest(){
        this.setForcebabelNet();
        Assert.assertEquals(jedisSynsetCache.getGlossFromSynset(START_SYNSET), BabelUtils.getGloss(START_SYNSET));
    }

    @Test(expected = InvalidSynsetIDException.class)
    public void getChildrenFromNonExistingSynsetTest(){
        jedisSynsetCache.getChildNodes(NON_EXISTING);
    }

    @Test(expected = InvalidSynsetIDException.class)
    public void getWordNonExistingSynsetTest(){
        jedisSynsetCache.getWordFromSynset(NON_EXISTING);
    }

    @Test(expected = InvalidSynsetIDException.class)
    public void getGlossNonExistingSynsetTest(){
        jedisSynsetCache.getGlossFromSynset(NON_EXISTING);
    }

}
