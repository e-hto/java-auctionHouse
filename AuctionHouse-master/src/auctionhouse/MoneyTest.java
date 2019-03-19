/**
 * 
 */
package auctionhouse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author pbj
 *
 */
public class MoneyTest {

    @Test    
    public void testAdd() {
        Money val1 = new Money("12.34");
        Money val2 = new Money("0.66");
        Money result = val1.add(val2);
        assertEquals("13.00", result.toString());
    }

    @Test
    public void testAddNegative(){
        Money val1 = new Money("12.34");
        Money val2 = new Money("-0.34");
        Money result = val1.add(val2);
        assertEquals("12.00",result.toString());
    }

    /*
     ***********************************************************************
     * BEGIN MODIFICATION AREA
     ***********************************************************************
     * Add all your JUnit tests for the Money class below.
     */

    @Test
    public void testSubstract() {
        Money val1 = new Money("12.34");
        Money val2 = new Money("0.34");
        Money result = val1.subtract(val2);
        assertEquals("12.00",result.toString());
    }

    @Test
    public void testSubstractNegative() {
        Money val1 = new Money("12.00");
        Money val2 = new Money("-0.34");
        Money result = val1.subtract(val2);
        assertEquals("12.34",result.toString());
    }

    @Test
    public void testAddPercent(){
        Money val1 = new Money("10.01");
        Money val2 = new Money("0.01");
        Money result1 = val1.addPercent(33.00);
        Money result2 = val2.addPercent(50.00);
        assertEquals("13.31", result1.toString());
        assertEquals("0.02", result2.toString());

    }

    @Test
    public void testAddNegativePercent(){
        Money val1 = new Money("10.00");
        Money val2 = new Money("0.04");
        Money result1 = val1.addPercent(-50.00);
        Money result2 = val2.addPercent(-33.00);
        assertEquals("5.00", result1.toString());
        assertEquals("0.03", result2.toString());

    }

    @Test
    public void testCompareTo(){
        Money val1 = new Money("5.0");
        Money val2 = new Money("12.5");
        Money val3 = new Money("10.3");
        Money val4 = new Money("12.50");
        int result1 = val1.compareTo(val2);
        int result2 = val2.compareTo(val3);
        int result3 = val2.compareTo(val4);
        assertEquals(-1,result1);
        assertEquals(1,result2);
        assertEquals(0,result3);



    }

    @Test
    public void testLessEqual(){
        Money val1 = new Money("8.53");
        Money val2 = new Money("8.54");
        boolean result1 = val1.lessEqual(val1);
        boolean result2 = val1.lessEqual(val2);
        boolean result3 = val2.lessEqual(val1);
        assertTrue(result1);
        assertTrue(result2);
        assertEquals(false,result3);

    }

    @Test
    public void testLessEqualNegative(){
        Money val1 = new Money("-1.00");
        Money val2 = new Money("-5.00");
        boolean result1 = val1.lessEqual(val1);
        boolean result2 = val1.lessEqual(val2);
        boolean result3 = val2.lessEqual(val1);
        assertTrue(result1);
        assertEquals(false,result2);
        assertTrue(result3);

    }

    @Test
    public void testEquals(){
        double nonMoney = 13.4;
        Money val1 = new Money("10.50");
        Money val2 = new Money("11");
        boolean result1 = val1.equals(nonMoney);
        boolean result2 = val1.equals(val2);
        boolean result3 = val1.equals(val1);
        assertEquals(false,result1);
        assertEquals(false,result2);
        assertTrue(result3);

    }

    @Test
    public void testNegativeEquals() {
        double nonMoney = -12.3;
        Money val1 = new Money("-10.50");
        Money val2 = new Money("11.0");
        boolean result1 = val1.equals(nonMoney);
        boolean result2 = val1.equals(val2);
        boolean result3 = val1.equals(val1);
        assertEquals(false, result1);
        assertEquals(false, result2);
        assertTrue(result3);

    }
        /*
     * Put all class modifications above.
     ***********************************************************************
     * END MODIFICATION AREA
     ***********************************************************************
     */


}
