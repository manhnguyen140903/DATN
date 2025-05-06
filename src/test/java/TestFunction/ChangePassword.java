package TestFunction;

import java.time.Duration;
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
public class ChangePassword {
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
    public void changePassword() throws InterruptedException {
        // Đăng nhập vào hệ thống (nếu chưa đăng nhập)
        login("lieuha7125@gmail.com", "Lieut2003@@@");
        Thread.sleep(10000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement iconLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@alt='avatar']")));
        iconLink.click();
        driver.findElement(By.xpath("//a[@href='/auth/profile']")).click();
        //đỏi mk
        // Chờ nút được hiển thị và nhấp vào
        WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content']//li[3]")));
        /*js.executeScript("arguments[0].scrollIntoView(true);", button);*/
        Actions actions = new Actions(driver);
        actions.moveToElement(button).click().build().perform();
        Thread.sleep(2000);
        //đối chiu tên trang
        WebElement title = driver.findElement(By.xpath("//div[@class='Profile_profile-content__title__g4DqP']"));

        Assert.assertEquals(title.getText(),"Đổi mật khẩu");
        //Thưc hien đổi MK
        // Chờ trang tải xong
        Thread.sleep(3000);

        // Tìm các trường nhập liệu và nút đổi mật khẩu
        WebElement currentPasswordField = driver.findElement(By.name("oldPassword")); // Tên trường nhập mật khẩu cũ
        WebElement newPasswordField = driver.findElement(By.name("newPassword")); // Tên trường nhập mật khẩu mới
        WebElement confirmPasswordField = driver.findElement(By.name("confirmPassword")); // Tên trường xác nhận mật khẩu mới
        WebElement changePasswordButton = driver.findElement(By.xpath("//button[@class='Button_wrapper__GqKsN Profile_update-btn__Ss3NP']")); // Xpath của nút đổi mật khẩu

        // Nhập thông tin mật khẩu
        currentPasswordField.sendKeys("Lieut2003@@@"); // Mật khẩu cũ
        Thread.sleep(1000);
        newPasswordField.sendKeys("Lieut2003@"); // Mật khẩu mới
        Thread.sleep(1000);
        confirmPasswordField.sendKeys("Lieut2003@"); // Xác nhận mật khẩu mới
        Thread.sleep(1000);
        actions.moveToElement(changePasswordButton).click().build().perform();
        // Nhấn nút "Đổi mật khẩu"


        // Chờ thông báo đổi mật khẩu thành công
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Cập nhật thành công.')]"))); // Thay đổi xpath nếu cần
        System.out.println(successMessage.getText()); // In thông báo thành công

        // Nếu có yêu cầu xác nhận lại mật khẩu cũ hoặc thông báo lỗi, bạn có thể xử lý thêm ở đây
        Thread.sleep(3000);

    }
    @Test
    public void changePasswordwitdrongPass() throws InterruptedException {
        // Đăng nhập vào hệ thống (nếu chưa đăng nhập)
        login("lieu1@gmail.com", "Lieut2003@@");
        Thread.sleep(10000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement iconLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@alt='avatar']")));
        iconLink.click();
        driver.findElement(By.xpath("//a[@href='/auth/profile']")).click();
        //đỏi mk
        // Chờ nút được hiển thị và nhấp vào
        WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content']//li[3]")));
        /*js.executeScript("arguments[0].scrollIntoView(true);", button);*/
        Actions actions = new Actions(driver);
        actions.moveToElement(button).click().build().perform();
        Thread.sleep(2000);
        //đối chiu tên trang
        WebElement title = driver.findElement(By.xpath("//div[@class='Profile_profile-content__title__g4DqP']"));

        Assert.assertEquals(title.getText(),"Đổi mật khẩu");
        //Thưc hien đổi MK
        // Chờ trang tải xong
        Thread.sleep(3000);

        // Tìm các trường nhập liệu và nút đổi mật khẩu
        WebElement currentPasswordField = driver.findElement(By.name("oldPassword")); // Tên trường nhập mật khẩu cũ
        Thread.sleep(1000);
        WebElement newPasswordField = driver.findElement(By.name("newPassword")); // Tên trường nhập mật khẩu mới
        Thread.sleep(1000);
        WebElement confirmPasswordField = driver.findElement(By.name("confirmPassword")); // Tên trường xác nhận mật khẩu mới
        Thread.sleep(1000);
        WebElement changePasswordButton = driver.findElement(By.xpath("//button[@class='Button_wrapper__GqKsN Profile_update-btn__Ss3NP']")); // Xpath của nút đổi mật khẩu

        // Nhập thông tin mật khẩu
        currentPasswordField.sendKeys("Lieut2003@@@"); // Mật khẩu cũ
        newPasswordField.sendKeys("NewPassword1@"); // Mật khẩu mới
        confirmPasswordField.sendKeys("NewPassword1@"); // Xác nhận mật khẩu mới

        actions.moveToElement(changePasswordButton).click().build().perform();
        // Nhấn nút "Đổi mật khẩu"


        // Chờ thông báo đổi mật khẩu thành công
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Mật khẩu cũ không chính xác')]"))); // Thay đổi xpath nếu cần
        System.out.println(successMessage.getText()); // In thông báo thành công

        // Nếu có yêu cầu xác nhận lại mật khẩu cũ hoặc thông báo lỗi, bạn có thể xử lý thêm ở đây
        Thread.sleep(3000);

    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}

