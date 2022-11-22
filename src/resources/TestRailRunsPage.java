package resources;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TestRailRunsPage extends TestRailSuitePage
{
	private String URL = "https://" + domain + ".testrail.net/index.php?/runs/view/60819&group_by=cases:section_id&group_order=asc&group_id=1840344";
	private final By TEST_RUN_NAME = By.className("content-header-title");
	
	public void setDomain(String domain) 
	{
		this.domain = domain;
		this.URL = "https://" + domain + ".testrail.net/index.php?/runs/view/60819&group_by=cases:section_id&group_order=asc&group_id=1840344";
	}
	
	public void getURL(WebDriver driver) {
		driver.get(URL);
	}
	
	public String getRunName(WebDriver driver) {
		return driver.findElement(TEST_RUN_NAME).getText();
	}
	
	public String downloadAllSections(WebDriver driver) throws InterruptedException 
	{
		
		Thread.sleep(2000);
		String runName = this.getRunName(driver);
		this.getExportButton(driver).click();
		Thread.sleep(1000);
		this.getExportCsvOption(driver).click();
		Thread.sleep(800);
		this.getColumnsNoneButton(driver).click();
		this.getColumnsTestCaseStatusCheckbox(driver).click();
		this.getExportSubmitButton(driver).click();
		Thread.sleep(3000);
		
		return runName;
	}
}
