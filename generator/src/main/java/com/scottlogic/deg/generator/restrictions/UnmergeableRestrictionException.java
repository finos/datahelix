package com.scottlogic.deg.generator.restrictions;

public class UnmergeableRestrictionException extends Exception {
    public UnmergeableRestrictionException () {
        super();
    }

    public UnmergeableRestrictionException (String message) {
        super(message);
    }

    public UnmergeableRestrictionException (Throwable cause) {
        super(cause);
    }

    public UnmergeableRestrictionException (String message, Throwable cause) {
        super(message, cause);
    }
}
