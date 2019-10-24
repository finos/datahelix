package com.scottlogic.deg.common.profile;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimeGranularity implements Granularity<LocalTime>{

    private final ChronoUnit granularity;

    public TimeGranularity(ChronoUnit granularity) {this.granularity = granularity;}

    @Override
    public boolean isCorrectScale(LocalTime value) {
        throw new NotImplementedException();
    }

    @Override
    public Granularity<LocalTime> merge(Granularity<LocalTime> otherGranularity) {
        throw new NotImplementedException();
    }

    @Override
    public LocalTime getNext(LocalTime value, int amount) {
        throw new NotImplementedException();
    }

    @Override
    public LocalTime trimToGranularity(LocalTime value) {
        throw new NotImplementedException();
    }

    @Override
    public LocalTime getPrevious(LocalTime value) {
        throw new NotImplementedException();
    }

    @Override
    public LocalTime getRandom(LocalTime min, LocalTime max, RandomNumberGenerator randomNumberGenerator) {
        throw new NotImplementedException();
    }
}
