# calculator

This is a simple command line calculator implementation. The project is based on Maven and Travis-CI.

To compile the project:

$ mvn clean install

To run the program:

$ java -jar calculator-1.0-SNAPSHOT-jar-with-dependencies.jar "add(1,2)"

result=3

To generate code coverage report (current coverage >90%):

$ mvn cobertura:cobertura


Assumptions and usage info:

The assignment operator ("let") remembers the variable value globally.

Input expression string length cannot be greater 256.

The space in the input string is ignored.

Arithmetic functions (add,sub,mult,div) and "let" operator are case-insenstive.

The "div" function returns only the integer part, for example: div(9,2) = 4

Arithmetic operation can cause exception such as overflow or div by zero. The error result is shown accordingly.

Should be able to run on any standard java environment although tested on Linux platform only.

Customized logger layer using log4j with three level control (ERROR, INFO, DEBUG) for the Calculator class. Optinally set by the second parameter, for example: 

$ java -jar calculator-1.0-SNAPSHOT-jar-with-dependencies.jar "add(1,add(2,2))" DEBUG

