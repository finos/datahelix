package com.scottlogic.deg.generator.restrictions;

public class MergeResult<T> {
    public final T restrictions;
    public final boolean successful;

    public MergeResult(T restrictions) {
        this(restrictions, true);
    }

    public MergeResult() {
        this(null, false);
    }

    public MergeResult(T restrictions, boolean successful) {
        this.restrictions = restrictions;
        this.successful = successful;
    }
}
