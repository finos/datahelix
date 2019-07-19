package com.scottlogic.deg.generator.walker;

public class ReductiveWalkerRetryChecker {
    private int numRetriesSoFar = 0;
    private boolean guaranteedNotFullyContradictory = false;
    private int retryLimit;

    public ReductiveWalkerRetryChecker(int retryLimit) {
        this.retryLimit = retryLimit;
    }

    void setRetrySuccessful() {
        guaranteedNotFullyContradictory = true;
    }

    void setRetryUnsuccessful() {
        numRetriesSoFar++;
        if (numRetriesSoFar > retryLimit && !guaranteedNotFullyContradictory) {
            throw new RetryLimitReachedException();
        }
    }

    void reset() {
        numRetriesSoFar = 0;
        guaranteedNotFullyContradictory = false;
    }
}
