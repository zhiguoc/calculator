package com.zgc;

import org.apache.log4j.Level;
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

      result = calc.calculate("add(1,mult(2,3))");
      Assert.assertEquals("7", result);

      result = calc.calculate("mult(add(2,2),div(9,3))");
      Assert.assertEquals("12", result);

      result = calc.calculate("let(a,5,add(a,a))");
      Assert.assertEquals("10", result);

      result = calc.calculate("let(a,5,let(b,mult(a,10),add(b,a)))");
      Assert.assertEquals("55", result);

      result = calc.calculate("let(a,let(b,10,add(b,b)),let(b,20,add(a,b)))");
      Assert.assertEquals("40", result);
   }

   @Test
   public void testExtended()
   {
      result = calc.calculate("12345");
      Assert.assertEquals("12345", result);

      result = calc.calculate("add(1,2)");
      Assert.assertEquals("3", result);

      result = calc.calculate("sub(2,1)");
      Assert.assertEquals("1", result);

      result = calc.calculate("mult(2,3)");
      Assert.assertEquals("6", result);

      result = calc.calculate("div(9,1)");
      Assert.assertEquals("9", result);

      result = calc.calculate("add(1,let(a,10,let(a,20,add(a,a))))");
      Assert.assertEquals("41", result);
   }

   @Test
   public void testSpaces()
   {
      result = calc.calculate(" let(a,let(b,10,add(b,b)),let(b,20,add(a,b))) ");
      Assert.assertEquals("40", result);

      result = calc.calculate("  let(a,   let(b,10,add(b  ,b)),  let(b,20,add(a,b))) ");
      Assert.assertEquals("40", result);
   }

   @Test
   public void testUpperCases()
   {
      result = calc.calculate(" let(a,LET(b,10,add(b,b)),let(b,20,ADD(a,b))) ");
      Assert.assertEquals("40", result);
   }

   @Test
   public void testEdgeCases()
   {
      int min = Integer.MIN_VALUE;
      int max = Integer.MAX_VALUE;

      result = calc.calculate("add(" + max + ",1)");
      Assert.assertEquals("Error:overflow", result);

      result = calc.calculate("sub(" + min + ",1)");
      Assert.assertEquals("Error:overflow", result);

      result = calc.calculate("mult(" + max + ",2)");
      Assert.assertEquals("Error:overflow", result);

      result = calc.calculate("mult(" + min + ",2)");
      Assert.assertEquals("Error:overflow", result);

      result = calc.calculate("div(" + max + "," + min + ")");
      Assert.assertEquals("0", result);
   }

   @Test
   public void testDivByZero()
   {
      result = calc.calculate("let(a,1, div(1,sub(a,a)))");
      Assert.assertEquals("Error:/ by zero", result);
   }

   @Test
   public void testErrorInput()
   {
      result = calc.calculate("substract(8,2)");
      Assert.assertTrue(result.startsWith("Error:"));

      result = calc.calculate("sub(8,2))");
      Assert.assertTrue(result.startsWith("Error:"));

      result = calc.calculate("(sub(8,2))");
      Assert.assertTrue(result.startsWith("Error:"));

      result = calc.calculate("sub(8,a)");
      Assert.assertTrue(result.startsWith("Error:"));

      result = calc.calculate("sub(8, 9a)");
      Assert.assertTrue(result.startsWith("Error:"));

      result = calc.calculate("9 99");
      Assert.assertTrue(result.startsWith("Error:"));

      result = calc.calculate("add(1, 99 a)");
      Assert.assertTrue(result.startsWith("Error:"));

      result = calc.calculate(")");
      Assert.assertTrue(result.startsWith("Error:"));

      result = calc.calculate(")(");
      Assert.assertTrue(result.startsWith("Error:"));

      result = calc.calculate("(");
      Assert.assertTrue(result.startsWith("Error:"));

      result = calc.calculate("()9");
      Assert.assertTrue(result.startsWith("Error:"));

      result = calc.calculate("let(1,5,add(1,2))");
      Assert.assertTrue(result.startsWith("Error:"));

      result = calc.calculate("add(1,)");
      Assert.assertTrue(result.startsWith("Error:"));

      result = calc.calculate("add(1)");
      Assert.assertTrue(result.startsWith("Error:"));

      result = calc.calculate("add(add(12,add(1,2)");
      Assert.assertTrue(result.startsWith("Error:"));
   }

   @Test
   public void testLongInput()
   {
      String dots256 = new String(new char[256]).replace('\0', '.');
      String input = "add(1,add(2,add(3," + dots256 + "  )))";
      result = calc.calculate(input);
      Assert.assertEquals("Error:input is too long", result);
   }
}
