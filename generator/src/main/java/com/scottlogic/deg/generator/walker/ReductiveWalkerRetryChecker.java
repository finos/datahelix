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
        if (!successOccurredAtLeastOnce) {
            numRetriesSoFar++;
            if (numRetriesSoFar > retryLimit) {
                throw new RetryLimitReachedException();
            }
        }
    }

    void reset() {
        numRetriesSoFar = 0;
        successOccurredAtLeastOnce = false;
    }
}
