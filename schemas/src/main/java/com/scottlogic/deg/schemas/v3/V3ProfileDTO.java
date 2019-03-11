package com.scottlogic.deg.schemas.v3;

import com.scottlogic.deg.schemas.common.BaseProfile;

import java.util.Collection;

public class V3ProfileDTO extends BaseProfile {
    public static final String SchemaVersion = "0.1";

    public Collection<FieldDTO> fields;
    public Collection<RuleDTO> rules;
    public String description;

    public V3ProfileDTO() {
        super(SchemaVersion);
    }
}
