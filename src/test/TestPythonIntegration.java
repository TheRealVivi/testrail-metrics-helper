package test;

import java.io.IOException;

import tools.TestRailMetricsGeneratorTool;

public class TestPythonIntegration 
{
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		if(args.length != 4)
		{
			System.out.println("Not enough or too many arguments provided.. Exiting...");
			return;
		}
		
		int option = Integer.parseInt(args[0]);
		String domain = args[1];
		String email = args[2];
		String password = args[3];
		TestRailMetricsGeneratorTool testRailTool = new TestRailMetricsGeneratorTool();
		testRailTool.mainMenu(option, domain, email, password);
	}
}
