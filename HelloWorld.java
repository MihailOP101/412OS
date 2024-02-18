package opsys;

public class HelloWorld extends UserLandProcess
{
	//creates an infinite loop to print out Hello World
	public void main() 
	{
		while(true)
		{
			System.out.println("Hello World");
			try
			{
				Thread.sleep(50);
			}
			catch(Exception e)
			{
				
			}
			cooperate();
		}
	}
}
