package TestBase;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReporterNG {

	static ExtentReports extent;

	public static ExtentReports getReporterObject() {
		String reportPath = System.getProperty("user.dir") + "\\extentsReport\\index.html";

		ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
		spark.config().setReportName("Web Automation Testing Report");
		spark.config().setDocumentTitle("Test Results Summary");

		extent = new ExtentReports();
		extent.attachReporter(spark);

		extent.setSystemInfo("Tester", "Devyani");
		extent.setSystemInfo("Framework", "Selenium + ExtentReports");
		return extent;
	}
	
	

}
