package automationtestingedge;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import TestBase.Android_BaseTest;
import automationtestingedge.pageObjects.android.CartPage;
import automationtestingedge.pageObjects.android.ProductCataloguePage;

public class AddProductToCartTest extends Android_BaseTest {

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
	@Test(description = "GSTORE-CART-003 | Validate adding a single product to cart", groups = { "sanity", "regression" }, priority = 1)
	public void verifySingleProductAddedToCart() {
		waitForVisibility(formPage.nameField);

		formPage.setNameField("TestUser");
		formPage.setGender("female");
		formPage.setCountrySelection("Argentina");

		ProductCataloguePage productCataloguePage = formPage.submitForm();
		productCataloguePage.addItemToCartByIndex(0);

		CartPage cartPage = productCataloguePage.goToCartPage();
		Assert.assertTrue(cartPage.isOnCartPage(), "Cart page did not load correctly.");

		double calculatedSum = cartPage.getProductSum();
		double displayedSum = cartPage.getTotalAmountDisplayed();

		Assert.assertEquals(calculatedSum, displayedSum, "Displayed total does not match calculated total.");

		cartPage.verifyTotalAmount();
		cartPage.acceptTermsConditions();
		cartPage.clickEmailCheckbox();
		cartPage.submitOrder();
	}

	@Test(description = "GSTORE-CART-004 | Validate adding multiple products to cart", groups = { "smoke", "regression" }, priority = 2, dependsOnMethods = "verifySingleProductAddedToCart")
	public void verifyMultipleProductsAddedToCart() {
		waitForVisibility(formPage.nameField);
		formPage.setNameField("TestUser");
		formPage.setGender("female");
		formPage.setCountrySelection("Argentina");

		ProductCataloguePage productCataloguePage = formPage.submitForm();
		productCataloguePage.addItemToCartByIndex(0);
		productCataloguePage.addItemToCartByIndex(0); // Add same product twice

		CartPage cartPage = productCataloguePage.goToCartPage();
		Assert.assertTrue(cartPage.isOnCartPage(), "Cart page did not load correctly.");

		double calculatedSum = cartPage.getProductSum();
		double displayedSum = cartPage.getTotalAmountDisplayed();

		Assert.assertEquals(calculatedSum, displayedSum, "Displayed total does not match calculated total.");

		cartPage.verifyTotalAmount();
		cartPage.acceptTermsConditions();
		cartPage.clickEmailCheckbox();
		cartPage.submitOrder();
	}
}
