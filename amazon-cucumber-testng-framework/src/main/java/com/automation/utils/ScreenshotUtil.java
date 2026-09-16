package com.automation.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ScreenshotUtil {

    private ScreenshotUtil() {
    }

    public static String captureScreenshotAsBase64(WebDriver driver) {
        if (driver == null) {
            Log.warn("WebDriver instance was null; cannot capture base64 screenshot.");
            return "";
        }
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    public static String captureScreenshotAsFile(WebDriver driver, String screenshotName) {
        if (driver == null) {
            Log.warn("WebDriver instance was null; cannot capture screenshot file.");
            return "";
        }
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        String dirPath = "target/screenshots/";
        String fileName = screenshotName.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + timestamp + ".png";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File targetFile = new File(dir, fileName);
        byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            fos.write(bytes);
            fos.flush();
            Log.info("Captured failure screenshot: " + targetFile.getAbsolutePath());
        } catch (IOException e) {
            Log.error("Error saving failure screenshot to disk", e);
        }

        return targetFile.getAbsolutePath();
    }

    public static String capturePageSource(WebDriver driver, String fileNamePrefix) {
        if (driver == null) {
            Log.warn("WebDriver instance was null; cannot capture raw DOM source.");
            return "";
        }
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        String dirPath = "target/dom-dumps/";
        String fileName = fileNamePrefix.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + timestamp + ".html";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, fileName);
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write(driver.getPageSource());
            writer.flush();
            Log.info("Captured raw DOM state dump: " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.error("Error writing raw DOM dump file", e);
        }

        return file.getAbsolutePath();
    }
}
