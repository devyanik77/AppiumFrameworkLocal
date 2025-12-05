package automationtestingedge.pageObjects.android;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.AppiumUtils;

public class TermsPopupPage extends AppiumUtils{
	 private final AndroidDriver driver;
	 
	public TermsPopupPage(AndroidDriver driver) {
		this.driver=driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}
	
	@AndroidFindBy(id = "com.androidsample.generalstore:id/termsButton")
	private WebElement termsButton;

	@AndroidFindBy(id = "android:id/button1")
	private WebElement popUpAcceptButton;

	@AndroidFindBy(xpath = "//android.widget.CheckBox[@text='Send me e-mails on discounts related to selected products in future']")
	private WebElement sendEmailCheckbox;

	@AndroidFindBy(id = "com.androidsample.generalstore:id/btnProceed")
	private WebElement proceedButton;
	

	public void openTerms() {
        waitForVisibility(termsButton).click();
    }

    /** Click Accept on the Terms popup dialog. */
    public void acceptTerms() {
        waitForVisibility(popUpAcceptButton).click();
    }

    /** Toggle the Send Email checkbox. */
    public void toggleSendEmailCheckbox() {
        waitForVisibility(sendEmailCheckbox).click();
    }

    /** Check if email checkbox is selected. */
    public boolean isEmailCheckboxSelected() {
        return sendEmailCheckbox.isSelected();
    }

    /** Tap on the Proceed button to navigate to the next page. */
    public void proceed() {
        waitForVisibility(proceedButton).click();
    }

    /** Convenience method: Accept terms + proceed. */
    public void acceptAndProceed() {
        openTerms();
        acceptTerms();
        proceed();
    }
}
