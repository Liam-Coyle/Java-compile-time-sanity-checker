
//Error = String literal is not properly closed by double quotes 
public class ErrorExample10
{
	public static void main(String [] args)
	{
		//Valid string
		String valid = "This string is properly closed";
		
		//Valid string containing \n
		String valid2 = "This string is properly closed \n";
		
		//Invalid string
		String invalid = "This string is never closed
		
		//This code is still checked for errors - It is not included as part of the string
		int x = 5;
	}
}