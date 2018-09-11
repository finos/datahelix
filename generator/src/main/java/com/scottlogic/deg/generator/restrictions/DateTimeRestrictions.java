package com.scottlogic.deg.generator.restrictions;

import java.time.LocalDateTime;

public class DateTimeRestrictions {
    public DateTimeLimit min;
    public DateTimeLimit max;

    public static boolean isDateTime(Object o){
        return o instanceof LocalDateTime;
    }

    public boolean match(Object o) {
        if(!DateTimeRestrictions.isDateTime(o)){
            return false;
        }

        LocalDateTime d = (LocalDateTime) o;

        if(min != null){
            if(d.compareTo(min.getLimit()) < (min.isInclusive() ? 0 : 1))
            {
                return false;
            }
        }

        if(max != null){
            if(d.compareTo(max.getLimit()) > (max.isInclusive() ? 0 : -1))
            {
                return false;
            }
        }

        return true;
    }

    public static class DateTimeLimit {
        private final LocalDateTime limit;
        private final boolean inclusive;

        public DateTimeLimit(LocalDateTime limit, boolean inclusive) {
            this.limit = limit;
            this.inclusive = inclusive;
        }

        public LocalDateTime getLimit() {
            return limit;
        }

        public boolean isInclusive() {
            return inclusive;
        }
    }
}
