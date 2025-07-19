package base.authentication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import freemarker.log.Logger;
import groovyjarjarantlr4.v4.runtime.misc.LogManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class baseClass {

	public static ExtentReports extentReports;
	public static ExtentTest extentTest;
	public static Properties prop;
	public static WebDriver driver;
	public static String x_session_token;
	protected PrintStream ps;

	public baseClass() {
		prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(
					"D:\\SeleniumPractice\\RestAssuredFramework\\src\\main\\resource\\config.properties");
			prop.load(fis);
			FileOutputStream fos = new FileOutputStream("logging.txt");
			ps = new PrintStream(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	@BeforeSuite
	public static ExtentReports setupReport() {
		ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter("test-output/RestAssuredHTMLReport.html");
		extentReports = new ExtentReports();
		extentReports.attachReporter(extentSparkReporter);

		extentReports.setSystemInfo("OS", "Windows");
		extentReports.setSystemInfo("Project-Details", "This is Rest Assured Project on AEUI");
		extentReports.setSystemInfo("Author", "AShutosh");

		extentSparkReporter.config().setReportName("REST_Assured");
		extentSparkReporter.config().setDocumentTitle("Report for API");
		extentSparkReporter.config().setTheme(Theme.DARK);
		extentSparkReporter.config().setEncoding("UTF-8");
		return extentReports;
	}

	public static String getScreenS(WebDriver driver, String ScreenshotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date(0));
		TakesScreenshot ts = (TakesScreenshot) driver;
		File srcFile = ts.getScreenshotAs(OutputType.FILE);
		String destinationFile = prop.getProperty("user.dir") + "/Screenshots" + "{" + dateName + "}" + ".png";
		File destfile = new File(destinationFile);
		FileUtils.copyFile(srcFile, destfile);
		return destinationFile;
	}

	@BeforeMethod
	public void authenticateUser() {
		Response response = RestAssured.given().relaxedHTTPSValidation().log().all().contentType(ContentType.URLENC)
				.formParam("username", "ashu").formParam("password", "Pass@123").when().
				 post("https://10.41.4.83:8443/aeengine/rest/authenticate").then().log().all()
				.statusCode(200).extract().response();
		//String responseResult = response.asPrettyString();
		//System.out.println("Response Result : " + responseResult);
		x_session_token = response.getHeader("sessionToken");
		if (x_session_token == null) {
		    x_session_token = response.jsonPath().getString("sessionToken");
		}

	}

	/*
	 * public static PrintStream LoggerUtil() { try { FileOutputStream fos = new
	 * FileOutputStream("logging.txt"); return new PrintStream(fos); } catch
	 * (Exception e) { throw new RuntimeException("Failed to create log file",e); }
	 * }
	 */
	
	@AfterMethod
	public void afterResult(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.SUCCESS) {
			extentTest = extentReports.createTest(result.getName());
			extentTest.log(Status.PASS, MarkupHelper
					.createLabel("This test Cases is passed : " + result.getName() + " ", ExtentColor.GREEN));
		} else if (result.getStatus() == ITestResult.FAILURE) {
			extentTest = extentReports.createTest(result.getName());
			extentTest.log(Status.FAIL,
					MarkupHelper.createLabel("This Test Case is failed : " + result.getName() + " ", ExtentColor.RED));
			//String ScrnShtPath = getScreenS(driver, result.getName());
			//extentTest.addScreenCaptureFromBase64String(ScrnShtPath);
			extentTest.fail(result.getThrowable());
		} else if (result.getStatus() == ITestResult.SKIP) {
			extentTest = extentReports.createTest(result.getName());
			extentTest.log(Status.SKIP, MarkupHelper
					.createLabel("This test cases are skipped : " + result.getName() + " ", ExtentColor.YELLOW));
		}
		//driver.close();
	}

	@AfterTest
	public void closeTest() {
		extentReports.flush();
	}
}
