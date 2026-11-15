package com.manarbedeir.qa.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DynamicLoadingPage extends BasePage {

    public DynamicLoadingPage(WebDriver driver) {
        super(driver);
    }

    public DynamicLoadingExamplePage openExample(int number) {
        click(By.cssSelector("a[href$='/dynamic_loading/" + number + "']"));
        return new DynamicLoadingExamplePage(driver);
    }
}
