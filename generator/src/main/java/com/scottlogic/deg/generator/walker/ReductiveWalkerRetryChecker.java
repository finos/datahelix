package com.scottlogic.deg.generator.walker;

class ReductiveWalkerRetryChecker {
    private int numRetriesSoFar = 0;
    private boolean guaranteedNotFullyContradictory = false;

    void setRetrySuccessful() {
        guaranteedNotFullyContradictory = true;
    }

    void setRetryUnsuccessful() {
        numRetriesSoFar++;
        int RETRY_LIMIT = 100;
        if (numRetriesSoFar > RETRY_LIMIT && !guaranteedNotFullyContradictory) {
            throw new RetryLimitReachedException();
        }
    }

    void reset() {
        numRetriesSoFar = 0;
        guaranteedNotFullyContradictory = false;
    }
}
