package com.scottlogic.deg.generator.config.detail;

public enum VisualiserLevel  {
    OFF {
        @Override
        public boolean sameOrHigherThan(VisualiserLevel level) {
            return level == this;
        }
    },
    STANDARD {
        @Override
        public boolean sameOrHigherThan(VisualiserLevel level) {
            return OFF.sameOrHigherThan(level) || level == this;
        }
    },
    DETAILED {
        @Override
        public boolean sameOrHigherThan(VisualiserLevel level) {
            return true;
        }
    };

    public abstract boolean sameOrHigherThan(VisualiserLevel level);


}
