package com.scottlogic.deg.generator.inputs.validation.messages;


public class GranularityConstraintValidationMessages implements StandardValidationMessages {


    private int originalGranularity;
    private int candidateGranularity;

    public GranularityConstraintValidationMessages(int originalGranularity, int candidateGranularity) {

        this.originalGranularity = originalGranularity;
        this.candidateGranularity = candidateGranularity;
    }

    @Override
    public String getVerboseMessage() {
        return String.format(
            "Detected two granularities for the same field: %s and %s decimal places. The smallest value will be selected.",
            originalGranularity,
            candidateGranularity);
    }
}

