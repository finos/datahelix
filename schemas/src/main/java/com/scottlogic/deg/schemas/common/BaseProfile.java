package com.scottlogic.deg.schemas.common;

import static com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.scottlogic.deg.schemas.v2.V2Profile;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "schemaVersion"
)
@JsonSubTypes({
    @Type(value = V2Profile.class, name = V2Profile.SchemaVersion)
})
public abstract class BaseProfile {
    public String schemaVersion;

    protected BaseProfile(String schemaVersion)
    {
        this.schemaVersion = schemaVersion;
    }
}
