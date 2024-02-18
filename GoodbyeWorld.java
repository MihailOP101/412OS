package opsys;

public class GoodbyeWorld extends UserLandProcess
{
	//creates an infinite loop to print out Goodbye World
	public void main() 
	{
		while(true)
		{
			System.out.println("Goodbye World");
			try
			{
				Thread.sleep(20);
			}
			catch(Exception e)
			{
				
			}			
			cooperate();
			OS.sleep(100);
		}
	}
}
