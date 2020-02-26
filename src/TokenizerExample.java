
public class TokenizerExample 
{
	/**
	 * This class is used to show how the Tokenizer parses a java program
	 * @author Liam Coyle 40270954 lcoyle21@qub.ac.uk
	 */
	
	public static void main(String[] args) 
	{
		String s = "This is a string literal";
		
		s = "Showcasing boolean literals, identifiers and dots";
		boolean b1 = true;
		boolean b2 = false;
		System.out.println("b1 is " + b1 + " and b2 is " + b2);
		
		s = "Showcasing int/double literals";
		int x =10;
		double y=0.5;
		System.out.println("x is " + x + " and y is " + y);
		
		s = "Showcasing arithmetic operands";
		double c = 10.53;
		c= x * y;
		c =x / y;
		c = x% y;
		c = x +y;
		c=x-y;
		System.out.println("c is " + c);
		
		s = "Showcasing comparison operands and if/else if keywords";
		if (x < y) System.out.println("x is less than y");
		if (x<= y) System.out.println("x is less thanor equal to y");
		if (x >y) System.out.println("x is greater than y");
		if (x>=y) System.out.println("x is greater than or equal to y");
		if(x == y) System.out.println("x equals y");
		else if (x!= y) System.out.println("x does not equal y");
		
		s = "Showcasing and,not,or,while keyword, and break keyword";
		if (b1&&b2) System.out.println("b1 and b2 are true");
		if (!b1) System.out.println("b1 is false");
		while(b1||b2) {System.out.println("While b1 or b2 is true, do this"); break;}
		
		s = "Showcasing for keyword and continue";
		for (int i = 0; i < 5; i = i+1)
		{
			if (i == 3) continue;
			System.out.println("i is " + i);
		}
		
		s = "Showcasing switch keyword, case keyword, colon, and default keyword";
		switch(x)
		{
			case 10: System.out.println("x = 10"); break;
			default: System.out.println("x is not 10"); break;
		}
		
		s = "Showcasing comma";
		int result = add(1,2);
		System.out.println("1 + 2 = " + result);
		
		
		s = "Showing how comments are skipped by the Tokenizer";
		//This is a comment
		//This is a comment containing code: int x = 5;

		System.out.println("The final value of s is : " + s);
	}
	
	public static int add(int a, int b)
	{
		String s = "'add' method successfully returned, Showcasing return keyword";
		System.out.println(s);
		return a +b;
	}
}