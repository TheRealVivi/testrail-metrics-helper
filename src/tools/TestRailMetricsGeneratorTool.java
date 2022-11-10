package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;

import resources.TestRailLoginPage;
import resources.TestRailRunsPage;
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
	int secNum;
	
	private final String BLOCKED_DESCRIPTION = "Unable to complete due to blocked process within pipeline";
	private final String IN_REVIEW_DESCRIPTION = "Test case needs to be reviewed and have modifications applied";
	private final String IN_PROGRESS_DESCRIPTION = "Test case is being worked on";
	private final String READY_FOR_REVIEW_DESCRIPTION = "Ready to be reviewed by the Test Review team";
	private final String TEST_REVIEWED_DESCRIPTION = "Ready to be reviewed by the Program Review team";
	private final String READY_DESCRIPTION = "Ready to be reviewed by the customer";
	private final String LINK_DESCRIPTION = "Test case up to standards; references need to be updated";
	
	boolean loggedIn;
	String email;
	String password;
	String domain;
	
	private final String DOWNLOAD_ALL_SUITE_SECTIONS = "Download all suite sections";
	private final String DOWNLOAD_SELECT_SUITE_SECTIONS = "Download select suite sections";
	private final String DOWNLOAD_ALL_RUN_SECTIONS = "Download all run sections";
	private final int CSV_TITLE = 0;
	private final int BR_SECTION = 2;
	private final int TEST_CASE_COUNT = 3;
	private final int PERCENT_COMPLETE = 4;
	private final int STATUS = 5;
	private int option;
	private String[] tableDetails;// 7 elements
	
	private final int[] ESTIMATES = { 390, 109, 80, 95, 188, 20, 71, 36, 8, 42, 14, 0, 18, 
			15, 19, 0, 7, 18, 10, 343, 4, 16, 23, 12, 56, 23, 14, 8, 49, 4, 19, 0, 25, 2 };
	
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
		option = 0;
		
		secNum = 1;
		tableDetails = new String[7];
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
							+ "3. Print test run metrics\n"
							+ "4. Quit\n"
							+ "Input option (1-4) >> ");
			int option = in.nextInt();
			
			if(option == 1) 
			{
				this.option = option;
				String home = System.getProperty("user.home");
				File outCsvFile = new File(home + "\\Documents\\consolidatedMetrics.csv");
				PrintWriter out = new PrintWriter(outCsvFile);
				String fileName = this.downloadAllSections();
				this.composeAllTestCaseStatusMetrics(fileName, out);
				out.close();
			}
			else if(option == 2) 
			{
				this.option = option;
				String home = System.getProperty("user.home");
				File outCsvFile = new File(home + "\\Documents\\sectionMetrics.csv");
				PrintWriter out = new PrintWriter(outCsvFile);
				/*
				System.out.print("From index >> ");
				int fromIndex = in.nextInt();
				System.out.print("To index >> ");
				int toIndex = in.nextInt();
				String fileName = this.downloadSelectSections(fromIndex, toIndex);
				this.composeAllTestCaseStatusMetrics(fileName);
				*/
				
				specificSectionNumbers(out); // TODO: swap out with a way to identify section names
				out.close();
				secNum = 1;
			}
			else if(option == 3) 
			{
				this.option = option;
				String home = System.getProperty("user.home");
				File outCsvFile = new File(home + "\\Documents\\testRunConsolidatedMetrics.csv");
				PrintWriter out = new PrintWriter(outCsvFile);
				String fileName = this.downloadAllRunSections();
				System.out.println(fileName);
				toolActive = false;
				//this.composeAllTestRunStatusMetrics(fileName, out);
				
				out.close();
				
			}
			else if(option == 4) 
			{
				toolActive = false;
			}
			
			
			System.out.println();
		} while (toolActive);
		
		System.out.println("\nGoodbye\n");
	}
	
	public void mainMenu(int option, String domain, String email, String password) throws IOException, InterruptedException 
	{
		if(!domain.isEmpty() && !email.isEmpty() && !password.isEmpty()) 
		{
			this.domain = domain;
			this.email = email;
			this.password = password;
			this.loggedIn = true;
		}
		
		if(option == 1) 
		{
			this.option = option;
			String home = System.getProperty("user.home");
			File outCsvFile = new File(home + "\\Documents\\consolidatedMetrics.csv");
			PrintWriter out = new PrintWriter(outCsvFile);
			String fileName = this.downloadAllSections();
			this.composeAllTestCaseStatusMetrics(fileName, out);
			out.close();
		}
		else if(option == 2) 
		{
			this.option = option;
			String home = System.getProperty("user.home");
			File outCsvFile = new File(home + "\\Documents\\sectionMetrics.csv");
			PrintWriter out = new PrintWriter(outCsvFile);
			/*
			System.out.print("From index >> ");
			int fromIndex = in.nextInt();
			System.out.print("To index >> ");
			int toIndex = in.nextInt();
			String fileName = this.downloadSelectSections(fromIndex, toIndex);
			this.composeAllTestCaseStatusMetrics(fileName);
			*/
				
			specificSectionNumbers(out); // TODO: swap out with a way to identify section names
			out.close();
			secNum = 1;
		}
		else if(option == 3) 
		{
			this.option = option;
			String home = System.getProperty("user.home");
			File outCsvFile = new File(home + "\\Documents\\testRunConsolidatedMetrics.csv");
			PrintWriter out = new PrintWriter(outCsvFile);
			String fileName = this.downloadAllRunSections();
			System.out.println(fileName);
			//this.composeAllTestRunStatusMetrics(fileName, out);
				
			out.close();
		
		}
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
	
	private void specificSectionNumbers(PrintWriter out) throws IOException, InterruptedException 
	{
		String fileName = this.downloadSelectSections(0, 35); // 2.1
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(36, 43); // 2.2
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(44, 56); // 2.3
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(57, 74); // 2.4
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(75, 83); // 2.5
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(84, 84); // 2.6
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(85, 90); // 2.7
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(91, 98); // 2.8
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(99, 99); // 2.9
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(100, 110); // 2.10
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(111, 118); // 2.11
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(119, 119); // 2.12
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(120, 124); // 2.13
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(125, 133); // 2.14
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(134, 136); // 2.15
		this.composeAllTestCaseStatusMetrics(fileName, out);
		
		fileName = this.downloadSelectSections(137, 138); // 3.1
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(139, 141); // 3.2
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(142, 148); // 3.3
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(149, 151); // 3.4
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(152, 191); // 3.5
		
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(192, 192); // 4.1
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(193, 195); // 4.2
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(196, 204); // 4.3
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(205, 207); // 4.4
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(208, 245); // 4.5
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(246, 250); // 4.6
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(251, 252); // 4.7
		this.composeAllTestCaseStatusMetrics(fileName, out);
		
		fileName = this.downloadSelectSections(253, 254); // 5.1
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(255, 256); // 5.2
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(257, 260); // 5.3
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(261, 263); // 5.4
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(264, 265); // 5.5
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(266, 266); // 5.6
		this.composeAllTestCaseStatusMetrics(fileName, out);
		fileName = this.downloadSelectSections(267, 268); // 5.7
		this.composeAllTestCaseStatusMetrics(fileName, out);
	}
	
	/**
	 * Requests:
	 * TestRail domain
	 * TestRail email
	 * TestRail password
	 * 
	 * Logs into TestRail, stores login info for same session use
	 */
	private void loginTestRailConsoleUI(TestRailSuitePage trsp, TestRailRunsPage trrp, TestRailLoginPage trlp) 
	{
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		// If first time running, provide credentials
		// Else, use stored credentials
		if(!loggedIn) 
		{
			System.out.print("TestRail domain: ");
			this.domain = in.nextLine();
			trsp.setDomain(this.domain);
			trrp.setDomain(this.domain);
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
			trrp.setDomain(this.domain);
			trsp.setDomain(this.domain);
			trlp.setEmail(this.email);
			trlp.setPassword(this.password);
		}
	}
	
	// Exports csv containing all suite sections
	private String downloadAllSections() throws InterruptedException 
	{	
		return downloadHelper(DOWNLOAD_ALL_SUITE_SECTIONS, 0, 0);
	}
	
	// Exports csv containing select suite sections
	private String downloadSelectSections(int fromSection, int toSection) throws InterruptedException 
	{
		return downloadHelper(DOWNLOAD_SELECT_SUITE_SECTIONS, fromSection, toSection);
	}
	
	private String downloadAllRunSections() throws InterruptedException 
	{
		return downloadHelper(DOWNLOAD_ALL_RUN_SECTIONS, 0, 0);
	}
	
	private String downloadHelper(String option, int fromSection, int toSection) throws InterruptedException 
	{
		String fileName;
		TestRailSuitePage trsp = new TestRailSuitePage();
		TestRailRunsPage trrp = new TestRailRunsPage();
		TestRailLoginPage trlp = new TestRailLoginPage();
		
		this.loginTestRailConsoleUI(trsp, trrp, trlp);
		
		WebDriver driver = TestRailLoginPage.initializeDriver();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(50));
		
		if(option.equals(DOWNLOAD_ALL_SUITE_SECTIONS)) {
			trsp.getURL(driver);
			trlp.login(driver);
			fileName = trsp.downloadAllSections(driver);
		}
		else if(option.equals(DOWNLOAD_SELECT_SUITE_SECTIONS)) {
			trsp.getURL(driver);
			trlp.login(driver);
			String[] temp = new String[2];
			temp = trsp.downloadMainSections(driver, fromSection, toSection);
			this.tableDetails[CSV_TITLE] = temp[CSV_TITLE];
			this.tableDetails[BR_SECTION] = temp[1];
			fileName = this.tableDetails[CSV_TITLE];
			//fileName = trsp.downloadMainSections(driver, fromSection, toSection);
		}
		else if(option.equals(DOWNLOAD_ALL_RUN_SECTIONS)) 
		{
			trrp.getURL(driver);
			trlp.login(driver);
			fileName = trrp.downloadAllSections(driver);
		}
		else {
			trsp.getURL(driver);
			trlp.login(driver);
			fileName = trsp.downloadAllSections(driver);
		}
		
		fileName = filefy(fileName);
		this.tableDetails[CSV_TITLE] = fileName;
		
		driver.quit();
		
		return fileName;
	}
	
	/**
	 * Pulls csv downloaded from TestRail, parses the Test Case status's, and prints status totals to console.
	 * Then deletes the csv file
	 */
	private void composeAllTestCaseStatusMetrics(String fileName, PrintWriter out) throws IOException 
	{
		//File csvFile = new File("/home/vivi/Downloads/" + fileName);
		String home = System.getProperty("user.home");
		File inCsvFile = new File(home + "\\Downloads\\" + fileName);
		
		String statusString = parseCsv(inCsvFile);
		
		parseStatus(statusString);
		addTotals();
		printStatusTotals();
		writeToCsv(out);
	    
		inCsvFile.delete();
		this.resetValues();
	}
	
	/** 
	 *  \Brief: Pulls project name, makes it lowercase, and replaces white space with underscores, then attaches .csv to the end 
	 *  \Returns: official file name
	 */
	private String filefy(String fileName) 
	{
		fileName = fileName.toLowerCase();
		fileName = fileName.replace(' ', '_');
		fileName = fileName.replace('-', '_');
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
		
		//sc.next();
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
	
	private void addTotals() 
	{
		total = blocked + inReview + inProgress + readyForReview + testReviewed + ready + link;
	}
	
	/** TODO:
	 * Update to be able to print any test case status's
	 * 
	 * Currently configured to print specific test case status's
	 */
	private void printStatusTotals() 
	{		
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
		
	}
	
	private void writeToCsv(PrintWriter out) throws FileNotFoundException 
	{		
		if(this.option == 1) 
		{
			out.print("Test Case Status, Test Count, Test Count %, Description\n");
			out.printf("Blocked, %d, %.2f%%, %s\n", this.blocked, ((double) blocked / total) * 100, this.BLOCKED_DESCRIPTION);
			out.printf("In Review, %d, %.2f%%, %s\n", this.inReview, ((double) inReview / total) * 100, this.IN_REVIEW_DESCRIPTION);
			out.printf("In Progress, %d, %.2f%%, %s\n", this.inProgress, ((double) inProgress / total) * 100, this.IN_PROGRESS_DESCRIPTION);
			out.printf("Ready for Review, %d, %.2f%%, %s\n", this.readyForReview, ((double) readyForReview / total) * 100, this.READY_FOR_REVIEW_DESCRIPTION);
			out.printf("Test Reviewed, %d, %.2f%%, %s\n", this.testReviewed, ((double) testReviewed / total) * 100, this.TEST_REVIEWED_DESCRIPTION);
			out.printf("Ready, %d, %.2f%%, %s\n", this.ready, ((double) ready / total) * 100, this.READY_DESCRIPTION);
			out.printf("Link, %d, %.2f%%, %s\n", this.link, ((double) link / total) * 100, this.LINK_DESCRIPTION);
			out.printf("Grand Total, %d, %.2f%%, %s\n", this.total, ((double) total / total) * 100, "Total number of test cases");
		}
		else if(this.option == 2) 
		{
			this.tableDetails[TEST_CASE_COUNT] = Integer.toString(this.total);
			this.tableDetails[STATUS] = "In Progress";
			
			if(secNum == 1)
				out.print("S.No, BR Section, Test Case Count, % Completed, Status, Comments\n");
			
			out.printf("%d, %s, %s, %.0f%%, %s, %.0f%% Blocked; %.0f%% In Review; %.0f%% In Progress; %.0f%% Ready for Review;"
					+ " %.0f%% Test Reviewed; %.0f%% Ready; %.0f%% Link\n", this.secNum, this.tableDetails[BR_SECTION],
					this.tableDetails[TEST_CASE_COUNT], ((double) total / this.ESTIMATES[secNum - 1]) * 100, this.tableDetails[STATUS],
					((double) blocked / total) * 100, ((double) inReview / total) * 100, ((double) inProgress / total) * 100, ((double) readyForReview / total) * 100,
					((double) testReviewed / total) * 100, ((double) ready / total) * 100, ((double) link / total) * 100);
			
			secNum++;
		}
	}
}
