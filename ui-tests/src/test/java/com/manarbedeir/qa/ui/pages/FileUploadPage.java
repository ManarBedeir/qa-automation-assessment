package com.manarbedeir.qa.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;

public class FileUploadPage extends BasePage {

    private static final By FILE_INPUT = By.id("file-upload");
    private static final By SUBMIT_BUTTON = By.id("file-submit");

    public FileUploadPage(WebDriver driver) {
        super(driver);
    }

    public FileUploadPage selectFile(Path file) {
        type(FILE_INPUT, file.toAbsolutePath().toString());
        return this;
    }

    public UploadedFilePage submit() {
        click(SUBMIT_BUTTON);
        return new UploadedFilePage(driver);
    }
}
