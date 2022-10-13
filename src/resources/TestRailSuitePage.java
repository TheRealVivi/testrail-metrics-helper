package resources;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class TestRailSuitePage extends Base
{
	private String domain;
	private String URL = "https://" + domain + ".testrail.net/index.php?/suites/view/33353&group_by=cases:section_id&group_order=asc&display_deleted_cases=0";
	
	private final By PROJECT_NAME_LINK = By.id("navigation-project");
	
	private final By EXPORT_BUTTON = By.className("icon-export");
	private final By EXPORT_CSV_OPTION = By.className("icon-csv-10");
	
	private final By EXPORT_ALL_SECTIONS_RADIO_BUTTON = By.id("exportCsvSectionsAll");
	private final By EXPORT_SELECT_SECTIONS_ONLY_RADIO_BUTTON = By.id("exportCsvSectionsSelected");
	
	private final By SECTION_SELECT = By.id("exportCsvSectionsSelection");
	
	private final By EXPORT_SUBMIT_BUTTON = By.id("exportSubmit");

	public Select getSectionSelect(WebDriver driver) 
	{
		return new Select(driver.findElement(SECTION_SELECT));
	}
	
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

	public WebElement getExportCsvOption(WebDriver driver) {
		return driver.findElement(EXPORT_CSV_OPTION);
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
		this.getExportCsvOption(driver).click();
		Thread.sleep(800);
		this.getExportSubmitButton(driver).click();
		Thread.sleep(3000);
		
		return projectName;
	}
	
	public String downloadMainSections(WebDriver driver, int fromSection, int toSection) throws InterruptedException 
	{
		Thread.sleep(2000);
		String projectName = this.getProjectName(driver);
		this.getExportButton(driver).click();
		Thread.sleep(1000);
		this.getExportCsvOption(driver).click();
		Thread.sleep(800);
		this.getExportSelectSectionsOnlyRadioButton(driver).click();
		
		Select sectionSelection = this.getSectionSelect(driver);
		
		List<WebElement> options = sectionSelection.getOptions();
		Actions aBuilder = new Actions(driver);
		
		aBuilder.keyDown(Keys.SHIFT)
				.click(options.get(fromSection))
				.click(options.get(toSection))
				.keyUp(Keys.SHIFT)
				.perform();
		
		System.out.println("===============\n"
						 + "Section " + sectionSelection.getFirstSelectedOption().getText());
		
				
		this.getExportSubmitButton(driver).click();
		Thread.sleep(3000);
		
		return projectName;
	}
	
}
