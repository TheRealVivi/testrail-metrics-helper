package resources;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Base 
{
	public static String runningOS;
	
	public static WebDriver initializeDriver()
	{
		WebDriver driver;
		
		if(runningOS.equals("Windows"))
			System.setProperty("webdriver.chrome.driver", "C:\\WebDrivers\\chromedriver.exe");
		else if(runningOS.equals("Linux") || runningOS.equals("MacOS"))
			System.setProperty("webdriver.chrome.driver", "/home/vivi/webdrivers/chromedriver");
		else
			System.setProperty("webdriver.chrome.driver", "C:\\WebDrivers\\chromedriver.exe");
		
		driver = new ChromeDriver();
		
		return driver;
	}
}
