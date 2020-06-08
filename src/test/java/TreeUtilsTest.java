import org.bowssf.util.TreeUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeUtilsTest {

    public static DefaultMutableTreeNode root;



    @Before
    public void setUp(){
        root = new DefaultMutableTreeNode("Root");
    }

    @Test
    public void testAdd(){
        TreeUtils.add(root,"prueba",true);
        Assert.assertNotNull(root.getChildAt(0));

    }

    @Test
    public void testFind(){
        Assert.assertNotNull(TreeUtils.find( TreeUtils.add( TreeUtils.add(root,"entity",true),"entity2",true),"entity2"));
    }
}
