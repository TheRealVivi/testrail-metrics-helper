package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;

import resources.TestRailLoginPage;
import resources.TestRailSuitePage;

public class TestRailMetricsGeneratorTool 
{
	int blocked;
	int inReview;
	int inProgress;
	int readyForReview;
	int testReviewed;
	int ready; 
	int link;
	int total;
	
	public TestRailMetricsGeneratorTool()
	{
		blocked = 0; 
		inReview = 0;
		inProgress = 0;
		readyForReview = 0;
		testReviewed = 0;
		ready = 0;
		link = 0;
		total = 0;
	}
	
	public void downloadAllSections() throws InterruptedException 
	{	
		TestRailSuitePage trsp = new TestRailSuitePage();
		TestRailLoginPage trlp = new TestRailLoginPage();
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		System.out.print("TestRail domain: ");
		trsp.setDomain(in.nextLine());
		System.out.print("TestRail email: ");
		trlp.setEmail(in.nextLine());
		System.out.print("TestRail password: ");
		trlp.setPassword(in.nextLine());
		
		System.setProperty("webdriver.chrome.driver", "C:\\WebDrivers\\chromedriver.exe");
		WebDriver driver = TestRailSuitePage.initializeDriver();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		trsp.getURL(driver);
		trlp.login(driver);
		trsp.downloadAllSections(driver);
		
		driver.quit();
	}
	
	public void composeAllMetrics() throws IOException 
	{
		File csvFile = new File("C:\\Users\\kodel\\Downloads\\chicago_program_system_testing.csv");
		String statusString = parseCsv(csvFile);
		
		parseStatus(statusString);
		printStatusTotals();
	    
		csvFile.delete();
	}
	
	private String parseCsv(File csvFile) throws FileNotFoundException 
	{
		Scanner sc = new Scanner(csvFile);
		sc.useDelimiter(",");
		
		sc.next();
		String statusString = sc.next();
		sc.close();
		return statusString;
		
	}
	
	private void parseStatus(String statusString) 
	{
		String status = "";
		
		for(int i = 0; i < statusString.length(); i++) 
		{
			if(statusString.charAt(i) == 10 || statusString.charAt(i) == 13) 
			{
				if(status.equalsIgnoreCase("\n\" Blocked\""))
					blocked++;
				else if(status.equalsIgnoreCase("\n\" In Review\""))
					inReview++;
				else if(status.equalsIgnoreCase("\n\" In Progress\""))
					inProgress++;
				else if(status.equalsIgnoreCase("\n\" Ready For Review\""))
					readyForReview++;
				else if(status.equalsIgnoreCase("\n\" Test Reviewed\""))
					testReviewed++;
				else if(status.equalsIgnoreCase("\n\" Ready\""))
					ready++;
				else if(status.equalsIgnoreCase("\n\" Link\""))
					link++;
				
				status = "";
			}
			status += statusString.charAt(i);
		}
	}
	
	private void printStatusTotals() 
	{
		total = blocked + inReview + inProgress + readyForReview + testReviewed + ready + link;
		
		System.out.println("\nBlocked = " + blocked);
		System.out.println("In Review = " + inReview);
		System.out.println("In Progress = " + inProgress);
		System.out.println("Ready For Review = " + readyForReview);
		System.out.println("Test Reviewed = " + testReviewed);
		System.out.println("Ready = " + ready);
		System.out.println("Link = " + link);
		System.out.println("Total = " + total);
		
		System.out.println("\nBlocked = " + ((double) blocked / total) * 100);
		System.out.println("In Review = " + ((double) inReview / total) * 100);
		System.out.println("In Progress = " + ((double) inProgress / total) * 100);
		System.out.println("Ready For Review = " + ((double) readyForReview / total) * 100);
		System.out.println("Test Reviewed = " + ((double) testReviewed / total) * 100);
		System.out.println("Ready = " + ((double) ready / total) * 100);
		System.out.println("Link = " + ((double) link / total) * 100);
		System.out.println("Total = " + ((double) total / total) * 100);
		System.out.println("Percent complete based on original estimates: " + ((double) total / 1767.0) * 100);
	}
}
