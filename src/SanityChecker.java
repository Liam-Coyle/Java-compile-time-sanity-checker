import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SanityChecker 
{
	/**
	 * This application parses an input program written in Java and finds various compile time errors.
	 * Additionally, the application can scan a Java program containing arithmetic operations between integers and print out all integer variables and their final values.
	 * @author Liam Coyle lcoyle21@qub.ac.uk
	 */
	
	//Defines Tokens
	public enum TokenType {OP_MULTIPLY, OP_DIVIDE, OP_MOD, OP_ADD, OP_SUBRACT, OP_LESS, OP_LESSEQUAL, OP_GREATER, OP_GREATEREQUAL, OP_EQUAL, OP_NOTEQUAL, OP_NOT, OP_ASSIGN, OP_AND, OP_OR, OP_DOT, LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, 
		LEFT_BRACKET, RIGHT_BRACKET, KEYWORD_IF, KEYWORD_ELSE, KEYWORD_WHILE, KEYWORD_RETURN, KEYWORD_MAIN, KEYWORD_INT, KEYWORD_DOUBLE, KEYWORD_BOOLEAN,KEYWORD_PUBLIC, KEYWORD_STRING, KEYWORD_CLASS, KEYWORD_VOID, KEYWORD_FOR, KEYWORD_CASE, KEYWORD_SWITCH, 
		KEYWORD_STATIC, KEYWORD_BREAK, KEYWORD_CONTINUE, KEYWORD_DEFAULT, SEMICOLON, COLON, COMMA, IDENTIFIER, INTEGER, DOUBLE, STRING, BOOLEAN};
		
	//Initialises ArrayLists which will store token names and token types respectively
	public static ArrayList<String> tokenNameArrayList = new ArrayList<String>();
	public static ArrayList<TokenType> tokenTypeArrayList = new ArrayList<TokenType>();
	public static ArrayList<Integer> lineNumberArrayList = new ArrayList<Integer>();
	
	//Initialises ArrayLists which will store token names/types from miniDetermineTokens, aswell as variable names and values
	public static ArrayList<String> miniTokenNameArrayList = new ArrayList<String>();
	public static ArrayList<TokenType> miniTokenTypeArrayList = new ArrayList<TokenType>();
	public static ArrayList<String> variableNameArrayList = new ArrayList<String>();
	public static ArrayList<Integer> variableValueArrayList = new ArrayList<Integer>();
	
	//Initialises counters
	public static int lineNumber = 1;
	public static int numOfBraces = 0;
	
	//Initialises string to store java file name
	public static String javaFileName = null;
	
	//Initialises error flags
	public static boolean errorsFound = false;
	public static boolean endOfCommentError = false;
	public static boolean classFound = false;
	public static boolean mainFound = false;
	
	public static void main(String[] args) 
	{
		//Instructions: Uncomment code you want to run
		
		//Shows how tokenizer works
		//scan((readFile("src/TokenizerExample.java")));
		
		//Error Detection Examples
		//scan(readFile("src/ErrorExample1.java"));  //Invalid identifier name
		//scan(readFile("src/ErrorExample2.java"));  //Unrecognized chracter in weeJava
		//scan(readFile("src/ErrorExample3.java"));  //Unexpected end of comment
		//scan(readFile("src/ErrorExample4.java"));  //Too many decimal points in double 
		//scan(readFile("src/ErrorExample5.java"));  //Missing parenthesis after if/while/for statements
		//scan(readFile("src/ErrorExample6.java"));  //Invalid main method
		//scan(readFile("src/ErrorExample7.java"));  //Missing / invalid class name
		//scan(readFile("src/ErrorExample8.java"));  //The left-hand side of an assignment must be a variable
		//scan(readFile("src/ErrorExample9.java"));  //Unclosed ClassBody
		//scan(readFile("src/ErrorExample10.java")); //String literal not closed by double quote
		
		//Variable Tracking Example
		//variableScan(readFile("src/VariableTrackerExample.java"));
	}

	/**
	 * Checks if a character is a mathematical operator (of length 1) and returns corresponding token 
	 * @param ch Input character which is being checked
	 * @return TokenType Corresponding operator token if found, else returns null
	 */
	public static TokenType getOp(char ch)
	{
		switch (ch)
		{
			case '+' : return TokenType.OP_ADD;	
			case '-' : return TokenType.OP_LESS;	
			case '*' : return TokenType.OP_MULTIPLY;	
			case '/' : return TokenType.OP_DIVIDE;
			case '%' : return TokenType.OP_MOD;
			case '<' : return TokenType.OP_LESS;
			case '>' : return TokenType.OP_GREATER;
			case '!' : return TokenType.OP_NOT;
			case '=' : return TokenType.OP_ASSIGN;
			case '.' : return TokenType.OP_DOT;
			default : return null;
		}
	}
	
	/**
	 * Checks if a string is a mathematical operator (of length 2) and returns the corresponding token
	 * @param s Input string which is being checked
	 * @return TokenType Corresponding operator token if found, else returns null
	 */
	public static TokenType getOp(String s)
	{
		switch (s)
		{
			case "<=" : return TokenType.OP_LESSEQUAL;
			case ">=" : return TokenType.OP_GREATEREQUAL;
			case "==" : return TokenType.OP_EQUAL;
			case "!=" : return TokenType.OP_NOTEQUAL;
			case "&&" : return TokenType.OP_AND;
			case "||" : return TokenType.OP_OR;
			default : return null;
		}
	}
	
	/**
	 * Checks if a character is a symbol and returns the corresponding token
	 * @param ch Input character which is being checked
	 * @return TokenType Corresponding symbol token if found, else returns null
	 */
	public static TokenType getSymbol(char ch)
	{
		switch (ch)
		{
			case '(' : return TokenType.LEFT_PAREN;
			case ')' : return TokenType.RIGHT_PAREN;
			case '{' : return TokenType.LEFT_BRACE;
			case '}' : return TokenType.RIGHT_BRACE;
			case '[' : return TokenType.LEFT_BRACKET;
			case ']' : return TokenType.RIGHT_BRACKET;
			case ';' : return TokenType.SEMICOLON;
			case ':' : return TokenType.COLON;
			case ',' : return TokenType.COMMA;
			default : return null;
		}		
	}
	
	/**
	 * Checks if a string is a keyword and returns the corresponding token
	 * @param s Input string which is being checked
	 * @return TokenType Corresponding keyword token if found, else returns null
	 */
	public static TokenType getKeyword(String s)
	{
		switch (s)
		{
		case "if" : return TokenType.KEYWORD_IF;	
		case "else" : return TokenType.KEYWORD_ELSE;
		case "while" : return TokenType.KEYWORD_WHILE;
		case "return" : return TokenType.KEYWORD_RETURN;
		case "main" : return TokenType.KEYWORD_MAIN;		
		case "int" : return TokenType.KEYWORD_INT;
		case "double" : return TokenType.KEYWORD_DOUBLE;
		case "boolean" : return TokenType.KEYWORD_BOOLEAN;
		case "String" : return TokenType.KEYWORD_STRING;
		case "public" : return TokenType.KEYWORD_PUBLIC;		
		case "class" : return TokenType.KEYWORD_CLASS;
		case "void" : return TokenType.KEYWORD_VOID;
		case "for" : return TokenType.KEYWORD_FOR;
		case "case" : return TokenType.KEYWORD_CASE;
		case "switch": return TokenType.KEYWORD_SWITCH;
		case "static" : return TokenType.KEYWORD_STATIC;
		case "break" : return TokenType.KEYWORD_BREAK;
		case "continue" : return TokenType.KEYWORD_CONTINUE;
		case "default" : return TokenType.KEYWORD_DEFAULT;
		default : return null;
		}
	}
	
	/**
	 * Checks if a character is a letter and returns boolean
	 * @param ch Input character which is being checked
	 * @return boolean true if the character is a letter, else it returns false
	 */
	public static boolean isLetter(char ch)
	{
		if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z')
		{
			return true;
		}
		else return false;
	}

	/**
	 * Checks if a character is a digit and returns boolean
	 * @param ch Character which is being checked
	 * @return boolean true if the character is a digit, else it returns false
	 */
	public static boolean isDigit(char ch)
	{
		if (ch >= '0' && ch <= '9')
		{
			return true;
		}
		else return false;
	}

	/**
	 * Checks if a character is whitespace (Space, Tab space, or linebreak) and returns boolean
	 * @param ch Character which is being checked
	 * @return boolean true if character is whitespace, else returns false
	 */
	public static boolean isWhiteSpace(char ch)
	{
		switch (ch)
		{
			case ' ' : return true;
			case '	': return true;
			case '\r': return true;
			case '\n': return true;
			default : return false;
		}
	}

	/**
	 * Checks if a string is a valid value for a boolean (ie. "true" or "false") and returns boolean
	 * @param s String which is being checked
	 * @return boolean true if string is a valid value for a boolean ("true" or "false"), else it returns false
	 */
	public static boolean isBoolean(String s)
	{
		if (s.equals("true") || s.equals("false")) return true;
		return false;
	}

	/**
	 * Checks if a string is of valid format (ie. Is surrounded by double quotes) and returns boolean
	 * @param s String which is being checked
	 * @return boolean true if the string is of valid format, else returns false
	 */
	public static boolean isValidString(String s)
	{	
		//Returns true if the first and last characters of the string are "
		if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"')
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Reads target file into a string
	 * @param fname File destination of file which is to be read
	 * @return String containing content of target file
	 */
	public static String readFile(String fname)
	{
		String content = null;
		
		//Try to store fname in content
		try
		{
			content = new String(Files.readAllBytes(Paths.get(fname)));
			extractExecutable(fname); 
		}
		
		//If it fails, return an error
		catch (IOException e)
		{
			System.out.println("ERROR: FAIL TO READ FILE");
			System.exit(1);
		}
		return content;
	}
	
	/**
	 * Method which stores the java executable file name in javaFileName variable
	 * @param fDirect String The directory of the file name you wish to be stored
	 */
	public static void extractExecutable(String fDirect)
	{
		final int LENGTH = fDirect.length();
		boolean slashFound = false;
		
		//Go through string backwards
		for (int i = LENGTH - 1; i > 0; i--)
		{
			//When you find /
			if (fDirect.charAt(i) == '/')
			{
				//Extract the java file into a substring and save it
				//Note : The reason for -5 is because '.java' is 5 characters long
				javaFileName = fDirect.substring(i+1, LENGTH - 5);
				slashFound = true;
			}
		}
		//If you never find a /, the target file must be in the same directory
		if (!slashFound) javaFileName = fDirect;
	}
	
	/**
	 * Detects if a character is a character which should be separately assessed, as opposed to being categorized as an identifier
	 * @param ch the character which is being checked
	 * @return boolean true if the character should be assesed seperately, else returns false
	 */
	public static boolean permittedInIdentifier(char ch)
	{
		//If it's an operator,symbol, & or |, return true, else return false
		if (getOp(ch) != null || getSymbol(ch) != null) return true;
		else if (ch == '&' || ch == '|') return true;
		return false;
	}
	
	/**
	 * Updates tokenName,tokenType, and lineNumber ArrayLists with corresponding values
	 * @param tokenName String The token name
	 * @param tokenType TokenType The token type
	 * @param lineNumber int The line number
	 */
	public static void updateArrayLists(String tokenName, TokenType tokenType, int lineNumber)
	{
		tokenNameArrayList.add(tokenName);
		tokenTypeArrayList.add(tokenType);
		lineNumberArrayList.add(lineNumber);
	}
	
	/**
	 * Updates miniTokenName and miniTokenType ArrayLists with corresponding values
	 * @param tokenName String The token name
	 * @param tokenType TokenType The token type
	 */
	public static void updateMiniArrayLists(String tokenName, TokenType tokenType)
	{
		miniTokenNameArrayList.add(tokenName);
		miniTokenTypeArrayList.add(tokenType);
	}
	
	/**
	 * Searches a string for a specific character, and returns the index of the FIRST occurance of that character if it exists
	 * @param string The string you wish to be searched
	 * @param startIdx int The index from which you wish to begin the search (Inclusive)
	 * @param endIdx int The index you want to search until (Inclusive)
	 * @param key char The character you are looking for
	 * @return int The index of the key in the string, if found. If the key is not found, returns -1
	 */
	public static int linearSearch(String string,int startIdx, int endIdx, char key)
	{
		final int LENGTH = string.length();
		
		//If endIdx is out of range, correct it so it at it's max
		if (endIdx > LENGTH) endIdx = LENGTH - 1;
		
		//For every char between startIdx and endIdx
		for (int i = startIdx; i <= endIdx; i++)
		{
			//If the character at that index is the same as our key, return i
			if (string.charAt(i) == key) return i;
		}
		return -1;
	}
	
	/**
	 * Adds error message to errorArrayList, and flags an error
	 * @param errorMessage string The error message you wish to add 
	 * @param lineNumber The line number on which the error occurs (Optional
	 * @param errorArrayList StringArrayList The array list you wish to add the error message to
	 */
	public static void addError(String errorMessage, ArrayList<String> errorArrayList)
	{
		errorsFound = true;
		errorArrayList.add(errorMessage);
	}
	
	/**
	 * Checks if a the class name is an identifier
	 * @param classTokenPosition int The index of the KEYWORD_CLASS token in tokenTypeArrayList 
	 * @param TOKEN_TYPE_ARRAY_LIST_LENGTH int the length of tokenTypeArrayList
	 * @return boolean true if the class name is invalid, otherwise returns false
	 */
	public static boolean invalidClassCheck(int classTokenPosition, int TOKEN_TYPE_ARRAY_LIST_LENGTH)
	{		
		//If there is a token after it which is the same as 
		if (classTokenPosition + 1 < TOKEN_TYPE_ARRAY_LIST_LENGTH && tokenNameArrayList.get(classTokenPosition + 1).equals(javaFileName))
		{
			return false;
		}
		else return true;
	}
	
	/**
	 * Checks if a the main method is in the correct format
	 * @param mainTokenPosition int The index of the KEYWORD_MAIN token in tokenTypeArrayList
	 * @param TOKEN_TYPE_ARRAY_LIST_LENGTH int length of tokenTypeArrayList
	 * @return boolean true if main method is never found or is invalid, else returns false
	 */
	public static boolean invalidMainCheck(int mainTokenPosition, int TOKEN_TYPE_ARRAY_LIST_LENGTH)
	{
		int mainStartIdx = mainTokenPosition - 3;
		int mainEndIdx = mainTokenPosition + 6;
		
		//If there are not 3 tokens before main, and 6 tokens after main, return true
		if (mainStartIdx < 0 || mainEndIdx > TOKEN_TYPE_ARRAY_LIST_LENGTH + 1) return true;
		
		//Set up correct order of tokens
		TokenType[] properMainOrder = {TokenType.KEYWORD_PUBLIC, TokenType.KEYWORD_STATIC, TokenType.KEYWORD_VOID, TokenType.KEYWORD_MAIN, TokenType.LEFT_PAREN,
				TokenType.KEYWORD_STRING, TokenType.LEFT_BRACKET, TokenType.RIGHT_BRACKET, TokenType.IDENTIFIER, TokenType.RIGHT_PAREN};
		
		//Initialises variable used to loop through properMainOrder
		int j = 0;
		
		//For every token between mainStartIdx and mainEndIdx (Inclusive)
		for (int i = mainStartIdx; i <= mainEndIdx; i++)
		{	
			//If the token at that position does not match the expected token, return true
			if (tokenTypeArrayList.get(i) != properMainOrder[j]) return true;
			j++;
		}
		return false;
	}
	
	/**
	 * Searches for a given char in an ArrayList and returns the index of the FIRST occurance of it
	 * @param arrayList TokenType ArrayList which is to be searched
	 * @param key TokenType The token type we are looking for
	 * @return int The index of the key in the array list, if found, else returns -1
	 */
	public static int linearSearch(ArrayList<TokenType> arrayList, TokenType key)
	{
		final int LENGTH = arrayList.size();
		
		for (int i = 0; i < LENGTH; i++)
		{
			TokenType thisToken = arrayList.get(i);
			if (thisToken == key) return i;
		}
		return -1;
	}
	
	/**
	 * Searches for a given char in an ArrayList and returns the index of the FIRST occurance of it
	 * @param arrayList String ArrayList which is to be searched
	 * @param key char The character which you are looking for 
	 * @return int the index of the key in the array list, if found, else returns -1
	 */
	public static int linearSearch(ArrayList<String> arrayList, char key)
	{
		final int LENGTH = arrayList.size();
		
		for (int i = 0; i < LENGTH; i++)
		{
			String thisElement = arrayList.get(i);
			
			if (thisElement.length() == 1 && thisElement.charAt(0) == key)
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Finds the index of a given string in a given array list
	 * @param arrayList The string array list which is to be searched
	 * @param key the string which is being looked for
	 * @return int Index of the string in array list, if it doesn't exist returns -1
	 */
	public static int linearSearch(ArrayList<String> arrayList, String key)
	{
		final int LENGTH = arrayList.size();
		
		for (int i = 0; i < LENGTH; i++)
		{
			String thisElement = arrayList.get(i);
			
			if (thisElement.equals(key))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * This method performs an operation of 2 strings (Which must ONLY contain integer values) and returns string value containing the resulting integer
	 * @param a String The first string containing integer a
	 * @param b String The second string containing integer b
	 * @param operation TokenType The TokenType of the operation you wish to be performed
	 * @return String containing integer result of the operation
	 */
	public static String performOpStr(String a, String b, char operation)
	{
		//Converts strings to ints
		int aInt = Integer.parseInt(a);
		int bInt = Integer.parseInt(b);
		
		int resultInt = 0;
	
		//Performs operation and converts back to string
		if (operation == '*') resultInt = aInt * bInt;
		else if (operation == '/') resultInt = aInt / bInt;
		else if (operation == '%') 	resultInt = aInt % bInt;
		else if (operation == '+') resultInt = aInt + bInt;
		else if (operation == '-') resultInt = aInt - bInt;
	
		//Converts result back to string
		String resultString = Integer.toString(resultInt);
		return resultString;
	}
	
	/**
	 * Checks if the token before an = is an identifier
	 * @param equalsTokenPosition int The index of = token in tokenTypeArrayList
	 * @return boolean true if the token before the = is an identifier, else returns false
	 */
	public static boolean invalidAssignmentCheck(int equalsTokenPosition)
	{
		//If the token before = is not an identifier, return true, else return false
		if (tokenTypeArrayList.get(equalsTokenPosition - 1) != TokenType.IDENTIFIER) return true;
		return false;
	}
	
	/**
	 * Checks if a variable name is valid 
	 * @param varName The variable name which is being checked
	 * @return boolean true if the variable name is valid, else returns false
	 */
	public static boolean variableNameCheck(String varName)
	{
		final int VAR_NAME_LENGTH = varName.length();
		char currentChar = varName.charAt(0);
		
		//If the variable DOESN'T begin with '_' or a letter
		if ((currentChar == '_' || (isLetter(currentChar))) == false)
		{
			//Return false
			return false;
		}

		//For every subsequent character
		for (int i = 1; i < VAR_NAME_LENGTH; i++)
		{
			currentChar = varName.charAt(i);
			
			//If it's NOT a letter, digit, or _, return false
			if ((isLetter(currentChar) || isDigit(currentChar) || currentChar == '_') == false)
			{
				//Return false
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if a double has more than 1 decimal point
	 * @param doubleValue double which will be checked
	 * @return boolean true if the double contains more than 1 decimal point, else returns false
	 */
	public static boolean doubleDotCheck(String doubleValue)
	{
		final int DOUBLE_VALUE_LENGTH = doubleValue.length();
		int numOfDecimal = 0;
		
		//For every char in doubleValue
		for (int i = 0; i < DOUBLE_VALUE_LENGTH; i++)
		{
			char currentChar = doubleValue.charAt(i);
			
			//If you find a dot, increment counter
			if (currentChar == '.') numOfDecimal++;
			
			//If you've found 2 decimal points, return true
			if (numOfDecimal == 2) return true;
		}
		return false;
	}
	
	/**
	 * Checks if the condition which is being checked by an if statement is encased in parenthesis
	 * @param tokenPosition int The index of the KEYWORD_IF/KEYWORD_WHILE/KEYWORD_FOR token in tokenTypeArrayList which is being checked
	 * @param TOKEN_TYPE_ARRAY_LIST_LENGTH int length of tokenTypeArrayList
	 * @return boolean true if the if statement syntax if not correct, else returns false
	 */
	public static boolean missingParenCheck(int tokenPosition, int TOKEN_TYPE_ARRAY_LIST_LENGTH)
	{
		int currentLine = lineNumberArrayList.get(tokenPosition);
		int nextIndex = tokenPosition + 1;
		
		//If there is a token after it, which is (
		if (nextIndex < TOKEN_TYPE_ARRAY_LIST_LENGTH && tokenTypeArrayList.get(nextIndex) == TokenType.LEFT_PAREN)
		{
			//For every subsequent token
			while (nextIndex + 1 < TOKEN_TYPE_ARRAY_LIST_LENGTH)
			{
				//If it's on the same line
				if (lineNumberArrayList.get(nextIndex + 1) == currentLine)
				{
					//If it's a ), return false
					if (tokenTypeArrayList.get(nextIndex + 1) == TokenType.RIGHT_PAREN) return false;
				}
				else return true;
				
				//Look at the next token
				nextIndex++;
			}
		}
		
		//If there is no token after it, or the token after it is not (, return true
		return true;
	}
	
	/**
	 * Parses a program into tokens, placing the token name into tokenNameArrayList, and placing the type of token into tokenTypeArrayList.
	 * The line each number each token occurs on is also inserted into lineNumberArrayList
	 * @param prog String Source code in string format
	 */
	public static void determineTokens(String prog)
	{
		final int PROG_LENGTH = prog.length();
		String tokenName = null;
		TokenType tokenType = null;
		
		//For every char in prog
		for (int  i = 0; i < PROG_LENGTH;)
		{
			char currentChar = prog.charAt(i);
			int nextIndex = i+1;
			
			//If it's whitespace, continue
			if (isWhiteSpace(currentChar))
			{
				//If it's a new line (\r occurs before \n), increment line number
				if (currentChar == '\r')
				{
					lineNumber++;
				}
				i++;  
				continue;
			}
			
			//If it's a letter or '_'
			if (isLetter(currentChar) || currentChar == '_')
			{				
				//For every subsequent character
				while (nextIndex < PROG_LENGTH)
				{
					char nextCharacter = prog.charAt(nextIndex);
					
					//If it's not whitespace or a character which could possibly come after an identifier (Eg Arithmetic operators, brackets, etc)
					if ((isWhiteSpace(nextCharacter) || permittedInIdentifier(nextCharacter)) == false)
					{
						//Include it as part of the token
						nextIndex++;
					}
					else break;
				}
				
				//Stores substring in tokenName
				tokenName = prog.substring(i, nextIndex);
				
				//Assumes tokenType is a keyword until proven otherwise (Saves time checking only once)
				tokenType = getKeyword(tokenName);
                
            			//Determine type
                		if (isBoolean(tokenName)) tokenType = TokenType.BOOLEAN;
               	 		else if (tokenType == null) tokenType = TokenType.IDENTIFIER;
								
                		//Updates ArrayLists
                		updateArrayLists(tokenName,tokenType,lineNumber);
                
				//Update i such that on the next iteration, the string which was placed in the tokenNameArray is skipped
				i = nextIndex;
			}
			
			//If it's a digit
			else if (isDigit(currentChar))
			{				
				//Creates flag which is turned to true if the digit is a double. This is to avoid checking for a decimal point twice.
				boolean isDouble = false;
				
				//Creates flag which is turned to true if the token is found to include letters or '_'
				boolean isIdentifier = false;
				
				while (nextIndex < PROG_LENGTH)
				{
					char nextCharacter = prog.charAt(nextIndex);
					
					//If the next character is a letter or '_'
					if (isLetter(nextCharacter) || nextCharacter == '_')
					{	
						isIdentifier = true;
						isDouble = false;
						
						//For every subsequent character
						while (nextIndex < PROG_LENGTH)          
						{
							nextCharacter = prog.charAt(nextIndex);
							
							//If it's not whitespace or a character which is permitted in an identifier
							if ((isWhiteSpace(nextCharacter) || permittedInIdentifier(nextCharacter)) == false)
							{
								//Include it as part of the token
								nextIndex++;
							}
							else break;
						}
					}
					
					//If the next character is a digit, include it in the token
					else if (isDigit(nextCharacter)) nextIndex++;
					
					//If the next character is a decimal point
					else if (nextCharacter == '.')
					{	
						//It must be a double
						nextIndex++;
						isDouble = true;
					}
					else break;
				}
				
				//Determine type, update ArrayLists and i
				tokenName = prog.substring(i, nextIndex);
				if (isDouble) tokenType = TokenType.DOUBLE;
				else if (isIdentifier) tokenType = TokenType.IDENTIFIER;
				else tokenType = TokenType.INTEGER;
				updateArrayLists(tokenName, tokenType, lineNumber);
				i = nextIndex;
			}
			
			//If it's a symbol
			else if (getSymbol(currentChar) != null)
			{	
				//Update ArrayLists
				tokenName = prog.substring(i, nextIndex);
				tokenType = getSymbol(tokenName.charAt(0));
				updateArrayLists(tokenName,tokenType,lineNumber);
				i = nextIndex;	
				
				//if it's a brace, increment counter
				if (tokenType == TokenType.LEFT_BRACE || tokenType == TokenType.RIGHT_BRACE) numOfBraces++;
			}
				
			//If it's a "
			else if (currentChar == '"')
			{									
				while (nextIndex < PROG_LENGTH)
				{
					char nextCharacter = prog.charAt(nextIndex);
					
					//If the next character is a "
					if (nextCharacter == '"')
					{
						//You've found the end of the string            
						nextIndex++;
						break;
					}
					//If it's not, keep looking
					else nextIndex++;
				}
				
				//Update ArrayLists and i
				tokenName = prog.substring(i, nextIndex);
				tokenType = TokenType.STRING;
				updateArrayLists(tokenName,tokenType,lineNumber);
				i = nextIndex;
			}
				
			
			//If it's an operator
			else if (getOp(currentChar) != null)
			{
				char nextCharacter = prog.charAt(nextIndex);
				boolean endFound = false;
				boolean isComment = false;
				
				//If the character next to it is also an operator
				if (nextIndex < PROG_LENGTH && getOp(prog.charAt(nextIndex)) != null)
				{
					//If it's a single line comment
					if (currentChar == '/' && nextCharacter == '/')
					{
						isComment = true;
						
						//Look for a \r, store it's index in newlineIdx if found
						int newlineIdx = linearSearch(prog, nextIndex, PROG_LENGTH, '\r');
						
						//If you found it, increment line number
						if (newlineIdx != -1)
						{	
							//Increment line number
							lineNumber++;
							
							//Set i such that the line is skipped (+2 so \r\n are skipped - Saves time)
							i = newlineIdx + 2;
						}
					}
					
					//If it's a multi-line comment
					else if (currentChar == '/' && nextCharacter == '*')
					{
						isComment = true;
						
						//For every subsequent character
						while (nextIndex+1 < PROG_LENGTH)
						{
							char thisChar = prog.charAt(nextIndex+1);
							
							//If it's a \r
							if (thisChar == '\r')
							{
								//Increment line number
								lineNumber++;
								
								//Increment nextIndex by 2, such that \r\n are skipped
								nextIndex += 2;
							}
							
							//Else if it's a *
							else if (thisChar == '*')
							{
								//If there are any characters after *
								if (nextIndex + 2 < PROG_LENGTH)
								{
									//If the next character is /
									if (prog.charAt(nextIndex+2) == '/')
									{
										//You've found the end of the comment
										endFound = true;
										
										//If / isn't the last character in the program
										if (nextIndex+3 < PROG_LENGTH)
										{
											//Set i such that the program skips scanning the */
											i = nextIndex+3;
										}
										
										//Else if it is the last character, finish the scan
										else i = PROG_LENGTH;
									}
					
									//If the next character isn't /, keep looking
									else nextIndex++;
								}
							}
							
							//Else keep looking
							else nextIndex++;
							
							//If you found the end of the comment, stop looking
							if (endFound) break;
						}
						
						//If you have searched the whole program and still haven't found the end of the comment
						if (!endFound)
						{
							//Flag an error
							endOfCommentError = true;
							
							//Stop the scan
							i = PROG_LENGTH;
						}
					}
					
					//If it's a comment, continue
					if (isComment) continue;
					
					//If it's not a comment
					else
					{
						//Include the operator, store token name and type
						nextIndex++;
						tokenName = prog.substring(i, nextIndex);
						tokenType = getOp(tokenName);
					}
				}
				
				//If the next character is not an operator
				else	
				{
					//Store token name and type
					tokenName = prog.substring(i, nextIndex);
					tokenType = getOp(tokenName.charAt(0));
				}
				
				//Update ArrayLists and i
				updateArrayLists(tokenName,tokenType,lineNumber);
				i = nextIndex;	
			}
			
			//EDGE CASE : If it's & or |
			else if (currentChar == '&' || currentChar == '|')
			{
				//If the next character is also &
				if (nextIndex < PROG_LENGTH && prog.charAt(nextIndex) == currentChar)
				{
					//Include it
					nextIndex++;
				}
				
				//Update ArrayList and i
				tokenName = prog.substring(i, nextIndex);
				tokenType = getOp(tokenName);
				updateArrayLists(tokenName,tokenType,lineNumber);
				i = nextIndex;
			}
				
			//Else if the character is unrecognised in weeJava
			else
			{
				//Update ArrayLists and i
				tokenName = prog.substring(i, nextIndex);
				tokenType = null;
				updateArrayLists(tokenName,tokenType,lineNumber);
				i = nextIndex;
			}
		}
	}
	
	/**
	 * Checks scanned program for errors, and prints them if found, before terminating the program
	 */
	public static void errorChecking()
	{
		//Initialises an ArrayList which will store error messages
		ArrayList<String> errorArrayList = new ArrayList<String>();
		
		//Initialise the variable which will hold the error message
		String errorMessage = null;
		
		//Stores arraylist length, so it does not have to be calculated each iteration
		final int TOKEN_TYPE_ARRAY_LIST_LENGTH = tokenTypeArrayList.size();
		
		//For every token type
		for (int i = 0; i < TOKEN_TYPE_ARRAY_LIST_LENGTH; i++)
		{
			//Saves current token type
			TokenType currentTokenType = tokenTypeArrayList.get(i);
			
			//Saves current token name
			String currentTokenName = tokenNameArrayList.get(i);
			
			//Resets error message
			errorMessage = null;
									
			//Check for null first (null can't be used in switch statement)
			if (currentTokenType == null) 
			{
				errorMessage = "ERROR Line " + lineNumberArrayList.get(i) + ": Unrecognized character \"" + currentTokenName + "\"";
			}
			
			//Run checks based on possible errors which could occur for that token
			else
			{
				switch (currentTokenType)
				{
					case KEYWORD_CLASS: 
						//Class has been found
						classFound = true;
						
						//If the class name is invalid, or the class name is never found, flag it
						if (invalidClassCheck(i, TOKEN_TYPE_ARRAY_LIST_LENGTH)) classFound = false;
						
						if (!classFound) 
						{
							errorMessage = "ERROR : Could not find or load main class " + javaFileName;
						}
						break;
					
					case KEYWORD_MAIN:
						
						//The main method exists
						mainFound = true;
						
						//If the main method is invalid, flag an error
						if (invalidMainCheck(i, TOKEN_TYPE_ARRAY_LIST_LENGTH)) mainFound = false;
						
						//If the main method has not been found or is invalid
						if (!mainFound)
						{
							//Initialises error message to null
							errorMessage = null;
							
							//If class name is valid
							if (classFound)
							{						
								errorMessage = "ERROR : Main method not found in class " + javaFileName + ", please define the main method as : \n\t public static void main(String[] args)";
								break;
							}
							
							//If the class name is invalid
							else errorMessage = "ERROR : Main method not found in class, please define the main method as : \n\t public static void main(String[] args)";
						}
						break;
					
					case OP_ASSIGN:
						//Check if it's a valid assignment
						if (invalidAssignmentCheck(i))
						{
							errorMessage = "ERROR Line " + lineNumberArrayList.get(i) + ": The left-hand side of an assignment must be a variable";
						}
						break;
					
					case IDENTIFIER:
						//If it's an invalid identifier name
						if (variableNameCheck(currentTokenName) == false)
						{
							errorMessage = "ERROR Line " + lineNumberArrayList.get(i) + ": Invalid identifier name " + currentTokenName;
						}
						break;

					case DOUBLE:
						//If it has >1 decimal point
						if (doubleDotCheck(currentTokenName))
						{
							errorMessage = "ERROR Line " + lineNumberArrayList.get(i) + ": Syntax error on token \"" + currentTokenName + "\"" + ", too many decimal points";
						}
						break;

					case STRING:
						if (!isValidString(currentTokenName))
						{
							errorMessage = "ERROR Line " + lineNumberArrayList.get(i) + ": String literal is not properly closed by double quotes";
						}
						break;

					case KEYWORD_IF:
						//If the condition which is being checked is not encased in parenthesis
						if (missingParenCheck(i, TOKEN_TYPE_ARRAY_LIST_LENGTH))
						{
							errorMessage = "ERROR Line " + lineNumberArrayList.get(i) + ": Missing parenthesis after IF statement";
						}
						break;

					case KEYWORD_WHILE:
						//If the condition which is being checked is not encased in parenthesis
						if (missingParenCheck(i, TOKEN_TYPE_ARRAY_LIST_LENGTH))
						{
							errorMessage = "ERROR Line " + lineNumberArrayList.get(i) + ": Missing parenthesis after WHILE statement";
						}
						break;

					case KEYWORD_FOR:	
						//If the condition which is being checked is not encased in parenthesis
						if (missingParenCheck(i, TOKEN_TYPE_ARRAY_LIST_LENGTH))
						{
							errorMessage = "ERROR Line " + lineNumberArrayList.get(i) + ": Missing parenthesis after FOR statement";
						}
						break;
						
					default: break;
				}
			}
			
			//If there's an error, add it to the list
			if (errorMessage != null) addError(errorMessage, errorArrayList);
			
		}
		
		//If there is an odd number of curly braces
		if (numOfBraces % 2 != 0)
		{
			//The class body is therefore not closed
			errorMessage = "ERROR Line " + lineNumber + ": Insert \"}\" to complete ClassBody";
			addError(errorMessage, errorArrayList);
		}
		
		//If there is a comment which hasn't been closed properly
		if (endOfCommentError)
		{
			errorMessage = "ERROR Line " + lineNumber + ": Unexpected end of comment";
			addError(errorMessage, errorArrayList);
		}
		
		//If there are errors
		if (errorsFound)
		{
			//Print them
			for (String error : errorArrayList) System.out.println(error);
			
			//Exit the program, returning value 1, indicating something went wrong
			System.exit(1);
		}
	}
	
	/**
	 * Mini version of determineTokens method - This method parses a program which ONLY contains arithmetic operations between integers in the main method.
	 * This method will insert each token name and corresponding token type into ArrayLists : miniTokenNameArrayList and miniTokenTypeArrayList.
	 * NOTE : THIS METHOD SHOULD ONLY BE USED IF THE PROGRAM IS ERROR-FREE, AND ONLY CONTAINS INTEGER VALUES
	 * @param prog String Source code in string format
	 */
	public static void miniDetermineTokens(String prog)
	{
		//Stores length of entire prog
		final int PROG_LENGTH = prog.length();
		String tokenName = null;
		TokenType tokenType = null;
		
		//For every char in prog
		for (int  i= 0; i < PROG_LENGTH;)
		{
			char currentChar = prog.charAt(i);
			int nextIndex = i+1;
			
			//If it's whitespace, continue
			if (isWhiteSpace(currentChar))
			{
				i++;  
				continue;
			}
			
			//If it's a letter or '_'
			if (isLetter(currentChar) || currentChar == '_')
			{				
				//For every subsequent character
				while (nextIndex < PROG_LENGTH)
				{
					char nextCharacter = prog.charAt(nextIndex);
					
					//If it's not whitespace or a character which could possibly come after an identifier (Eg Arithmetic operators, brackets, etc)
					if ((isWhiteSpace(nextCharacter) || permittedInIdentifier(nextCharacter)) == false)
					{
						//Include it as part of the token
						nextIndex++;
					}
					else break;
				}
				
				//Stores substring in token name
				tokenName = prog.substring(i, nextIndex);
											
				//Assumes tokenType is a keyword until proven otherwise (Saves time checking only once)
				tokenType = getKeyword(tokenName);
				
				//If it's KEYWORD_INT, update ArrayLists
				if (tokenType == TokenType.KEYWORD_INT) updateMiniArrayLists(tokenName,tokenType);
				
				//Else if it's not a keyword(Excluding int) and is not a boolean, it must be an identifier
				else if (tokenType == null && !isBoolean(tokenName))
                {
    				tokenType = TokenType.IDENTIFIER; 
    				updateMiniArrayLists(tokenName,tokenType);
                }
												
				//Update i such that on the next iteration, the string which was placed in the miniTokenNameArray is skipped
				i = nextIndex;
			}
			
			//If it's a digit
			else if (isDigit(currentChar))
			{					
				while (nextIndex < PROG_LENGTH)
				{
					char nextCharacter = prog.charAt(nextIndex);
					
					//If the next character is a letter or '_'
					if (isLetter(nextCharacter) || nextCharacter == '_')
					{	
						tokenType = TokenType.IDENTIFIER;
						
						//For every subsequent character
						while (nextIndex < PROG_LENGTH)          
						{
							nextCharacter = prog.charAt(nextIndex);
							
							//If it's not whitespace or a character which is permitted in an identifier
							if ((isWhiteSpace(nextCharacter) || permittedInIdentifier(nextCharacter)) == false)
							{
								//Include it as part of the token
								nextIndex++;
							}
							else break;
						}
					}
					
					//If the next character is a digit, include it in the token
					else if (isDigit(nextCharacter)) nextIndex++;
					else break;
				}
				
				//Add tokenName to array
				tokenName = prog.substring(i, nextIndex);
				
				//If it wasn't an identifier, it must have been an integer
				if (tokenType != TokenType.IDENTIFIER) tokenType = TokenType.INTEGER;

				//Update miniArrayLists and i
				updateMiniArrayLists(tokenName, tokenType);
				i = nextIndex;
			}
			
			//If it's an operator
			else if (getOp(currentChar) != null)
			{		
				//Update miniArrayLists
				tokenName = prog.substring(i, nextIndex);
				tokenType = getOp(tokenName.charAt(0));
				updateMiniArrayLists(tokenName, tokenType);
				i = nextIndex;	
			}
			
			//If it's a ;, ( or )
			else if (getSymbol(currentChar) == TokenType.SEMICOLON || getSymbol(currentChar) == TokenType.LEFT_PAREN || getSymbol(currentChar) == TokenType.RIGHT_PAREN)
			{
				//Update miniArrayLists
				tokenName = prog.substring(i, nextIndex);
				tokenType = getSymbol(currentChar);
				updateMiniArrayLists(tokenName,tokenType);
				i = nextIndex;	
			}
			
			//Else skip to the next character (We don't care about any other tokens)
			else i++; continue;
		}
	}
	
	/**
	 * Calculates final value of all integer variables and stores them in variableValueArrayList
	 */
	public static void evaluateVariables()
	{
		ArrayList<String> expression = new ArrayList<String>();
		final int LENGTH = miniTokenTypeArrayList.size();
		
		//Go through miniTokenTypeArrayList
		for (int i = 0; i < LENGTH; i++)
		{
			TokenType currentToken = miniTokenTypeArrayList.get(i);
			
			//If current token is KEYWORD_INT
			if (currentToken == TokenType.KEYWORD_INT)
			{
				//The subsequent token must be a new identifier, so add it to the variableNameArrayList 
				//(We know i+1 exists, because we are assuming this program has no errors)
				String variable = miniTokenNameArrayList.get(i+1);
				variableNameArrayList.add(variable);
				
				//Also add null to the corresponding value array list, for now 
				//(This ensures both arrays are always at the same length)
				variableValueArrayList.add(null);
			}
			
			//If the current token is OP_ASSIGN
			else if (currentToken == TokenType.OP_ASSIGN)
			{
				//For every tokenName on the right hand side of the '=', until you reach a ;
				for (int j = 1; i+j < LENGTH; j++)
				{
					String thisToken = miniTokenNameArrayList.get(i+j);
					
					//If it's a ;
					if (thisToken.equals(";"))
					{						
						//Evaluate the expression on the right of the =, and store it in 'result'
						int result = evaluate(expression, i);
						
						//Get the variable which is on the left of the =
						String variable = miniTokenNameArrayList.get(i - 1);
						
						//Find the position of this variable in variableNameArrayList
						int variableIndex = linearSearch(variableNameArrayList, variable);
						
						//Update the corresponding variable value
						variableValueArrayList.set(variableIndex, result);
						
						//Clear temp array list
						expression.clear();
						
						//Stop looking
						break;
					}
					
					else expression.add(thisToken);
				}
			}
		}
	}
	
	/**
	 * Given an ArrayList containing a mathmatical expression, this method will perform the operations and return an integer result
	 * @param expression StringArrayList The expression you want to be evaluated, given in a string ArrayList
	 * @param assignIdx int The index of the OP_ASSIGN token in miniVariableTypeArrayList
	 * @return int Returns the integer result of the expression
	 */
	public static int evaluate(ArrayList<String> expression, int assignIdx)
	{
		removeVariables(expression, assignIdx);
		return performBimdas(expression);
	}
	
	/**
	 * Replaces all unknown variables in an ArrayList with their known values
	 * @param expression The ArrayList which you wish to remove the variables from
	 * @param assignIdx int The index of the OP_ASSIGN token in miniVariableTypeArrayList
	 */
	public static void removeVariables(ArrayList<String> expression, int assignIdx)
	{
		int length = expression.size();
		
		//For every element in the ArrayList
		for (int i = 0; i < length; i++)
		{
			String thisElement = expression.get(i);
			TokenType thisTokenType = miniTokenTypeArrayList.get(assignIdx + (i + 1));
			
			//If it's not operator,bracket, or integer
			if (getOp(thisElement.charAt(0)) == null && thisTokenType != TokenType.INTEGER
				&& thisTokenType != TokenType.LEFT_PAREN && thisTokenType != TokenType.RIGHT_PAREN)
			{
				//It must be a variable
				String targetVariable = expression.get(i);
				
				//Search our array list which contains every Initialised integer variable
				final int VARIABLE_LIST_SIZE = variableNameArrayList.size();
				
				for (int j = 0; j < VARIABLE_LIST_SIZE; j++)
				{
					String thisVariable = variableNameArrayList.get(j);
					
					//If we have already seen the target variable
					if (thisVariable.equals(targetVariable))
					{
						//Replace the variable in arrayList with the known integer literal
						String intLiteral = Integer.toString(variableValueArrayList.get(j));
						expression.set(i, intLiteral);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Given an expression in String ArrayList format, calculates the answer and returns it
	 * @param expression StringArrayList The expression in a string array list which you want to be evaluated
	 * @return int Result of expression
	 */
	public static int performBimdas(ArrayList<String> expression)
	{
		//If there's only 1 number, return it
		int length = expression.size();		
		if (length == 1) return Integer.parseInt(expression.get(0));
		
		ArrayList<String> subExpression = new ArrayList<String>();

		//If there's a ( in the expression, find it's index
		int openParenthesisPosition = linearSearch(expression, '(');
		
		//If you found a (
		if (openParenthesisPosition != -1)
		{
			//Initialise offset, so we can find the ) which corresponds to our (
			int parenOffset = 0;
			
			//For every tokenName after that
			for (int i = openParenthesisPosition + 1; i < length; i++)
			{
				String thisTokenName = expression.get(i);
				
				//If you find another (, increment offset
				if (thisTokenName.equals("(")) parenOffset++;
				
				//If you find a )
				if (thisTokenName.equals(")"))
				{
					//If it's the corresponding one we're looking for, stop adding tokens to subExpression
					if (parenOffset == 0) break;
					
					//Else decrement offset
					else parenOffset--;
				}
				
				//Add token to subExpression
				subExpression.add(thisTokenName);
			}
			
			//Stores length of subExpression
			final int SUB_EXPRESSION_LENGTH = subExpression.size();
			
			//Perform bimdas on this subExpression
			int resultInt = performBimdas(subExpression); 
			String resultStr = Integer.toString(resultInt);
			
			//Stores result in position of ( in original expression
			expression.set(openParenthesisPosition, resultStr);
			
			//Removes values that were between the ( and ), including )
			for (int i = 0; i < SUB_EXPRESSION_LENGTH + 1; i++)
			{
				expression.remove(openParenthesisPosition + 1);
			}
			
			//Perform bimdas on expression again
			performBimdas(expression);
		}
		
		//Else if the expression is free of parenthesis
		else
		{
			//If there's only 1 token, return it
			length = expression.size();
			if (length == 1) return Integer.parseInt(expression.get(0));
			
			//If there are 2 tokens
			if (length == 2)
			{
				//If the first one is a -
				if (expression.get(0).equals("-"))
				{
					//Convert the integer that follows it to a negative, and return it
					String resultStrPositive = expression.get(1);
					int resultIntPositive = Integer.parseInt(resultStrPositive);
					int resultIntNegative = resultIntPositive * -1;
					return resultIntNegative;
				}
			}
			
			//Perform operations in correct order
			char[] bimdas = {'*','/','%','+','-'};
			for (int i = 0; i < 5; i++)
			{
				char currentOperator = bimdas[i];
				int operatorPosition = linearSearch(expression, currentOperator);
				
				//If you find an operator
				if (operatorPosition != -1)
				{
					//Stores positions of numbers which are being multiplied
					String previousElement = expression.get(operatorPosition - 1);
					String nextElement = expression.get(operatorPosition + 1);
					
					//Stores result
					String result = performOpStr(previousElement, nextElement, currentOperator);
					
					//Store the result in the position the 'previous element' was
					expression.set(operatorPosition - 1, result);
					
					//Remove the operator and the 'nextElement'
					expression.remove(operatorPosition);
					expression.remove(operatorPosition);
					
					//Set decrease i by 1, so that multiple of the same operators are assesed
					i -= 1;
				}
			}
		}
		
		//There will be 1 element left in the expression ArrayList, so return the integer representation of this element
		String resultStr = expression.get(0);
		int resultInt = Integer.parseInt(resultStr);
		return resultInt;
	}
	
	/**
	 * Prints all the info in : lineNumberArrayList, tokenTypeArrayList, and tokenNameArrayList
	 */
	public static void printTokenInfo()
	{
		for (int i = 0; i < tokenNameArrayList.size(); i++)
		{
			System.out.println(lineNumberArrayList.get(i) + ", " + tokenTypeArrayList.get(i) + ", " + tokenNameArrayList.get(i));
		}
	}
	
	/**
	 * Prints all integer variables and their values
	 */
	public static void printVariableInfo()
	{
		final int LENGTH = variableValueArrayList.size();
		
		for (int i = 0; i < LENGTH; i++)
		{
			System.out.println(variableNameArrayList.get(i) + " : " + variableValueArrayList.get(i));
			System.out.println("");
		}
	}
	
	/**
	 * Scans a program into 'tokens' before checking the program for compile-time errors. If errors are found, these are returned.
	 * Otherwise, each token is printed, along with it's type and the line number it occurs on
	 * @param prog String The program you wish to be scanned in string format
	 */
	public static void scan(String prog)
	{
		//Put corresponding data into tokenNameArrayList, tokenTypeArrayList, and lineNumberArrayList
		determineTokens(prog);
		
		//Check for errors
		errorChecking();
		
		//Print the data
		printTokenInfo();
	}
	
	/**
	 * Scans a program for variables and keeps track of the values of these variables throughout the program.
	 * This method will print out the final value of each integer variable in the program
	 * @param prog The program you wish to scan for variables
	 */
	public static void variableScan(String prog)
	{
		//Put corresponding data into miniTokenNameArrayList, miniTokenTypeArrayList
		miniDetermineTokens(prog);
		
		//Calculate final value of all integer variables and stores them in variableValueArrayList
		evaluateVariables();
		
		//Print the data
		printVariableInfo();
	}
}
