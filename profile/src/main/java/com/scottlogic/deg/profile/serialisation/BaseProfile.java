Copyright 2019 Scott Logic Ltd /
/
Licensed under the Apache License, Version 2.0 (the \"License\");/
you may not use this file except in compliance with the License./
You may obtain a copy of the License at/
/
    http://www.apache.org/licenses/LICENSE-2.0/
/
Unless required by applicable law or agreed to in writing, software/
distributed under the License is distributed on an \"AS IS\" BASIS,/
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied./
See the License for the specific language governing permissions and/
limitations under the License.
package com.scottlogic.deg.profile.serialisation;

import static com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.scottlogic.deg.profile.v0_1.ProfileDTO;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "schemaVersion"
)
@JsonSubTypes({
    @Type(value = ProfileDTO.class, name = ProfileDTO.SchemaVersion)
})
public abstract class BaseProfile {
    @JsonIgnore // not sure why I have to do this - otherwise two instances are serialised. possibly because of the JsonTypeInfo above?
    public String schemaVersion;

    protected BaseProfile(String schemaVersion)
    {
        this.schemaVersion = schemaVersion;
    }
}
