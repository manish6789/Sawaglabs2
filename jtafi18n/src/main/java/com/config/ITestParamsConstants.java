
package com.config;

import org.openqa.selenium.remote.CapabilityType;


public interface ITestParamsConstants {

	String BROWSER = CapabilityType.BROWSER_NAME;
	String APPURL = "appURL";
	String CHROME_DRIVER_EXE = "chromeDriverExe";
	String IE_DRIVER_EXE = "IEDriverExe";
	String FF_DRIVER_EXE = "FFDriverExe";
	String IMPLICIT_WAIT_IN_SECS = "implicitWaitInSecs";
	String PAGE_LOAD_TIME_OUT_IN_SECS = "pageLoadTimeOutInSecs";
	String SCREENSHOT_LOCATION = "screenshotLocation";
	String SCREENSHOT_PICTURE_FORMAT = "screenshotPictureFormat";
	String HTML_REPORT = "htmlReport";
	String BOOL_APPEND_EXISTING = "boolAppendExisting";
	String EXTENT_CONFIG_FILE = "extentConfigFile";
	String EXTENT_TEST_VISIBILITY_MODE = "extentTestVisibilityMode";
	String AUTOMATION_KIT = "automationKit";

	
}
