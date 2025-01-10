package testcases;

import java.time.LocalDateTime;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import reusables.GlobalFunctions;

public class Basic_Pre_Post_Conditions extends GlobalFunctions
{
	@DataProvider
	public Object [][] JobLocationDP ()
	{
		int dctsize = Dictionary.values().size();
		Object[][] data = new Object [dctsize][2];
		int i=0;
		for (Object obj : Dictionary.values())
		{
			String[] Words = obj.toString().split("\\+");
			String SearchJobName = Words[0];
			String SearchLocation = Words[1];
			data[i][0] = SearchJobName;
			data[i][1] = SearchLocation;
			i++;
		}
		return data;
	}
	
	@BeforeTest (groups={"sanity","regression"})
	@Parameters({"os","browser"})
	public void TestPreconditions(@Optional String os,@Optional String br) throws Exception 
	{
		CreateFolder(AutomationFolder);
		CreateFolder(TestLogFolder);
		CreateFolder(TestReportFolder);
		CreateFolder(TestResultFolder);
		CreateFolder(TestSnapFolder);
		CreateFolder(TestSnapCaseFolder);
		CreateFolder(TestJsonApiDBFolder);
		CreateFolder(TestTempDataFolder);
		CreateFolder(TestHTMLReportFolder);
		LoadConfigFile();
		DriverSettings(br);
		PostExecDetails("Before Test : All Folders Created And Logger",micInfo,false);
	}
	
	@AfterTest (groups={"sanity","regression"})
	public void TestPostConditions() throws Exception 
	{
		DeleteAllTempDataFiles();
		PostExecDetails("After Test : Driver Quit and Log Manager ShutDown",micInfo,false);
		PostExecDetails("Execution Ends " + LocalDateTime.now().toString(),micInfo,false);
		ExitTest();
	}

}
