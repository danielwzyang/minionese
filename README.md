
# Minionese
Minionese is an interpreted language that I wrote using Java. The lexer uses RegEx to convert the minionese into tokens, which are converted into statements and expressions by the parser. These statements / expressions have evaluate functions that return a value object to represent the result of the statement / expression. The value objects have different children that represent different value types and each of them hold their own unique functionalities. 

## Usage
Clone the repo in your local.

    git clone https://github.com/danielwzyang/minionese.git

Go into the root folder and compile all the files.

    javac lexer/*.java parser/*.java runtime/*.java test/*.java

Now you can either run the REPL or interpret a text file. Always run the files from the root folder. 
To see some example minionese code, look at the .minion files inside of the test folder.

<br>

To declare testing variables, follow the example provided in either file. The declareVariable function takes in a string for the name, a value, and a boolean to determine whether it's final or not.

Example:

    globalEnvironment.declareVariable("x", new NumberValue(3), false);
    globalEnvironment.declareVariable("y", new BooleanValue(), false);
    globalEnvironment.declareVariable("z", new StringValue("I'm a final variable!"), true);

This code creates three variables: x with a value of 3.0, y with a value of false, and z with a string value. The variables x and y are not final while z is.

<br>

To run the REPL:

    java test/REPL.java

During the REPL, you can type statements or expressions. Enter ? to see the current variables in the global scope, and type stop to end the program.

<br>

To interpret a file, 

    java test/ReadFile.java <PATH_TO_FILE>

Example:

    java test/ReadFile.java test/test.minion

# Basic Syntax
The overall structure of the language is similar to Javascript. The main difference is that the code isn't line based because of the way the lexer and parser are implemented. There are no semicolons to end lines and line breaks are a formatting choice. 

Whitespace isn't necessary unless it causes confusion in the lexer. For example, 3  -1 will be lexed as 3 and -1, not 3 minus 1.

To add comments, use the double quotations. This can be single line or multiline, as the lexer just parses this as a string.

# Declarations + Assignments
To declare a variable, use the "la" keyword. Variables lack types so a variable can be declared with one type and assigned to a different type. 

Example declaration and assignment:

    la var = 3
    var = "hello world!"

This defines a variable called var with the value of 3 before assigning it to the value "hello world!".

Variables can also be defined with no value. They will be initialized with a null value. 

Example:

    la x
    bello(x) "prints out null"

## Final Declarations
Variables can also be defined as final using the "fin" keyword, which means that they cannot be assigned new values after declaration. These cannot be initialized without a value. A final variable can only be defined as null by using the "nool" keyword.

Example:

	fin x = 3
	fin y = nool
    
	"the following lines of code do not work"
	x = 10
    fin z

## Shorthand Assignment Operators
Currently the supported shorthand assignment operators are the following:

 - x += y
 - x -= y
 - x++
 - x-\-
 - x *= y
 - x ^= y (math power operator not bitwise xor)
 - x /= y
 - x //= y (floor division)
 - x %= y

 # User Defined Methods
 To define a method, use the "mef" keyword. A method can be defined with or without parameters, and the body must be surrounded by braces. 

The parameters have no explicit type and cannot be optional. They must also have unique names, both from each other and variables outside of the scope.

By default, a method is void and returns a null value unless a return value is specified with the "paratu" keyword.

    "input can only be an array of numbers"
    
    mef avg(nums) {
	    la sum = 0
	    para i:len(nums) sum += nums[i]
	    paratu sum / len(nums)
	}

This creates a method that takes in an array of numbers and returns the average. An example call to this method would be avg([1, 2, 3]).

Methods cannot mutate an argument variable.

    mef increment(num) {
	    num++
    }
	
	la x = 5
	increment(x)
	bello(x) "x is still 5, not 6"
 
# Native Methods
## Miscellaneous
### bello( ...args )
Prints every argument's value joined together with a space. 
Arguments can be any type.

    la world = "world!"
    bello(world)
    bello()
    bello("hello", world)
   
Output:

    world!
    
    hello world!

### veela( )
Returns a string of the current date in the form MMMM d, yyyy.
Takes in no arguments.

    bello(veela())

Output: 

	October 31, 2024
   
# Conditionals + Loops
## If Else Statements
An if statement can be created using the "asa" keyword. The condition does not need to be in parentheses. If the statement body is only one statement, then braces may be omitted.

The else statement can be created using the "eko" keyword. The "eko" keyword must immediately follow the if statement body.

	la height = 55
    la tallEnough = no
    
    asa height < 60
	    bello("person is not five foot")
	eko {
		bello("person is at least five foot")
		tallEnough = da
	}
	
	bello("tall enough?:", tallEnough)

Output:
	
	person is not five foot
	tall enough?: false

## While Loops
A while statement can be created using the "weebo" keyword. This follows the same syntax rules as the if statement.

    la y = 10
    
    weebo y > 0 {
	    y -= 3
	    bello(y)
	}

Output:

    7
    4
    1
    -2

## For Loops
A for loop can be created using the "para" keyword. It then takes a variable name to store the index with before getting the left and right bounds separated by a colon. These bounds have to be numbers, and the left bound can be omitted to have it start at 0 by default. The statement body follows the same rules as the if and while statement bodies.

    para i 1:3
	    bello(i)
	
	la sum = 0
	para i :5 {
		sum += i
	}
	bello(sum)
	
## Break and Continue
To break out of a loop, use the "stopa" key word.
To continue a loop, use the "go" keyword.
This can be used for both while and for loops.

# Numbers

# Booleans

# Strings

# Arrays

# Objects

# Maps
