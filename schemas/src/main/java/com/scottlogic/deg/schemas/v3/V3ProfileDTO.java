package com.scottlogic.deg.schemas.v3;

import com.scottlogic.deg.schemas.common.BaseProfile;

import java.util.Collection;

public class V3ProfileDTO extends BaseProfile {
    public static final String SchemaVersion = "v3";

    public Collection<FieldDTO> fields;
    public Collection<RuleDTO> rules;

    public V3ProfileDTO() {
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
	"rules": [
        {
            "rule": "id is a non-nullable string",
            "constraints":
            [
                { "field": "id", "is": "ofType", "value": "string" },
		        { "not": { "field": "id", "is": "null" } }
            ]
        },

        {
            "rule": "low_price is a non-nullable positive integer",
            "constraints": [
                { "field": "low_price", "is": "ofType", "value": "numeric" },
                { "not": { "field": "low_price", "is": "null" } },
                { "field": "low_price", "is": "greaterThanOrEqual", "value": 0 }
			]
		},

		{ "field": "country", "is": "inSet", "values": [ "USA", "GB", "FRANCE" ] },

		{
			"if": {
				"anyOf": [
					{ "field": "type", "is": "equalTo", "value": "USA" },
					{ "field": "type", "is": "null" }
				]
			},
			"then": { "field": "tariff", "is": "null" },
			"else": { "not": { "field": "tariff", "is": "null" } }
		}
	]
}

*/