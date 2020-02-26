
//Error = Too many decimal points in double
public class ErrorExample4 
{
	public static void main(String[] args)
	{	
		//Valid double initialization
		double x = 2.5;
		
		//Invalid double initialization
		double y = 2..5;
		
		//Also works outside of initializations
		x = x + 3...2;
	}
}