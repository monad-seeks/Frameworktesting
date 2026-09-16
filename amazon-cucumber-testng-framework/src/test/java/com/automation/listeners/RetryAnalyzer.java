package com.automation.listeners;

import com.automation.utils.Log;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int counter = 0;
    private static final int MAX_RETRY_LIMIT = 2;

    @Override
    public boolean retry(ITestResult result) {
        if (counter < MAX_RETRY_LIMIT) {
            counter++;
            Log.warn("Retrying failed test: " + result.getName() + " | Attempt " + (counter + 1) + " of " + (MAX_RETRY_LIMIT + 1));
            return true;
        }
        return false;
    }
}
