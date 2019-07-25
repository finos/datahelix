package com.scottlogic.deg.generator.walker;

public class ReductiveWalkerRetryChecker {
    private int numRetriesSoFar = 0;
    private int retryLimit;

    public ReductiveWalkerRetryChecker(int retryLimit) {
        this.retryLimit = retryLimit;
    }

    void retrySuccessful() {
        numRetriesSoFar = 0;
    }

    void retryUnsuccessful() {
        numRetriesSoFar++;
        if (numRetriesSoFar > retryLimit) {
            throw new RetryLimitReachedException();
        }
    }

    void reset() {
        numRetriesSoFar = 0;
    }
}
