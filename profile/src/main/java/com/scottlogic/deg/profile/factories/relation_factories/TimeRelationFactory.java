package com.scottlogic.deg.profile.factories.relation_factories;

import com.scottlogic.deg.common.profile.Granularity;
import com.scottlogic.deg.common.profile.TimeGranularity;

public class TimeRelationFactory extends FieldSpecRelationFactory {

    @Override
    Granularity createGranularity(String offsetUnit) {
        return offsetUnit == null ? null : TimeGranularity.create(offsetUnit);
    }
}
