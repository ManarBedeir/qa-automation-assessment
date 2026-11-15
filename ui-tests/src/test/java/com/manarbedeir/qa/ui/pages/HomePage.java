package com.manarbedeir.qa.ui.pages;

import com.manarbedeir.qa.commons.config.Config;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        String url = Config.get("base.url");
        log.info("Opening {}", url);
        driver.get(url);
        return this;
    }
}
