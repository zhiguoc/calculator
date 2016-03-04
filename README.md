# calculator

This is a simple command line calculator implementation. The project is based on Maven and Travis-CI.

To compile the project:

$ mvn clean install

To run the program:

java -jar calculator-1.0-SNAPSHOT-jar-with-dependencies.jar "add(1,2)"

Assumptions:

The assignment operator ("let") remember the variable values globally.

Input expression string length cannot be greater 256.

The space in the input string is ignored.

Arithmetic functions (add,sub,mult,div) and "let" operator are case-insenstive.

Tested on Linux platform

To do:

set the log4j level dynamically
