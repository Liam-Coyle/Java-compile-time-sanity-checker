
//Error = Unrecognized character
public class ErrorExample2 
{
	public static void main(String[] args)
	{	
		boolean x = true;
		boolean y = false;
		
		// # is not a recognized character in weeJava
		#
		
		// $ is not a recognized character in weeJava
		$
		
		// ^ is not a recognized character in weeJava
		^
		
		// ~ is not a recognized character in weeJava
		~
				
		//'&' and '|' are also unrecognized
		if (x == true & y == true)
		{
			System.out.println("x and y are true");
		}
		
		else if (x == true | y == true)
		{
			System.out.println("x is true, or y is true, or both are true");
		}
		
		//However, && and || still work
		if (x == true && y == true)
		{
			System.out.println("x and y are true");
		}
		
		else if (x == true || y == true)
		{
			System.out.println("x is true, or y is true, or both are true");
		}
		
		//These characters can still be printed
		System.out.println("#$^~&|");
		
		//These characters can still be included in comments : #$%^~
		
		//Note : Every character which is not included in weeJava is considered unrecognized. I have only listed a few here.
	}
}


