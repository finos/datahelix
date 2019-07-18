# Creating a Profile

This page will walk you through creating basic profiles with which you can generate data.

[Profiles](../ProfileDeveloperGuide.md) are JSON documents consisting of three sections, the schema version, the list 
of fields and the rules.

- **Schema Version** - Dictates the method of serialisation of the profile in order for the generator to 
interpret the profile fields and rules. The latest version is 0.1.
```
    "schemaVersion": "0.1",
```
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
- **Rules** - an array of constraints defined with a description. Constraints reduce the data in each column from the [universal set](../../generator/docs/SetRestrictionAndGeneration.md)
to the desired range of values. They are formatted as JSON objects. There are three types of constraints: 

    - [Predicate Constraints](../ProfileDeveloperGuide.md#Predicate-constraints) - predicates that define any given value as being 
    _valid_ or _invalid_
    - [Grammatical Constraints](../ProfileDeveloperGuide.md#Grammatical-constraints) - used to combine or modify other constraints
    - [Presentational Constraints](../ProfileDeveloperGuide.md#Presentational-constraints) - used by output serialisers where
     string output is required 
     
Here is a list of two rules comprised of one constraint each:
    
```
    "rules": [
        {
          "rule": "Column 1 is a string",
          "constraints": [
            {
              "field": "Column 1",
              "is": "ofType",
              "value": "string"
            }
          ]
        },
        {
          "rule": "Column 2 is a number",
          "constraints": [
            {
              "field": "Column 2",
              "is": "ofType",
              "value": "integer"
            }
          ]
        }
      ]

```


These three sections are combined to form the [complete profile](ExampleProfile1.json).

## Further Information 
* More detail on key decisions to make while constructing a profile can be found [here](../KeyDecisions.md)
* FAQs about constraints can be found [here](../FrequentlyAskedQuestions.md)
* For a larger profile example see [here](../Schema.md)
* Sometimes constraints can contradict one another, click [here](../../generator/docs/Contradictions.md) to find out what happens in these cases

#

[Contents](StepByStepInstructions.md) | [Next Section >](GeneratingData.md)
