package TestFunction;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ViewProductDetail {
    String URL_login = "https://hauifood.com/auth/login";
    String URL_dashBoard = "https://hauifood.com/";
    WebDriver driver;

    private WebElement loginButton; // Biến instance để lưu nút Đăng nhập

    public void login(String email, String password) {
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        loginButton = driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[2]/div[1]/form[1]/div[4]/button[1]"));
        loginButton.click(); // Gọi click nếu cần thao tác đăng nhập
    }
    @BeforeMethod
    public void setUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:/Users/Admin/Downloads/chromedriver-win64/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(URL_dashBoard);
        Thread.sleep(6000);
    }
    //xem chi tiet san pham khong can dang nhap
    @Test
    public void viewProductDetailNotLoggedIn() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Nhập từ khóa tìm kiếm và nhấn nút tìm kiếm
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("banner-search")));
        searchInput.sendKeys("Bánh mì siêu nhân");
        WebElement searchButton = driver.findElement(By.xpath("//button[contains(text(),'Tìm kiếm')]"));
        searchButton.click();
        // Chờ danh sách sản phẩm xuất hiện
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Danh sách sản phẩm')]")));

        // Cuộn trang nếu cần
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");

        // Lấy danh sách sản phẩm
        List<WebElement> productList = driver.findElements(By.xpath("//body/div[@id='root']/div[@class='App']/div[@class='wrapper']/div[@class='content']/div[@class='Home_home__9Ke73']/div[@class='container gx-5']/div[@class='home__search-result-container Home_home__search-result-show__+VEO+']/div[@class='list']/div"));
        Assert.assertFalse(productList.isEmpty(), "Danh sách sản phẩm trống!");

/*        WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='ProductCard_product__name__6llVj']")));
        productLink.click();*/

        WebElement productLink = driver.findElement(By.xpath("//div[@class='ProductCard_product__wrapper__-iCKU']")); // Giả định sản phẩm chứa liên kết trong thẻ <a>
        productLink.click();

// Xác minh rằng trang chi tiết sản phẩm đã được mở
        WebElement productTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='QuantityDrawer_quantity-drawer__wrapper__IxaUM QuantityDrawer_quantity-drawer__wrapper--show__W2e-0']")));
        Assert.assertTrue(productTitle.isDisplayed(), "Không thể mở trang chi tiết sản phẩm!");
        String namProduct = driver.findElement(By.xpath("//div[@class='QuantityDrawer_quantity-drawer__product-name__uH2+W']")).getText().trim();
        Assert.assertEquals(namProduct, "Bánh Mì Siêu Nhân");
        driver.quit();

    }

    @Test
    public void viewProductDetailLoggedIn() {
        driver.get(URL_login);
        login("dinhhlieu22@gmail.com","Lieut2003@@@");
        // Nhập từ khóa tìm kiếm và nhấn nút tìm kiếm
        String keySearch = "Bánh mì siêu nhân";
        WebElement searchInput = driver.findElement(By.xpath("//input[@id='banner-search']"));
        searchInput.sendKeys(keySearch);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement searchButton = driver.findElement(By.xpath("//button[contains(text(),'Tìm kiếm')]"));
        searchButton.click();
        // Chờ danh sách sản phẩm xuất hiện
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Danh sách sản phẩm')]")));
        // Cuộn trang nếu cần
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
        // Lấy danh sách sản phẩm
        List<WebElement> productList = driver.findElements(By.xpath("//body/div[@id='root']/div[@class='App']/div[@class='wrapper']/div[@class='content']/div[@class='Home_home__9Ke73']/div[@class='container gx-5']/div[@class='home__search-result-container Home_home__search-result-show__+VEO+']/div[@class='list']/div"));
        Assert.assertFalse(productList.isEmpty(), "Danh sách sản phẩm trống!");
        WebElement productLink = driver.findElement(By.xpath("//div[@class='ProductCard_product__wrapper__-iCKU']")); // Giả định sản phẩm chứa liên kết trong thẻ <a>
        productLink.click();

// Xác minh rằng trang chi tiết sản phẩm đã được mở
        WebElement productTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='QuantityDrawer_quantity-drawer__wrapper__IxaUM QuantityDrawer_quantity-drawer__wrapper--show__W2e-0']")));
        Assert.assertTrue(productTitle.isDisplayed(), "Không thể mở trang chi tiết sản phẩm!");
        String namProduct = driver.findElement(By.xpath("//div[@class='QuantityDrawer_quantity-drawer__product-name__uH2+W']")).getText().trim();
        Assert.assertEquals(namProduct, "Bánh Mì Siêu Nhân");
        driver.quit();
    }
}
