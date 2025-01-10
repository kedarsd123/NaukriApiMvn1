package reusables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Reporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.json.simple.JSONObject;
import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class GlobalFunctions implements GlobalVariables
{
	public static WebDriver d;
	public static ExtentSparkReporter htmlreporter;
	public static ExtentReports htmlreport;
	public static ExtentTest htmllogger;
	public static Logger log = Logger.getLogger(GlobalFunctions.class.getName());
	public static int ApiJsonTimer = 1;
	//public static Object [] objJsonTitle;
	//public static Object [] objJsonJobList;
	
	//For Pasting Screenshots in Excel
	public static int PicPasteRow = 0;
	//Logger Only One Time Creation
	public static int LogCreationTime = 0;
	//Snapshot Naming and creation
	public static int cssnum=0;
	public static String SnapShotName;
	//log4j details
	//public static Logger log = Logger.getLogger(LoggerLibrary.class.getName());
	//Excel Writing Rows
	public static int ExcelStartRow = 1;
	//Excel Static Log Time
	public static String LocalStepTime = "";
	
	public static void LoadConfigFile()
	{
		String strLine = "";
		List<String> list = new ArrayList<String>();

		BufferedReader br;
		try 
		{
			br = new BufferedReader(new FileReader(ConfigPath));
			strLine = br.readLine();
	        while (strLine != null)
	        {
	        	System.out.println(strLine);
	            list.add(strLine);
	            strLine = br.readLine();
	        }
	        br.close();
	        
	        int lstsize = list.size();
	        
	        for (int i=0;i<lstsize;i++)
	        {
	        	String[] words = list.get(i).split("=");
	        	ConfigDct.put(words[0].trim().toString(), words[1].trim().toString());
	        }
	        PostExecDetails("Config VBS File Read Successfully",micInfo,false);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void CreateFolder(String S)
	{
		File folder = new File (S);
		folder.mkdir();
	}
	
	public void DeleteAllTempDataFiles()
	{
		File directory = new File(TestTempDataFolder);
		for(File file : directory.listFiles())
		{
			file.delete();
		}
	}
	
	public static void DriverSettings(String browsername) throws Exception
	{
		if (browsername == null)
		{
			browsername = ConfigDct.get("browser");
		}
		
		if (browsername.toUpperCase().equals("CHROME"))
		{
			d = new ChromeDriver();
			ChromeOptions ch = new ChromeOptions();
			ch.addArguments("--disable-notifications");
			ch.addArguments("--remote-allow-origins=*");
		}
		else if (browsername.toUpperCase().equals("EDGE"))
		{
			d = new EdgeDriver();
		}
		else
		{
			PostExecDetails("Case Not Handled For Browser " + ConfigDct.get("browser"),micInfo,false);
			return;
		}
		d.manage().timeouts().implicitlyWait(Duration.ofSeconds(SHORTWAIT));
		d.manage().window().maximize();
	}
	
	public static void LaunchURL(String srtURL)
	{
		d.get(srtURL);
	}
	
	public static void KillExcessWindows ()
	{
		String ParentWindow = d.getWindowHandle();
		Set <String> AllWindows = d.getWindowHandles();
		Iterator <String> I1 = AllWindows.iterator();
		
		while (I1.hasNext())
		{
			String ExtraWindow = I1.next();
			if (!ParentWindow.equals(ExtraWindow))
			{
				d.switchTo().window(ExtraWindow).close();
			}
		}
		d.switchTo().window(ParentWindow);
	}
	
	public static void CreateLogger() throws Exception
	{
		File srcfile = new File(SampleLogger);
		File dstfile = new File(ResultLogger);
		FileHandler.copy(srcfile, dstfile);
	}
	
	public static void CaptureScreenShot()
	{
		cssnum++;
		SnapShotName = "SnapShot_".concat(String.valueOf(cssnum).concat(".png"));
		File SrcFile = ((TakesScreenshot)d).getScreenshotAs(OutputType.FILE);
		File DstFile = new File (TestSnapCaseFolder.concat("\\").concat(SnapShotName));
		try 
		{
			FileHandler.copy(SrcFile,DstFile);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			//PrintLog(e.toString());
		}
	}
	
	public static void PostResult(String S,String strStatus,Boolean Snap) throws Exception
	{
		File ResLogger = new File(ResultLogger);
		FileInputStream InputStream = new FileInputStream (ResLogger);
		Workbook LoggerExcel = new XSSFWorkbook(InputStream);
		if (Snap.equals(true))
		{
			CaptureScreenShot();
			Sheet Screenshots = LoggerExcel.getSheet("Screenshots");
			InputStream SnapStream = new FileInputStream(TestSnapCaseFolder.concat("\\").concat(SnapShotName));
			byte[] bytes = IOUtils.toByteArray(SnapStream);
			//Adds a picture to the workbook
			int pictureIdx = LoggerExcel.addPicture(bytes, LoggerExcel.PICTURE_TYPE_PNG);
			//close the input stream
			SnapStream.close();
			//Returns an object that handles instantiating concrete classes
			CreationHelper helper = LoggerExcel.getCreationHelper();
			//Creates the top-level drawing patriarch.
			Drawing drawing = Screenshots.createDrawingPatriarch();
			//Create an anchor that is attached to the worksheet
			ClientAnchor anchor = helper.createClientAnchor();
			anchor.setCol1(1);
			anchor.setRow1(PicPasteRow);
			//Creates a picture
			Picture pict = drawing.createPicture(anchor, pictureIdx);
			//Reset the image to the original size
			pict.resize();
			
			PicPasteRow = PicPasteRow + 46;
		}
		
		LocalStepTime = LocalDateTime.now().toString();
		Sheet Details = LoggerExcel.getSheet("Details");
		Row RowControl = Details.createRow(ExcelStartRow);
		RowControl.createCell(0).setCellValue(String.valueOf(ExcelStartRow));
		RowControl.createCell(1).setCellValue(LocalStepTime);
		RowControl.createCell(2).setCellValue(S);
		RowControl.createCell(3).setCellValue(strStatus);
		if (Snap.equals(true))
		{
			String temp = "HYPERLINK(\""+ TestSnapCaseFolder.concat("\\").concat(SnapShotName) + "\", \"Snap\")";
			RowControl.createCell(4).setCellFormula(temp);
		}
		//AutoFit Columns
		int lcn = Details.getRow(0).getLastCellNum();
		for (int i=0; i<lcn; i++)
		{
			Details.autoSizeColumn(i);
		}
		FileOutputStream fileOut = new FileOutputStream(ResultLogger);
		LoggerExcel.write(fileOut);
		LoggerExcel.close();
		ExcelStartRow++;
	}
	
	public static void FormatExcelLogger() throws Exception
	{
		HashMap<String, Object> property1 = new HashMap<String, Object>();
		
		//Set Border Around Cell
		property1.put(CellUtil.BORDER_TOP, BorderStyle.THIN);
		property1.put(CellUtil.BORDER_BOTTOM, BorderStyle.THIN);
		property1.put(CellUtil.BORDER_LEFT, BorderStyle.THIN);
		property1.put(CellUtil.BORDER_RIGHT, BorderStyle.THIN);
		
		File file = new File(ResultLogger);
		FileInputStream InputStream = new FileInputStream (file);
		Workbook JsrWb = new XSSFWorkbook(InputStream);
		
		Sheet sh = JsrWb.getSheet("Details");
		int FirstRowNum = sh.getFirstRowNum();
		int LastRowNum = sh.getLastRowNum();
		int RowCount = LastRowNum - FirstRowNum + 1;
		
		//j for number of rows
		for (int j=1;j<RowCount;j++)
		{
			Row R1 = sh.getRow(j);
			int CellCount = R1.getLastCellNum();
			
			for (int k=0;k<CellCount;k++)
			{
				Cell c1 = R1.getCell(k);
				CellUtil.setCellStyleProperties(c1, property1);
			}
		}
		
		FileOutputStream fileOut = new FileOutputStream(ResultLogger);
		JsrWb.write(fileOut);
		JsrWb.close();
	}
	
	public static void HtmlSettings()
	{
		htmlreporter = new ExtentSparkReporter(HTMLResultPath);
		htmlreporter.config().setEncoding("utf-8");
		htmlreporter.config().setDocumentTitle("NaukriDotCom HTML Report");
		htmlreporter.config().setReportName("NaukriDotCom TestNG Runs");
		htmlreporter.config().setTheme(Theme.STANDARD);
		htmlreport = new ExtentReports();
		htmlreport.setSystemInfo("Browser", "Chrome");
		htmlreport.setSystemInfo("Automation","Selenium");
		htmlreport.setSystemInfo("Technology","TestNG");
		htmlreport.setSystemInfo("Report Type","Excel + HTML");
		htmlreport.attachReporter(htmlreporter);
	}
	
	public static void LogHTMLPage (String S,String strStatus,Boolean Snap) throws Exception
	{
		htmllogger = htmlreport.createTest(S);
		
		if (strStatus.equalsIgnoreCase(micPass))
			htmllogger.log(Status.PASS,"Passed");
		else if (strStatus.equalsIgnoreCase(micFail))
			htmllogger.log(Status.FAIL,"Failed");
		else if (strStatus.equalsIgnoreCase(micWarning))
			htmllogger.log(Status.WARNING,"Warning");
		else if (strStatus.equalsIgnoreCase(micInfo))
			htmllogger.log(Status.INFO,"Information");
		
		if (Snap.equals(true))
		{
			htmllogger.addScreenCaptureFromPath(TestSnapCaseFolder.concat("\\").concat(SnapShotName));
		}
		
		if (S.equalsIgnoreCase("System Exit"))
		{
			htmlreport.flush();
		}
	}
	
	public static void WriteToDataBase (int SrNo,String StepTime,String S,String strStatus) throws Exception
	{
		int DbNo = SrNo - 1;
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/xe",ConfigDct.get("DBUserName"),ConfigDct.get("DBPassword"));
		Statement sqlstatement = con.createStatement();
		String sqlquery = "INSERT INTO NAUKRI_DB (SRNO,STEPTIME,ACTION_DETAIL,STATUS) VALUES ('"+DbNo+"','"+StepTime+"','"+S+"','"+strStatus+"')";
		sqlstatement.executeQuery(sqlquery);
		con.close();
	}
	
	public static void CheckAssertStatus(String strStatus)
	{
		if (strStatus.equalsIgnoreCase(micPass) || strStatus.equalsIgnoreCase(micInfo))
			SA.assertTrue(true);
		else if (strStatus.equalsIgnoreCase(micFail) || strStatus.equalsIgnoreCase(micWarning))
			SA.assertTrue(false);
	}
	
	public static void FlushAssert()
	{
		SA.assertAll();
	}
	
	public static void ExitTest() throws Exception
	{
		PostExecDetails("System Exit",micInfo,false);
		ConfigDct.clear();
		d.quit();
	}
	
	public static void PostJsonDB(Object [] objJsonTitle,Object [] objJsonJobList) throws Exception
	{
		if (ApiJsonTimer == 1)
		{
			CreateJsonApiDB();
			ApiJsonTimer++;
		}
		
		int arrlen = objJsonTitle.length;
		
		for (int i=0;i<arrlen;i++)
		{
			JsonMap.put(objJsonTitle[i].toString(),objJsonJobList[i].toString());
		}
		
		JSONObject request = new JSONObject(JsonMap);
		RestAssured.baseURI = "http://localhost:3000/";
		
	    given().contentType(ContentType.JSON).accept(ContentType.JSON).header("Content-Type","Application/Json").
	    body(request.toJSONString()).
	    when().
	    post("/jobs").
	    then().statusCode(201).
	    log().all();
	}
	
	public static void CreateJsonApiDB () throws Exception
	{
		File srcfile = new File(SrcJsonApiDB);
		File dstfile = new File(DstJsonApiDB);
		
		FileHandler.copy(srcfile, dstfile);
		Thread.sleep(10000);
		
		//Runtime.getRuntime().exec("cmd.exe /c start " + "json-server --watch JsonApiDB.json");
		Runtime.getRuntime().exec("cmd.exe /c start cmd /k \" json-server --watch JsonApiDB.json \"",null,new File(TestJsonApiDBFolder));
	}
	
	public static void StoreJsonApiDB () throws Exception
	{
		File srcfile = new File(DstJsonApiDB);
		File dstfile = new File(StoredDstJsonApiDB);
		
		FileHandler.copy(srcfile, dstfile);
	}
	
	public static void PostExecDetails (String S,String strStatus,Boolean Snap) throws Exception
	{
		if (LogCreationTime==0)
		{
			CreateLogger();
			HtmlSettings();
		}
		CheckAssertStatus(strStatus);
		System.out.println(strStatus + " --> " + S);
		log.info(S);
		Reporter.log(strStatus + " --> " + S);
		PostResult(S,strStatus,Snap);
		LogHTMLPage(S,strStatus,Snap);
		WriteToDataBase(ExcelStartRow,LocalStepTime,S,strStatus);
		
		if (S.equalsIgnoreCase("System Exit"))
		{
			FormatExcelLogger();
			StoreJsonApiDB();
		}
		LogCreationTime++;
	}
}
