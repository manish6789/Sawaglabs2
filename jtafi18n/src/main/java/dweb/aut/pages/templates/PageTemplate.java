package dweb.aut.pages.templates;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.config.ITestParamsConstants;
import com.testreport.IReporter;
import com.utilities.ReusableLibs;


public abstract class PageTemplate {
	private static final Logger LOG = Logger.getLogger(PageTemplate.class);
	protected WebDriver wd = null;
	protected IReporter testReport = null;
	protected int implicitWaitInSecs = 0;
	protected int pageLoadTimeOutInSecs = 0;	
	protected PageTemplate(WebDriver webDriver, IReporter testReport) {
		this.wd = webDriver;
		this.testReport = testReport;
		this.implicitWaitInSecs = Integer
				.parseInt(ReusableLibs.getConfigProperty(ITestParamsConstants.IMPLICIT_WAIT_IN_SECS));
		this.pageLoadTimeOutInSecs = Integer
				.parseInt(ReusableLibs.getConfigProperty(ITestParamsConstants.PAGE_LOAD_TIME_OUT_IN_SECS));
		
	}

	protected void sendKeys(By byLocator, String text, String log) {
		try {
			this.waitUntilElementIsClickable(byLocator);
			try {
				this.wd.findElement(byLocator).clear();
				this.wd.findElement(byLocator).sendKeys(text);
				LOG.info(String.format("SendKeys Successful - (By - %s, text - %s)", byLocator, text));
				this.testReport.logSuccess("SendKeys",
						String.format("Entered Text - <mark>%s</mark> To field "+log, text, byLocator));
			} catch (Exception ex) {
				this.sendKeysWithJavascript(byLocator, text);
			}
			if (this.getAttribute(byLocator, "value").equalsIgnoreCase(text)) {
				LOG.info(String.format("SendKeys Successful - (By - %s, text - %s)", byLocator, text));
				this.testReport.logSuccess("input Text Validation",
						String.format("Text present in "+log+" field is \""+text+"\"", text, byLocator));
			}

		} catch (Exception ex) {
			LOG.error(String.format("Exception Encountered - %s", ex.getMessage()));
			this.testReport.logFailure("SendKeys", String
					.format("Failed To Enter Text - <mark>%s</mark> To Locator - <mark>%s</mark>", text, byLocator),
					this.getScreenShotName());
			this.testReport.logException(ex);
			SoftAssert softAssert = new SoftAssert();
			softAssert.fail(ex.getMessage(), ex);			
			softAssert.assertAll();
		}

	}

	protected void sendKeysWithJavascript(By byLocator, String text) {
		try {
			this.waitUntilElementIsClickable(byLocator);
			((JavascriptExecutor) this.wd).executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);",
					this.wd.findElement(byLocator), "value", text);
			LOG.info(String.format("sendKeysWithJavascript Successful - (By - %s, text - %s)", byLocator, text));
			this.testReport.logSuccess("sendKeysWithJavascript",
					String.format("Entered Text - <mark>%s</mark> To Locator - <mark>%s</mark>", text, byLocator));

		} catch (Exception ex) {
			if (!this.getAttribute(byLocator, "value").equalsIgnoreCase(text)) {
				LOG.error(String.format("Exception Encountered - %s", ex.getMessage()));
				this.testReport.logFailure("sendKeysWithJavascript", String
						.format("Failed To Enter Text - <mark>%s</mark> To Locator - <mark>%s</mark>", text, byLocator),
						this.getScreenShotName());
				this.testReport.logException(ex);
			}

		}
	}

	protected void moveToElement(By byLocator) {
		try {
			this.waitUntilElementIsClickable(byLocator);
			Actions action = new Actions(this.wd);
			action.moveToElement(this.wd.findElement(byLocator)).build().perform();
			LOG.info(String.format("moveToElement Successful - (By - %s)", byLocator));
			this.testReport.logSuccess("moveToElement",
					String.format("moveToElement Performed On Locator - <mark>%s</mark>", byLocator));

		} catch (Exception ex) {
			LOG.error(String.format("Exception Encountered - %s", ex.getMessage()));
			this.testReport.logFailure("moveToElement",
					String.format("Failed To Perform moveToElement On Locator - <mark>%s</mark>", byLocator),
					this.getScreenShotName());
			this.testReport.logException(ex);

		}

	}

	protected void click(By byLocator, String log) {
		try {
			try {
				WebDriverWait wait = new WebDriverWait(this.wd, this.implicitWaitInSecs);
				wait.until(ExpectedConditions.elementToBeClickable(byLocator));
			} catch (Exception ex) {}		
			this.wd.findElement(byLocator).click();

			LOG.info(String.format("Click Successful - (By - %s)", byLocator));
			if (this.testReport != null) {
				this.testReport.logSuccess("Click",
						String.format("Click Performed On <mark>"+log+"</mark>", byLocator));
			}

		} catch (Exception ex) {
			LOG.error(String.format("Exception Encountered - %s", ex.getMessage()));
			if (this.testReport != null) {
				this.testReport.logFailure("Click",
						String.format("Failed To Perform Click On Locator - <mark>%s</mark>", byLocator),
						this.getScreenShotName());
				this.testReport.logException(ex);
			}

		}

	}

	protected String getAttribute(By byLocator, String attribute) {
		String attributeValue = null;
		try {
			this.waitUntilElementIsClickable(byLocator);
			attributeValue = this.wd.findElement(byLocator).getAttribute(attribute);
			LOG.info(String.format("Method - %s returns value - %s for attribute - %s for Locator - %s", "getAttribute",
					attributeValue, attribute, byLocator));
			this.testReport.logInfo(String.format(
					"Method - <mark>%s</mark> returns value - <mark>%s</mark> for attribute - <mark>%s</mark> for Locator - <mark>%s</mark>",
					"getAttribute", attributeValue, attribute, byLocator));

		} catch (Exception ex) {
			LOG.error(String.format("Exception Encountered - %s", ex.getMessage()));
			this.testReport.logFailure(
					String.format("getAttribute For Element - %s, For Attribute - %s", byLocator, attribute),
					String.format("Exception Encountered - %s, StackTrace - %s", ex.getMessage(), ex.getStackTrace()),
					this.getScreenShotName());

		}
		return attributeValue;

	}
	
	protected String getValue(By byLocator) {
		String value = null;
		try {
			this.waitUntilElementIsClickable(byLocator);
			value = this.wd.findElement(byLocator).getAttribute("value");
			LOG.info(String.format("Method - %s returns value - %s for attribute - %s for Locator - %s", "getAttribute",
					value, "value", byLocator));
			this.testReport.logInfo(String.format(
					"Method - <mark>%s</mark> returns value - <mark>%s</mark> for attribute - <mark>%s</mark> for Locator - <mark>%s</mark>",
					"getAttribute", value, "value", byLocator));

		} catch (Exception ex) {
			LOG.error(String.format("Exception Encountered - %s", ex.getMessage()));
			this.testReport.logFailure(
					String.format("getAttribute For Element - %s, For Attribute - %s", byLocator, "value"),
					String.format("Exception Encountered - %s, StackTrace - %s", ex.getMessage(), ex.getStackTrace()),
					this.getScreenShotName());

		}
		return value;

	}

	protected String getText(By byLocator) {
		String attributeValue = null;
		try {
			this.waitUntilElementIsClickable(byLocator);
			attributeValue = this.wd.findElement(byLocator).getText();
			LOG.info(String.format("Method - %s returns text - %s for Locator - %s", "getText", attributeValue,
					byLocator));
			this.testReport.logInfo(String.format(
					"Method - <mark>%s</mark> returns value - <mark>%s</mark> for Locator - <mark>%s</mark>", "text",
					attributeValue, byLocator));

		} catch (Exception ex) {
			LOG.error(String.format("Exception Encountered - %s", ex.getMessage()));
			this.testReport.logFailure(String.format("getText For Element - %s", byLocator),
					String.format("Exception Encountered - %s, StackTrace - %s", ex.getMessage(), ex.getStackTrace()),
					this.getScreenShotName());

		}
		return attributeValue;

	}

	protected boolean waitUntilElementIsClickable(By byLocator) {
		boolean isSuccess = false;
		try {
			WebDriverWait wait = new WebDriverWait(this.wd, this.implicitWaitInSecs);
			wait.until(ExpectedConditions.elementToBeClickable(byLocator));
			LOG.info(String.format("Element clickable - (By - %s)", byLocator));
			this.testReport.logSuccess("waitUntilElementIsClickable",
					String.format("Element clickable - (By - %s)", byLocator));
			isSuccess = true;
		} catch (Exception ex) {
			isSuccess = false;
			LOG.error(String.format("Exception Encountered - %s", ex.getMessage()));
			this.testReport.logWarning("waitUntilElementIsClickable",
					String.format("Exception Encountered - %s, StackTrace - %s", ex.getMessage(), ex.getStackTrace()),
					this.getScreenShotName());
			this.testReport.logFailure(String.format("getText For Element - %s", byLocator),
					String.format("Exception Encountered - %s, StackTrace - %s", ex.getMessage(), ex.getStackTrace()),
					this.getScreenShotName());
			this.testReport.logException(ex);
			SoftAssert softAssert = new SoftAssert();
			softAssert.fail(ex.getMessage(), ex);			
			softAssert.assertAll();
		}
		return isSuccess;
	}

	protected boolean waitUntilElementIsVisible(By byLocator) {
		boolean isSuccess = false;
		try {
			WebDriverWait wait = new WebDriverWait(this.wd, this.implicitWaitInSecs);
			wait.until(ExpectedConditions.visibilityOfElementLocated(byLocator));
			LOG.info(String.format("Element visible - (By - %s)", byLocator));
			this.testReport.logSuccess("waitUntilElementIsVisible",
					String.format("Element visible - (By - %s)", byLocator));
			isSuccess = true;
		} catch (Exception ex) {
			isSuccess = false;
			LOG.error(String.format("Exception Encountered - %s", ex.getMessage()));
			this.testReport.logFailure(String.format("Wait for Element - %s", byLocator),
					String.format("Exception Encountered - %s, StackTrace - %s", ex.getMessage(), ex.getStackTrace()),
					this.getScreenShotName());
			this.testReport.logException(ex);
			SoftAssert softAssert = new SoftAssert();
			softAssert.fail(ex.getMessage(), ex);			
			softAssert.assertAll();
			
			/*this.testReport.logWarning("waitUntilElementIsVisible",
					String.format("Exception Encountered - %s, StackTrace - %s", ex.getMessage(), ex.getStackTrace(),
							this.getScreenShotName()));*/
		}
		return isSuccess;
	}

	protected boolean waitUntilDataUpdatedInBackend(String dbUrl, HashMap<String, String> dbCredentials, String sql) {
		WebDriverWait wait = new WebDriverWait(this.wd, this.implicitWaitInSecs);
		return wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver webDriver) {
				ResultSet resultSet = null;
				boolean isSuccess = false;
				try {
					Connection conn = DriverManager.getConnection(dbUrl, dbCredentials.get("userName"),
							dbCredentials.get("password"));
					Statement statement = conn.createStatement();
					resultSet = statement.executeQuery(sql);
					if (resultSet.next()) {
						isSuccess = true;
					}

				} catch (Exception ex) {
					isSuccess = false;
					LOG.error(String.format("Exception Encountered - %s", ex.getMessage()));

				}
				return isSuccess;

			}
		});

	}

	protected boolean isElementPresent(By byLocator) {
		boolean isSuccess = false;
		try {
			// validate element is displayed or not
			implicitwait(2);
			Assert.assertEquals(wd.findElements(byLocator).size() > 0, true);
			LOG.info(String.format("Element Present - (By - %s)", byLocator));
			this.testReport.logSuccess("isElementPresent", String.format("Element Present - (By - %s)", byLocator));
			isSuccess = true;
		} catch (Exception | AssertionError ex) {
			isSuccess = false;
			LOG.info(String.format("Element Not Prensent - (By - %s)", byLocator));
			this.testReport.logInfo(String.format("Element Not Prensent - (By - %s)", byLocator));
		}

		return isSuccess;
	}

	protected boolean isElementVisible(By byLocator) {
		boolean isSuccess = false;
		try {
			// validate element is displayed or not
			implicitwait(2);
			Assert.assertEquals(wd.findElement(byLocator).isDisplayed(), true);
			LOG.info(String.format("Element Present - (By - %s)", byLocator));
			this.testReport.logSuccess("isElementVisible", String.format("Element Present - (By - %s)", byLocator));
			isSuccess = true;
		} catch (Exception | AssertionError ex) {
			isSuccess = false;
			LOG.info(String.format("Element Not Prensent - (By - %s)", byLocator));
			this.testReport.logInfo(String.format("Element Not Prensent - (By - %s)", byLocator));
		}

		return isSuccess;
	}

	protected void implicitwait(int sec) {
		try {
			TimeUnit.SECONDS.sleep(sec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected synchronized String getScreenShotName() {
		String screenShotLocation = ReusableLibs.getConfigProperty(ITestParamsConstants.SCREENSHOT_LOCATION);
		String fileExtension = ReusableLibs.getConfigProperty(ITestParamsConstants.SCREENSHOT_PICTURE_FORMAT);
		return ReusableLibs.getScreenshotFile(screenShotLocation, fileExtension);
	}
	
	/*****Generic Application Level Validations */
	
	/**
	 * @param actual
	 * @param expected
	 * Validates Text equals
	 */
	public void validateTextEquals(String actual, String expected)
	{
		try{
			if((actual.replaceAll("[\r\n]+", " ")).equalsIgnoreCase(expected))
			{
				this.testReport.logSuccess("Validate Text Present", "Expected Test is "+expected+" Actual Text is "+actual);
			}
			else
			{
				this.testReport.logFailure("Validate Text Present", "Expected Test is "+expected+" Actual Text is "+actual, this.getScreenShotName());
			}
		}catch(Exception e)
		{
			this.testReport.logFailure("Validate Text Present", e.getMessage().toString(), this.getScreenShotName());
		}
	}
	
	public void validateTextPresent(String actual, String expected)
	{
		try{
			if((actual.replaceAll("[\r\n]+", " ")).equalsIgnoreCase(expected))
			{
				this.testReport.logSuccess("Validate Text Present", "Expected Test is "+expected+" Actual Text is "+actual);
			}
			else
			{
				this.testReport.logFailure("Validate Text Present", "Expected Test is "+expected+" Actual Text is "+actual, this.getScreenShotName());
			}
		}catch(Exception e)
		{
			this.testReport.logFailure("Validate Text Present", e.getMessage().toString(), this.getScreenShotName());
		}
	}
	
	public List<WebElement> findElements(By locator)
	{
		try{
			List<WebElement> list = this.wd.findElements(locator);
			return list;
		}catch(Exception e){
			this.testReport.logFailure("Get all the elements", e.getMessage().toString(), this.getScreenShotName());
			return null;
		}
	}
	
}
