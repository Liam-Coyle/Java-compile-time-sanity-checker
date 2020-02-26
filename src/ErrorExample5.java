
//Error = Missing parenthesis after if/while/for statement
public class ErrorExample5
{
	public static void main(String[] args)
	{	
		boolean valid = true;
		
		//Valid if statement
		if (valid)
		{
			System.out.println("Valid");
		}
		
		//Invalid if statement (Missing both parenthesis)
		if !valid
		{
			System.out.println("Invalid");
		}
		
		//Valid while loop
		int i = 0;
		
		while (i < 3)
		{
			System.out.println("i = " + i);
			i = i + 1;
		}
		
		//Invalid while loop (Missing closing parenthesis)
		while (i < 3
		{
			System.out.println("i = " + i);
			i = i + 1;
		}
		
		//Valid for loop
		for (int j = 0; j < 3; j = j + 1)
		{
			System.out.println("j = " + j);
		}
		
		//Invalid for loop (Missing opening parenthesis)
		for int j = 0; j < 3; j = j + 1)
		{
			System.out.println("j = " + j);
		}
	}
}