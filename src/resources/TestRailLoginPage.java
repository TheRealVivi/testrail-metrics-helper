package resources;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TestRailLoginPage extends Base
{
	private String domain;
	private String URL = "https://" + domain + ".testrail.net/index.php?/auth/login/L3N1aXRlcy92aWV3LzMzMzUzJmdyb3VwX2J5PWNhc2VzOnNlY3Rpb25faWQmZ3JvdXBfb3JkZXI9YXNjJmRpc3BsYXlfZGVsZXRlZF9jYXNlcz0wLTg2MzRkYzU2NGFhZGYwODJjM2Q1YTM2YzVkNmQxMDg1MTc1MjE1MWFiYTM1MjdkZTNjNDNiYjQ4YWFkMThiYjc:";
	
	private final By LOGIN_FIELD = By.id("name");
	
	private final By PASSWORD_FIELD = By.id("password");
	private final By LOGIN_BUTTON = By.id("button_primary");
	
	private String email;
	private String password;
	
	public void login(WebDriver driver) 
	{
		this.getLoginField(driver).sendKeys(this.email);
		this.getPasswordField(driver).sendKeys(this.password);
		this.getLoginButton(driver).click();
	}
	
	
	public void getURL(WebDriver driver) {
		driver.get(URL);
	}
	
	public WebElement getLoginField(WebDriver driver) {
		return driver.findElement(LOGIN_FIELD);
	}
	
	public WebElement getPasswordField(WebDriver driver) {
		return driver.findElement(PASSWORD_FIELD);
	}
	
	public WebElement getLoginButton(WebDriver driver) {
		return driver.findElement(LOGIN_BUTTON);
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) 
	{
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) 
	{
		this.password = password;
	}
}
