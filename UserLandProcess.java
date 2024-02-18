package opsys;
import java.util.concurrent.Semaphore;

public abstract class UserLandProcess implements Runnable
{
	
	private Thread thread;
	private Semaphore sem;
	private boolean isExpired;
	
	public UserLandProcess()
	{
		this.thread = new Thread(this);
		this.sem = new Semaphore(0);
		this.isExpired = false;
		thread.start();
	}
	
	//sets the boolean indicating that this process’ quantum has expired
	public void requestStop() 
	{
		isExpired = true;
	} 
	
	// will represent the main of our “program”
	abstract void main();
	
	//indicates if the semaphore is 0
	public boolean isStopped() 
	{
		return sem.availablePermits() == 0;
	}
	
	//true when the Java thread is not alive
	public boolean isDone() 
	{
		return !thread.isAlive();
	}
	
	//releases (increments) the semaphore, allowing this thread to run
	public void start()
	{
		OS.debugMessage("UserLandProcess.start()");
		sem.release();
	}
	
	//acquires (decrements) the semaphore, stopping this thread from running
	public void stop() 
	{
		try 
		{
			sem.acquire();
		} 
		catch (InterruptedException e) 
		{ 
			
		}
		
	}
	
	//acquire the semaphore, then call main
	public void run() 
	{
		OS.debugMessage("UserLandProcess.run()");
		try 
		{
			sem.acquire();
		} 
		catch (InterruptedException e) 
		{
			
		} 
		
		main();
	} 
	
	//if the boolean is true, set the boolean to false and call OS.switchProcess()
	public void cooperate() 
	{
		OS.debugMessage("UserLandProcess.cooperate()");
		if (isExpired == true)
		{
			//sets bool to false
			isExpired = false;
			
			//calls on the OS to switch Process
			OS.switchProcess();
		}
	} 
	
	public void execute()
	{
		thread.start();
	}

}
