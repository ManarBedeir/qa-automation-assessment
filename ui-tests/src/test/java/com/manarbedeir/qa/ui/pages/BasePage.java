package com.manarbedeir.qa.ui.pages;

import com.manarbedeir.qa.commons.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePage {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Config.getSeconds("explicit.wait.seconds"));
    }

    protected void click(By locator) {
        log.info("Clicking {}", locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        log.info("Typing into {}", locator);
        wait.until(ExpectedConditions.presenceOfElementLocated(locator)).sendKeys(text);
    }

    protected String textOf(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
    }
}
