package dweb.test.templates;

import java.lang.reflect.Method;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;
import org.testng.xml.XmlTest;

import com.config.IConstants;
import com.config.ITestParamsConstants;
import com.factories.WebDriverFactory;
import com.testreport.ExtentReporter;
import com.testreport.ExtentReporter.ExtentTestVisibilityMode;
import com.testreport.ReportFactory;
import com.testreport.ReportFactory.ReportType;
import com.utilities.ReusableLibs;



public abstract class TestTemplateMethodLevelInit extends TestTemplate {

	protected SoftAssert softAssert = null;
	/**
	 * Configuration/Initialization before starting suite
	 * 
	 * @param testContext
	 * @param xmlTest
	 * @throws Exception
	 */
	@BeforeSuite
	protected void beforeSuite(ITestContext testContext, XmlTest xmlTest) throws Exception {
		TestTemplate.implicitWaitInSecs = ReusableLibs.getConfigProperty(ITestParamsConstants.IMPLICIT_WAIT_IN_SECS);
		TestTemplate.pageLoadTimeOutInSecs = ReusableLibs
				.getConfigProperty(ITestParamsConstants.PAGE_LOAD_TIME_OUT_IN_SECS);
		String extentTestVisibilityMode = ReusableLibs
				.getConfigProperty(ITestParamsConstants.EXTENT_TEST_VISIBILITY_MODE);

		TestTemplate.testReport = ReportFactory.getInstance(ReportType.ExtentHtml,
				ExtentTestVisibilityMode.valueOf(extentTestVisibilityMode));
	}

	@AfterSuite(alwaysRun = true)
	protected void afterSuite(ITestContext testContext) {
		TestTemplate.testReport.updateTestCaseStatus();
	}

	/**
	 * Configuration/Initialization before running each test
	 * 
	 * @param testContext
	 */
	@BeforeTest
	protected void beforeTest(ITestContext testContext) {
		if (((ExtentReporter) TestTemplate.testReport)
				.getExtentTestVisibilityMode() == ExtentTestVisibilityMode.TestNGTestTagAsTestsAtLeft) {
			TestTemplate.testReport
					.createTestNgXMLTestTag(String.format("%s", testContext.getCurrentXmlTest().getName()));

		}
		String automationKit = this.getTestParameter(testContext, ITestParamsConstants.AUTOMATION_KIT);	
	}

	/**
	 * Configuration/Initialization after running each test
	 * 
	 * @param testContext
	 */
	@AfterTest(alwaysRun = true)
	protected void afterTest(ITestContext testContext) {
		TestTemplate.testReport.updateTestCaseStatus();
		if (threadLocalWebDriver.get() != null) {
			threadLocalWebDriver.get().quit();
		}
	}

	/**
	 * Configuration/Initialization before running each test case Driver is
	 * initialized before running each test method
	 * 
	 * @param testContext
	 * @param m
	 * @throws URISyntaxException
	 */
	@BeforeMethod(alwaysRun = true)
	protected void beforeMethod(ITestContext testContext, Method m) throws URISyntaxException {

		try {
			this.softAssert = new SoftAssert();
			WebDriver webDriver = null;
			if (TestTemplate.testReport instanceof ExtentReporter) {

				if (((ExtentReporter) TestTemplate.testReport)
						.getExtentTestVisibilityMode() == ExtentTestVisibilityMode.TestNGTestTagAsTestsAtLeft) {
					TestTemplate.testReport.initTestCase(String.format("%s", m.getName()));
				} else if (((ExtentReporter) TestTemplate.testReport)
						.getExtentTestVisibilityMode() == ExtentTestVisibilityMode.TestNGTestMethodsAsTestAtLeft) {
					TestTemplate.testReport.initTestCase(
							String.format("%s-%s", testContext.getCurrentXmlTest().getName(), m.getName()));
				}
			}

			// Use APPURL if provided in Test Suite XML
			String url = this.getTestParameter(testContext, ITestParamsConstants.APPURL);

			webDriver = WebDriverFactory.getWebDriver(this.getAllTestParameters(testContext));

			try {
				webDriver.get(url);
			} catch (TimeoutException ex) {
				testReport.logException(ex);
			}
			threadLocalWebDriver.set(webDriver);

		} catch (Exception ex) {
			testReport.logException(ex);

		} finally {
			//Log Info to extent report
			TestTemplate.testReport.logInfo(String.format(
					"Thread - %d , Executes Next Test Method - %s On Browser - %s On URL - %s", Thread.currentThread().getId(),
					m.getName(), this.getTestParameter(testContext, ITestParamsConstants.BROWSER), this.getTestParameter(testContext, ITestParamsConstants.APPURL)));
		}

	}

	/**
	 * Configuration/Initialization after running each test case webdriver is killed
	 * after each test case execution
	 * 
	 * @param testContext
	 * @param testResult
	 * @param m
	 * @throws Exception
	 */
	@AfterMethod(alwaysRun = true)
	protected void afterMethod(ITestContext testContext, ITestResult testResult, Method m) throws Exception {		
		TestTemplate.testReport.logInfo(String.format(
				"Thread - %d , Completes Executing Test Method - %s On Browser - %s", Thread.currentThread().getId(),
				m.getName(), this.getTestParameter(testContext, ITestParamsConstants.BROWSER)));

		try {
			threadLocalWebDriver.get().quit();
		} catch (Exception ex) {
			testReport.logException(ex);
		}

		finally {
			TestTemplate.testReport.updateTestCaseStatus();			
		}
	}

}
