import org.bowssf.util.BabelUtils;
import org.junit.Assert;
import org.junit.Test;

public class BabelTest {

    public final String START_SYNSET = "bn:00031027n";
    @Test
    public void getChildrenNotNullTest(){
        Assert.assertFalse(BabelUtils.getChildNodes(START_SYNSET).isEmpty());
    }

    @Test
    public void startSynsetNotNull(){
        Assert.assertFalse(BabelUtils.getGloss(START_SYNSET).isEmpty());
        Assert.assertFalse(BabelUtils.getWord(START_SYNSET).isEmpty());
    }

    @Test
    public void synonymsStartSynsetNotNull(){
        Assert.assertFalse(BabelUtils.getSynonyms(START_SYNSET).isEmpty());
    }



}
