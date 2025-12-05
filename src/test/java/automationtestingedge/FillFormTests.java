package automationtestingedge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import TestBase.Android_BaseTest;
import automationtestingedge.pageObjects.android.ProductCataloguePage;

/**
 * Test suite covering form validation and submission flows. Includes both
 * negative and positive scenarios.
 */
public class FillFormTests extends Android_BaseTest {

	private static final Logger logger = LogManager.getLogger(FillFormTests.class);
	private static final String EXPECTED_MISSING_NAME_TOAST = "Please enter your name";

	@BeforeMethod(alwaysRun = true)
	public void resetApplicationState() {
		try {
			driver.terminateApp("com.androidsample.generalstore");
		} catch (Exception ignored) {
			System.out.println("Warning: terminateApp failed, app may not be running.");
		}

		try {
			driver.activateApp("com.androidsample.generalstore");
		} catch (Exception e) {
			System.err.println("Warning: activateApp failed: " + e.getMessage());
		}
	}


	@Test(description = "GSTORE-FORM-001 | Validate missing name error", groups = { "smoke", "regression" }, priority = 1)
	public void verifyMissingNameErrorToast() {
		waitForVisibility(formPage.nameField);

		formPage.setGender("female");
		formPage.setCountrySelection("Argentina");
		formPage.submitForm();

		String toastMessage = formPage.getToastMessage();
		logger.info("Toast message received: {}", toastMessage);

		Assert.assertEquals(toastMessage, EXPECTED_MISSING_NAME_TOAST, "Incorrect toast message displayed for missing name");
	}

	@Test(description = "GSTORE-FORM-002 | Validate successful form submission", groups = { "sanity", "regression" }, priority = 2)
	public void verifySuccessfulFormSubmission() {
		waitForVisibility(formPage.nameField);

		formPage.setNameField("TestUser");
		formPage.setGender("female");
		formPage.setCountrySelection("Argentina");

		ProductCataloguePage productPage = formPage.submitForm();
		int productCount = productPage.getProductCount();

		logger.info("Product count loaded after form submission: {}", productCount);

		Assert.assertTrue(productCount > 0, "No products loaded after form submission");
	}
}
