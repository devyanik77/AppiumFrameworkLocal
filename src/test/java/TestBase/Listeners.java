package TestBase;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.appium.java_client.android.AndroidDriver;
import utils.AppiumUtils;

public class Listeners extends AppiumUtils implements ITestListener {

	ExtentReports extent = ExtentReporterNG.getReporterObject();
	ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	ExtentTest extentTest;

	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getTestClass().getName() + " : " + result.getMethod().getMethodName();
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
        if (test.get() != null) {
            test.get().log(Status.INFO, "Test Started: " + testName);
        }
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		if (test.get() != null) {
            test.get().log(Status.PASS, "Test Passed");
            test.get().pass("Test completed successfully");
        } else {
            System.out.println("onTestSuccess: ExtentTest was null for " + result.getMethod().getMethodName());
        }
		
	}
	

	@Override
	public void onTestFailure(ITestResult result) {
//		test.get().log(Status.FAIL, "Test Failed");
//		test.get().fail(result.getThrowable());
//		try {
//			driver = (AndroidDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//		test.fail(result.getThrowable());
//		try {
//			test.addScreenCaptureFromPath(getScreenshotPath(result.getMethod().getMethodName(), driver), result.getMethod().getMethodName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		  if (test.get() != null) {
	            test.get().log(Status.FAIL, "Test Failed: " + result.getMethod().getMethodName());
	            test.get().fail(result.getThrowable());
	        } else {
	            System.out.println("onTestFailure: ExtentTest was null for " + result.getMethod().getMethodName());
	        }
		  
		  AndroidDriver captureDriver = null;
	        try {
	            Object instance = result.getInstance();
	            if (instance != null) {
	                try {
	                    // attempt to read a public field named 'driver' from the test class
	                    Object fieldDriver = result.getTestClass().getRealClass().getField("driver").get(instance);
	                    if (fieldDriver instanceof AndroidDriver) {
	                        captureDriver = (AndroidDriver) fieldDriver;
	                    }
	                } catch (NoSuchFieldException nsf) {
	                    // field might be private; try to access it reflectively via declared field
	                    try {
	                        java.lang.reflect.Field declared = result.getTestClass().getRealClass().getDeclaredField("driver");
	                        declared.setAccessible(true);
	                        Object fd = declared.get(instance);
	                        if (fd instanceof AndroidDriver) captureDriver = (AndroidDriver) fd;
	                    } catch (NoSuchFieldException | IllegalAccessException ignore) {
	                        // ignore, will fallback below
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        if (captureDriver == null) {
	            captureDriver = AppiumUtils.driver;
	        }

	        if (captureDriver != null && test.get() != null) {
	            try {
	                String path = getScreenshotPath(result.getMethod().getMethodName(), captureDriver);
	                if (path != null) test.get().addScreenCaptureFromPath(path, result.getMethod().getMethodName());
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		if (test.get() != null) {
            test.get().log(Status.SKIP, "Test Skipped");
            if (result.getThrowable() != null) test.get().skip(result.getThrowable());
        }
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

	@Override
	public void onStart(ITestContext context) {
	}

	@Override
	public void onFinish(ITestContext context) {
		extent.flush();
        test.remove();
	}

}
