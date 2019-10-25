package unittests;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Hashtable;

import org.testng.annotations.Test;

import com.config.IConstants;
import com.excel.Xls_Reader;
import com.google.common.io.Resources;
import com.utilities.TestUtil;

import dweb.test.templates.TestTemplate;

public class TestUnit extends TestTemplate {
	
	@Test(dataProvider = "getDataFromExcel" )
	public void testData() throws URISyntaxException
	{
		URL urlFilePath = Resources.getResource(String.format("%s/%s", IConstants.TEST_DATA_LOCATION, IConstants.TEST_DATA_EXCEL_FILE));
		String filePath = Paths.get(urlFilePath.toURI()).toFile().getAbsolutePath();
		Xls_Reader xlsReader = new Xls_Reader(filePath);
		Object[][] objMetrics = TestUtil.getData("PhoneBookSearch", xlsReader, "PhoneBook");
		for(Object[] o : objMetrics)
		{
			//convert object to Hashtable
			Hashtable table = (Hashtable) o[0];
			System.out.println(table.get("FirstName"));
		}		
	}
}
