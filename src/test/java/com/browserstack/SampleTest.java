package com.browserstack;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;

import org.testng.annotations.Test;
import java.io.File;
import java.time.Duration;

public class SampleTest extends DriverUtils {

    @Test
    public void test() throws Exception {
        // Handle location permission dialog if it appears
        try {
            // WebDriverWait now uses Duration.ofSeconds()
            WebElement dontAllowButton = new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                ExpectedConditions.elementToBeClickable(AppiumBy.id("com.android.permissioncontroller:id/permission_deny_button")));
            dontAllowButton.click();
            Thread.sleep(1000); 
        } catch (TimeoutException e) {
            System.out.println("No location permission dialog found, proceeding with test");
        }
      
        // Also try alternative permission dialog selectors
        try {
            WebElement dontAllowButton = new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                ExpectedConditions.elementToBeClickable(AppiumBy.xpath("//android.widget.Button[contains(@text, \"Don't allow\") or contains(@text, 'Deny') or contains(@text, 'Not now')]")));
            dontAllowButton.click();
            Thread.sleep(1000);
        } catch (TimeoutException e) {
            System.out.println("No text-based permission dialog found, proceeding with test");
        }
      
        // Find URL input
        WebElement inputElement = new WebDriverWait(driver, Duration.ofSeconds(30)).until(
            ExpectedConditions.elementToBeClickable(AppiumBy.id("com.bsstag.espressotesting:id/url")));
        inputElement.sendKeys("http://fb.com");

        // Take screenshot
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Click Go
        WebElement goElement = new WebDriverWait(driver, Duration.ofSeconds(30)).until(
            ExpectedConditions.elementToBeClickable(AppiumBy.id("com.bsstag.espressotesting:id/go")));
        goElement.click();
    }
}
