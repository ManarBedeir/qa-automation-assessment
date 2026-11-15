package com.manarbedeir.qa.ui.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Arrays;

public enum Browser {

    CHROME {
        @Override
        public WebDriver create(boolean headless) {
            ChromeOptions options = new ChromeOptions();
            if (headless) {
                options.addArguments("--headless=new", "--window-size=1920,1080");
            }
            return new ChromeDriver(options);
        }
    },
    EDGE {
        @Override
        public WebDriver create(boolean headless) {
            EdgeOptions options = new EdgeOptions();
            if (headless) {
                options.addArguments("--headless=new", "--window-size=1920,1080");
            }
            return new EdgeDriver(options);
        }
    },
    FIREFOX {
        @Override
        public WebDriver create(boolean headless) {
            FirefoxOptions options = new FirefoxOptions();
            if (headless) {
                options.addArguments("-headless", "--width=1920", "--height=1080");
            }
            return new FirefoxDriver(options);
        }
    };

    public abstract WebDriver create(boolean headless);

    public static Browser from(String name) {
        try {
            return valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unsupported browser '" + name + "'. Use one of " + Arrays.toString(values()));
        }
    }
}
