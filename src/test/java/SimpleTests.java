import org.junit.Assert;
import org.junit.Test;

import mock.Calculator;

/**
 * Created by pwilkin on 19-Apr-18.
 */
public class SimpleTests {

    @Test
    public void testAdd() {
        Calculator cal = new Calculator();
        cal.add(2.0);
        double fin = cal.add(2.0);
        Assert.assertEquals(4.0, fin, 0.01);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNull() {
        Calculator cal = new Calculator();
        Double d = null;
        cal.add(d);
    }

    @Test
    public void testDoStuff() {
        Calculator cal = new Calculator();
        cal.add(2.0);
        cal.reset();
        cal.add(4.0);
        cal.reset();
        cal.add(423454.0);
        cal.add(4634535345.4);
        cal.reset();
        cal.reset();
    }

}
