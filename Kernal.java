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
		OS.debugMessage("Kernal.start()");
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
				OS.debugMessage("Kernal.run() : mySemaphore.aquire");
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			switch(OS.currentCall)
			{
			case CREATE_PROCESS:
				OS.debugMessage("Kernal.run() : Create_process");
				createProcess();
				break;
			case SWITCH_PROCESS:
				OS.debugMessage("Kernal.run() : Switch_process");
				switchProcess();
				break;
			}
			
			OS.debugMessage("Kernal.run() : currentProcess.start()");
			scheduler.currentProcess.start();
			
		}
		
	}

	private void switchProcess()
	{
		OS.debugMessage("Kernal.switchProcess()");
		try
		{
			scheduler.SwitchProcess();
			OS.debugMessage("Kernal.switchProcess() : scheduler.SwitchProcess()");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}

	private void createProcess()
	{
		OS.debugMessage("Kernal.createProcess");
		if (OS.parameters.get(0) instanceof UserLandProcess)
		{
			//The CreateProcess method is returning the PID of the new UserLandProcess
			OS.returnValue = scheduler.CreateProcess((UserLandProcess)OS.parameters.get(0));
			OS.debugMessage("Kernal.createProcess : OS.returnValue");
		}
		else
		{
			System.out.println("The parameter is not an instance of UserLandProcess");
		}
		
	}

}
