package automationtestingedge.pageObjects.android;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.AppiumUtils;

public class ProductCataloguePage extends AppiumUtils {
	AndroidDriver driver;

	public ProductCataloguePage(AndroidDriver driver) {
//		super(driver);
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

	@AndroidFindBy(id = "com.androidsample.generalstore:id/productName")
	private List<WebElement> productNames;

	@AndroidFindBy(xpath = "//android.widget.TextView[@text='ADD TO CART']")
	private List<WebElement> addToCartButtons;

	@AndroidFindBy(id = "com.androidsample.generalstore:id/appbar_btn_cart")
	private WebElement cartButton;
	
	@AndroidFindBy(id="com.androidsample.generalstore:id/productName")
	private WebElement firstProduct;
	// ---------- Actions ----------
	
	public WebElement getProductListElement() {
	    return firstProduct;
	}

	// Click "ADD TO CART" by index (0-based)
	public void addItemToCartByIndex(int index) {
		waitForElementToAppear(addToCartButtons.get(index));
		addToCartButtons.get(index).click();
	}

	// Scroll to product by name and add it to cart
	public void addItemToCartByName(String name) {
		scrollToText(name); // from AndroidActions
		for (int i = 0; i < productNames.size(); i++) {
			if (productNames.get(i).getText().equalsIgnoreCase(name)) {
				addToCartButtons.get(i).click();
				break;
			}
		}
	}

	public int getProductCount() {
		return productNames.size();
	}

	public CartPage goToCartPage() {
		cartButton.click();
		return new CartPage(driver);
	}
}
