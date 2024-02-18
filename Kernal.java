package opsys;

import java.util.concurrent.Semaphore;

public class Kernal implements Runnable
{
	public Scheduler scheduler;
	private Thread thread;
	private Semaphore mySemaphore;
	
	public Kernal()
	{
		scheduler = new Scheduler();
		thread = new Thread(this);
		mySemaphore = new Semaphore(0);
		
		thread.start();
	}
	
	public void start()
	{
		OS.debugMessage("Kernal.start()\t" + mySemaphore);
		mySemaphore.release();
	}

	public void run() 
	{
		OS.debugMessage("Kernal.run()");
		while(true)
		{
			try 
			{
				mySemaphore.acquire(); //see if i should be running
				OS.debugMessage("Kernal.run() : mySemaphore.aquire \t" + mySemaphore);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			switch(OS.currentCall)
			{
				case CREATE_PROCESS:
					OS.debugMessage("Kernal.run() : while.try.Create_process");
					createProcess();
					break;
				case SWITCH_PROCESS:
					OS.debugMessage("Kernal.run() : while.try.Switch_process");
					switchProcess();
					break;
				case SLEEP:
					OS.debugMessage("Kernal.run() : while.try.Sleep");
					sleep();
					break;
			}
			
			OS.debugMessage("Kernal.run() : currentProcess.start()");
			scheduler.currentProcess.start();
			
		}
		
	}

	private void switchProcess()
	{
		OS.debugMessage("Kernal.switchProcess()");
		scheduler.SwitchProcess();
	}

	private void createProcess()
	{
		OS.debugMessage("Kernal.createProcess()");
		if (OS.parameters.get(0) instanceof UserLandProcess && OS.parameters.get(1) instanceof Priority)
		{
			//The CreateProcess method is returning the PID of the new UserLandProcess
			OS.returnValue = scheduler.CreateProcess((UserLandProcess)OS.parameters.get(0), (Priority)OS.parameters.get(1));
			OS.debugMessage("Kernal.createProcess : OS.returnValue");
		}
		else 
		{
			OS.returnValue = -1;
			System.out.println("The parameter is not an instance of UserLandProcess or second parameter is not instance of Priority");
		}
		
	}
	/**
	 * calls the schedule's sleep to set the amount of time to sleep 
	 * @param milliseconds the amount of milliseconds it will sleep for
	 */
	public void sleep()
	{
		OS.debugMessage("Kernal.sleep()");
		if (OS.parameters.get(0) instanceof Integer)
		{
			scheduler.sleep((Integer)OS.parameters.get(0));
			
		}
	}

}
