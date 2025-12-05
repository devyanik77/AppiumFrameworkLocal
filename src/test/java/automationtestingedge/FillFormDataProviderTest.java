package automationtestingedge;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import TestBase.Android_BaseTest;
import automationtestingedge.pageObjects.android.ProductCataloguePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FillFormDataProviderTest extends Android_BaseTest {

	private static final Logger log = LogManager.getLogger(FillFormDataProviderTest.class);
	private static final String EXPECTED_TOAST_MISSING_NAME = "Please enter your name";

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
	@Test(description = "GSTORE-FORM-005 | Validate toast message when name is missing", dataProvider = "invalidFormData", groups = { "regression" }, priority = 1)
	public void verifyMissingNameToast(String gender, String country) {
		log.info("Executing verifyMissingNameToast with gender={}, country={}", gender, country);
		waitForVisibility(formPage.nameField);
		formPage.setGender(gender);
		formPage.setCountrySelection(country);
		formPage.submitForm();
		String toastMessage = formPage.getToastMessage();
		log.info("Toast message received: {}", toastMessage);
		Assert.assertEquals(toastMessage, EXPECTED_TOAST_MISSING_NAME, "Toast message for missing name did not match expected value.");
	}

	@Test(description = "GSTORE-FORM-006 | Validate form submission with valid data", dataProvider = "validFormData", groups = { "regression" }, priority = 2)
	public void verifyFormSubmissionLoadsProducts(String name, String gender, String country) {
		log.info("Executing verifyFormSubmissionLoadsProducts with name={}, gender={}, country={}", name, gender, country);
		waitForVisibility(formPage.nameField);
		formPage.setNameField(name);
		formPage.setGender(gender);
		formPage.setCountrySelection(country);
		ProductCataloguePage productCataloguePage = formPage.submitForm();
		int productCount = productCataloguePage.getProductCount();

		log.info("Product count loaded: {}", productCount);

		Assert.assertTrue(productCount > 0, "No products loaded after form submission!");
	}

	// ---------------------- DATA PROVIDERS ----------------------

	@DataProvider(name = "validFormData")
	public Object[][] validFormData() {
		return new Object[][] { { "testUser1", "female", "Argentina" }, { "testUser2", "male", "Aruba" } };
	}

	@DataProvider(name = "invalidFormData")
	public Object[][] invalidFormData() {
		// Missing name → only gender & country provided
		return new Object[][] { { "female", "Argentina" }, { "male", "Aruba" } };
	}
}
