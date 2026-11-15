package com.manarbedeir.qa.ui.tests;

import com.manarbedeir.qa.commons.config.Config;
import com.manarbedeir.qa.ui.driver.Browser;
import com.manarbedeir.qa.ui.pages.HomePage;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BaseTest {

    private static final Logger LOG = LoggerFactory.getLogger(BaseTest.class);
    private static final Path SCREENSHOT_DIR = Paths.get("target", "screenshots");
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = Browser.from(Config.get("browser")).create(Config.getBoolean("headless"));
        driver.manage().timeouts().pageLoadTimeout(Config.getSeconds("page.load.timeout.seconds"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (driver == null) {
            return;
        }
        if (result.getStatus() == ITestResult.FAILURE) {
            saveScreenshot(result);
        }
        driver.quit();
    }

    protected HomePage openHomePage() {
        return new HomePage(driver).open();
    }

    private void saveScreenshot(ITestResult result) {
        String name = result.getTestClass().getRealClass().getSimpleName() + "_"
                + result.getMethod().getMethodName() + "_" + LocalDateTime.now().format(TIMESTAMP) + ".png";
        try {
            Files.createDirectories(SCREENSHOT_DIR);
            byte[] image = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(SCREENSHOT_DIR.resolve(name), image);
            LOG.info("Saved screenshot {}", name);
        } catch (IOException e) {
            LOG.warn("Could not save screenshot {}", name, e);
        }
    }
}
