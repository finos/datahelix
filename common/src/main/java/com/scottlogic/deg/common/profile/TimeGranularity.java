package com.scottlogic.deg.common.profile;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeGranularity implements Granularity<LocalDateTime>{

    private final ChronoUnit granularity;

    public TimeGranularity(ChronoUnit granularity) {this.granularity = granularity;}

    @Override
    public boolean isCorrectScale(LocalDateTime value) {
        throw new NotImplementedException();
    }

    @Override
    public Granularity<LocalDateTime> merge(Granularity<LocalDateTime> otherGranularity) {
        throw new NotImplementedException();
    }

    @Override
    public LocalDateTime getNext(LocalDateTime value, int amount) {
        throw new NotImplementedException();
    }

    @Override
    public LocalDateTime trimToGranularity(LocalDateTime value) {
        throw new NotImplementedException();
    }

    @Override
    public LocalDateTime getPrevious(LocalDateTime value) {
        throw new NotImplementedException();
    }

    @Override
    public LocalDateTime getRandom(LocalDateTime min, LocalDateTime max, RandomNumberGenerator randomNumberGenerator) {
        throw new NotImplementedException();
    }
}
