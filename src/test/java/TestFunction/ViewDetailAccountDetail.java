package TestFunction;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
public class ViewDetailAccountDetail {

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
    public void setUp(){
        System.setProperty("webdriver.chrome.driver", "C:/Users/Admin/Downloads/chromedriver-win64/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(URL_login);
        login("dinhhlieu22@gmail.com","Lieut2003@@@");

    }
    @Test
    public void viewAccountDetailCheck() throws InterruptedException {
        Thread.sleep(10000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement iconLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@alt='avatar']")));
        iconLink.click();

//        driver.findElement(By.xpath("//p[contains(text(),'Thông tin cá nhân')]")).click();
        driver.findElement(By.xpath("//a[@href='/auth/profile']")).click();
        WebElement inputElementName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("fullName")));

        // Lấy giá trị của thuộc tính "value"
        String actualValueName = inputElementName.getAttribute("value");

        // So sánh giá trị thực tế với giá trị kỳ vọng
        String expectedValue = "Đinh Hồng Liễu";
        Assert.assertEquals(actualValueName, expectedValue, "Giá trị của thuộc tính 'value' không khớp!");
        Thread.sleep(2000);

        WebElement inputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        Thread.sleep(2000);
        // Lấy giá trị của thuộc tính "value"
        String actualValueEmail = inputElement.getAttribute("value");

        // So sánh giá trị thực tế với giá trị kỳ vọng
        String expectedEamil = "dinhhlieu22@gmail.com";
        Assert.assertEquals(actualValueEmail, expectedEamil, "Giá trị của thuộc tính 'value' không khớp!");

        driver.quit();
    }
    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
