package com.zgc;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 * Unit test
 */
public class CalculatorTest
{
   Calculator calc;
   String result;

   @Before
   public void setup()
   {
      calc = new Calculator();
   }

   @Test
   public void testNormal()
   {
      result = calc.calculate("add(1,2)");
      Assert.assertEquals("3", result);
   }
}
