package com.scottlogic.deg.common.profile.constraintdetail;

/**
 * Exception to signify that the specified constraint type cannot be violated.
 */
public class UnviolatableConstraintException extends RuntimeException {
    public UnviolatableConstraintException(String message) {
        super(message);
    }
}
