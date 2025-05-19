package TestFunction;

import java.io.*;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;


//public static String callChatGPT(String prompt, String apiKey) throws Exception {
//    HttpClient client = HttpClient.newHttpClient();
//
//    String requestBody = """
//    {
//      "model": "gpt-4o-mini",
//      "messages": [{"role": "user", "content": "%s"}]
//    }
//    """.formatted(prompt.replace("\"", "\\\"").replace("\n", "\\n"));
//
//    HttpRequest request = HttpRequest.newBuilder()
//            .uri(URI.create("https://api.openai.com/v1/chat/completions"))
//            .header("Content-Type", "application/json")
//            .header("Authorization", "Bearer " + apiKey)
//            .POST(BodyPublishers.ofString(requestBody))
//            .build();
//
//    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
//
//    // Đơn giản lấy phần text đầu tiên trả về (bạn có thể parse JSON kỹ hơn)
//    String json = response.body();
//    // Dùng thư viện JSON (Jackson, Gson) để parse phần 'choices[0].message.content' nếu cần
//    return json;
//}
public class Chatbot {

    // Hàm tính điểm cosine similarity đơn giản dựa trên tần suất từ
    public static double cosineSimilarity(String s1, String s2) {
        String[] words1 = s1.toLowerCase().split("\\W+");
        String[] words2 = s2.toLowerCase().split("\\W+");
        java.util.Map<String, Integer> freq1 = new java.util.HashMap<>();
        java.util.Map<String, Integer> freq2 = new java.util.HashMap<>();
        for (String w : words1) freq1.put(w, freq1.getOrDefault(w, 0) + 1);
        for (String w : words2) freq2.put(w, freq2.getOrDefault(w, 0) + 1);

        java.util.Set<String> allWords = new java.util.HashSet<>();
        allWords.addAll(freq1.keySet());
        allWords.addAll(freq2.keySet());

        double dot = 0, mag1 = 0, mag2 = 0;
        for (String w : allWords) {
            int v1 = freq1.getOrDefault(w, 0);
            int v2 = freq2.getOrDefault(w, 0);
            dot += v1 * v2;
            mag1 += v1 * v1;
            mag2 += v2 * v2;
        }
        if (mag1 == 0 || mag2 == 0) return 0.0;
        return dot / (Math.sqrt(mag1) * Math.sqrt(mag2));
    }

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try (
                FileInputStream file = new FileInputStream(new File("D:\\test.xlsx"));
                Workbook workbook = new XSSFWorkbook(file)
        ) {
            DataFormatter formatter = new DataFormatter();

            boolean firstResetDone = false;
            boolean pageLoaded = false;

            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                System.out.println("\n--- Đang xử lý sheet: " + sheet.getSheetName() + " ---");

                // Xác định vị trí các cột
                Row headerRow = sheet.getRow(0);
                if (headerRow == null) continue;

                int idCol = -1, preconditionCol = -1, sampleCol = -1, expectedCol = -1, statusCol = -1, actualCol = -1, scoreCol = -1;

                for (Cell cell : headerRow) {
                    String colName = cell.getStringCellValue().trim().toLowerCase();
                    switch (colName) {
                        case "id": idCol = cell.getColumnIndex(); break;
                        case "precondition": preconditionCol = cell.getColumnIndex(); break;
                        case "sample": sampleCol = cell.getColumnIndex(); break;
                        case "expected": expectedCol = cell.getColumnIndex(); break;
                        case "status": statusCol = cell.getColumnIndex(); break;
                        case "actual_result": actualCol = cell.getColumnIndex(); break;
                        case "score": scoreCol = cell.getColumnIndex(); break;
                    }
                }

                // Nếu chưa có cột score thì thêm cột này vào cuối
                if (scoreCol == -1) {
                    scoreCol = headerRow.getLastCellNum();
                    Cell scoreHeaderCell = headerRow.createCell(scoreCol);
                    scoreHeaderCell.setCellValue("score");
                }

                // Kiểm tra các cột bắt buộc
                if (sampleCol == -1 || expectedCol == -1 || actualCol == -1 || statusCol == -1 || preconditionCol == -1) {
                    System.err.println("❌ Thiếu cột bắt buộc trong sheet: " + sheet.getSheetName());
                    continue;
                }

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    String id = idCol != -1 ? formatter.formatCellValue(row.getCell(idCol)) : "Row " + i;
                    String sample = formatter.formatCellValue(row.getCell(sampleCol));
                    String expected = formatter.formatCellValue(row.getCell(expectedCol));
                    String precondition = formatter.formatCellValue(row.getCell(preconditionCol)).trim();

                    System.out.println("\n🔍 Test case: " + id + " | Sample: " + sample + " | Expected: " + expected + " | Precondition: " + precondition);

                    try {
                        if ("$RESET".equalsIgnoreCase(precondition)) {
                            if (!firstResetDone) {
                                driver.get("https://acb.com.vn/");

                                WebElement chatIcon = wait.until(ExpectedConditions.elementToBeClickable(By.id("fab_icon")));
                                chatIcon.click();

                                WebElement startBtn = wait.until(d -> {
                                    WebElement el = d.findElement(By.id("welcome_screen_button"));
                                    String classAttr = el.getAttribute("class");
                                    String text = el.getText().trim();
                                    return (!classAttr.contains("is-disabled") && text.equalsIgnoreCase("BẮT ĐẦU")) ? el : null;
                                });

                                System.out.println("StartBtn tag: " + startBtn.getTagName());
                                System.out.println("StartBtn outerHTML: " + startBtn.getAttribute("outerHTML"));

                                try {
                                    startBtn.click();
                                } catch (Exception e1) {
                                    try {
                                        new Actions(driver).moveToElement(startBtn).click().perform();
                                    } catch (Exception e2) {
                                        ((JavascriptExecutor) driver).executeScript(
                                                "arguments[0].click ? arguments[0].click() : arguments[0].dispatchEvent(new MouseEvent('click', {bubbles:true}))",
                                                startBtn);
                                    }
                                }

                                WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("vinbase_input_name")));
                                nameInput.clear();
                                nameInput.sendKeys("Nguyen Van A");

                                WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("vinbase_submit_user_info")));
                                submitBtn.click();

                                Thread.sleep(5000);

                                firstResetDone = true;
                                pageLoaded = true;
                            } else {
                                driver.navigate().refresh();
                                WebElement chatIcon = wait.until(ExpectedConditions.elementToBeClickable(By.id("fab_icon")));
                                chatIcon.click();
                                pageLoaded = true;
                            }
                        } else {
                            if (!pageLoaded) {
                                driver.get("https://acb.com.vn/");
                                WebElement chatIcon = wait.until(ExpectedConditions.elementToBeClickable(By.id("fab_icon")));
                                chatIcon.click();
                                pageLoaded = true;
                            }
                        }

                        // Nhập câu hỏi
                        WebElement inputBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("chatInput")));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].innerText = arguments[1];", inputBox, sample);

                        inputBox.sendKeys(Keys.ENTER);

                        Thread.sleep(5000);

                        WebDriverWait waitBot = new WebDriverWait(driver, Duration.ofSeconds(15));
                        waitBot.until(drv -> {
                            List<WebElement> replies = drv.findElements(By.cssSelector(".chat-message_text_content"));
                            if (replies.isEmpty()) return false;
                            String lastText = replies.get(replies.size() - 1).getText();
                            return lastText != null && !lastText.trim().isEmpty();
                        });

                        List<WebElement> botReplies = driver.findElements(By.cssSelector(".chat-message_text_content"));

                        String actual = "";
                        String status = "";
                        double score = 0.0;

                        if (!botReplies.isEmpty()) {
                            actual = botReplies.get(botReplies.size() - 1).getText();
                            System.out.println("📨 Bot reply: " + actual);

                            score = cosineSimilarity(actual, expected);
                            System.out.println("Score similarity: " + score);

                            if (score > 0.8) {
                                status = "PASSED";
                                System.out.println("✅ PASSED");
                            } else {
                                status = "FAILED";
                                System.out.println("❌ FAILED");
                            }
                        } else {
                            actual = "Không có phản hồi từ chatbot.";
                            status = "FAILED";
                            System.out.println("❌ FAILED: " + actual);
                        }

                        // Ghi kết quả vào Excel
                        Cell actualCell = row.getCell(actualCol);
                        if (actualCell == null) actualCell = row.createCell(actualCol);
                        actualCell.setCellValue(actual);

                        Cell statusCell = row.getCell(statusCol);
                        if (statusCell == null) statusCell = row.createCell(statusCol);
                        statusCell.setCellValue(status);

                        Cell scoreCell = row.getCell(scoreCol);
                        if (scoreCell == null) scoreCell = row.createCell(scoreCol);
                        scoreCell.setCellValue(score);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Nghỉ 8 giây khi chuyển sheet
                System.out.println("⏳ Nghỉ 8 giây trước khi chuyển sang sheet kế tiếp...");
                Thread.sleep(8000);
                pageLoaded = false; // reset trạng thái để load trang khi sheet mới
            }

            try (FileOutputStream outFile = new FileOutputStream(new File("D:\\test.xlsx"))) {
                workbook.write(outFile);
            }

            System.out.println("\n✅ Đã lưu kết quả vào file Excel.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
