package com.scottlogic.deg.generator.config.detail;

public enum VisualiserLevel  {
    OFF,
    STANDARD,
    DETAILED;

    public boolean sameOrMoreVerboseThan(VisualiserLevel level) {
        return level == OFF || level == STANDARD && this != OFF || level == this;
    }


}
