package com.manarbedeir.qa.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DynamicLoadingExamplePage extends BasePage {

    private static final By START_BUTTON = By.cssSelector("#start button");
    private static final By RESULT = By.cssSelector("#finish h4");

    public DynamicLoadingExamplePage(WebDriver driver) {
        super(driver);
    }

    public DynamicLoadingExamplePage start() {
        click(START_BUTTON);
        return this;
    }

    public String resultText() {
        return textOf(RESULT);
    }
}
