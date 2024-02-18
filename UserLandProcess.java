package opsys;
import java.util.concurrent.Semaphore;

public abstract class UserLandProcess implements Runnable
{
	//members
	private Thread thread;
	private Semaphore sem;
	private boolean isExpired;
	private PCB pcb;
	
	//constructer 
	public UserLandProcess()
	{
		this.thread = new Thread(this);
		this.sem = new Semaphore(0);
		this.isExpired = false;
		thread.start();
	}
	
	/**
	 * sets the boolean indicating that this process’ quantum has expired
	 */
	public void requestStop() 
	{
		OS.debugMessage("UserLandProcess.requestStop()");
		isExpired = true;
	} 
	
	/**
	 * will represent the main of our “program”
	 */
	abstract void main();
	
	/**
	 * indicates if the semaphore is 0
	 * @return true/false
	 */
	public boolean isStopped() 
	{
		OS.debugMessage("UserLandProcess.isStopped()");
		return sem.availablePermits() == 0;
	}
	
	/**
	 * true when the Java thread is not alive
	 * @return true/false
	 */
	public boolean isDone() 
	{
		OS.debugMessage("UserLandProcess.isDone()");
		return !thread.isAlive();
	}
	
	/**
	 * releases (increments) the semaphore, allowing this thread to run
	 */
	public void start()
	{
		OS.debugMessage("UserLandProcess.start()");
		sem.release();
	}
	
	/**
	 * acquires (decrements) the semaphore, stopping this thread from running
	 */
	public void stop() 
	{
		OS.debugMessage("UserLandProcess.stop()");
		try 
		{
			sem.acquire();
		} 
		catch (InterruptedException e) 
		{ 
			
		}
		
	}
	
	/**
	 * acquire the semaphore, then call main
	 */
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
	
	/**
	 * if the boolean is true, set the boolean to false and call OS.switchProcess()
	 */
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
	
	/**
	 * causes there to be a thread to begin execution.
	 */
	public void execute()
	{
		OS.debugMessage("UserLandProcess.execute()");
		thread.start();
	}
	
	/**
	 * 
	 * @param pcb sets it to equal the current pcb
	 */
	public void setPCB(PCB pcb)
	{
		OS.debugMessage("UserLandProcess.setPCB");
		this.pcb = pcb;
	}
	
	/**
	 * gets the process ID
	 * @return the Process ID 
	 */
	public int getPID()
	{
		OS.debugMessage("UserLandProcess.getPID()");
		return pcb.getPid();
	}

}
