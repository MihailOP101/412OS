package opsys;

import java.util.ArrayList;

/**
 * the enum for the different types of processes
 */
enum CallType
{
	CREATE_PROCESS,
	SWITCH_PROCESS,
	SLEEP
}

/**
 * the enum for the priority of the activity
 */
enum Priority
{
	REAL_TIME,
	INTERACTIVE,
	BACKGROUND
}

public class OS 
{
	//Creates a Kernal implementation 
	private static Kernal kernal = new Kernal();
	
	//Shared data for communication between Userland and Kernal
	public static CallType currentCall;
	public static Priority priority;
	public static ArrayList<Object> parameters = new ArrayList<>();
    public static Object returnValue;
    
    public static void switchProcess()
    {
    	OS.debugMessage("OS.switchProcess()");
    	//Clears parameters
    	parameters.clear();
    	
    	//Sets the currentCall
    	currentCall = CallType.SWITCH_PROCESS;
    	
		switchToKernal();
    }
    
    //Method to switch to the kernel and perform the kernel function call
    public static void switchToKernal()
    {
    	OS.debugMessage("OS.switchToKernal()");
    	//Switch to Kernal and switch the functions there
    	kernal.start();
    	if (kernal.scheduler.currentProcess != null)
    	{
    		OS.debugMessage("OS.switchToKernal() : if PCB not null");
    		kernal.scheduler.currentProcess.stop();
    	}
    }
    
    public static int CreateProcess(UserLandProcess up)
    {
    	OS.debugMessage("OS.CreateProcess() : up");
    	//Clears parameters
    	parameters.clear();
    
    	//Adds a new parameters to the Kernal
    	parameters.add(up);
    
    	//Adds a priority parameter to the Kernal 
    	parameters.add(Priority.INTERACTIVE);
    	
    	//Sets the currentCall
    	currentCall = CallType.CREATE_PROCESS;
    	
		switchToKernal();

		while(true)
		{
			try
			{
				OS.debugMessage("OS.CreateProcess() : while.try 1");
				return (int) returnValue;
			}
			catch(Exception e)
			{
				
			}
		}
    }
    
    //Method to make an enum entry for CreateProcess, and follow the steps above
	public static int CreateProcess(UserLandProcess up, Priority priority)
	{
		OS.debugMessage("OS.CreateProcess() : up and priority");
    	//Clears parameters
    	parameters.clear();
    
    	//Adds a ULP parameter to the Kernal
    	parameters.add(up);
    	
    	//Adds a priority parameter to the Kernal 
    	parameters.add(priority);
    
    	//Sets the currentCall
    	currentCall = CallType.CREATE_PROCESS;
    	
		switchToKernal();

		while(true)
		{
			try
			{
				OS.debugMessage("OS.CreateProcess() : while.try 2");
				return (int) returnValue;
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	
	//Method creates the Kernel() and calls CreateProcess twice – once for “init” and once for the idle process.
	public static void Startup(UserLandProcess init) 
	{
		OS.debugMessage("OS.Startup()");
		//Calls create process to initialize the kernal
		CreateProcess(init);
		
		
		//Calls idleProcess and calls createProcess for it
		IdleProcess idleProc = new IdleProcess();
		CreateProcess(idleProc, Priority.BACKGROUND);
	}

	/**
	 * 
	 * @param milliseconds calls the Kernal's sleep process for an amount of milliseconds
	 */
	public static void sleep(int milliseconds)
	{
		parameters.clear();
		parameters.add(milliseconds);
		currentCall = CallType.SLEEP;
		switchToKernal();
	}
	
	//Creates the debug messages to show the steps
	public static void debugMessage(String message)
	{
		//comment out to stop getting most of the messages
//		System.out.println('\t' + message);
	}
	
	
}
