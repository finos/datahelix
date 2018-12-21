package com.scottlogic.deg.generator.inputs.validation;

public interface VisitableProfileElement{
    void accept(ProfileVisitor visitor);
}
