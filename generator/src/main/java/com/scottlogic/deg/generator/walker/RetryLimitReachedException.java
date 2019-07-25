package com.scottlogic.deg.generator.walker;

public class RetryLimitReachedException extends RuntimeException {
    // This exception must be a RuntimeException to be able to break out of a stream evaluation.
    public RetryLimitReachedException() {
        super();
    }
}
