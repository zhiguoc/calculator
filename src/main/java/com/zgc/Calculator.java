package com.zgc;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.google.common.math.IntMath;

public class Calculator
{
   private static final int MAX_INPUT_LENGTH = 256;
   private static final char OPEN_BRACKET = '(';
   private static final char CLOSE_BRACKET = ')';
   private static final String COMMA = ",";
   private Map<String, Integer> vars;
   private static final Logger logger = Logger.getLogger(Calculator.class);

   private enum Operator
   {
      ADD("add"), SUB("sub"), MULT("mult"), DIV("div"), ASSIGN("let");

      private final String value;

      Operator(final String value)
      {
         this.value = value;
      }

      static Operator fromString(final String value)
      {
         for (Operator op: Operator.values())
         {
            if (op.value.equalsIgnoreCase(value))
               return op;
         }
         throw new IllegalArgumentException("not recognized operator " + value);
      }
   }

   private class Param
   {
      String left;
      String right;
   }

   private class Variable
   {
      final String name;
      final int value;
      final String formula;

      Variable(final String name, final int value, final String formula)
      {
         this.name = name;
         this.value = value;
         this.formula = formula;
      }
   }

   public String calculate(final String input)
   {
      vars = new HashMap<String, Integer>();

      try
      {
         if (input.length() > MAX_INPUT_LENGTH)
         {
            final String message = "Error:input is too long";
            logger.error(message);
            return message;
         }

         final int result = calculate(0, input);

         return Integer.toString(result);
      }
      catch (Exception e)
      {
         final String message = "Error:" + e.getMessage();
         logger.error(message);
         return message;
      }
   }

   private int calculate(final int level, final String origInput)
   {
      final int nextLevel = level + 1;

      final String input = origInput.trim();
      logger.info(getLogIndetion(level) + "calculating " + input + ", level=" + level);
      if (isInteger(input))
      {
         final int result = Integer.valueOf(input);
         logger.info(getLogIndetion(level) + "result=" + result);
         return result;
      }

      if (isVariable(input))
      {
         if (vars.containsKey(input))
         {
            final int result = vars.get(input);
            logger.info(getLogIndetion(level) + "result=" + result);
            return result;
         }
         throw new IllegalArgumentException("variable is not set: " + input);
      }

      final int beginParamIndex = input.indexOf(OPEN_BRACKET);
      final int endParamIndex = input.lastIndexOf(CLOSE_BRACKET);

      if (beginParamIndex <= 0 || beginParamIndex >= endParamIndex || endParamIndex <= 0 || endParamIndex != input.length() - 1)
      {
         throw new IllegalArgumentException("malformated formula " + input);
      }

      final String opStr = input.substring(0, beginParamIndex);
      final Operator op = Operator.fromString(opStr);
      logger.info(getLogIndetion(level) + "op=" + op);
      final String params = input.substring(beginParamIndex + 1, endParamIndex);
      logger.info(getLogIndetion(level) + "params=" + params);

      if (op == Operator.ASSIGN)
      {
         final Variable var = getVar(level + 1, params);
         vars.put(var.name, var.value);
         final int result = calculate(level, var.formula);
         logger.info(getLogIndetion(level) + "result=" + result);
         return result;
      }
      final Param param = getParam(level, params);

      final int result;
      switch (op)
      {
         case ADD:
            result = IntMath.checkedAdd(calculate(nextLevel, param.left), calculate(nextLevel, param.right));
            break;
         case SUB:
            result = IntMath.checkedSubtract(calculate(nextLevel, param.left), calculate(nextLevel, param.right));
            break;
         case MULT:
            result = IntMath.checkedMultiply(calculate(nextLevel, param.left), calculate(nextLevel, param.right));
            break;
         case DIV:
            result = calculate(nextLevel, param.left) / calculate(nextLevel, param.right);
            break;
         default:
            // should never happen
            throw new IllegalStateException("op should be known" + op);
      }

      logger.info(getLogIndetion(level) + "result=" + result);
      return result;
   }

   private Variable getVar(final int level, final String params)
   {
      Param param = getParam(level + 1, params);
      final String name = param.left;
      if (!isVariable(name))
      {
         throw new IllegalArgumentException("invalid variable name " + name);
      }

      logger.debug(getLogIndetion(level) + "var name=" + name);

      final String restParams = param.right.trim();
      param = getParam(level + 1, restParams);

      final int value = calculate(level + 1, param.left);

      logger.debug(getLogIndetion(level) + "var value=" + value);
      logger.debug(getLogIndetion(level) + "var fomular=" + param.right);

      return new Variable(name, value, param.right);
   }

   private boolean isInteger(final String input)
   {
      return input.matches("[0-9]+") || input.matches("-[0-9]+");
   }

   private boolean isVariable(final String input)
   {
      return input.matches("[a-zA-Z]+");
   }

   private String getLogIndetion(final int level)
   {
      final StringBuilder strBuilder = new StringBuilder();

      for (int i = 0; i < level; i++)
         strBuilder.append("\t");

      return strBuilder.toString();
   }

   private Param getParam(final int level, final String params)
   {
      final Param result = new Param();
      final int commaIndex = params.indexOf(COMMA);

      if (commaIndex == -1 || commaIndex >= params.length() - 1)
      {
         throw new IllegalArgumentException("malformed params " + params);
      }

      final int openBracketIndex = params.indexOf(OPEN_BRACKET);
      if (openBracketIndex == -1)
      {
         final String[] splitted = params.split(COMMA);
         result.left = splitted[0].trim();
         result.right = splitted[1].trim();
         return result;
      }

      final int leftParamEndIndex;
      if (openBracketIndex < commaIndex)
      {
         final int matchingCloseBracketIndex = getMatchingBracket(params.toCharArray(), openBracketIndex);
         if (matchingCloseBracketIndex >= params.length() - 2)
         {
            throw new IllegalArgumentException("missing closing bracket: " + params);
         }
         leftParamEndIndex = matchingCloseBracketIndex + 1;
      }
      else
      {
         leftParamEndIndex = commaIndex;
      }

      final String leftParam = params.substring(0, leftParamEndIndex);

      logger.debug(getLogIndetion(level) + "left param=" + leftParam);

      result.left = leftParam;
      String rightParam = params.substring(leftParamEndIndex).trim();
      if (!rightParam.startsWith(","))
      {
         throw new IllegalArgumentException("malformed params " + params);
      }
      rightParam = rightParam.substring(1);

      logger.debug(getLogIndetion(level) + "right param=" + rightParam);
      result.right = rightParam;
      return result;
   }

   private int getMatchingBracket(final char[] input, final int openPos)
   {
      int closePos = openPos;
      int counter = 1;
      while (counter > 0)
      {
         char c = input[++closePos];
         if (c == OPEN_BRACKET)
         {
            counter++;
         }
         else if (c == CLOSE_BRACKET)
         {
            counter--;
         }
      }
      return closePos;
   }
}
