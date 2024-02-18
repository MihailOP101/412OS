package opsys;

public class IdleProcess extends UserLandProcess
{
	//This works while the program is in an idle process
	void main() 
	{
		while (true)
		{
			cooperate();
			try
			{
				Thread.sleep(50); //thread sleeps for 50 milliseconds
			}
			catch (InterruptedException e) 
			{
				Thread.currentThread().interrupt();
			} 
		}
		
	}
}
