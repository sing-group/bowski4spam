
import it.uniroma1.lcl.jlt.util.Pair;
import org.bowssf.ModelConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class ModelConstructorTest {

    private Pair<Integer,Integer> pair;


    @Before
    public void setUp(){
        pair = new Pair<>(1,2);
    }


    @Test
    public void testModel(){
        Assert.assertEquals(ModelConstructor.calculate(pair),0.5,1);
        pair = new Pair<>(0,1);
        Assert.assertEquals(ModelConstructor.calculate(pair),0.33,1);
    }
}
