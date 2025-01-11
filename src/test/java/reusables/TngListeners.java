package reusables;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class TngListeners extends GlobalFunctions implements ITestListener
{
	public void onTestStart(ITestResult result) 
	{
		try
		{
			PostExecDetails("Test Start : "+ result.getName(),micInfo,false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}  
	  
	public void onTestSuccess(ITestResult result) 
	{
		try
		{
			PostExecDetails("Test Success : "+ result.getName(),micPass,false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}  
	  
	public void onTestFailure(ITestResult result) 
	{
		try
		{
			PostExecDetails("Test Failed : "+ result.getName(),micFail,false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	 
	public void onTestSkipped(ITestResult result) 
	{
		try
		{
			PostExecDetails("Test Skipped : "+ result.getName(),micWarning,false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
