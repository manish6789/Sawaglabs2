package com.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.testng.log4testng.Logger;

import com.config.IConstants;
import com.google.common.io.Resources;

public class ReusableLibs 
{
    private static final Logger LOG = Logger.getLogger(ReusableLibs.class);

    /**
     * Purpose: Invoke unlock.vbs process in master machine 
     */
    public static void invokeUnlockProcessInHub()
    {
        try
        {
            String sVBSFilePath = System.getProperty("user.dir") + File.separatorChar + "unlock.vbs";
            Runtime rt = Runtime.getRuntime();
            rt.exec("wscript.exe " + sVBSFilePath);
        }
        catch(Exception e)
        {
            System.out.println("Error Exception occured in Method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
            
        }
    }

    /**
     * Purpose: Gets Config property
     * @param sKeyName
     * @return String
     */
    public static String getConfigProperty(String sKeyName)
    {
        try
        {
            Properties prop = new Properties();          

            String sFilePath = Paths.get(Resources.getResource(IConstants.TEST_EXEC_CONFIG_FILE).toURI()).toFile().getAbsolutePath();

            FileInputStream file = new FileInputStream(sFilePath);
            InputStreamReader inputFileStreamReader = new InputStreamReader(file,"UTF-8");

            prop.load(inputFileStreamReader);
            String sValue = prop.getProperty(sKeyName);
            return sValue;
        }
        catch(Exception e)
        {

           System.out.println("Error" + e.getCause().toString());
            return "";
        }
    }

    
    public static boolean fileExists(String sFileName)
    {		
        try
	    {		
	        File objFile = new File(sFileName);
	        if (objFile.exists()){
	            return true;
	        }		
        }
        
        catch(Exception e)
        {
        	LOG.error(e.getStackTrace());
          
        }
	    return false;
    }
    
    public static boolean makeDir(String sFileName)
    {		
        try
	    {		
	        if(!fileExists(sFileName))	
	        {
	        	File objFile = new File(sFileName);
	        	objFile.mkdir();
	        	return true;
	        }
        }
        
        catch(Exception e)
        {
        	LOG.error(e.getStackTrace());
          
        }
	    return false;
    }

    
    public static String getLocalizedKeyValue(Locale locale, String sbaseName, String sKey)
    {
    	String keyValue = null;
    	try
    	{
	    	ResourceBundle resourceBundle = ResourceBundle.getBundle(sbaseName, locale);
	    	keyValue = resourceBundle.getString(sKey);
    	}
    	catch(Exception ex) {}    	
    	return keyValue;
    }
    
    public synchronized static String getScreenshotFile(String screenshotFolder, String extension)
	{
		String fileName = null;
		int count = 1;
		String strScreenshotFileName = String.format("%s%s%s_%d_%s_%d.%s", screenshotFolder, File.separatorChar, "Screenshot", Thread.currentThread().getId(), new SimpleDateFormat("dd-MM-yyyy_HH_mm_ss_SSS").format(new Date()), count, extension);
		
		while(fileExists(strScreenshotFileName))
		{
			//get new file name
			//increase count
			strScreenshotFileName = String.format("%s%s%s_%d_%s_%d.%s", screenshotFolder, File.separatorChar, "Screenshot", Thread.currentThread().getId(),  new SimpleDateFormat("dd-MM-yyyy_HH_mm_ss_SSS").format(new Date()), ++count, extension);
			
		}		
		fileName = strScreenshotFileName;
		return fileName;
	}
    
    public static int getResponseCode(String url) throws MalformedURLException, IOException
    {
    	HttpURLConnection httpUrlConn = (HttpURLConnection) new URL(url).openConnection();
    	httpUrlConn.setRequestMethod("HEAD");          
    	httpUrlConn.connect();          
        int respCode = httpUrlConn.getResponseCode(); 
        return respCode;
        
    }
}