package com.zgc;

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
         logger.warn("invalid number of parameters");
      }
      else
      {
         final String input = args[0];

         String level = "info";
         if (args.length == 2)
         {
            level = args[1];
         }

         final Calculator calc = new Calculator();
         final String result = calc.calculate(input);
         System.out.println("result=" + result);
      }

      logger.info("<MainApp> exit");
   }
}
