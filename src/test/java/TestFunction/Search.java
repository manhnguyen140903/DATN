package TestFunction;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Search {
    String URL_login = "https://hauifood.com/auth/login";
    String URL_dashBoard = "https://hauifood.com/";
    WebDriver driver;
    public void login(String email, String password) {
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        WebElement loginButton = driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[2]/div[1]/form[1]/div[4]/button[1]"));
        loginButton.click(); // Gọi click nếu cần thao tác đăng nhập
    }
    @BeforeTest
    public void setUp() throws InterruptedException {
        WebDriverManager.chromedriver().setup(); // Dùng WebDriverManager thay cho setProperty
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=vi");
        driver = new ChromeDriver(options); // Khởi tạo browser
        driver.manage().window().maximize(); // Tối đa hóa cửa sổ trình duyệt
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); // Timeout ngầm định
        driver.get(URL_dashBoard); // Mở trang web
    }
    @AfterMethod
    public void screenshort(ITestResult result) throws IOException {
        // Tạo tham chiếu của TakesScreenshot với driver hiện tại
        TakesScreenshot ts = (TakesScreenshot) driver;

        // Gọi hàm capture screenshot - getScreenshotAs
        File source = ts.getScreenshotAs(OutputType.FILE);

        // Kiểm tra folder tồn tại. Nếu không thì tạo mới folder
        File theDir = new File("./Screenshots/");
        if (!theDir.exists()) {
            theDir.mkdirs();
        }

        // Lấy tên class và tên method (test case)
        String className = result.getTestClass().getRealClass().getSimpleName(); // tên class
        String methodName = result.getName(); // tên method

        // Tạo đường dẫn file với tên class và method
        String fileName = className + "_" + methodName + ".png";

        // Copy file screenshot
        FileHandler.copy(source, new File("./Screenshots/" + fileName));
        System.out.println("Screenshot taken: " + fileName);
    }
    @Test(priority=0, enabled = true)
    public void searchWithCorrectName() throws InterruptedException {
        Thread.sleep(4000);
        String keySearch = "Bánh mì siêu nhân";
        WebElement searchInput = driver.findElement(By.xpath("//input[@id='banner-search']"));
        searchInput.sendKeys(keySearch);
        Thread.sleep(4000);

        // Click the search button
        WebElement timKiem = driver.findElement(By.xpath("//button[contains(text(),'Tìm kiếm')]"));
        timKiem.click();
        Thread.sleep(4000);

        // Check the list of products
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement searchResultsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Danh sách sản phẩm')]")));

        // Scroll down by half the page height
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, window.innerHeight / 2);");
        Thread.sleep(4000);

        List<WebElement> productList = driver.findElements(By.xpath("//body/div[@id='root']/div[@class='App']/div[@class='wrapper']/div[@class='content']/div[@class='Home_home__9Ke73']/div[@class='container gx-5']/div[@class='home__search-result-container Home_home__search-result-show__+VEO+']/div[@class='list']/div[1]"));

        Assert.assertTrue(searchResultsElement.isDisplayed());
        Assert.assertEquals(searchResultsElement.getText(), "Danh sách sản phẩm");
        Assert.assertFalse(productList.isEmpty(), "Product list is empty!");
        Thread.sleep(2000);
    }

    @Test(priority=1, enabled = true)
    public void searchApproximatelyName() throws InterruptedException {
        String keySearch = "MÌ";
        WebElement searchInput = driver.findElement(By.xpath("//input[@id='banner-search']"));
        searchInput.sendKeys(keySearch);
        Thread.sleep(5000);

        // Kích vào nút "Tìm kiếm"
        WebElement timKiem = driver.findElement(By.xpath("//button[contains(text(),'Tìm kiếm')]"));
        timKiem.click();
        Thread.sleep(5000);

        // Kiểm tra tiêu đề "Danh sách sản phẩm"
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement searchResultsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Danh sách sản phẩm')]")));

        Assert.assertTrue(searchResultsElement.isDisplayed());
        Assert.assertEquals(searchResultsElement.getText(), "Danh sách sản phẩm");

        // Cuộn xuống để hiển thị toàn bộ danh sách sản phẩm
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Tìm danh sách sản phẩm
        List<WebElement> productList = driver.findElements(By.xpath("//body/div[@id='root']/div[@class='App']/div[@class='wrapper']/div[@class='content']/div[@class='Home_home__9Ke73']/div[@class='container gx-5']/div[@class='home__search-result-container Home_home__search-result-show__+VEO+']/div[@class='list']/div"));

        // Cuộn qua từng phần tử
        for (int i = 0; i < productList.size(); i++) {
            js.executeScript("arguments[0].scrollIntoView(true);", productList.get(i));
            Thread.sleep(4000); // Tạm dừng để kiểm tra
        }
        Thread.sleep(1000);
        // Kiểm tra danh sách không rỗng
        Assert.assertTrue(!productList.isEmpty());

    }


    @Test(priority=2, enabled = true)
    public void searchWithIncorrectName() throws InterruptedException {
        String keySearch = "Bánh bao";
        WebElement searchInput = driver.findElement(By.xpath("//input[@id='banner-search']"));
        searchInput.sendKeys(keySearch);
        Thread.sleep(4000);

        // Click search button
        WebElement timKiem = driver.findElement(By.xpath("//button[contains(text(),'Tìm kiếm')]"));
        timKiem.click();
        Thread.sleep(4000);

        // Scroll down to the "No Results" element
        WebElement noProcductElement = driver.findElement(By.xpath("//div[@class='NoResult_no-result__container__2sxtq']"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", noProcductElement);
        Thread.sleep(3000);

        // Assert the "No Results" element is displayed
        Assert.assertTrue(noProcductElement.isDisplayed(), "No results message is not displayed.");
        Thread.sleep(1000);
        // Verify the text content
        String actualText = noProcductElement.getText().trim();
        String expectedText = "Rất tiếc, hiện không có món ăn nào";
        Assert.assertEquals(actualText, expectedText, "Text does not match the expected message.");
        Thread.sleep(2000);
    }

    @AfterTest
    public void tearDown(){
        driver.quit();
    }
}



















