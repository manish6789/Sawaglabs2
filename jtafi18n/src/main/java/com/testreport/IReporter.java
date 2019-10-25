package com.testreport;

public interface IReporter {
	
	 void initTestCase(String testcaseName); 	 
	 
	 void createTestNgXMLTestTag(String testCaseName);	 
	 
	 void logSuccess(String stepName);
	 void logSuccess(String stepName, String stepDescription);
	 void logSuccess(String stepName, String stepDescription, String screenShotPath);
	 
	 void logFailure(String stepName);
	 void logFailure(String stepName, String stepDescription);
	 void logFailure(String stepName, String stepDescription, String screenShotPath);
	 
	 void logInfo(String message);
	 void logInfo(String message, String screenShotPath);
	 
	 void logWarning(String stepName);
	 void logWarning(String stepName, String stepDescription);
	 void logWarning(String stepName, String stepDescription, String screenShotPath);
	 
	 void logException(Throwable ex);
	 void logException(Throwable ex, String screenShotPath);
	 
	 void logFatal(Throwable ex);
	 void logFatal(Throwable ex, String screenShotPath);
	 
	 
	 void updateTestCaseStatus();
	 void close();
	 void manipulateTestReport(ITestReportManipulator objTestReportManipulator);
}
