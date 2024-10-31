# Minionese

Minionese is an interpreted language that I wrote using Java. The lexer uses RegEx to convert the minionese into tokens, which are converted into statements and expressions by the parser. These statements / expressions have evaluate functions that return a value object to represent the result of the statement / expression. The value objects have different children that represent different value types and each of them hold their own unique functionalities. 

## Usage
Clone the repo in your local.

    git clone https://github.com/danielwzyang/minionese.git

Go into the root folder and compile all the files.

    javac lexer/*.java parser/*.java runtime/*.java test/*.java

Now you can either run the REPL or interpret a text file. Always run the files from the root folder. 
To see some example minionese code, look at the .minion files inside of the test folder.
\
\
To declare testing variables, follow the example provided in either file. The declareVariable function takes in a string for the name, a value, and a boolean to determine whether it's final or not.

Example:

    globalEnvironment.declareVariable("x", new NumberValue(3), false);
    globalEnvironment.declareVariable("y", new BooleanValue(), false);
    globalEnvironment.declareVariable("z", new StringValue("I'm a final variable!"), true);

This code creates three variables: x with a value of 3.0, y with a value of false, and z with a string value. The variables x and y are not final while z is.
\
\
To run the REPL:

    java test/REPL.java

During the REPL, you can type statements or expressions. Enter ? to see the current variables in the global scope, and type stop to end the program.
\
\
To interpret a file, 

    java test/ReadFile.java <PATH_TO_FILE>

Example:

    java test/ReadFile.java test/test.minion

# Documentation
## Basic Syntax
The overall structure of the language is similar to Javascript. The main difference is that the code isn't line based because of the way the lexer and parser are implemented. Thus there are no semicolons to end lines, and line breaks are a formatting choice. Whitespace isn't necessary unless it causes confusion in the lexer. For example, 3  -1 will be lexed as 3 and -1, not 3 minus 1.

To add comments, use the double quotations. This can be single line or multiline, as the lexer just parses this as a string.

## Declarations + Assignments
To declare a variable, use the keyword "la," followed by the equals sign and an expression.
To assign a variable, use the identifier name followed by an expression.
Variables lack types so a variable can be declared with one type and assigned to a different type. 

Example declaration and assignment:

    la var = 3
    var = "hello world!"

This defines a variable called var with the value of 3 before assigning it to the value "hello world!".
 
## Native Functions

## Conditionals + Loops

## Numbers

## Booleans

## Strings

## Arrays

## Objects

## Maps
