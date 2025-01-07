package testcases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.io.FileHandler;
import org.testng.annotations.Test;

public class TC_003 extends Basic_Pre_Post_Conditions
{
	XSSFWorkbook rsworkbook = new XSSFWorkbook();
	
	@Test
	public void Post_Jobs_Excel_Summary_Report() throws Exception 
	{
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
						Object[] objJsonTitle = null;
						objJsonTitle = new Object[] {"Job Title","Company Name","Experience","Salary","Location","Expectation","Skill Expected"};
						JobStorage.put(i, objJsonTitle);
					}
					String JBDetails = pr.getProperty(String.valueOf(i));
					Object [] objJsonJobList = null;
					objJsonJobList = JBDetails.split("\\@");
					JobStorage.put(i+1, objJsonJobList);
				}
				
				//Create Excel Sheets And Write Content
				XSSFSheet spreadsheet = rsworkbook.createSheet(FileName.split("\\.")[0].toString());
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
				for (int j=0; j<lcn; j++)
				{
					spreadsheet.autoSizeColumn(j);
				}
				JobStorage.clear();
			}
		}
		
		//Write To Excel Workbook And Save
		File tempfile = new File(JobSummaryReportPath);
		FileOutputStream out;
		try 
		{
			out = new FileOutputStream(tempfile);
			rsworkbook.write(out);
			out.close();
			rsworkbook.close();
		}
		catch (Exception e) 
		{
			System.out.println(e.toString());
		}
		PostExecDetails("Job Results Exported To Excel",micPass,false);
		
		//Format Summary Report
		HashMap<String, Object> property1 = new HashMap<String, Object>();
		//Set Border Around Cell
		property1.put(CellUtil.BORDER_TOP, BorderStyle.THIN);
		property1.put(CellUtil.BORDER_BOTTOM, BorderStyle.THIN);
		property1.put(CellUtil.BORDER_LEFT, BorderStyle.THIN);
		property1.put(CellUtil.BORDER_RIGHT, BorderStyle.THIN);
				
		File file = new File(JobSummaryReportPath);
		FileInputStream InputStream = new FileInputStream (file);
		Workbook JsrWb = new XSSFWorkbook(InputStream);
				
		int wscount = JsrWb.getNumberOfSheets();
				
		//i for number of worksheets
		for (int i=0;i<wscount;i++)
		{
			Sheet sh = JsrWb.getSheetAt(i);
			int FirstRowNum = sh.getFirstRowNum();
			int LastRowNum = sh.getLastRowNum();
			int RowCount = LastRowNum - FirstRowNum + 1;
					
			//j for number of rows
			for (int j=0;j<RowCount;j++)
			{
				Row R1 = sh.getRow(j);
				int CellCount = R1.getLastCellNum();
						
				for (int k=0;k<CellCount;k++)
				{
					Cell c1 = R1.getCell(k);
					CellUtil.setCellStyleProperties(c1, property1);
				}
			}
		}
		FileOutputStream fileOut = new FileOutputStream(JobSummaryReportPath);
		JsrWb.write(fileOut);
		JsrWb.close();
		PostExecDetails("Summary Report Format Successfull",micInfo,false);
		
		//Save Job To Local Format Date Time
		PostExecDetails("Saving JobReportSummary by local date format",micInfo,false);
		File srcfile = new File(JobSummaryReportPath);
		File dstfile = new File(StoredJobSummaryReportPath);
		FileHandler.copy(srcfile, dstfile);
		Thread.sleep(10000);
		PostExecDetails("Saved To "+StoredJobSummaryReportPath,micInfo,false);
		srcfile.delete();
		
		FlushAssert();
	}
}
