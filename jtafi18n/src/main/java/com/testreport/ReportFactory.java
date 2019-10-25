package com.testreport;

import org.apache.log4j.Logger;

import com.config.ITestParamsConstants;
import com.testreport.ExtentReporter.ExtentTestVisibilityMode;
import com.utilities.ReusableLibs;


public class ReportFactory {
	
	private static IReporter testReport = null;	
	private static final Logger LOG = Logger.getLogger(ReportFactory.class);
	
	public enum ReportType
	{
		ExtentHtml
	}
	
	private ReportFactory()
	{
		
	}
	
	public synchronized static IReporter getInstance(ReportType reportType, ExtentTestVisibilityMode extentTestVisibilityMode) throws Exception
	{
		if(ReportFactory.testReport == null)
		{
			switch(reportType)	
			{
				case ExtentHtml :
					
					String htmlReportName = ReusableLibs.getConfigProperty(ITestParamsConstants.HTML_REPORT);
					String screenShotLocation = ReusableLibs.getConfigProperty(ITestParamsConstants.SCREENSHOT_LOCATION);		
					String strBoolAppendExisting = ReusableLibs.getConfigProperty(ITestParamsConstants.BOOL_APPEND_EXISTING);
					String extentConfigFile = ReusableLibs.getConfigProperty(ITestParamsConstants.EXTENT_CONFIG_FILE);	
					boolean boolAppendExisting = false;
					if(strBoolAppendExisting !=null && strBoolAppendExisting.equalsIgnoreCase("true"))
					{
						boolAppendExisting = true;
					}
					
					ReusableLibs.makeDir(screenShotLocation);
					String filePath = String.format("%s", htmlReportName);
					ReportFactory.testReport = new ExtentReporter(filePath, extentConfigFile, boolAppendExisting, extentTestVisibilityMode);
										
					break;				
				
				default:
					throw new Exception("Html Report Other Than Extent Is Not Currently Supported...");
					
			}
			
		}
		return ReportFactory.testReport;
			
	}
	
	
}
