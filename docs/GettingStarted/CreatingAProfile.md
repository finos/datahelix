#Creating a Profile

This guide will walk you through creating basic profiles with which you can generate data

[Profiles](../Profiles.md) are JSON documents consisting of three sections, the schema version, the list 
of fields and the rules.

- **Schema Version** - Dictates the method of serialisation of the profile in order for the generator to 
interpret the profile fields and rules. The latest version is v3.
```
{
    "schemaVersion": "v3",
```
- **List of Fields** - An array of column headings is defined with unique "name" keys.
```
    "fields": [
        {
            "name": "Column 1 Header"
        },
        {
            "name": "Column 2 Header"
        }
    ],
```
- **Rules** - Constraints are defined to reduce the data in each column from the universal set
to the desired range of values. There are three types of constraints: 
    - [Epistemic Constraints](../EpistemicConstraints.md) - predicates that define any given value as being 
    _valid_ or _invalid_
    - [Grammatical Constraints](../GrammaticalConstraints.md) - used to combine or modify other constraints
    - [Presentational Constraints](../PresentationalConstraints.md) - used by output serialisers where
     string output is required 
     
Rules are defined as an array of constraint objects and can be defined with or without a description:
    
```
    "rules": [
        {
            "rule": "Rule Description - column 1 and 2 are strings",
            "constraints": [
                {
                    "field": "Column 1 Header",
                    "is": "ofType",
                    "value": "string"
                },
                {
                    "field": "Column 2 Header",
                    "is": "ofType",
                    "value": "string"
                }
            ]
        }
    ]
}
```

This is equivalent to:
    
```
    "rules": [
        {
            "field": "Column 1 Header",
            "is": "ofType",
            "value": "string"
        },
        {
            "field": "Column 2 Header",
            "is": "ofType",
            "value": "string"
        }
    ]
}
```

These three sections are combined to form the [complete profile](ExampleProfile1.json).
