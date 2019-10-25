package dweb.test.templates;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import com.config.IConstants;
import com.config.ITestParamsConstants;
import com.excel.Xls_Reader;
import com.google.common.io.Resources;
import com.testreport.IReporter;
import com.utilities.ReusableLibs;
import com.utilities.TestUtil;

public abstract class TestTemplate {

	private static final Logger LOG = Logger.getLogger(TestTemplate.class);
	protected static IReporter testReport = null;
	protected String ChromeDriverExe = null;
	// protected String url = null;
	protected static String implicitWaitInSecs = null;
	protected static String pageLoadTimeOutInSecs = null;
	public static ThreadLocal<WebDriver> threadLocalWebDriver = new ThreadLocal<WebDriver>();
	
	/**
	 * get parameter from either test context or framework configuration file where
	 * test context parameter overrides framework configuration parameter
	 * 
	 * @param testContext
	 * @param parameter
	 * @return
	 */
	protected String getTestParameter(ITestContext testContext, String parameter) {
		String parameterVal = testContext.getCurrentXmlTest().getParameter(parameter) == null
				? ReusableLibs.getConfigProperty(parameter)
				: testContext.getCurrentXmlTest().getParameter(parameter);
		LOG.info(String.format("Test Execution Input Parameter = %s, Value = %s", parameter, parameterVal));
		return parameterVal;
	}

	/**
	 * 
	 * @param testContext
	 * @return
	 */
	protected Map<String, String> getAllTestParameters(ITestContext testContext) {
		return testContext.getCurrentXmlTest().getAllParameters();
	}

	/**
	 * 
	 * @param testContext
	 * @param regExp
	 * @return
	 */
	protected Map<String, String> getAllTestParameters(ITestContext testContext, String regExp) {
		Map<String, String> mapMobileParams = new HashMap<String, String>();
		this.getAllTestParameters(testContext).forEach((k, v) -> {
			if (k.matches(regExp)) {
				mapMobileParams.put(k, v);
			}
		});

		return mapMobileParams;
	}
	
	protected DesiredCapabilities convertTestParamsToCapabilities(ITestContext testContext)
	{
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setBrowserName(this.getTestParameter(testContext, ITestParamsConstants.BROWSER));
		this.getAllTestParameters(testContext).forEach((k, v) ->{
			cap.setCapability(k, v);
		});
		
		return cap;
	}
	
	/**
	 * 
	 * @param testContext
	 * @param regExp
	 * @return
	 */
	protected DesiredCapabilities convertTestParamsToCapabilities(ITestContext testContext, String regExp)
	{
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setBrowserName(this.getTestParameter(testContext, ITestParamsConstants.BROWSER));
		this.getAllTestParameters(testContext, regExp).forEach((k, v) ->{
			cap.setCapability(k, v);
		});
		
		return cap;
	}
	/**
	 * Dataprovider to return data matrix from excel
	 * 
	 * @return
	 * @throws URISyntaxException
	 */
	@DataProvider(name = "getDataFromExcel", parallel = false)
	protected Object[][] getDataFromExcel() throws URISyntaxException {
		URL urlFilePath = Resources.getResource(String.format("%s/%s", IConstants.TEST_DATA_LOCATION, IConstants.TEST_DATA_EXCEL_FILE));
		String filePath = Paths.get(urlFilePath.toURI()).toFile().getAbsolutePath();
		Xls_Reader xlsReader = new Xls_Reader(filePath);
		Object[][] objMetrics = TestUtil.getData("PhoneBookSearch", xlsReader, "PhoneBook");
		return objMetrics;
	}
	
	@DataProvider(name = "getTitleFromExcel", parallel = false)
	protected Object[][] getTitleFromExcel() throws URISyntaxException {
		URL urlFilePath = Resources.getResource(String.format("%s/%s", IConstants.TEST_DATA_LOCATION, IConstants.TEST_DATA_EXCEL_FILE));
		String filePath = Paths.get(urlFilePath.toURI()).toFile().getAbsolutePath();
		Xls_Reader xlsReader = new Xls_Reader(filePath);
		Object[][] objMetrics = TestUtil.getData("PhoneBookPageTitleVerify", xlsReader, "PhoneBook");
		return objMetrics;
	}
	
	@DataProvider(name = "getVendorDataFromExcel", parallel = false)
	protected Object[][] getVendorDataFromExcel() throws URISyntaxException {
		URL urlFilePath = Resources.getResource(String.format("%s/%s", IConstants.TEST_DATA_LOCATION, IConstants.TEST_DATA_EXCEL_FILE));
		String filePath = Paths.get(urlFilePath.toURI()).toFile().getAbsolutePath();
		Xls_Reader xlsReader = new Xls_Reader(filePath);
		Object[][] objMetrics = TestUtil.getData("JDEVendorSearch", xlsReader, "VendorSearch");
		return objMetrics;
	}

	
	/**
	 * Returns screenshot name for screenshot being captured
	 * 
	 * @return
	 */
	protected String getScreenShotName() {
		String screenShotLocation = ReusableLibs.getConfigProperty(ITestParamsConstants.SCREENSHOT_LOCATION);
		String fileExtension = ReusableLibs.getConfigProperty(ITestParamsConstants.SCREENSHOT_PICTURE_FORMAT);
		synchronized (this) {
			String screenShotName = ReusableLibs.getScreenshotFile(screenShotLocation, fileExtension);
			LOG.debug(String.format("ScreenShot Name For Captured Screen Shot = %s", screenShotName));
			return screenShotName;
		}
	}
	
	

}
