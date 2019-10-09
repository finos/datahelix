package com.scottlogic.deg.common.util.defaults;

import com.scottlogic.deg.common.profile.constraintdetail.Granularity;

public interface LinearDefaults<T extends Comparable>  {
    T min();
    T max();
    Granularity<T> granularity();
}
