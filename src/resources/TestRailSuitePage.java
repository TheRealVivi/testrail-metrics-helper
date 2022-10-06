package resources;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TestRailSuitePage extends Base
{
	private String domain;
	private String URL = "https://" + domain + ".testrail.net/index.php?/suites/view/33353&group_by=cases:section_id&group_order=asc&display_deleted_cases=0";
	
	private final By PROJECT_NAME_LINK = By.id("navigation-project");
	
	private final By EXPORT_BUTTON = By.className("icon-export");
	private final By EXPORT_EXCEL_OPTION = By.className("icon-excel-10");
	
	private final By EXPORT_ALL_SECTIONS_RADIO_BUTTON = By.id("exportCsvSectionsAll");
	private final By EXPORT_SELECT_SECTIONS_ONLY_RADIO_BUTTON = By.id("exportCsvSectionsSelected");
	
	private final By EXPORT_SUBMIT_BUTTON = By.id("exportSubmit");

	public void setDomain(String domain) 
	{
		this.domain = domain;
		this.URL = "https://" + domain + ".testrail.net/index.php?/suites/view/33353&group_by=cases:section_id&group_order=asc&display_deleted_cases=0";
	}
	
	public void getURL(WebDriver driver) {
		driver.get(URL);
	}
	
	public String getProjectName(WebDriver driver) {
		return driver.findElement(PROJECT_NAME_LINK).getText();
	}

	public WebElement getExportButton(WebDriver driver) {
		return driver.findElement(EXPORT_BUTTON);
	}

	public WebElement getExportExcelOpton(WebDriver driver) {
		return driver.findElement(EXPORT_EXCEL_OPTION);
	}

	public WebElement getExportAllSectionsRadioButton(WebDriver driver) {
		return driver.findElement(EXPORT_ALL_SECTIONS_RADIO_BUTTON);
	}

	public WebElement getExportSelectSectionsOnlyRadioButton(WebDriver driver) {
		return driver.findElement(EXPORT_SELECT_SECTIONS_ONLY_RADIO_BUTTON);
	}

	public WebElement getExportSubmitButton(WebDriver driver) {
		return driver.findElement(EXPORT_SUBMIT_BUTTON);
	}
	
	public String downloadAllSections(WebDriver driver) throws InterruptedException 
	{
		
		Thread.sleep(2000);
		String projectName = this.getProjectName(driver);
		this.getExportButton(driver).click();
		Thread.sleep(1000);
		this.getExportExcelOpton(driver).click();
		Thread.sleep(500);
		this.getExportSubmitButton(driver).click();
		Thread.sleep(3000);
		
		return projectName;
	}
	
}
