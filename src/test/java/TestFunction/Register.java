package TestFunction;
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

public class Register {
    String URL_register = "https://hauifood.com/auth/signup";
    String URL_dashBoard = "https://hauifood.com/";


    WebDriver driver;


    public void registerAccount(String fullname, String email, String password) {
        driver.findElement(By.xpath("//input[@placeholder='Họ tên']")).sendKeys(fullname);
        driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys(email);
        driver.findElement(By.xpath("//input[@placeholder='Mật khẩu']")).sendKeys(password);
        driver.findElement(By.xpath("//div[@class='form__group SignUp_signup__btn-group__kDAFQ']")).click();

    }
    @BeforeMethod
    public void setUp(){
        System.setProperty("webdriver.chrome.driver", "C:/Users/Admin/Downloads/chromedriver-win64/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(URL_register);
    }
    @Test(priority=0, enabled = true)
    public void registerByGuest() throws InterruptedException {
        registerAccount("Dinh Lieu", "lieuha2@gmail.com","Lieut2003@@@");
        Thread.sleep(2000);
        Assert.assertTrue(driver.findElement(By.xpath("//div[contains(text(),'Đăng ký tài khoản thành công')]")).isDisplayed());
        Assert.assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Đăng ký tài khoản thành công')]")).getText(),"Đăng ký tài khoản thành công");
        Thread.sleep(1000);
    }

    @Test(priority=1, enabled = true)
    public void registerByExistedUser() throws InterruptedException {
        String oldEmail = "dinhhlieu22@gmail.com";
        registerAccount("Dinh Lieu", oldEmail,"Lieut2003@@@");
        Thread.sleep(2000);
        Assert.assertTrue(driver.findElement(By.xpath("//div[contains(text(),'Email đã tồn tại')]")).isDisplayed());
        Assert.assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Email đã tồn tại')]")).getText(),"Email đã tồn tại");
        Thread.sleep(1000);

    }

    @Test(priority=2, enabled = true)
    public void checkTextName() throws InterruptedException {
        driver.findElement(By.xpath("//input[@placeholder='Họ tên']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys("lieu@gmail.com");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@placeholder='Mật khẩu']")).sendKeys("12345L!");
        Thread.sleep(2000);
        Assert.assertTrue(driver.findElement(By.xpath("//p[contains(text(),'Vui lòng nhập họ tên')]")).isDisplayed());
        Assert.assertEquals(driver.findElement(By.xpath("//p[contains(text(),'Vui lòng nhập họ tên')]")).getText(),"Vui lòng nhập họ tên");
        Thread.sleep(1000);

    }

    @Test(priority=3, enabled = true)
    public void checkTextEmail() throws InterruptedException {
        driver.findElement(By.xpath("//input[@placeholder='Họ tên']")).sendKeys("DInh Lieu");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@placeholder='Email']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@placeholder='Mật khẩu']")).sendKeys("12345L!");
        Thread.sleep(2000);
        Assert.assertTrue(driver.findElement(By.xpath("//p[contains(text(),'Vui lòng nhập email.')]")).isDisplayed());
        Assert.assertEquals(driver.findElement(By.xpath("//p[contains(text(),'Vui lòng nhập email.')]")).getText(),"Vui lòng nhập email.");
        Thread.sleep(1000);

    }

    @Test(priority=4, enabled = true)
    public void checkTextPassword() throws InterruptedException {
        driver.findElement(By.xpath("//input[@placeholder='Họ tên']")).sendKeys("Dinh Lieu");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys("lieu@gmail.com");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@placeholder='Mật khẩu']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@placeholder='Họ tên']")).click();

        Assert.assertTrue(driver.findElement(By.xpath("//p[contains(text(),'Vui lòng nhập mật khẩu.')]")).isDisplayed());
        Assert.assertEquals(driver.findElement(By.xpath("//p[contains(text(),'Vui lòng nhập mật khẩu.')]")).getText(),"Vui lòng nhập mật khẩu.");
        Thread.sleep(1000);

    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
