package reusables;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.testng.asserts.SoftAssert;

public interface GlobalVariables 
{	
	//Constants
	int SHORTWAIT = 60;
	
	//SoftAssert
	SoftAssert SA = new SoftAssert();
	
	//Excel Result Logger Modifier
	String formatDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).replaceAll(":", "_");
	
	//Directories
	String ProjectFolder = System.getProperty("user.dir");
	String DataScriptFolder = ProjectFolder.concat("\\DataScript");
	String AutomationFolder = ProjectFolder.concat("\\Automation");
	String TestLogFolder = AutomationFolder.concat("\\TestLog");
	String TestReportFolder = AutomationFolder.concat("\\TestReport");
	String TestResultFolder = AutomationFolder.concat("\\TestResult");
	String TestSnapFolder = AutomationFolder.concat("\\TestScreenshot");
	String TestSnapCaseFolder = TestSnapFolder.concat("\\").concat(formatDateTime);
	String TestHTMLReportFolder = AutomationFolder.concat("\\TestHTMLReport");
	String TestJsonApiDBFolder = AutomationFolder.concat("\\TestJsonApiDB");
	String TestTempDataFolder = AutomationFolder.concat("\\TestTempData");
	
	//Excel
	//XSSFWorkbook rsworkbook = new XSSFWorkbook();
	String ExcelPath = DataScriptFolder.concat("\\FindJobs.xlsx");
	String JobSummaryReportPath = TestReportFolder.concat("\\JobSummaryReport.xlsx");
	String StoredJobSummaryReportPath = TestReportFolder.concat("\\JobSummaryReport_").concat(formatDateTime).concat(".xlsx");
	String SampleLogger = DataScriptFolder.concat("\\Logger.xlsx");
	String ResultLogger = TestResultFolder.concat("\\Logger_").concat(formatDateTime).concat(".xlsx");
	String SrcJsonApiDB = DataScriptFolder.concat("\\JsonApiDB.json");
	String DstJsonApiDB = TestJsonApiDBFolder.concat("\\JsonApiDB.json");
	String StoredDstJsonApiDB = TestJsonApiDBFolder.concat("\\JsonApiDB_").concat(formatDateTime).concat(".json");
	
	//Mail Config VBS Path
	String ConfigPath = DataScriptFolder.concat("\\Config.vbs");
	
	//HTML Result Path
	String HTMLResultPath = TestHTMLReportFolder.concat("\\HTMLLogger_").concat(formatDateTime).concat(".html");
	
	//Dictionaries
	HashMap<String, String> ConfigDct = new HashMap<String, String>();
	HashMap<Integer, String> Dictionary = new HashMap<Integer, String>();
	HashMap<Integer, String> EmailDct = new HashMap<Integer, String>();
	HashMap<Integer, Object[]> JobStorage = new HashMap<Integer, Object[]>();
	Map <String,Object> JsonMap = new HashMap <String, Object> ();
	
	//URLs
	String NaukriURL = "https://www.naukri.com/";
	String RediffURL = "https://www.rediff.com/";
	
	//AutoIT Script
	String AutoITexe = DataScriptFolder.concat("\\AutoitScript\\JobSummaryReport.exe");
	
	//Logger Parameters
	String micPass = "Passed", micFail = "Failed", micWarning = "Warning", micInfo = "Info";	
}
