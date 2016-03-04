package com.zgc;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
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
         final String message = "invalid number of parameters";
         logger.warn(message);
         System.out.println(message);
      }
      else
      {
         final String input = args[0];

         String inputLevel = "ERROR";
         if (args.length == 2)
         {
            inputLevel = args[1];
         }

         Level level = null;
         if ("ERROR".equalsIgnoreCase(inputLevel))
         {
            level = Level.ERROR;
         }
         else if ("INFO".equalsIgnoreCase(inputLevel))
         {
            level = Level.INFO;
         }
         else if ("DEBUG".equalsIgnoreCase(inputLevel))
         {
            level = Level.ALL;
         }
         else
         {
            final String message = "invalid level (must be ERROR,INFO,DEBUG)" + inputLevel;
            logger.warn(message);
            System.out.println(message);
         }

         if (level != null)
         {
            // TODO:set level
            Calculator calc = new Calculator();
            String result = calc.calculate(input);
            System.out.println("result=" + result);

            logger.info("<MainApp> input=" + input);
            logger.info("<MainApp> level=" + level);
            logger.info("<MainApp> result=" + result);
         }
      }

      logger.info("<MainApp> exit");
   }
}
