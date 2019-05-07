package com.scottlogic.deg.generator.restrictions;

public class MergeResult<T> {
    public final T restrictions;
    public final boolean successful;

    public final static MergeResult UNSUCCESSFUL = new MergeResult();

    public MergeResult(T restrictions) {
        this.restrictions = restrictions;
        this.successful = true;
    }

    private MergeResult() {
        this.restrictions = null;
        this.successful = false;
    }
}
