package com.zgc;

import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 * A simple command line calculator application
 */
public class MainApp
{
   final static Logger logger = Logger.getLogger(MainApp.class);

   public static void main(String[] args)
   {
      logger.info("<MainApp> start");

      if (args.length == 0 || args.length > 2)
      {
         final String message = "invalid number of parameters:" + Arrays.toString(args);
         logger.warn(message);
         System.out.println(message);
      }
      else
      {
         final String input = args[0];

         String level = "ERROR";
         if (args.length == 2)
         {
            level = args[1].toUpperCase();
         }

         if ("ERROR".equals(level) || "INFO".equals(level) || "DEBUG".equals(level))
         {
            Calculator calc = new Calculator(level);

            String result = calc.calculate(input);
            System.out.println("result=" + result);

            logger.info("<MainApp> input=" + input);
            logger.info("<MainApp> level=" + level);
            logger.info("<MainApp> result=" + result);
         }
         else
         {
            final String message = "invalid level (must be ERROR,INFO,DEBUG)" + level;
            logger.warn(message);
            System.out.println(message);
         }
      }

      logger.info("<MainApp> exit");
   }
}