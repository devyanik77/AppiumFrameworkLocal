package automationtestingedge;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import TestBase.Android_BaseTest;
import automationtestingedge.pageObjects.android.CartPage;
import automationtestingedge.pageObjects.android.ProductCataloguePage;
import utils.AppiumUtils;

public class EndToEndFlowTest extends Android_BaseTest {

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

	@Test(description = "GSTORE-FORM-007 | Validate toast when name is missing", groups = { "regression" })
	public void verifyMissingNameToast() {
		waitForVisibility(formPage.nameField);

		formPage.setGender("female");
		formPage.setCountrySelection("Argentina");
		formPage.submitForm();

		String toastMessage = formPage.getToastMessage();
		Assert.assertEquals(toastMessage, "Please enter your name", "Toast message mismatch when submitting form without name.");
	}

	@Test(description = "GSTORE-FORM-008 | Validate product catalogue visibility after valid form submit", dataProvider = "formDataProvider", groups = { "regression" })
	public void verifyProductCatalogueDisplayed(String name, String gender, String country) {
		waitForVisibility(formPage.nameField);

		formPage.setNameField(name);
		formPage.setGender(gender);
		formPage.setCountrySelection(country);

		ProductCataloguePage productCataloguePage = formPage.submitForm();

		wait.until(ExpectedConditions.visibilityOf(productCataloguePage.getProductListElement()));
		Assert.assertTrue(productCataloguePage.getProductListElement().isDisplayed(), "Product catalogue list is not visible after submitting the form.");
	}

	@Test(description = "GSTORE-FORM-009 | Validate single product cart total", dataProvider = "formDataProvider", groups = { "regression" })
	public void verifySingleProductCartTotal(String name, String gender, String country) {
		waitForVisibility(formPage.nameField);

		formPage.setNameField(name);
		formPage.setGender(gender);
		formPage.setCountrySelection(country);

		ProductCataloguePage productCataloguePage = formPage.submitForm();
		waitForVisibility(productCataloguePage.getProductListElement());

		productCataloguePage.addItemToCartByIndex(0);
		CartPage cartPage = productCataloguePage.goToCartPage();

		Assert.assertTrue(cartPage.isOnCartPage(), "Cart page did not load correctly.");

		double calculatedSum = cartPage.getProductSum();
		double displayedSum = cartPage.getTotalAmountDisplayed();

		Assert.assertEquals(calculatedSum, displayedSum, "Calculated product sum does not match displayed total.");

		cartPage.verifyTotalAmount();
		cartPage.acceptTermsConditions();
		cartPage.clickEmailCheckbox();
		cartPage.submitOrder();
	}

	@Test(description = "GSTORE-FORM-010 | Validate multiple product cart total", dataProvider = "formDataProvider", groups = { "sanity" })
	public void verifyMultipleProductCartTotal(String name, String gender, String country) {
		waitForVisibility(formPage.nameField);

		formPage.setNameField(name);
		formPage.setGender(gender);
		formPage.setCountrySelection(country);

		ProductCataloguePage productCataloguePage = formPage.submitForm();
		waitForVisibility(productCataloguePage.getProductListElement());

		productCataloguePage.addItemToCartByIndex(0);
		productCataloguePage.addItemToCartByIndex(0);

		CartPage cartPage = productCataloguePage.goToCartPage();
		Assert.assertTrue(cartPage.isOnCartPage(), "Cart page did not load correctly.");

		double calculatedSum = cartPage.getProductSum();
		double displayedSum = cartPage.getTotalAmountDisplayed();

		Assert.assertEquals(calculatedSum, displayedSum, "Calculated product sum does not match displayed total for multiple items.");

		cartPage.verifyTotalAmount();
		cartPage.acceptTermsConditions();
		cartPage.clickEmailCheckbox();
		cartPage.submitOrder();
	}

	// ---------------------- DATA PROVIDER ----------------------

	@DataProvider(name = "formDataProvider")
	public Object[][] getFormData() throws IOException {
		String jsonFilePath = System.getProperty("user.dir") + "/src/test/java/testData/eCommerceData.json";
		List<HashMap<String, String>> data = AppiumUtils.getJsonData(jsonFilePath);

		return new Object[][] { { data.get(0).get("name"), data.get(0).get("gender"), data.get(0).get("country") }, { data.get(1).get("name"), data.get(1).get("gender"), data.get(1).get("country") } };
	}
}
