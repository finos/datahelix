package com.scottlogic.deg.schemas.v3;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scottlogic.deg.schemas.common.BaseProfile;

import javax.xml.bind.annotation.XmlTransient;
import java.util.Collection;

public class V3Profile extends BaseProfile {
    public static final String SchemaVersion = "v3";

    public Collection<Field> fields;
    public Collection<Constraint> constraints;

    public V3Profile() {
        super(SchemaVersion);
    }
}

/*
Example:

{
    "schemaVersion": "v3",
	"fields": [
		{ "name": "id" },
		{ "name": "time" },
		{ "name": "country" },
		{ "name": "tariff" },
		{ "name": "low_price" },
		{ "name": "high_price" }
	],
	"constraints": [
		{ "field": "id", "type": "isOfType", "value": "temporal" },
		{ "field": "id", "type": "not isNull" },

		{ "field": "low_price", "type": "isOfType", "value": "numeric" },
		{ "field": "low_price", "type": "not isNull" },
		{ "field": "low_price", "type": "isGreaterThanOrEqual", "value": "0" },

		{ "field": "country", "type": "isInSet", "values": [ "USA", "GB", "FRANCE" ] },

		{
			"type": "conditional",
			"condition": {
				"type": "or",
				"constraints": [
					{ "field": "type", "type": "isEqualTo", "value": "USA" },
					{ "field": "type", "type": "isNull" }
				]
			},
			"then": { "field": "tariff", "type": "isNull" },
			"else": { "field": "tariff", "type": "not isNull" }
		}
	]
}

*/