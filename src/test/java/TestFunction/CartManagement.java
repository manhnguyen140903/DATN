package TestFunction;
import java.time.Duration;
import java.util.List;
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

public class CartManagement {
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
    public void setUp(){
        System.setProperty("webdriver.chrome.driver", "C:/Users/Admin/Downloads/chromedriver-win64/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(URL_login);
    }
    @Test(priority=0, enabled = true)
    public void viewCartHasNoProduct() throws InterruptedException {
        login("dinhhlieu22@gmail.com","Lieut2003@@@");
        Thread.sleep(10000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement iconCart = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='Header_header__actions-group__uAY5S Header_header__actions-cart__DNsxJ']")));
        iconCart.click();
        WebElement emptyCartMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[contains(text(),'Giỏ hàng trống')]")));
        Assert.assertTrue(emptyCartMessage.isDisplayed());
        Assert.assertEquals(emptyCartMessage.getText().trim(), "Giỏ hàng trống");
    }
    @Test(priority= 1, enabled = true)
    public void addNewProductToCart() throws InterruptedException {
        login("dinhhlieu22@gmail.com","Lieut2003@@@");
        String keySearch = "Bánh Mì Siêu Nhân";
        //String keySearch = "Cơm đảo gà rang";

        WebElement searchInput = driver.findElement(By.xpath("//input[@id='banner-search']"));
        searchInput.sendKeys(keySearch);
        Thread.sleep(4000);
        //Kich enter
        WebElement timKiem = driver.findElement(By.xpath("//button[contains(text(),'Tìm kiếm')]"));
        timKiem.click();

        WebElement buttonAdd = driver.findElement(By.xpath("//div[@class='ProductCard_product__add-cart-btn__Ze7Gs']"));
        buttonAdd.click();
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));
        WebElement buttonAddToCart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='Button_wrapper__GqKsN QuantityDrawer_quantity-drawer__add-btn__a7VEh Button_primary__9MLUH']")));
        buttonAddToCart.click();
        Thread.sleep(3000);
        WebElement messagaeElement = buttonAddToCart.findElement(By.xpath("//div[contains(text(),'Thêm sản phẩm vào giỏ hàng thành công')]"));
        String message = messagaeElement.getText();
        Assert.assertEquals(message, "Thêm sản phẩm vào giỏ hàng thành công");
        Thread.sleep(7000);
        WebElement buttonCart = driver.findElement(By.xpath("//button[@class='Button_wrapper__GqKsN Button_outline__monpX Button_action__n+ONF Button_haveProducts__TqT1C']"));
        buttonCart.click();
        Thread.sleep(3000);
        //sản phẩm trong giỏ hàng
        WebElement cartItems = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart__products-list")));
        List<WebElement> productNames = cartItems.findElements(By.xpath("//body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/div[3]"));

        boolean productFound = false;
        for(WebElement productName : productNames){
            if (productName.getText().equalsIgnoreCase(keySearch)){
                productFound = true;
                break;
            }

        }
        Assert.assertFalse(productFound, "Sản phẩm "+ keySearch + " không có trong giỏ hàng!");
    }

    @Test(priority=3, enabled = true)
    public void removeProduct() throws InterruptedException {
        // Đăng nhập
        login("dinhhlieu22@gmail.com","Lieut2003@@@");
        Thread.sleep(7000);
        // Tìm và nhấp vào biểu tượng giỏ hàng
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='Button_wrapper__GqKsN Button_outline__monpX Button_action__n+ONF Button_haveProducts__TqT1C']")));
        cartIcon.click();

        // Lặp qua và xóa từng sản phẩm nếu có
        while (true) {
            // Lấy lại danh sách sản phẩm trong giỏ hàng
            List<WebElement> productsInCart = driver.findElements(By.className("CartItem_product__Tp4ck"));

            // Kiểm tra nếu giỏ hàng còn sản phẩm
            if (productsInCart.isEmpty()) {
                break; // Nếu không còn sản phẩm, thoát khỏi vòng lặp
            }

            // Lấy sản phẩm đầu tiên
            WebElement product = productsInCart.get(0);
            WebElement removeButton = product.findElement(By.xpath(".//button[normalize-space()='Xóa']")); // Xpath tương đối
            removeButton.click();

            // Chờ một chút để giỏ hàng cập nhật
            Thread.sleep(7000); // Tùy chỉnh thời gian chờ nếu cần
        }

        System.out.println("Đã xóa tất cả sản phẩm trong giỏ hàng!");
    }


    @Test(priority=2, enabled = true)
    public void ViewCartHasProduct() throws InterruptedException {
        login("dinhhlieu22@gmail.com","Lieut2003@@@");
        Thread.sleep(7000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Bước 1: Kiểm tra số lượng sản phẩm trên biểu tượng giỏ hàng
        WebElement cartQuantityElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@class='Header_header__actions-quantity__yVhp3']")
        ));
        int cartQuantity = Integer.parseInt(cartQuantityElement.getText());
        Assert.assertTrue(cartQuantity >= 1, "Giỏ hàng trống hoặc số lượng sản phẩm hiển thị sai!");

        // Bước 2: Mở giao diện chi tiết giỏ hàng
        WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@class='Button_wrapper__GqKsN Button_outline__monpX Button_action__n+ONF Button_haveProducts__TqT1C']")
        ));
        cartIcon.click();

        // Bước 3: Kiểm tra sản phẩm hiển thị trong giỏ hàng
        WebElement productNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[@class='CartItem_product__detail-name__ryiYE']")
        ));
        Assert.assertTrue(productNameElement.isDisplayed(), "Tên sản phẩm không hiển thị trong giỏ hàng!");

        // So sánh tên sản phẩm (nếu tên mong đợi đã biết)
        String actualProductName = productNameElement.getText();
        String expectedProductName = "Bánh Mì Siêu Nhân";
        Assert.assertEquals(actualProductName, expectedProductName, "Tên sản phẩm trong giỏ hàng không khớp!");


    }
    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
