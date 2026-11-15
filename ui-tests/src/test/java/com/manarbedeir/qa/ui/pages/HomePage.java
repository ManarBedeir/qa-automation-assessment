package com.manarbedeir.qa.ui.pages;

import com.manarbedeir.qa.commons.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private static final By FILE_UPLOAD_LINK = By.linkText("File Upload");
    private static final By DYNAMIC_LOADING_LINK = By.linkText("Dynamic Loading");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        String url = Config.get("base.url");
        log.info("Opening {}", url);
        driver.get(url);
        return this;
    }

    public FileUploadPage openFileUpload() {
        click(FILE_UPLOAD_LINK);
        return new FileUploadPage(driver);
    }

    public DynamicLoadingPage openDynamicLoading() {
        click(DYNAMIC_LOADING_LINK);
        return new DynamicLoadingPage(driver);
    }
}
