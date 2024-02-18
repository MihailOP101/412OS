package opsys;

/**
 * Process Control Block (PCB) will control the queues of the 
 */
public class PCB 
{
	//members
	private static int nextPid = 0;
	private int pid;
	private UserLandProcess HoldULP;
	private Priority priority;
	private int counter = 0;
	
	//constructor
	public PCB(UserLandProcess up, Priority priority)
	{
		this.HoldULP = up;
		this.priority = priority;
		this.pid = nextPid++;
	}

	/**
	 * Calls UserLandProces stop
	 * Loops with Thread.sleep() until ulp.isStopped() is true. 
	 * @throws InterruptedException 
	 */
	public void stop() 
	{
		OS.debugMessage("PCB.stop()" + toString());
		HoldULP.stop();
//		if(HoldULP.isStopped() == false)
//		{
//			try 
//			{
//				Thread.sleep(10);
//			} 
//			catch (InterruptedException e) 
//			{
//				
//			}
//		}
	}
	
	public void requestStop()
	{
		OS.debugMessage("PCB.requestStop()" + toString());
		HoldULP.requestStop();
	}
	
	/**
	 * calls UserLandProces isDone() 
	 */
	public boolean isDone()
	{
		OS.debugMessage("PCB.isDone()");
		return HoldULP.isDone();
	}
	
	/**
	 * calls UserLandProces start()
	 */
	public void start()
	{
		OS.debugMessage("PCB.start()");
		HoldULP.start();
	}
	
	/**
	 * @return returns the 
	 */
	public int getPid()
	{
		OS.debugMessage("PCB.getPid()");
		return pid;
	}
	
	/**
	 * gets the priority of the item and 
	 * @return priority of item
	 */
	public Priority getPriority()
	{
		OS.debugMessage("PCB.getPriority()");
		return priority;
	}
	
	public void setPriority(Priority priority)
	{
		OS.debugMessage("PCB.setPriority()");
		this.priority = priority;
	}
	
	public String toString()
	{
		return "" + HoldULP.getClass();
	}
	
	public void setInterruptCounter(int counter)
	{
		this.counter = counter;
	}
	
	public int getInterruptCounter()
	{
		return counter;
	}
}
