package opsys;

import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.time.Clock;

public class Scheduler 
{
	private LinkedList<PCB> backgroundList;
	private LinkedList<PCB> interactiveList;
	private LinkedList<PCB> realTimeList;
	private LinkedList<SleepEntry> sleepList;
	private Timer time;
	public PCB currentProcess;
	public static Clock clock = Clock.systemDefaultZone();
	
	public Scheduler()
	{
		backgroundList = new LinkedList<>();
		interactiveList = new LinkedList<>();
		realTimeList = new LinkedList<>();
		sleepList = new LinkedList<>();
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
			checkToDemote();
			currentProcess.requestStop();
		}	
	}
	
	/**
	 * Creates a new process based on the UserLandProcess
	 * @param up uses the UserLandProcess to see if there's a 
	 * @return pid of up
	 */
	public int CreateProcess(UserLandProcess up, Priority priority)
	{
		OS.debugMessage("Scheduler.Createprocess()");
	    PCB pcb = new PCB(up, priority);
		if (currentProcess == null)
		{
			currentProcess = pcb;
		}
		else
		{
			placeInList(pcb);
		}
	    return pcb.getPid();
	}
	
	
	public void placeInList(PCB pcb)
	{
		switch (pcb.getPriority()) 
	    {
	    	case REAL_TIME:
	    		OS.debugMessage("Scheduler.placeInList() : REAL_TIME end of list \t" + pcb.toString());
	    		realTimeList.add(pcb);
	    		break;
	         case INTERACTIVE:
	        	 OS.debugMessage("Scheduler.placeInList() : INTERACTIVE end of list \t" + pcb.toString());
	        	 interactiveList.add(pcb);
	        	 break;
	         case BACKGROUND:
	        	 OS.debugMessage("Scheduler.placeInList() : BACKGROUND end of list \t" + pcb.toString());
	        	 backgroundList.add(pcb);
	        	 break;
	    }
	}
	
	/**
	 * Checks if the current process is done and if the queue is empty before switching process
	 * @throws Exception for when the queue is empty and the current process is done
	 */
	public void SwitchProcess()
	{
		OS.debugMessage("Scheduler.SwitchProcess()" );
		
		if(currentProcess.isDone())
		{
			OS.debugMessage("Scheduler.SwitchProcess() : if Process is done");
			if (backgroundList.isEmpty())
			{
				System.out.println("Background List is empty");
			}
		}
		else
		{
			OS.debugMessage("Scheduler.SwitchProcess() : else Process is not done");
			placeInList(currentProcess);
		}
		
		currentProcess = getNextProcess();
		
		
		while(sleepList.size() > 0 && sleepList.get(0).getMillisecWait() > clock.millis())
		{
			if(sleepList.get(0).getMillisecWait() < clock.millis())
			{
				//put process back in priority queues
				placeInList(sleepList.get(0).getProcess());
			}
		}
	}	
	
	/**
	 * First it adds an interrupt counter and 
	 * if the counter if greater than or equal to 5 
	 * it will demote it down a process type
	 * so it doesn't take up all our resources 
	 */
	public void checkToDemote()
	{
		OS.debugMessage("Scheduler.checkToDemote()");
		currentProcess.setInterruptCounter(currentProcess.getInterruptCounter() + 1);
		System.out.println(currentProcess + " counter is now: " + currentProcess.getInterruptCounter());
		if(currentProcess.getInterruptCounter() >= 5)
		{
			currentProcess.setInterruptCounter(0);
			switch(currentProcess.getPriority())
			{
				case REAL_TIME:
					OS.debugMessage("Scheduler.checkToDemote() : Real_Time");
					currentProcess.setPriority(Priority.INTERACTIVE);
					System.out.println(currentProcess + " new Priority is " + currentProcess.getPriority());
					break;
				case INTERACTIVE:
					OS.debugMessage("Scheduler.checkToDemote() : Interactive");
					currentProcess.setPriority(Priority.BACKGROUND);
					System.out.println(currentProcess + " new Priority is " + currentProcess.getPriority());
					break;
				default:
					System.out.println(currentProcess + " is already at Background");
					break;
			}
		}
	}
	
	
	/**
	 * 
	 * @return the first of the linkedList
	 */
	private PCB getNextProcess()
	{
		OS.debugMessage("Scheduler.getNextProcess()");
		Random random = new Random();
		int probability = random.nextInt(10);
		
		if (!realTimeList.isEmpty() && probability < 6)
		{
			OS.debugMessage("Scheduler.getNextProcess() : realTimeList\t" + probability + realTimeList.toString());
			return realTimeList.poll();
		}
		else if (!interactiveList.isEmpty() && probability < 9)
		{
			OS.debugMessage("Scheduler.getNextProcess() : interactiveList\t" + probability + interactiveList.toString());
			return interactiveList.poll();
		}
		else
		{
			OS.debugMessage("Scheduler.getNextProcess() : backgroundList\t" + probability + backgroundList.toString());
			return backgroundList.poll();
		}
	}
	
	/**
	 * 
	 */
	private static class SleepEntry
	{
		PCB currentProcess;
		long millisecWait;
		
		
		SleepEntry(PCB currentProcess, long millisecWait)
		{
			this.currentProcess = currentProcess;
			this.millisecWait = millisecWait;
		}
		
		PCB getProcess()
		{
			OS.debugMessage("Scheduler : SleepEntry.getProcess()");
			return currentProcess;
		}
		
		long getMillisecWait()
		{
			OS.debugMessage("Scheduler : SleepEntry.getMillisecWait()");
			return millisecWait;
		}
	}
	
	/*
	 * sets the time in milliseconds for the task to pause
	 */
	public void sleep(int milliseconds) 
	{
		OS.debugMessage("Scheduler.sleep()");
		currentProcess.setInterruptCounter(0);;
		long millisecWait = (long) clock.millis() + milliseconds;
		SleepEntry sleepEntry = new SleepEntry(currentProcess, millisecWait);
		for (int i = 0; i < sleepList.size(); i++)
		{
			if (millisecWait < sleepList.get(i).getMillisecWait())
			{
				sleepList.add(i, sleepEntry);
			}
		}
		SwitchProcess();
	}
}
