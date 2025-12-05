package automationtestingedge.pageObjects.android;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.AppiumUtils;

public class CartPage extends AppiumUtils {
	AndroidDriver driver;

	public CartPage(AndroidDriver driver) {
//		super(driver);
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

	// ------------------- Locators --------------------

	@AndroidFindBy(id = "com.androidsample.generalstore:id/toolbar_title")
	private WebElement cartTitle;

	@AndroidFindBy(id = "com.androidsample.generalstore:id/productPrice")
	private List<WebElement> productPrices;

	@AndroidFindBy(id = "com.androidsample.generalstore:id/totalAmountLbl")
	private WebElement totalAmount;

	@AndroidFindBy(id = "com.androidsample.generalstore:id/termsButton")
	private WebElement termsButton;

	@AndroidFindBy(id = "android:id/button1")
	private WebElement acceptTermsButton;

	@AndroidFindBy(xpath = "//android.widget.CheckBox[@text=\"Send me e-mails on discounts related to selected products in future\"]")
	private WebElement emailCheckbox;

	@AndroidFindBy(id = "com.androidsample.generalstore:id/btnProceed")
	private WebElement proceedButton;

	// ------------------- Actions --------------------

	public boolean isOnCartPage() {
		waitForElementToAppear(cartTitle);
		return cartTitle.getText().contains("Cart");
	}

//	public List<WebElemnt> getProductList(){
//		return productList();
//	}
	public double getProductSum() {
		double totalSum = 0;
		for (WebElement price : productPrices) {
			totalSum += getFormatedAmount(price.getText());
		}
		return totalSum;
	}

	public double getTotalAmountDisplayed() {
		return getFormatedAmount(totalAmount.getText());
	}

	public void verifyTotalAmount() {
		double calculated = getProductSum();
		double displayed = getTotalAmountDisplayed();
		if (calculated != displayed) {
			throw new AssertionError("Total mismatch → Calculated: " + calculated + " | Displayed: " + displayed);
		}
	}

	public void acceptTermsConditions() {
		longPressAction(termsButton);
		acceptTermsButton.click();
	}

	public void clickEmailCheckbox() {
		emailCheckbox.click();
	}

	public void submitOrder() {
		proceedButton.click();
	}

	public void switchToWebView() throws InterruptedException {
		Thread.sleep(3000);
		for (String context : driver.getContextHandles()) {
			if (context.contains("WEBVIEW")) {
				driver.context(context);
				break;
			}
		}
	}

	public void searchInWebView(String text) throws InterruptedException {
		Thread.sleep(5000);
		driver.findElement(AppiumBy.name("q")).sendKeys(text);
		Thread.sleep(5000);
		driver.findElement(AppiumBy.name("q")).submit();
	}

	public void goBackToNative() throws InterruptedException {
		Thread.sleep(5000);
		driver.context("NATIVE_APP");
		Thread.sleep(5000);
	}

}
