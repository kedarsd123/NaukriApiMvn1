package actionsteps;

//import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import testcases.Basic_Pre_Post_Conditions;
import testcases.TC_001;
import testcases.TC_002;
import testcases.TC_003;
import testcases.TC_004;
import testcases.TC_005;

//@Listeners(reusables.TngListeners.class)
public class Driver extends Basic_Pre_Post_Conditions
{
	@Test (priority=1,groups={"sanity","regression"})
	public void TC_001_Run()
	{
		TC_001 TestRun = new TC_001();
		TestRun.Input_Read_Jobs();
	}
	
	@Test (priority=2,groups={"sanity","regression"},dependsOnMethods= {"TC_001_Run"},dataProvider="JobLocationDP")
	public void TC_002_Run(String SearchJobName, String SearchLocation) throws Exception
	{
		TC_002 TestRun = new TC_002();
		TestRun.Launch_NDC_SearchJobs(SearchJobName, SearchLocation);
	}
	
	@Test (priority=3,groups={"sanity","regression"},dependsOnMethods= {"TC_001_Run","TC_002_Run"})
	public void TC_003_Run() throws Exception
	{
		TC_003 TestRun = new TC_003();
		TestRun.Post_Jobs_Excel_Summary_Report();
	}
	
	@Test (priority=4,groups={"sanity","regression"},dependsOnMethods= {"TC_001_Run","TC_002_Run","TC_003_Run"})
	public void TC_004_Run() throws Exception
	{
		TC_004 TestRun = new TC_004();
		TestRun.Post_Jobs_Json_DB();
	}
	
	@Test (priority=5,groups={"sanity","regression"},dependsOnMethods= {"TC_001_Run","TC_002_Run","TC_003_Run"})
	public void TC_005_Run() throws Exception
	{
		TC_005 TestRun = new TC_005();
		TestRun.SendEmail();
	}
}
