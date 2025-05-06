package TestFunction;

import java.time.Duration;
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
public class Logout {
    String URL_login = "https://hauifood.com/auth/login";
    String URL_dashBoard = "https://hauifood.com/";
    WebDriver driver;
    private WebElement loginButton; // Biến instance để lưu nút Đăng nhập

    public void login(String email, String password) {
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement passwordField = driver.findElement(By.name("password"));

        try {
            for (char c : email.toCharArray()) {
                emailField.sendKeys(String.valueOf(c));
                Thread.sleep(100); // Chờ 100ms sau mỗi ký tự
            }

            for (char c : password.toCharArray()) {
                passwordField.sendKeys(String.valueOf(c));
                Thread.sleep(100); // Chờ 100ms sau mỗi ký tự
            }
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted: " + e.getMessage());
        }

        loginButton = driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[2]/div[1]/form[1]/div[4]/button[1]"));
        loginButton.click();
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
    public void logoutCheck() throws InterruptedException {
        Thread.sleep(10000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement iconLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@alt='avatar']")));
        iconLink.click();

        driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[1]/div[1]/div[1]/nav[1]/div[2]/ul[1]/li[1]")).click();

        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait2.until(ExpectedConditions.or(ExpectedConditions.urlToBe(URL_dashBoard)));

        Assert.assertEquals(driver.getCurrentUrl(), URL_dashBoard);

    }
    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
