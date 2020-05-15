## Beta Feature - Relational data

**Relational data** can only be produced when the output format is JSON. Output to console (streaming) and output to file are both supported. An attempt to produce CSV data where there is a relationship will result in an error. Profiles without relationships can still produce CSV data.

This feature introduces the following **additional functionality**. The changes are fully backward compatible. A new property can be added to the profile called `relationships`, this is a collection of 'relationship descriptions'.

For example

```
{
    "fields": [ ... ],
    "constraints": [ ... ],
    "relationships": [
        ...
    ]
}
```

A relationship represents additional data that should be included in the output data as a sub-object (one-to-one) or sub-array (one-to-many). Each relationship is considered to be one-to-one, unless extents (the number of sub-objects to produce) are included.

## Relationship description
There can be any number of relationships within each profile. Sub profiles can also contain relationships.

A relationship description can have the following properties

| property | type | requirement | description |
| ---- | ---- | ---- | ---- |
| `name` | string | required | The name of the relationship, this is the name of the property in which the related data will be embedded |
| `description` | string | optional | A description for the relationship, this is for documentation only - it has no bearing on the generation process | 
| `profileFile` | string | optional* | A relative path to a profile file, that describes the data that should be generated within this relationship |
| `profile` | object | optional* | that describes the data that should be generated within this relationship |
| `extents` | array of `extents` | optional | the min and max number of records within a one-to-many relationship, if absent will be treated as a one-to-one relationship |

For example:
```
{
  ...
  "relationships": [ 
    {
      "name": "name",
      "profileFile": "profile-filename",
      "extents": [
          { "field": "min", "equalTo": 1 },
          { "field": "max", "equalTo": 3 }
      ],
    }
  ]
}
```

\* One of `profile` or `profileFile` must be supplied, `profile` takes precedence over `profileFile`.

**NOTE**: If `profileFile` is used, it is possible to create a circular/recursive 'dependency' on profiles. No current strategy has been implemented to prevent/detect this, however it should be simple to integrate.

An `extent` is a description of the limit on the number of rows that must be produced, it is described as a regular - numeric - constraint, but for a virtual field called `min` or `max`, as in the example above. Any constraint that results in a integer value can be used.

**If any extents are supplied** then a constraint for the `max` extent must be supplied. The `min` extent is 0 by default, but can be overridden with any &gt;= 0 value. Conditional constraints are supported.

For one-to-many relationships a random number of records will be produced between `min` and `max`. Other strategies can be introduced in the fullness of time, this is the only approach at present.

## Example profile:

```
{
  "fields": [
    {
      "name": "shortName",
      "type": "faker.name.firstName",
      "nullable": false
    }
  ],
  "constraints": [
    {
      "field": "shortName",
      "shorterThan": 6
    },
    {
      "field": "shortName",
      "matchingRegex": "J.*"
    }
  ],
  "relationships": 
  [ 
    {
        "name": "dependants",
        "description": "presence of min/max indicates that it is a collection (one-to-many)",
        "profileFile": "dependants.profile.json",
        "extents": [
            { "field": "min", "equalTo": 0 },
            {
              "if": { "field": "shortName", "matchingRegex": "J[ae].*" },
              "then": { "field": "min", "equalTo": 2 },
              "else": { "field": "max", "equalTo": 3 }
            },
            { "field": "max", "equalTo": 3 },
            {
              "if": { "field": "shortName", "matchingRegex": "J[iou].*" },
              "then": { "field": "max", "equalTo": 1 }
            }
        ]
    },
    {
        "name": "mother",
        "description": "absence of min/max indicates that it is a sub-object (one-to-one)",
        "profile": {
            "fields": [
              {
                "name": "name",
                "type": "faker.name.firstName",
                "nullable": false
              },
              {
                "name": "age",
                "type": "integer",
                "nullable": false
              }
            ],
            "constraints": [
              {
                "field": "age",
                "greaterThanOrEqualTo": 16
              },
              {
                "field": "age",
                "lessThanOrEqualTo": 100
              }
            ]
        }
    }
  ]
}
```

This will produce data that looks like below:

```
{"mother":{"age":52,"name":"Vivien"},"shortName":"Jeri","dependants":[{"age":9,"name":"Ferne"},{"age":4,"name":"Eleonor"},{"age":8,"name":"Virgilio"}]}
{"mother":{"age":61,"name":"Dante"},"shortName":"James","dependants":[{"age":0,"name":"Elli"},{"age":2,"name":"Jewel"}]}
{"mother":{"age":24,"name":"Lana"},"shortName":"Jame","dependants":[{"age":3,"name":"Long"},{"age":9,"name":"Vergie"}]}
{"mother":{"age":67,"name":"Angelica"},"shortName":"Jan","dependants":[{"age":14,"name":"Wyatt"},{"age":3,"name":"Charline"}]}
{"mother":{"age":17,"name":"Hal"},"shortName":"Jamel","dependants":[{"age":5,"name":"Aurora"},{"age":3,"name":"Elisa"}]}
{"mother":{"age":38,"name":"Connie"},"shortName":"John","dependants":[{"age":14,"name":"Antonio"}]}
{"mother":{"age":88,"name":"Shae"},"shortName":"Jamey","dependants":[{"age":12,"name":"Enola"},{"age":6,"name":"Dania"}]}
{"mother":{"age":50,"name":"Aja"},"shortName":"Jodi","dependants":[]}
{"mother":{"age":24,"name":"Sherley"},"shortName":"James","dependants":[{"age":13,"name":"Thanh"},{"age":14,"name":"Stacey"},{"age":16,"name":"Dewayne"}]}
{"mother":{"age":66,"name":"Rosetta"},"shortName":"Joana","dependants":[]}
```

Note that data within `mother` is:
- A sub-object - because no `extents` were provided in the relationship
- Is embedded within the property `mother` as that is the `name` of the relationship

Note that data within `dependants` is:
- A sub-array - because there are `extents` provided in the relationship
- Is embedded within the property `dependants` as that is the `name` of the relationship
- Sometimes an empty array, as the minimum extent is 0
- Various sizes - the number of records to produce is a random number between `min` and `max` at generation time.

### What it can do
- Represent sub-objects (one-to-one)
- Represent sub-arrays (one-to-many)
- Represent n-levels of sub-objects and sub-arrays

### What it cannot do (yet)
- Constrain objects in one relationship by values in another, i.e.
  - if `children` sub-objects have some `age`s &gt; 18 then `placesLived` must contain a `number-of-rooms` &gt;= 3 
- Constrain the parent object by values in a relationship, i.e.
  - if `children` sub-objects have all `age`s &gt; 18 then `councilTaxRateBand` = `A`
- Constrain circular relationships to a maximum depth, i.e.
  - Generate a tree of ancestors (or conversely children) from a given person (aka genealogical data)