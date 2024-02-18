package opsys;

public class Main 
{
	/**
	 * is the main of the programs
	 * @param args the arguments of the program
	 */
	public static void main(String[] args)
	{
		OS.Startup(new HelloWorld());
		
		OS.CreateProcess(new GoodbyeWorld());
		
		OS.CreateProcess(new IfLoop(), Priority.REAL_TIME);
		OS.CreateProcess(new ForLoop(), Priority.REAL_TIME);
	}
	
}
