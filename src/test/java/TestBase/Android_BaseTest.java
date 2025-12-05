package TestBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import automationtestingedge.pageObjects.android.FormPage;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import utils.AppiumUtils;

/**
 * Base class for all Android test classes. Handles Appium server startup,
 * driver initialization, and teardown.
 */
public class Android_BaseTest extends AppiumUtils {

	protected AndroidDriver driver;
	protected FormPage formPage;
	protected WebDriverWait wait;

	private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
	private static final String CONFIG_PATH = "/src/main/java/resources/globalData.properties";

	@BeforeClass(alwaysRun = true)
	public void configureAppium() throws IOException {
		Properties prop = loadProperties();

		String ipAddress = System.getProperty("ipAddress", prop.getProperty("ipAddress"));
		int port = Integer.parseInt(prop.getProperty("port"));
		String deviceName = prop.getProperty("AndroidDeviceName");
		String apkPath = resolvePath(prop.getProperty("apkRelativePath"));
		String chromeDriverPath = resolvePath(prop.getProperty("chromeRelativePath"));
		String mainJS = prop.getProperty("mainJS");
		String PlatformVersion = prop.getProperty("PlatformVersion");


		// Start Appium server
		service = startAppiumServer(ipAddress, port, mainJS);
		System.out.println(">>> Appium running: " + service.isRunning());

		// Configure driver options
		UiAutomator2Options options = new UiAutomator2Options();
		options.setPlatformName("Android"); // explicit
		options.setAutomationName("UiAutomator2"); // explicit
		options.setDeviceName(deviceName);
		options.setApp(apkPath);
		options.setChromedriverExecutable(chromeDriverPath);
		options.setPlatformVersion(PlatformVersion);
		

		// Create Android driver session
		driver = new AndroidDriver(service.getUrl(), options);
		AppiumUtils.driver = driver;

		System.out.println(">>> Session created. App should now be launching.");

		driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT);
		wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
		formPage = new FormPage(driver);
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
		if (service != null) {
			service.stop();
		}
	}

	// ---------------------- Utility Methods ----------------------

	private Properties loadProperties() throws IOException {
		Properties prop = new Properties();
		String fullPath = System.getProperty("user.dir") + CONFIG_PATH;
		try (FileInputStream fis = new FileInputStream(fullPath)) {
			prop.load(fis);
		}
		return prop;
	}

	private String resolvePath(String relativePath) {
		return System.getProperty("user.dir") + "/" + relativePath;
	}
}
