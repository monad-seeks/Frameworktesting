package com.automation.stepdefinitions;

import com.automation.driver.DriverManager;
import com.automation.pages.AmazonPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AmazonSteps {

    private AmazonPage amazonPage;

    private AmazonPage getAmazonPage() {
        if (amazonPage == null) {
            amazonPage = new AmazonPage(DriverManager.getDriver());
        }
        return amazonPage;
    }

    @Given("user navigates to {string}")
    public void userNavigatesTo(String url) {
        getAmazonPage().navigateTo(url);
    }

    @When("user searches for {string}")
    public void userSearchesFor(String query) {
        getAmazonPage().searchFor(query);
    }

    @When("user clicks search icon")
    public void userClicksSearchIcon() {
        getAmazonPage().clickSearchIcon();
    }

    @Then("user verifies the search result description {string}")
    public void userVerifiesTheSearchResultDescription(String description) {
        if (description != null && !description.trim().isEmpty()) {
            getAmazonPage().verifyDescriptionPresentInResults(description);
        }
    }
}
