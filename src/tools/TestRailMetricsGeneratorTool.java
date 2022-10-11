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
	
	boolean loggedIn;
	String email;
	String password;
	String domain;
	
	private final String DOWNLOAD_ALL_SECTIONS = "Download all sections";
	private final String DOWNLOAD_SELECT_SECTIONS = "Download select sections";
	
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
		
		loggedIn = false;
	}
	
	public void mainMenu() throws IOException, InterruptedException 
	{
		boolean toolActive = true;
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		do {
			System.out.print("Welcome to the TestRail Metrics Generator Tool!\n"
							+ "Please select an option:\n"
							+ "1. Print consolidated section metrics\n"
							+ "2. Print metrics per section\n"
							+ "3. Quit\n"
							+ "Input option (1-3) >> ");
			int option = in.nextInt();
			
			if(option == 1) 
			{
				String fileName = this.downloadAllSections();
				this.composeAllTestCaseStatusMetrics(fileName);
			}
			else if(option == 2) 
			{
				/*
				System.out.print("From index >> ");
				int fromIndex = in.nextInt();
				System.out.print("To index >> ");
				int toIndex = in.nextInt();
				String fileName = this.downloadSelectSections(fromIndex, toIndex);
				this.composeAllTestCaseStatusMetrics(fileName);
				*/
				
				specificSectionNumbers(); // TODO: swap out with a way to identify section names
				
			}
			else if(option == 3) 
			{
				toolActive = false;
			}
			
			System.out.println();
		} while (toolActive);
		
		System.out.println("\nGoodbye\n");
	}
	
	private void resetValues() 
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
	
	private void specificSectionNumbers() throws IOException, InterruptedException 
	{
		String fileName = this.downloadSelectSections(0, 35);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(36, 43);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(44, 56);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(57, 74);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(75, 83);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(84, 84);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(85, 90);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(91, 98);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(99, 99);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(100, 110);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(111, 118);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(119, 119);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(120, 124);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(125, 133);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(134, 136);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(137, 138);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(139, 141);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(142, 148);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(149, 151);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(152, 191);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(192, 192);
		
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(193, 195);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(196, 204);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(205, 207);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(208, 245);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(246, 250);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(251, 252);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(253, 254);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(255, 256);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(257, 260);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(261, 263);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(264, 265);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(266, 266);
		this.composeAllTestCaseStatusMetrics(fileName);
		fileName = this.downloadSelectSections(267, 268);
		this.composeAllTestCaseStatusMetrics(fileName);
	}
	/**
	 * Requests:
	 * TestRail domain
	 * TestRail email
	 * TestRail password
	 * 
	 * Logs into TestRail, stores login info for same session use
	 */
	private void loginTestRailConsoleUI(TestRailSuitePage trsp, TestRailLoginPage trlp) 
	{
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		if(!loggedIn) 
		{
			System.out.print("TestRail domain: ");
			this.domain = in.nextLine();
			trsp.setDomain(this.domain);
			System.out.print("TestRail email: ");
			this.email = in.nextLine();
			trlp.setEmail(this.email);
			System.out.print("TestRail password: ");
			this.password = in.nextLine();
			trlp.setPassword(this.password);
			
			loggedIn = true;
		}
		else 
		{
			trsp.setDomain(this.domain);
			trlp.setEmail(this.email);
			trlp.setPassword(this.password);
		}
	}
	
	// Exports csv containing all suite sections
	private String downloadAllSections() throws InterruptedException 
	{	
		return downloadHelper(DOWNLOAD_ALL_SECTIONS, 0, 0);
	}
	
	// Exports csv containing select suite sections
	private String downloadSelectSections(int fromSection, int toSection) throws InterruptedException 
	{
		return downloadHelper(DOWNLOAD_SELECT_SECTIONS, fromSection, toSection);
	}
	
	private String downloadHelper(String option, int fromSection, int toSection) throws InterruptedException 
	{
		String fileName;
		TestRailSuitePage trsp = new TestRailSuitePage();
		TestRailLoginPage trlp = new TestRailLoginPage();
		
		this.loginTestRailConsoleUI(trsp, trlp);
		
		WebDriver driver = TestRailSuitePage.initializeDriver();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		trsp.getURL(driver);
		trlp.login(driver);
		
		if(option.equals(DOWNLOAD_ALL_SECTIONS))
			fileName = trsp.downloadAllSections(driver);
		else if(option.equals(DOWNLOAD_SELECT_SECTIONS))
			fileName = trsp.downloadMainSections(driver, fromSection, toSection);
		else
			fileName = trsp.downloadAllSections(driver);
		
		fileName = filefy(fileName);
				
		driver.quit();
		
		return fileName;
	}
	
	/**
	 * Pulls csv downloaded from TestRail, parses the Test Case status's, and prints status totals to console.
	 * Then deletes the csv file
	 */
	private void composeAllTestCaseStatusMetrics(String fileName) throws IOException 
	{
		//File csvFile = new File("/home/vivi/Downloads/" + fileName);
		String home = System.getProperty("user.home");
		File csvFile = new File(home + "\\Downloads\\" + fileName);
		String statusString = parseCsv(csvFile);
		
		parseStatus(statusString);
		printStatusTotals();
	    
		csvFile.delete();
		this.resetValues();
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
		
		System.out.printf("\nBlocked = %.0f%%; In Review = %.0f%%; In Progress = %.0f%%; Ready For Review = %.0f%%; Test Reviewed = %.0f%%; Ready = %.0f%%; Link = %.0f%%\n",
				((double) blocked / total) * 100, ((double) inReview / total) * 100, ((double) inProgress / total) * 100, ((double) readyForReview / total) * 100,
				((double) testReviewed / total) * 100, ((double) ready / total) * 100, ((double) link / total) * 100);
		System.out.printf("\nPercent complete based on original estimates: %.0f%%\n", ((double) total / 1767.0) * 100);
		
		/*
		System.out.printf("\nBlocked = %.0f%%\n", ((double) blocked / total) * 100);
		System.out.printf("In Review = %.0f%%\n", ((double) inReview / total) * 100);
		System.out.printf("In Progress = %.0f%%\n", ((double) inProgress / total) * 100);
		System.out.printf("Ready For Review = %.0f%%\n", ((double) readyForReview / total) * 100);
		System.out.printf("Test Reviewed = %.0f%%\n", ((double) testReviewed / total) * 100);
		System.out.printf("Ready = %.0f%%\n", ((double) ready / total) * 100);
		System.out.printf("Link = %.0f%%\n", ((double) link / total) * 100);
		System.out.printf("Total = %.0f%%\n", ((double) total / total) * 100);
		System.out.printf("Percent complete based on original estimates: %.0f%%\n", ((double) total / 1767.0) * 100);
		*/
	}
}
