package TestFunction;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
public class ViewListOfProductsToBuy {

    String URL_login = "https://hauifood.com/auth/login";
    String URL_dashBoard = "https://hauifood.com/";
    WebDriver driver;
    public void login(String email, String password) {
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        WebElement loginButton = driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[2]/div[1]/form[1]/div[4]/button[1]"));
        loginButton.click(); // Gọi click nếu cần thao tác đăng nhập
    }
    @BeforeMethod
    public void setUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:/Users/Admin/Downloads/chromedriver-win64/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(URL_login);
    }

    @Test
    public void ViewOrderHistoryHasNoProduct() throws InterruptedException {
        login("lieuha1@gmail.com", "Lieut2003@@@");
        Thread.sleep(10000);
        // Chờ avatar sẵn sàng và nhấp vào
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[1]/div[1]/div[1]/nav[1]/div[2]")));
        WebElement iconUser = driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[1]/div[1]/div[1]/nav[1]/div[2]"));
        iconUser.click();

        // Chờ liên kết profile sẵn sàng và nhấp vào
        WebElement linkUser = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='/auth/profile']//li[@class='Header_header__user-option__SCGNH']")));
        /*Actions actions = new Actions(driver);
        actions.moveToElement(linkUser).build().perform();*/


        linkUser.click();
        WebElement title = driver.findElement(By.xpath("//div[@class='Profile_profile-content__title__g4DqP']"));

        Assert.assertEquals(title.getText(),"Thông tin cá nhân");

        // Cuộn trang để đảm bảo phần tử trong tầm nhìn
        JavascriptExecutor js = (JavascriptExecutor) driver;


        // Chờ nút được hiển thị và nhấp vào
        WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[2]/ul[1]/li[6]")));
        /*js.executeScript("arguments[0].scrollIntoView(true);", button);*/
        Actions actions = new Actions(driver);
        actions.moveToElement(button).click().build().perform();
        Thread.sleep(3000);
        // Xác nhận thông báo "Bạn hiện không có đơn hàng nào"
        WebElement emptyOrderHistoryMS = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("HistoryOder_empty-order__desc__xhv2F")));
        Assert.assertTrue(emptyOrderHistoryMS.isDisplayed());
        Assert.assertEquals(emptyOrderHistoryMS.getText().trim(), "Bạn hiện không có đơn hàng nào");
    }

    @Test
    public void ViewOrderHistoryHasProduct() throws InterruptedException{
        login("dinhhlieu22@gmail.com","Lieut2003@@@");
        Thread.sleep(10000);
        // Chờ avatar sẵn sàng và nhấp vào
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[1]/div[1]/div[1]/nav[1]/div[2]")));
        WebElement iconUser = driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[1]/div[1]/div[1]/nav[1]/div[2]"));
        iconUser.click();

        // Chờ liên kết profile sẵn sàng và nhấp vào
        WebElement linkUser = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='/auth/profile']//li[@class='Header_header__user-option__SCGNH']")));
        /*Actions actions = new Actions(driver);
        actions.moveToElement(linkUser).build().perform();*/


        linkUser.click();
        WebElement title = driver.findElement(By.xpath("//div[@class='Profile_profile-content__title__g4DqP']"));

        Assert.assertEquals(title.getText(),"Thông tin cá nhân");

        // Cuộn trang để đảm bảo phần tử trong tầm nhìn
        JavascriptExecutor js = (JavascriptExecutor) driver;


        // Chờ nút được hiển thị và nhấp vào
        WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[2]/ul[1]/li[6]")));
        /*js.executeScript("arguments[0].scrollIntoView(true);", button);*/
        Actions actions = new Actions(driver);
        actions.moveToElement(button).click().build().perform();

        Thread.sleep(3000);
        WebElement listItem = driver.findElement(By.xpath("//div[@class='list-item']"));
        Assert.assertTrue(listItem.isDisplayed());
    }
    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
