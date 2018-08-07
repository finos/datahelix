package com.scottlogic.deg.schemas.v3;

import com.scottlogic.deg.schemas.common.BaseProfile;

import java.util.Collection;

// Documentation is available at:
// https://scottlogic.atlassian.net/wiki/spaces/DEG/pages/693043318/Schema+Version+3

public class V3ProfileDTO extends BaseProfile {
    public static final String SchemaVersion = "v3";

    public Collection<FieldDTO> fields;
    public Collection<RuleDTO> rules;

    public V3ProfileDTO() {
        super(SchemaVersion);
    }
}
