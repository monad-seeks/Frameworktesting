package com.automation.pages;

import com.automation.utils.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class AmazonPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By amazonLogo = By.id("nav-logo-sprites");
    private final By searchBox = By.id("twotabsearchtextbox");
    private final By searchSubmit = By.id("nav-search-submit-button");
    private final By resultsGrid = By.xpath("//div[contains(@class,'s-result-item') or contains(@data-component-type,'s-search-result')]");

    public AmazonPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigateTo(String url) {
        Log.info("Navigating to Amazon India page: " + url);
        driver.get(url);
        wait.until(ExpectedConditions.presenceOfElementLocated(amazonLogo));
    }

    public void searchFor(String query) {
        Log.info("Searching Amazon.in for: " + query);
        WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        search.clear();
        search.sendKeys(query);
    }

    public void clickSearchIcon() {
        Log.info("Clicking Amazon.in search icon");
        WebElement icon = wait.until(ExpectedConditions.elementToBeClickable(searchSubmit));
        try {
            icon.click();
        } catch (Exception e) {
            Log.warn("Default search click failed; using JavaScript click fallback.");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", icon);
        }

        wait.until(ExpectedConditions.presenceOfElementLocated(resultsGrid));
    }

    public void verifyDescriptionPresentInResults(String expectedDescription) {
        Log.info("Checking if Amazon search result contains requested product description.");
        boolean found = false;

        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(resultsGrid));
            List<WebElement> resultCards = driver.findElements(resultsGrid);

            for (WebElement card : resultCards) {
                String cardText = normalize(card.getText());
                String clipped = cardText.length() > 1200 ? cardText.substring(0, 1200) : cardText;
                if (clipped.contains(normalize(expectedDescription))) {
                    found = true;
                    break;
                }
            }
        } catch (TimeoutException e) {
            Log.warn("No suitable Amazon result cards found within the search page wait window.");
        }

        Assert.assertTrue(found, "Expected Amazon result description was not found in the search results: " + expectedDescription);
    }

    private String normalize(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&quot;", "\"")
                .replace("&amp;", "&")
                .replace("\u2019", "'")
                .replace("\u2013", "-")
                .replace("\u2014", "-")
                .replace("\n", " ")
                .replace("\r", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
