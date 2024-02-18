package opsys;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler 
{
	private LinkedList<UserLandProcess> queueList;
	private Timer time;
	public UserLandProcess currentProcess;
	
	public Scheduler()
	{
		queueList = new LinkedList<>();
		interrupt pauseTask = new interrupt();
		time = new Timer();
		time.schedule(pauseTask, 250, 250);
	}
	
	/**
	 * Creates a mini class that interrupts the current process
	 */
	private class interrupt extends TimerTask
	{
		public void run() 
		{
			OS.debugMessage("Scheduler.interrupt.run()");
			currentProcess.requestStop();
		}	
	}
	
	/**
	 * Creates a new process based on the UserLandProcess
	 * @param up uses the UserLandProcess to see if theres a 
	 * @return pid of up
	 */
	public int CreateProcess(UserLandProcess up)
	{
		OS.debugMessage("Scheduler.Createprocess()");
		if (currentProcess == null)
		{
			currentProcess = up;
		}
		else
		{
			queueList.add(up);
		}
		
		return 0; //to be added 
	}
	
	/**
	 * Checks if the current process is done and if the queue is empty before switching process
	 * @throws Exception for when the queue is empty and the current process is done
	 */
	public void SwitchProcess() throws Exception
	{
		if(currentProcess.isDone())
		{
			if (queueList.isEmpty())
			{
				throw new Exception("Queue is empty");
			}
			currentProcess = queueList.removeFirst();
		}
		
		else
		{
			queueList.addLast(currentProcess);
			currentProcess = queueList.removeFirst();
		}
	}	
}
