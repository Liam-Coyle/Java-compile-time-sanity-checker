
public class VariableTrackerExample
{
	public static void main(String[] args)
	{	
		int a = 190;
		
		int b =-5;
		
		int c = a * b;
		
		a = a - 20;
		
		int d = 5;
		
		int result = a+(2%((b-c)*(d/2)));
		
		System.out.println("The result is " + result);
	}
}
