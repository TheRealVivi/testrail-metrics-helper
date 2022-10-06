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
	// Test Case status's are to a specific TestRail project, will update later to make more general
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
	
	/**
	 * From console: Requests user to input:
	 * TestRail domain
	 * TestRail email
	 * TestRail password
	 * 
	 * Then logs into TestRail, exports desired suite to csv
	 */
	public String downloadAllSections() throws InterruptedException 
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
		
		WebDriver driver = TestRailSuitePage.initializeDriver();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		trsp.getURL(driver);
		trlp.login(driver);
		String fileName = trsp.downloadAllSections(driver);
		
		//System.out.println("fileName: " + fileName);
		fileName = filefy(fileName);
		//System.out.println("fileName: " + fileName);
		
		driver.quit();
		
		return fileName;
	}
	
	/**
	 * Pulls csv downloaded from TestRail, parses the Test Case status's, and prints status totals to console.
	 * Then deletes the csv file
	 */
	public void composeAllMetrics(String fileName) throws IOException 
	{
		//File csvFile = new File("/home/vivi/Downloads/" + fileName);
		String home = System.getProperty("user.home");
		File csvFile = new File(home + "\\Downloads\\" + fileName);
		String statusString = parseCsv(csvFile);
		
		parseStatus(statusString);
		printStatusTotals();
	    
		csvFile.delete();
	}
	
	private String filefy(String fileName) 
	{
		fileName = fileName.toLowerCase();
		fileName = fileName.replace(' ', '_');
		fileName += ".csv";
		
		return fileName;
	}
	
	/** TODO:
	 * Update to parse all csv's
	 * 
	 * Currently configured to parse csv with specific configuration
	 */
	private String parseCsv(File csvFile) throws FileNotFoundException 
	{
		Scanner sc = new Scanner(csvFile);
		sc.useDelimiter(",");
		
		sc.next();
		String statusString = sc.next();
		sc.close();
		return statusString;
		
	}
	
	/** TODO:
	 * Update to parse for all Test Case status's
	 * 
	 * Currently configured to parse specific test case status's
	 */
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
	
	/** TODO:
	 * Update to be able to print any test case status's
	 * 
	 * Currently configured to print specific test case status's
	 */
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
		
		System.out.printf("\nBlocked = %.0f%%\n", ((double) blocked / total) * 100);
		System.out.printf("In Review = %.0f%%\n", ((double) inReview / total) * 100);
		System.out.printf("In Progress = %.0f%%\n", ((double) inProgress / total) * 100);
		System.out.printf("Ready For Review = %.0f%%\n", ((double) readyForReview / total) * 100);
		System.out.printf("Test Reviewed = %.0f%%\n", ((double) testReviewed / total) * 100);
		System.out.printf("Ready = %.0f%%\n", ((double) ready / total) * 100);
		System.out.printf("Link = %.0f%%\n", ((double) link / total) * 100);
		System.out.printf("Total = %.0f%%\n", ((double) total / total) * 100);
		System.out.printf("Percent complete based on original estimates: %.0f%%\n", ((double) total / 1767.0) * 100);
	}
}
