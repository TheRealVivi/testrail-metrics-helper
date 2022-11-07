package test;

import java.io.IOException;

import tools.TestRailMetricsGeneratorTool;

public class TestPythonIntegration 
{
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		int option = Integer.parseInt(args[0]);
		TestRailMetricsGeneratorTool testRailTool = new TestRailMetricsGeneratorTool();
		testRailTool.mainMenu(option);
	}
}
