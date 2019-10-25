package test.swag.testcase;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Hashtable;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.config.IConstants;
import com.config.ITestParamsConstants;
import com.excel.Xls_Reader;
import com.google.common.io.Resources;
import com.utilities.TestUtil;

import dweb.test.templates.TestTemplate;
import dweb.test.templates.TestTemplateMethodLevelInit;
import pages.ctis.HomePage;


public class SWAGlabs extends TestTemplateMethodLevelInit{
	
	@DataProvider(name = "SWAG", parallel = false)
	protected Object[][] getGMEdataFromExcel() throws URISyntaxException {
		URL urlFilePath = Resources
				.getResource(String.format("%s/%s", IConstants.TEST_DATA_LOCATION, IConstants.TEST_DATA_EXCEL_FILE));
		String filePath = Paths.get(urlFilePath.toURI()).toFile().getAbsolutePath();
		Xls_Reader xlsReader = new Xls_Reader(filePath);
		Object[][] objMetrics = TestUtil.getData("SWAGTestData", xlsReader, "SWAG");
		return objMetrics;
	}
	
	
	@Test(dataProvider = "SWAG")
	public void standardUserLogin(ITestContext testContext, Hashtable<String, String> data)
	{
		//Initialization
		HomePage homePage = new HomePage(TestTemplate.threadLocalWebDriver.get(), TestTemplate.testReport);
		
		//String browser = getTestParameter(testContext, ITestParamsConstants.BROWSER);	
		
		//Login to Application
		homePage.login(data.get("StandardUser"), data.get("password"));
		
		//Verify logged in succesfully
		homePage.verifyApplicationLoggedIn();
		
		//Logout
		homePage.logout();
	}
	
	@Test(dataProvider = "SWAG")
	public void lockedUserLogin(ITestContext testContext, Hashtable<String, String> data)
	{
		//Initialization
		HomePage homePage = new HomePage(TestTemplate.threadLocalWebDriver.get(), TestTemplate.testReport);
		
		//Login to Application
		homePage.login(data.get("lockedUser"), data.get("password"));
		
		//Verify application displayed error
		homePage.verifyApplicationDisplayedError();
		
		//Validate error message displayed
		homePage.verifyErrorMessageDisplayed(data.get("ErrorMessageLockedUser"));
		
	}
	
	@Test(dataProvider = "SWAG")
	public void problemUserLogin(ITestContext testContext, Hashtable<String, String> data)
	{
		//Initialization
		HomePage homePage = new HomePage(TestTemplate.threadLocalWebDriver.get(), TestTemplate.testReport);
		
		//Login to Application
		homePage.login(data.get("problemUser"), data.get("password"));
		
		//Verify application displayed error
		homePage.verifyApplicationDisplayedError();
		
	}
	
	@Test(dataProvider = "SWAG")
	public void performanceUserLogin(ITestContext testContext, Hashtable<String, String> data)
	{
		//Initialization
		HomePage homePage = new HomePage(TestTemplate.threadLocalWebDriver.get(), TestTemplate.testReport);
		
		//Login to Application
		homePage.login(data.get("PerformanceUser"), data.get("password"));
		
		//Verify logged in succesfully
		homePage.verifyApplicationLoggedIn();
		
		//Logout
		homePage.logout();	
	}
	
	@Test(dataProvider = "SWAG")
	public void invalidPasswordLogin(ITestContext testContext, Hashtable<String, String> data)
	{
		//Initialization
		HomePage homePage = new HomePage(TestTemplate.threadLocalWebDriver.get(), TestTemplate.testReport);
		
		//Login to Application
		homePage.login(data.get("StandardUser"), data.get("InvalidPassword"));
		
		//Verify application displayed error
		homePage.verifyApplicationDisplayedError();
		
		//Validate error message displayed
		homePage.verifyErrorMessageDisplayed(data.get("ErrorMessageInvalidPassword"));	
	}

}
