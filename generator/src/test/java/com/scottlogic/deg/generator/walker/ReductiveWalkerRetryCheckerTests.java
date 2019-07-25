package com.scottlogic.deg.generator.walker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReductiveWalkerRetryCheckerTests {
    @Test
    public void retryChecker_withRepeatedFailure_throwsException() {
        //Arrange
        ReductiveWalkerRetryChecker retryChecker = new ReductiveWalkerRetryChecker(2);

        //Act
        retryChecker.retryUnsuccessful();
        retryChecker.retryUnsuccessful();

        //Assert
        assertThrows(RetryLimitReachedException.class, retryChecker::retryUnsuccessful);
    }

    @Test
    public void retryChecker_withRepeatedFailureAfterSuccess_doesNotThrowException() {
        //Arrange
        ReductiveWalkerRetryChecker retryChecker = new ReductiveWalkerRetryChecker(2);

        //Act
        retryChecker.retryUnsuccessful();
        retryChecker.retrySuccessful();
        retryChecker.retryUnsuccessful();

        //Assert
        assertDoesNotThrow(retryChecker::retryUnsuccessful);
    }

    @Test
    public void reset_resetsLimit() {
        //Arrange
        ReductiveWalkerRetryChecker retryChecker = new ReductiveWalkerRetryChecker(2);

        //Act
        retryChecker.retryUnsuccessful();
        retryChecker.retryUnsuccessful();
        retryChecker.reset();
        retryChecker.retryUnsuccessful();
        retryChecker.retryUnsuccessful();

        //Assert
        assertThrows(RetryLimitReachedException.class, retryChecker::retryUnsuccessful);
    }

    @Test
    public void reset_resetsGuaranteeOfSuccess() {
        //Arrange
        ReductiveWalkerRetryChecker retryChecker = new ReductiveWalkerRetryChecker(2);

        //Act
        retryChecker.retrySuccessful();
        retryChecker.reset();
        retryChecker.retryUnsuccessful();
        retryChecker.retryUnsuccessful();

        //Assert
        assertThrows(RetryLimitReachedException.class, retryChecker::retryUnsuccessful);
    }
}
