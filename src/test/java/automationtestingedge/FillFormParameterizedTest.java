package automationtestingedge;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import TestBase.Android_BaseTest;
import automationtestingedge.pageObjects.android.ProductCataloguePage;
import utils.AppiumUtils;

public class FillFormParameterizedTest extends Android_BaseTest {

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

	@Test(description = "GSTORE-FORM-001 | Should show error toast when name is missing", dataProvider = "formDataProvider", groups = { "regression" })
	public void shouldShowErrorToastWhenNameIsMissing(String name, String gender, String country) throws InterruptedException {
		waitForVisibility(formPage.nameField);
		formPage.setGender(gender);
		formPage.setCountrySelection(country);
		formPage.submitForm();
		String toastMessage = formPage.getToastMessage();
		Assert.assertEquals(toastMessage, EXPECTED_TOAST_MISSING_NAME, "Toast message did not match expected value for missing name.");
	}

	  @Test(description = "GSTORE-FORM-002 | Submitting valid form navigates to product catalogue",
	          dataProvider = "formDataProvider", groups = { "regression" })
	public void shouldNavigateToProductCatalogueAfterValidFormSubmit(String name, String gender, String country) throws InterruptedException {
		waitForVisibility(formPage.nameField);
		formPage.setNameField(name);
		formPage.setGender(gender);
		formPage.setCountrySelection(country);
		ProductCataloguePage productCataloguePage = formPage.submitForm();
		waitForVisibility(productCataloguePage.getProductListElement());
		int productCount = productCataloguePage.getProductCount();
		Assert.assertTrue(productCount > 0, "No products loaded after form submit!");
	}

	@DataProvider(name = "formDataProvider")
	public Object[][] getData() throws IOException {
		String jsonFilePath = System.getProperty("user.dir") + "/src/test/java/testData/eCommerceData.json";
		List<HashMap<String, String>> data = AppiumUtils.getJsonData(jsonFilePath);
		return new Object[][] { { data.get(0).get("name"), data.get(0).get("gender"), data.get(0).get("country") }, { data.get(1).get("name"), data.get(1).get("gender"), data.get(1).get("country") } };

	}

}
