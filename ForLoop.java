package opsys;

public class ForLoop extends UserLandProcess
{
	//will not be forced to demote
	void main() 
	{
		while(true)
		{
			System.out.println("For Loop");
			try
			{
				Thread.sleep(10);
			}
			catch(Exception e)
			{
				
			}			
			cooperate();
			OS.sleep(50);
		}
	}

}
