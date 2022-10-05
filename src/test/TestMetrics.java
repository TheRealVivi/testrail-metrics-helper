package test;


import java.io.IOException;
import tools.TestRailMetricsGeneratorTool;

public class TestMetrics 
{
	public static void main(String[] args) throws InterruptedException, IOException
	{
		TestRailMetricsGeneratorTool testRailTool = new TestRailMetricsGeneratorTool();
		testRailTool.downloadAllSections();
		testRailTool.composeAllMetrics();
	}
}
