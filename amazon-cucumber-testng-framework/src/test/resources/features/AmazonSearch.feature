@Amazon @Regression
Feature: Amazon.in Search Result Description Validation

  Scenario: Verify the Amazon India search result contains the requested Bip Max smartwatch description
    Given user navigates to "https://www.amazon.in"
    When user searches for "amaze fit max bip"
    And user clicks search icon
    Then user verifies the search result description "Bip Max 50mm Smartwatch, 2.07\" AMOLED Display, 20 Days of Battery, GPS, 4GB Storage, Offline Maps, Fitness Tracker, Hybrid Training, 150+ Sports, HYROX Mode, 5 ATM for Android & iPhone, Silver"
