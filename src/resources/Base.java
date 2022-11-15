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
			System.setProperty("webdriver.chrome.driver", "../webdrivers/Windows/chromedriver.exe");
		else if(runningOS.equals("Linux") || runningOS.equals("MacOS"))
			System.setProperty("webdriver.chrome.driver", "../webdrivers/Linux/chromedriver");
		else
			System.setProperty("webdriver.chrome.driver", "../webdrivers/Linux/chromedriver");
		
		driver = new ChromeDriver();
		
		return driver;
	}
}
