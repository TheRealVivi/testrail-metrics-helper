package resources;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Base 
{
	public static WebDriver initializeDriver()
	{
		WebDriver driver;
		
		System.setProperty("webdriver.chrome.driver", "C:\\WebDrivers\\chromedriver.exe");
		driver = new ChromeDriver();
		
		return driver;
	}
}
