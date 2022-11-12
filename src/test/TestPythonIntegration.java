package test;

import java.io.IOException;

import resources.Base;
import tools.TestRailMetricsGeneratorTool;

public class TestPythonIntegration 
{
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		final int RUNNING_OS = 0;
		final int OPTION = 1;
		final int DOMAIN = 2;
		final int EMAIL = 3;
		final int PASSWORD = 4;
		
		if(args.length != 4)
		{
			System.out.println("Not enough or too many arguments provided.. Exiting...");
			return;
		}
		
		Base.runningOS = args[RUNNING_OS];
		int option = Integer.parseInt(args[OPTION]);
		String domain = args[DOMAIN];
		String email = args[EMAIL];
		String password = args[PASSWORD];
		TestRailMetricsGeneratorTool testRailTool = new TestRailMetricsGeneratorTool();
		testRailTool.mainMenu(option, domain, email, password);
	}
}
