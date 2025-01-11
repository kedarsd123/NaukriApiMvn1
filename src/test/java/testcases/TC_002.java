package testcases;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import pageobjects.NaukriHomePage;

public class TC_002 extends Basic_Pre_Post_Conditions
{
	int JobNo = 1;
	Properties pr = new Properties();
	
	@Test (dataProvider="JobLocationDP")
	public void Launch_NDC_SearchJobs(String SearchJobName, String SearchLocation) throws Exception
	{
		LaunchURL(NaukriURL);
		KillExcessWindows();
		if (d.getTitle().contains("Naukri.com"))
		{
			PostExecDetails("Website nakridotcom launched successfully",micPass,true);
		}
		else
		{
			PostExecDetails("Website nakridotcom failed to launch",micFail,true);
		}
		
		NaukriHomePage NHP = new NaukriHomePage(d);
		NHP.SetSkillName(SearchJobName);
		NHP.SetJobLocation(SearchLocation);
		PostExecDetails("Job Search For : "+SearchJobName+" "+SearchLocation,micInfo,true);
		NHP.ClickSubmitButton();
		
		ArrayList<WebElement> AllResults = (ArrayList<WebElement>) d.findElements(By.xpath("//div[@class='srp-jobtuple-wrapper']"));
		
		/*objJsonTitle = null;
		objJsonTitle = new Object[] {"Job Title","Company Name","Experience","Salary","Location","Expectation","Skill Expected"};
		JobStorage.put(JobNo, objJsonTitle);*/
		
		for (WebElement SingleResult : AllResults)
		{
			String strJobTitle = SingleResult.findElement(By.cssSelector("a[class='title ']")).getText();
			//String strCompanyName = SingleResult.findElement(By.xpath("//span[@class=' comp-dtls-wrap']")).findElement(By.xpath("//a[@class=' comp-name mw-25' or @class=' comp-name ']")).getAttribute("title");
			String strCompanyName = SingleResult.findElement(By.xpath("div/div/span/a[contains(@class,'comp-name')]")).getText();
			//String strCompanyName = "TBD";
			String strExperienceReq = SingleResult.findElement(By.cssSelector("span[class='exp-wrap']")).getText();
			String strSalary = SingleResult.findElement(By.cssSelector("span[class='ni-job-tuple-icon ni-job-tuple-icon-srp-rupee sal']")).getText();
			String strLocation = SingleResult.findElement(By.cssSelector("span[class='ni-job-tuple-icon ni-job-tuple-icon-srp-location loc']")).getText();
			String strExpectation = SingleResult.findElement(By.cssSelector("span[class='job-desc ni-job-tuple-icon ni-job-tuple-icon-srp-description']")).getText();
			WebElement AllSkillSets = SingleResult.findElement(By.cssSelector("ul[class='tags-gt ']"));
			String TotalSkills = "";
			ArrayList<WebElement> Skillset = (ArrayList<WebElement>) AllSkillSets.findElements(By.cssSelector("li[class='dot-gt tag-li ']"));
			for (WebElement Singleset:Skillset)
			{
				if (TotalSkills=="")
					TotalSkills = Singleset.getText();
				else
					TotalSkills = TotalSkills + "," + Singleset.getText();
			}
			
			String strJobList = strJobTitle+"@"+strCompanyName+"@"+strExperienceReq+"@"+strSalary+"@"+strLocation+"@"+strExpectation+"@"+TotalSkills;
			pr.setProperty(String.valueOf(JobNo), strJobList);
			JobNo++;
			//JobStorage.put(JobNo, objJsonJobList);
			//PostJsonDB();
		}
		
		FileWriter fw = new FileWriter (TestTempDataFolder+"\\"+SearchJobName+" - "+SearchLocation+".properties");
		pr.store(fw, SearchJobName+" - "+SearchLocation);
		PostExecDetails("Job Results For "+SearchJobName+ " "+SearchLocation,micInfo,true);
		
		/*//WriteJobs
		XSSFSheet spreadsheet = rsworkbook.createSheet(SearchJobName+" - "+SearchLocation);
		XSSFRow row;
		
		Set <Integer> AllKeyIds = JobStorage.keySet();
		int RowID = 0;
		
		for (Integer key : AllKeyIds)
		{
			row = spreadsheet.createRow(RowID++);
			Object [] objArr = JobStorage.get(key);
			int CellID = 0;
			for (Object obj : objArr)
			{
				Cell cell = row.createCell(CellID++);
				cell.setCellValue(obj.toString());
			}
		}
		//AutoFit Columns
		int lcn = spreadsheet.getRow(0).getLastCellNum();
		for (int i=0; i<lcn; i++)
		{
			spreadsheet.autoSizeColumn(i);
		}
		JobStorage.clear(); */
		
		FlushAssert();
	}
}
