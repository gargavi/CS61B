import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class BSTStringSetTest  {


    @Test
    public void testput(){
        BSTStringSet total = new BSTStringSet();
        total.put("Avi");
        total.put("Bavi");
        total.put("Davi");
        total.put("Cavi");


        assertEquals(true, total.contains("Avi"));
        assertEquals(true, total.contains("Bavi"));
        assertEquals(true, total.contains("Cavi"));
        assertEquals(true, total.contains("Davi"));
        assertEquals(false, total.contains("Mavi"));
        System.out.println(total.asList());
    }

    @Test
    public void testNothing() {
        // FIXME: Delete this function and add your own tests
    }
}
