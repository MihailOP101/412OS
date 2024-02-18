package opsys;

public class IfLoop extends UserLandProcess
{
	//will be demoted forcefully
	void main() 
	{
		while(true)
		{
			System.out.println("If Loop");
			try
			{
				Thread.sleep(100);
			}
			catch(Exception e)
			{
				
			}			
			cooperate();
		}
	}
}
