package com.automation.stepdefinitions;

import com.automation.driver.DriverManager;
import com.automation.utils.Log;
import com.automation.utils.ScreenshotUtil;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Hooks {

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        Log.info("==========================================================================================");
        Log.info("STARTING SCENARIO: " + scenario.getName());
        Log.info("==========================================================================================");
        DriverManager.initDriver();
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        if (scenario.isFailed() && driver != null) {
            try {
                byte[] stepScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(stepScreenshot, "image/png", "Step_Failure_" + System.currentTimeMillis());
                Log.info("Attached failure screenshot to report on failed step.");
            } catch (Exception e) {
                Log.error("Error capturing step failure screenshot: " + e.getMessage(), e);
            }
        }
    }

    @After(order = 1)
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            try {
                if (scenario.isFailed()) {
                    String cleanName = scenario.getName().replaceAll("[^a-zA-Z0-9_-]", "_");
                    String screenshotPath = ScreenshotUtil.captureScreenshotAsFile(driver, cleanName);
                    String domDumpPath = ScreenshotUtil.capturePageSource(driver, cleanName);

                    byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    scenario.attach(screenshotBytes, "image/png", "Final_Scenario_Failure");
                    scenario.attach("Saved DOM Dump: " + domDumpPath + "\nScreenshot File: " + screenshotPath, "text/plain", "Failure_Diagnostics");
                    Log.error("Scenario [" + scenario.getName() + "] marked as FAILED. State saved to disk.");
                } else {
                    Log.info("Scenario [" + scenario.getName() + "] marked as PASSED.");
                }
            } catch (Exception e) {
                Log.error("Teardown capture error: " + e.getMessage(), e);
            } finally {
                DriverManager.quitDriver();
                Log.info("==========================================================================================");
                Log.info("COMPLETED SCENARIO: " + scenario.getName());
                Log.info("==========================================================================================");
            }
        }
    }
}
