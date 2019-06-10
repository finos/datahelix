package com.scottlogic.deg.common.output;

import com.scottlogic.deg.common.profile.Field;

/** A set of values representing one complete, discrete output (eg, this could be used to make a full CSV row) */
public interface GeneratedObject {
    Object getFormattedValue(Field field);
}
