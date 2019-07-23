package com.scottlogic.deg.generator.walker;

public class ReductiveWalkerRetryChecker {
    private int numRetriesSoFar = 0;
    private boolean successOccurredAtLeastOnce = false;
    private int retryLimit;

    public ReductiveWalkerRetryChecker(int retryLimit) {
        this.retryLimit = retryLimit;
    }

    void retrySuccessful() {
        successOccurredAtLeastOnce = true;
    }

    void retryUnsuccessful() {
        numRetriesSoFar++;
        if (numRetriesSoFar > retryLimit && !successOccurredAtLeastOnce) {
            throw new RetryLimitReachedException();
        }
    }

    void reset() {
        numRetriesSoFar = 0;
        successOccurredAtLeastOnce = false;
    }
}
