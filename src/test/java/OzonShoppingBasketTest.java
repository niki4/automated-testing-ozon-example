import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;


public class OzonShoppingBasketTest {
    private WebDriver driver;
    private String baseURL;
    private StringBuffer verificationErrors = new StringBuffer();
    private String userLogin;
    private String userPass;
    private String searchValue;

    private By locatorSearchMainField = By.id("SearchText");
    private By locatorEvenItemsToBasketBtn = By.cssSelector("#bTilesModeShow .bOneTile:nth-child(even) .mAddToCart.mTitle.bFlatButton.js_add");
    private By locatorEvenItemsTitle = By.cssSelector("#bTilesModeShow .bOneTile:nth-child(even) .eOneTile_ItemName");
    private By locatorBasketInnerSectionItems = By.cssSelector(".jsViewCollection .bCartSplit .eCartItem_nameValue:nth-child(n)");
    private By locatorBasketRemoveAllBtn = By.cssSelector(".bCartSplit:nth-child(n) .eCartControls_buttons .bIconButton.mRemove.mGray.jsRemoveAll");
    private By locatorUserMenu = By.cssSelector(".jsQuickPanelUserMenu");
    private By locatorUserMenuLoginOption = By.cssSelector(".ePanelLinks_term.jsOption.jsLoginPanel");
    private By locatorUserMenuLogoffOption = By.cssSelector(".ePanelLinks_term.jsOption.jsLogOff");
    private By locatorUserLoginField = By.xpath("//input[@name='login']");
    private By locatorUserPassField = By.xpath("//input[@name='Password']");
    private By locatorUserLoginConfirmBtn = By.cssSelector(".jsLoginWindowButton");

    @Before
    public void setUp() throws Exception {
        // Comment the line below if you already have geckodriver in PATH system variable, otherwise
        // update the path to geckodriver in code as below
        System.setProperty("webdriver.gecko.driver",
                "C:\\AutomatedTesting\\Selenium\\geckodriver-v0.19.1-win32\\geckodriver.exe");

        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        baseURL = "https://ozon.ru";
        userLogin = "dhjtpwyq1o@dooboop.com";
        userPass = "Password!";
        searchValue = "Now";
    }


    @Test
    @DisplayName("OZON.RU - Authorization and Shopping Basket operations")
    public void testAddAndRemoveItemsInBasket() throws Exception {
        Wait<WebDriver> wait = new WebDriverWait(driver, 30)
                .withMessage("Element was not found")
                .ignoring( NoSuchElementException.class, StaleElementReferenceException.class );

        driver.get(baseURL + '/');

        login();

        driver.findElement(locatorSearchMainField).clear();
        driver.findElement(locatorSearchMainField).sendKeys(searchValue);
        driver.findElement(locatorSearchMainField).sendKeys(Keys.ENTER);


        // get list for even (2n) items from all which are available to add to basket; nth-child(even) == 2n
        List <WebElement> itemsLinksToClick = driver.findElements(locatorEvenItemsToBasketBtn);
        List <WebElement> itemsTitlesToCompare = driver.findElements(locatorEvenItemsTitle);
        ArrayList<String> listOfItemsTitlesToCompare = new ArrayList<>();


        // now add all selected goods to the basket
        for (int i = 0; i < itemsLinksToClick.size(); i++) {
            System.out.format("itemsTitlesToCompare: %s \n", itemsTitlesToCompare.get(i).getText());
            listOfItemsTitlesToCompare.add(itemsTitlesToCompare.get(i).getText());
            itemsLinksToClick.get(i).click();
        }

        openBasket();

        // verification over the list in basket and in itemsLinksToClick [itemsLinksToClick.get(i).getText();] + ASSERT //
        /*       List <WebElement> itemsInBasket = driver.findElements(By.cssSelector(
                ".jsViewCollection .bCartItem:nth-child(n) .eCartItem_nameValue"));    */
        List <WebElement> itemsInBasket = driver.findElements(locatorBasketInnerSectionItems);

        System.out.format("Number of items in basket: %s \n", itemsInBasket.size());

        // To simply compare number of items selected vs those in basket. Test will fail if sums do not match.
        Assert.assertEquals(itemsLinksToClick.size(), itemsInBasket.size());

        wait.until(ExpectedConditions.elementToBeClickable(locatorBasketRemoveAllBtn));
        List <WebElement> removeAllButtons = driver.findElements(locatorBasketRemoveAllBtn);

        System.out.format("removeAllButtons number: %s \n", removeAllButtons.size());

        // FIXME: Test is flaky in case if there 2 or more 'delete all' buttons in basket because of dynamic DOM building
        for (int i = 0; i < removeAllButtons.size(); i++){
            wait.until(ExpectedConditions.elementToBeClickable(removeAllButtons.get(i)));
            removeAllButtons.get(i).click();
        }

        logout();

        // authorization and verification that there are no any items left in the basket
        login();
        openBasket();
        Assert.assertEquals("Корзина пуста",
                driver.findElement(By.xpath("//div[@class='eCartPage_title']")).getText());
    }


    private void login() {
        driver.findElement(locatorUserMenu).click();
        driver.findElement(locatorUserMenuLoginOption).click();
        driver.findElement(locatorUserLoginField).sendKeys(userLogin);
        driver.findElement(locatorUserPassField).sendKeys(userPass);
        driver.findElement(locatorUserLoginConfirmBtn).click();
    }

    private void logout() {
        driver.findElement(locatorUserMenu).click();
        driver.findElement(locatorUserMenuLogoffOption).click();
    }

    private void openBasket() {
        driver.get(baseURL + "/context/cart/");
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

}
