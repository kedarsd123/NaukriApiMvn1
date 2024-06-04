package testcases;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.testng.annotations.Test;

public class TC_004 extends Basic_Pre_Post_Conditions
{
	@Test
	public void Post_Jobs_Json_DB() throws Exception
	{
		Object[] objJsonTitle = new Object[] {"Job Title","Company Name","Experience","Salary","Location","Expectation","Skill Expected"};
		
		//Read Proerty File
		File directory = new File(TestTempDataFolder);
		for(File file : directory.listFiles())
		{
			if (file.isFile())
			{
				String FileName = file.getName();
				FileReader fr = new FileReader (TestTempDataFolder+"\\"+FileName);
				Properties pr = new Properties();
				pr.load(fr);
				for (int i=1; i<=pr.size(); i++)
				{
					if (i==1)
					{
						JobStorage.put(i, objJsonTitle);
					}
					String JBDetails = pr.getProperty(String.valueOf(i));
					Object [] objJsonJobList = null;
					objJsonJobList = JBDetails.split("\\@");
					JobStorage.put(i+1, objJsonJobList);
					PostJsonDB (objJsonTitle,objJsonJobList);
					JobStorage.clear();
				}
			}
		}
		FlushAssert();
	}
}