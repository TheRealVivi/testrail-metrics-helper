package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import org.openqa.selenium.WebDriver;

import resources.Base;
import resources.TestRailLoginPage;
import resources.TestRailRunsPage;
import resources.TestRailSuitePage;

public class TestRailMetricsGeneratorTool 
{
	private final String WINDOWS_PATH_SUITE_CONSOLIDATEDMETRICS = "\\Documents\\consolidatedMetrics.csv";
	private final String LINUX_MACOS_PATH_SUITE_CONSOLIDATEDMETRICS = "/Documents/consolidatedMetrics.csv";
	//private final String WINDOWS_PATH_SUITE_SECTIONMETRICS = "\\Documents\\sectionMetrics.csv";
	private final String WINDOWS_PATH_RUN_CONSOLIDATEDMETRICS = "\\Documents\\testRunConsolidatedMetrics.csv";
	
	int total;
	int secNum;
	
	
	HashMap<String, Integer> testCaseStatus;
	
	// DEPRECATED
	private final String BLOCKED_DESCRIPTION = "Unable to complete due to blocked process within pipeline";
	private final String IN_REVIEW_DESCRIPTION = "Test case needs to be reviewed and have modifications applied";
	private final String IN_PROGRESS_DESCRIPTION = "Test case is being worked on";
	private final String READY_FOR_REVIEW_DESCRIPTION = "Ready to be reviewed by the Test Review team";
	private final String TEST_REVIEWED_DESCRIPTION = "Ready to be reviewed by the Program Review team";
	private final String READY_DESCRIPTION = "Ready to be reviewed by the customer";
	private final String LINK_DESCRIPTION = "Test case up to standards; references need to be updated";
	// END DEPRECATED
	
	boolean loggedIn;
	String email;
	String password;
	String domain;
	String suiteOrRunID;
	
	private final String DOWNLOAD_ALL_SUITE_SECTIONS = "Download all suite sections";
	private final String DOWNLOAD_SELECT_SUITE_SECTIONS = "Download select suite sections";
	private final String DOWNLOAD_ALL_RUN_SECTIONS = "Download all run sections";
	private final int CSV_TITLE = 0;
	private final int BR_SECTION = 2;
	private int option;
	private String[] tableDetails;// 7 elements
	
	
	public TestRailMetricsGeneratorTool()
	{
		total = 0;
		
		testCaseStatus = new HashMap<String, Integer>();
		
		loggedIn = false;
		option = 0;
		
		secNum = 1;
		tableDetails = new String[7];
	}
	
	@Deprecated
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
				File outCsvFile = new File(home + LINUX_MACOS_PATH_SUITE_CONSOLIDATEDMETRICS);
				PrintWriter out = new PrintWriter(outCsvFile);
				String fileName = this.downloadAllSections();
				this.composeAllTestCaseStatusMetrics(fileName, out);
				out.close();
			}
			else if(option == 2) 
			{
				//this.option = option;
				//String home = System.getProperty("user.home");
				//File outCsvFile = new File(home + WINDOWS_PATH_SUITE_SECTIONMETRICS);
				//PrintWriter out = new PrintWriter(outCsvFile);
				/*
				System.out.print("From index >> ");
				int fromIndex = in.nextInt();
				System.out.print("To index >> ");
				int toIndex = in.nextInt();
				String fileName = this.downloadSelectSections(fromIndex, toIndex);
				this.composeAllTestCaseStatusMetrics(fileName);
				*/
				
				//specificSectionNumbers(out); // TODO: swap out with a way to identify section names
				//out.close();
				//secNum = 1;
			}
			else if(option == 3) 
			{
				this.option = option;
				String home = System.getProperty("user.home");
				File outCsvFile = new File(home + WINDOWS_PATH_RUN_CONSOLIDATEDMETRICS);
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
	
	public void mainMenu(int option, String domain, String email, String password, String suiteOrRunID) throws IOException, InterruptedException 
	{
		if(!domain.isEmpty() && !email.isEmpty() && !password.isEmpty()) 
		{
			this.domain = domain;
			this.email = email;
			this.password = password;
			this.suiteOrRunID = suiteOrRunID;
			this.loggedIn = true;
		}
		
		if(option == 1) 
		{
			this.option = option;
			String home = System.getProperty("user.home");
			File outCsvFile;
			
			if(Base.runningOS.equals("Windows")) 
			{
				outCsvFile = new File(home + WINDOWS_PATH_SUITE_CONSOLIDATEDMETRICS);
			}
			else if(Base.runningOS.equals("Linux") || Base.runningOS.equals("MacOS")) 
			{
				outCsvFile = new File(home + LINUX_MACOS_PATH_SUITE_CONSOLIDATEDMETRICS);
			}
			else 
			{
				outCsvFile = new File(home + WINDOWS_PATH_SUITE_CONSOLIDATEDMETRICS);
			}
			
			PrintWriter out = new PrintWriter(outCsvFile);
			String fileName = this.downloadAllSections();
			this.composeAllTestCaseStatusMetrics(fileName, out);
			out.close();
		}
		else if(option == 2) 
		{
			System.out.println("This option is still in development...");
			//this.option = option;
			//String home = System.getProperty("user.home");
			//File outCsvFile = new File(home + WINDOWS_PATH_SUITE_SECTIONMETRICS);
			//PrintWriter out = new PrintWriter(outCsvFile);
			/*
			System.out.print("From index >> ");
			int fromIndex = in.nextInt();
			System.out.print("To index >> ");
			int toIndex = in.nextInt();
			String fileName = this.downloadSelectSections(fromIndex, toIndex);
			this.composeAllTestCaseStatusMetrics(fileName);
			*/
			//out.close();
			//secNum = 1;
		}
		else if(option == 3) 
		{
			this.option = option;
			String home = System.getProperty("user.home");
			File outCsvFile = new File(home + WINDOWS_PATH_RUN_CONSOLIDATEDMETRICS);
			PrintWriter out = new PrintWriter(outCsvFile);
			String fileName = this.downloadAllRunSections();
			System.out.println(fileName);
			//this.composeAllTestRunStatusMetrics(fileName, out);
				
			out.close();
		
		}
	}
	
	private void resetValues() 
	{
		testCaseStatus = new HashMap<String, Integer>();
		total = 0;
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
			trsp.setDomainAndSuite(this.domain, "33353");
			trrp.setDomainAndRun(this.domain, "66685");
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
			trrp.setDomainAndRun(this.domain, this.suiteOrRunID);
			trsp.setDomainAndSuite(this.domain, this.suiteOrRunID);
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
	@Deprecated
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
		}//TODO: Update format to be generic for selecting specific sections in suite
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
		File inCsvFile;
		if(Base.runningOS.equals("Windows"))
			inCsvFile = new File(home + "\\Downloads\\" + fileName);
		else if(Base.runningOS.equals("Linux") || Base.runningOS.equals("MacOS"))
			inCsvFile = new File(home + "/Downloads/" + fileName);
		else
			inCsvFile = new File(home + "\\Downloads\\" + fileName);
		
		String statusString = parseCsv(inCsvFile);
		
		parseStatus(statusString);
		addTotals();
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
	 *  Add ignore filter for user file fed statuses
	 */
	private void parseStatus(String statusString) 
	{
		String status = "";
		
		for(int i = 0; i < statusString.length(); i++) 
		{
			if(statusString.charAt(i) == 10 || statusString.charAt(i) == 13) 
			{
				if(!testCaseStatus.containsKey(status) && !status.contains("Test Case Status") && !status.isBlank()) 
				{
					System.out.println("Status to be added: " + status);
					testCaseStatus.put(status, 0);
				}
				else if(testCaseStatus.containsKey(status))
					testCaseStatus.replace(status, testCaseStatus.get(status) + 1);
				
				status = "";
			}
			status += statusString.charAt(i);
		}
	}
	
	private void addTotals() 
	{
		//total = blocked + inReview + inProgress + readyForReview + testReviewed + ready + link;
		Collection<Integer> totals = testCaseStatus.values();
		
		for(Integer x : totals) 
		{
			System.out.println("Next int to add: " + x);
			total += x;
		}
		
		System.out.println("Total to display: " + total);
	}
	
	private void writeToCsv(PrintWriter out) throws FileNotFoundException 
	{		
		if(this.option == 1) 
		{
			Set<String> statuses = testCaseStatus.keySet();
			
			Iterator<String> itS = statuses.iterator();
			
			out.print("Test Case Status, Test Count, Test Count %, Description\n");
			String status;
			while(itS.hasNext()) 
			{
				status = itS.next();
				System.out.println("Next status: " + status);
				out.printf("%s, %d, %.2f%%, %s\n", status, testCaseStatus.get(status), ((double) testCaseStatus.get(status) / total) * 100, this.BLOCKED_DESCRIPTION);
			}
			out.printf("Grand Total, %d, %.2f%%, %s\n", this.total, ((double) total / total) * 100, "Total number of test cases");
		}
		/*
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
		*/
	}
}
