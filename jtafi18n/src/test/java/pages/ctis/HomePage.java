package pages.ctis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.asserts.SoftAssert;

import com.testautomationguru.utility.PDFUtil;
import com.testreport.IReporter;
import com.utilities.ReusableLibs;

import dweb.aut.pages.templates.PageTemplate;


public class HomePage extends PageTemplate {

	private SoftAssert softAssert = null;
	public HomePage(WebDriver webDriver, IReporter testReport) {
		super(webDriver, testReport);
		this.softAssert = new SoftAssert();
	}
	
	ReusableLibs confi = new ReusableLibs();
	String currentDirectory = System.getProperty("user.dir");
	String downloadFilepath = currentDirectory;
	
	private By userName					=	By.cssSelector("[id='user-name']");
	private By password					=	By.cssSelector("[id='password']");
	private By LoginButton				=	By.cssSelector("[type='submit'][value='LOGIN']");
	private By shoppingCart				=	By.cssSelector("[id='shopping_cart_container']");
	private By burgerButton				=	By.cssSelector("[class='bm-burger-button']");
	private By logout					=	By.cssSelector("[id='logout_sidebar_link']");
	private By errorMessage				=	By.cssSelector("[data-test='error']");
	
	//Enter User Name, Password and click on Login
	public void login(String userName_App, String password_App)
	{
		String userNameValue = userName_App;;
		String passwordValue = password_App;
		
		this.waitUntilElementIsVisible(userName);
		this.sendKeys(userName, userNameValue, "User Name");
		this.implicitwait(2);
		this.sendKeys(password, passwordValue, "Password");
		this.click(LoginButton, "Login button");
	}
	
	//verify application logged in successfully
	public void verifyApplicationLoggedIn()
	{
		this.waitUntilElementIsVisible(shoppingCart);
	}
	
	//verify application logged in successfully
	public void verifyApplicationDisplayedError()
	{
		this.waitUntilElementIsVisible(errorMessage);
	}
	
	//Log out from application
	public void logout(){
		this.waitUntilElementIsClickable(burgerButton);
		this.click(burgerButton, "Burger Button");
		this.waitUntilElementIsVisible(logout);
		this.waitUntilElementIsClickable(logout);
		this.implicitwait(2);
		this.click(logout, "logout");
		this.waitUntilElementIsVisible(userName);
	}
	
	//verify error message displayed
	public void verifyErrorMessageDisplayed(String expectedMessage) {
		this.waitUntilElementIsVisible(errorMessage);
		String message = this.getText(errorMessage);
		this.validateTextPresent(message, expectedMessage);
	}

}
