package com.scottlogic.deg.generator.restrictions;

public class MergeResult<T> {

    private static final MergeResult<?> UNSUCCESSFUL = new MergeResult<>();

    public final T restrictions;
    public final boolean successful;

    public static <T> MergeResult<T> unsuccessful() {
        @SuppressWarnings("unchecked")
        MergeResult<T> result = (MergeResult<T>) UNSUCCESSFUL;
        return result;
    }

    public MergeResult(T restrictions) {
        this.restrictions = restrictions;
        this.successful = true;
    }

    private MergeResult() {
        this.restrictions = null;
        this.successful = false;
    }
}
