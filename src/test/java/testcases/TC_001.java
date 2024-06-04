package testcases;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

//@Listeners(reusables.TngListeners.class)
public class TC_001 extends Basic_Pre_Post_Conditions
{
	@Test
	public void Input_Read_Jobs()
	{
		File file = new File(ExcelPath);
		FileInputStream InputStream;
		try 
		{
			InputStream = new FileInputStream (file);
			Workbook FindJobs = new XSSFWorkbook(InputStream);
			Sheet Entries = FindJobs.getSheet("Entries");
			int FirstRowNum = Entries.getFirstRowNum();
			int LastRowNum = Entries.getLastRowNum();
			int RowCount = LastRowNum - FirstRowNum + 1;
			for (int i=1; i<RowCount; i++)
			{
				Row RowControl = Entries.getRow(i);
				if (RowControl.getCell(1).getStringCellValue().equalsIgnoreCase("Yes"))
				{
					String JobName = RowControl.getCell(2).getStringCellValue();
					String JobLocation = RowControl.getCell(3).getStringCellValue();
					Dictionary.put(i, JobName + "+" + JobLocation);
				}
			}
			FindJobs.close();
			PostExecDetails("Input Jobs Read Successfully",micPass,false);
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		FlushAssert();
	}
}
