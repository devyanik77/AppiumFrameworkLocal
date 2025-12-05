package automationtestingedge.pageObjects.android;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.AppiumUtils;

public class FormPage extends AppiumUtils {
	AndroidDriver driver;

	public FormPage(AndroidDriver driver) {
//		super(driver);
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

	@AndroidFindBy(id = "com.androidsample.generalstore:id/nameField")
	public WebElement nameField;

	@AndroidFindBy(id = "com.androidsample.generalstore:id/radioFemale")
	private WebElement femaleOption;

	@AndroidFindBy(id = "com.androidsample.generalstore:id/radioMale")
	private WebElement maleOption;

	@AndroidFindBy(id = "android:id/text1")
	private WebElement countryDropdown;

	@AndroidFindBy(id = "com.androidsample.generalstore:id/btnLetsShop")
	private WebElement shopButton;
	


	// --- Actions ---

	
	public void setNameField(String name) {
		nameField.sendKeys(name);
		driver.hideKeyboard();
	}

	public void setGender(String gender) {
		if (gender != null && gender.toLowerCase().contains("female")) {
			femaleOption.click();
		} else {
			maleOption.click();
		}
	}

	public void setCountrySelection(String countryName) {
		countryDropdown.click();
		scrollToText(countryName);
		driver.findElement(AppiumBy.xpath("//android.widget.TextView[@resource-id='android:id/text1' and @text='" + countryName + "']")).click();
	}

	public ProductCataloguePage submitForm() {
		waitForElementToAppear(shopButton);
		shopButton.click();
		return new ProductCataloguePage(driver);
	}

	public String getToastMessage() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

		WebElement toast = wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath("//android.widget.Toast")));

		String message = toast.getAttribute("text");
		if (message == null || message.isEmpty()) {
			message = toast.getAttribute("name");
		}

		return message;
	}

	public void verifyToastMessage(String expectedText) {
		String actual = getToastMessage();
		if (!actual.equals(expectedText)) {
			throw new AssertionError("Toast mismatch ⇒ Expected: " + expectedText + ", Actual: " + actual);
		}
	}

	public void setActivity() throws InterruptedException {
//		((JavascriptExecutor) driver).executeScript("mobile: startActivity", ImmutableMap.of("intent", "com.androidsample.generalstore/com.androidsample.generalstore.MainActivity"));
			Thread.sleep(8000);
			driver.terminateApp("com.androidsample.generalstore");
			driver.activateApp("com.androidsample.generalstore");
	}



}
