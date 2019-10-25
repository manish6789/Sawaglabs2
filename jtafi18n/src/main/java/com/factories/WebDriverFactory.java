package com.factories;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.config.ITestParamsConstants;
import com.google.common.io.Resources;
import com.utilities.ReusableLibs;


public class WebDriverFactory {
		
	private static final Logger LOG = Logger.getLogger(WebDriverFactory.class);
	private WebDriverFactory() {}	
	
	public static WebDriver getWebDriver(Map<String, String> dcMap) throws URISyntaxException, MalformedURLException
	{
		return getLocalWebDriver(dcMap);
						
	}
	
	private static WebDriver getLocalWebDriver(Map<String, String> capMap) throws MalformedURLException, URISyntaxException
	{
		WebDriver wd = null;
		/*AppiumDriver<MobileElement> appiumDriver = null;*/
		if(capMap.get(ITestParamsConstants.AUTOMATION_KIT).equalsIgnoreCase("selenium"))
		{
			switch(capMap.get(ITestParamsConstants.BROWSER).toUpperCase())		
			{
			case "CHROME": 
				String chromeDriverExe = ReusableLibs.getConfigProperty(ITestParamsConstants.CHROME_DRIVER_EXE);
				URL urlFilePath = Resources
						.getResource(String.format("%s%s%s", "drivers", File.separatorChar, chromeDriverExe));
				String chromedriverPath = Paths.get(urlFilePath.toURI()).toFile().getAbsolutePath();
				System.setProperty("webdriver.chrome.driver", chromedriverPath);
		
				/* Chrome Settings */
				String currentDirectory = System.getProperty("user.dir");
				String downloadFilepath = currentDirectory+"\\downloads";
				Map<String, Object> prefs = new HashMap<String, Object>();
				ChromeOptions options = new ChromeOptions();
				options.addArguments("disable-extensions");
				prefs.put("credentials_enable_service", false);
				prefs.put("profile.password_manager_enabled", false);
				prefs.put("profile.default_content_settings.popups", 0);
				prefs.put("download.prompt_for_download", "false");
				prefs.put("plugins.always_open_pdf_externally", true);
				prefs.put("download.default_directory", downloadFilepath);
				prefs.put("plugins.plugins_disabled", new String[]{
					    "Adobe Flash Player", "Chrome PDF Viewer"});
				options.setExperimentalOption("prefs", prefs);
				
				wd = new ChromeDriver(options);		
				LOG.info(String.format("Driver Initialized For Browser - %s", "Chrome"));
				break;
				
			case "FIREFOX":
                String ffDriverExe = ReusableLibs.getConfigProperty(ITestParamsConstants.FF_DRIVER_EXE);
                urlFilePath = Resources
                                .getResource(String.format("%s%s%s", "drivers", File.separatorChar, ffDriverExe));
                String ffdriverPath = Paths.get(urlFilePath.toURI()).toFile().getAbsolutePath();
                System.setProperty("webdriver.gecko.driver", ffdriverPath);     
                FirefoxOptions option = new FirefoxOptions();
                option.setBinary("C:\\work_new\\geckodriver.exe");
                wd = new FirefoxDriver();
                LOG.info(String.format("Driver Initialized For Browser - %s", "Firefox"));
                break;

				
			case "IE":			
			case "INTERNETEXPLORER":
				String ieDriverExe = ReusableLibs.getConfigProperty(ITestParamsConstants.IE_DRIVER_EXE);
				urlFilePath = Resources
						.getResource(String.format("%s%s%s", "drivers", File.separatorChar, ieDriverExe));
				String iedriverPath = Paths.get(urlFilePath.toURI()).toFile().getAbsolutePath();
				System.setProperty("webdriver.ie.driver", iedriverPath);	
				InternetExplorerOptions ieOptions = new InternetExplorerOptions();
				ieOptions.enableNativeEvents();
				ieOptions.takeFullPageScreenshot();
				ieOptions.requireWindowFocus();
				ieOptions.introduceFlakinessByIgnoringSecurityDomains();				
				ieOptions.ignoreZoomSettings();
				ieOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
				ieOptions.destructivelyEnsureCleanSession();
				ieOptions.setCapability("disable-popup-blocking", true);
				ieOptions.setCapability("ignoreProtectedModeSettings", false);
				wd = new InternetExplorerDriver(ieOptions);
				LOG.info(String.format("Driver Initialized For Browser - %s", "IE"));
				break;
			}
		}
		
			
		wd.manage().timeouts().implicitlyWait(Integer.parseInt(ReusableLibs.getConfigProperty(ITestParamsConstants.IMPLICIT_WAIT_IN_SECS)),
				TimeUnit.SECONDS);
		wd.manage().timeouts().pageLoadTimeout(Integer.parseInt(ReusableLibs.getConfigProperty(ITestParamsConstants.PAGE_LOAD_TIME_OUT_IN_SECS)),
				TimeUnit.SECONDS);
		wd.manage().window().maximize();
		
		return wd;
	}

}
