package com.scottlogic.deg.generator.walker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReductiveWalkerRetryCheckerTests {
    @Test
    public void retryChecker_withRepeatedFailure_throwsException() {
        //Arrange
        ReductiveWalkerRetryChecker retryChecker = new ReductiveWalkerRetryChecker(2);

        //Act
        retryChecker.setRetryUnsuccessful();
        retryChecker.setRetryUnsuccessful();

        //Assert
        assertThrows(RetryLimitReachedException.class, retryChecker::setRetryUnsuccessful);
    }

    @Test
    public void retryChecker_withRepeatedFailureAfterSuccess_doesNotThrowException() {
        //Arrange
        ReductiveWalkerRetryChecker retryChecker = new ReductiveWalkerRetryChecker(2);

        //Act
        retryChecker.setRetryUnsuccessful();
        retryChecker.setRetrySuccessful();
        retryChecker.setRetryUnsuccessful();

        //Assert
        assertDoesNotThrow(retryChecker::setRetryUnsuccessful);
    }

    @Test
    public void reset_resetsLimit() {
        //Arrange
        ReductiveWalkerRetryChecker retryChecker = new ReductiveWalkerRetryChecker(2);

        //Act
        retryChecker.setRetryUnsuccessful();
        retryChecker.setRetryUnsuccessful();
        retryChecker.reset();
        retryChecker.setRetryUnsuccessful();
        retryChecker.setRetryUnsuccessful();

        //Assert
        assertThrows(RetryLimitReachedException.class, retryChecker::setRetryUnsuccessful);
    }

    @Test
    public void reset_resetsGuaranteeOfSuccess() {
        //Arrange
        ReductiveWalkerRetryChecker retryChecker = new ReductiveWalkerRetryChecker(2);

        //Act
        retryChecker.setRetrySuccessful();
        retryChecker.reset();
        retryChecker.setRetryUnsuccessful();
        retryChecker.setRetryUnsuccessful();

        //Assert
        assertThrows(RetryLimitReachedException.class, retryChecker::setRetryUnsuccessful);
    }
}
