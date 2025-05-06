package TestFunction;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Login {
    String URL_login = "https://hauifood.com/auth/login";
    String URL_dashBoard = "https://hauifood.com/";
    String loginWrongMess = "Tài khoản hoặc mật khẩu không chính xác";

    WebDriver driver;

    private WebElement loginButton; // Biến instance để lưu nút Đăng nhập

//    public void login(String email, String password) {
//        driver.findElement(By.name("email")).sendKeys(email);
//        driver.findElement(By.name("password")).sendKeys(password);
//        loginButton = driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[2]/div[1]/form[1]/div[4]/button[1]"));
//        loginButton.click(); // Gọi click nếu cần thao tác đăng nhập
//    }

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


    @Test(priority = 0, enabled = true)
    public void loginByUser() throws InterruptedException {
        login("dinhhlieu22@gmail.com", "Lieut2003@@@");
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        wait.until(ExpectedConditions.urlToBe(URL_dashBoard));
//        Assert.assertEquals(driver.getCurrentUrl(), URL_dashBoard);
        Thread.sleep(2000);
        Assert.assertTrue(driver.findElement(By.xpath("//div[contains(text(),'Đăng nhập thành công')]")).isDisplayed());
        Assert.assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Đăng nhập thành công')]")).getText(),"Đăng nhập thành công");
        Thread.sleep(3000);
    }


    @BeforeMethod
    public void setUp(){
        System.setProperty("webdriver.chrome.driver", "C:/Users/Admin/Downloads/chromedriver-win64/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(URL_login);
    }

    @Test(priority = 1, enabled = true)
    public void loginWrongEmail() {
        login("dinhlieu@gmail.com", "Lieut2003@@@");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement loginWrongElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Tài khoản hoặc mật khẩu không chính xác')]")));
        String loginWrong = loginWrongElement.getText().trim();
        Assert.assertEquals(loginWrong, loginWrongMess);  // Kiểm tra với thông báo lỗi tiếng Việt
    }
    @Test(priority = 2, enabled = true)
    public void loginWrongPassword(){
        login("dinhhlieu22@gmail.com","12345678L!");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement loginWrongElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Tài khoản hoặc mật khẩu không chính xác')]")));
        String loginWrong = loginWrongElement.getText().trim();
        Assert.assertEquals(loginWrong, loginWrongMess);
    }


    @Test(priority = 3, enabled = true)
    public void checkTextEmail() throws InterruptedException {
        driver.findElement(By.name("email")).click();
        Thread.sleep(2000);
        driver.findElement(By.name("password")).sendKeys("Lieut2003@");
        Thread.sleep(2000);

        driver.findElement(By.name("email")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//p[contains(text(),'Vui lòng nhập email.')]")).isDisplayed());
        Assert.assertEquals(driver.findElement(By.xpath("//p[contains(text(),'Vui lòng nhập email.')]")).getText(),"Vui lòng nhập email.");
        Thread.sleep(2000);

    }

    @Test(priority = 4, enabled = true)
    public void checkTextPassword() throws InterruptedException {
        driver.findElement(By.name("email")).sendKeys("lieu@gmail.com");
        Thread.sleep(2000);
        driver.findElement(By.name("password")).click();
        Thread.sleep(2000);
        driver.findElement(By.name("email")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//p[contains(text(),'Vui lòng nhập mật khẩu.')]")).isDisplayed());
        Assert.assertEquals(driver.findElement(By.xpath("//p[contains(text(),'Vui lòng nhập mật khẩu.')]")).getText(),"Vui lòng nhập mật khẩu.");
        Thread.sleep(2000);

    }
    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
 


