# Creating a Profile

This page will walk you through creating basic profiles with which you can generate data.

[Profiles](../UserGuide.md#profiles) are JSON documents consisting of two sections: the list of fields and the rules.

- **List of Fields** - An array of column headings is defined with unique "name" keys.
```
    "fields": [
        {
            "name": "Column 1"
        },
        {
            "name": "Column 2"
        }
    ]
```
- **Constraints** - Constraints reduce the data in each column from the [universal set](../SetRestrictionAndGeneration.md)
to the desired range of values. They are formatted as JSON objects. There are three types of constraints: 

    - [Predicate Constraints](../UserGuide.md#Predicate-constraints) - predicates that define any given value as being 
    _valid_ or _invalid_
    - [Grammatical Constraints](../UserGuide.md#Grammatical-constraints) - used to combine or modify other constraints
    - [Presentational Constraints](../UserGuide.md#Presentational-constraints) - used by output serialisers where
     string output is required 
     
Here is a list of two constraints:
    
```
          "constraints": [
            {
              "field": "Column 1",
              "is": "ofType",
              "value": "string"
            },
            {
              "field": "Column 2",
              "is": "ofType",
              "value": "integer"
            }
          ]

```


These three sections are combined to form the [complete profile](ExampleProfile1.json).

## Further Information 
* More detail on key decisions to make while constructing a profile can be found [here](../../developer/KeyDecisions.md)
* FAQs about constraints can be found [here](../FrequentlyAskedQuestions.md)
* For a larger profile example see [here](../Schema.md)
* Sometimes constraints can contradict one another, click [here](../Contradictions.md) to find out what happens in these cases

#

[Contents](StepByStepInstructions.md) | [Next Section >](GeneratingData.md)
