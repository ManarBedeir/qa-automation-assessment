package com.manarbedeir.qa.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class UploadedFilePage extends BasePage {

    private static final By HEADING = By.cssSelector("#content h3");
    private static final By UPLOADED_FILES = By.id("uploaded-files");

    public UploadedFilePage(WebDriver driver) {
        super(driver);
    }

    public String heading() {
        return textOf(HEADING);
    }

    public String uploadedFileName() {
        return textOf(UPLOADED_FILES);
    }
}
