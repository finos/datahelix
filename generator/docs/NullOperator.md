# `null`, presence, absence and the empty set.

The `null` operator in a profile, expressed as `"is": "null"` or the negated equivalent has several meanings. It can mean (and emit the behaviour) as described below:

| Absence / Presence<br />Described in other technologies as nullability | Field value<br /> |
| ---- | ---- |
| (A) _null operator omitted_<br /> **The default**. The field's value may be absent or present | (B) `is null`<br />The field will have _no value_ |
| (C) `not(is null)`<br />The field's value must be present | (D) `not(is null)`<br />The field must have a value |

Therefore the null operator can:
- (C, D) `not(is null)` express fields that must have a value (otherwise known as a non-nullable field)
- (B) `is null` express fields as having no value (otherwise known as setting the value to `null`)
- (A) _By omitting the constraint_: express fields as permitting absence or presence of a value (otherwise known as a nullable field)

### `null` and interoperability
`null` is a keyword/term that exists in other technologies and languages, so far as this tool is concerned it relates to the absence or the presence of a value. See [set restriction and generation](SetRestrictionAndGenerarion.md) for more details.

When a field is serialised or otherwise written to a medium, as the output of the generator, it may choose to represent the absence of a value by using the formats' `null` representation, or some other form such as omitting the property and so on.

#### For illustrstion
CSV files don't support representing the absence of a value differently to an empty string (unless all strings are always wrapped in quotes ([#441](https://github.com/ScottLogic/data-engineering-generator/pull/441)). 

JSON files could be presented with `null` as the value for a property or excluding the property from the serialised result. This is the responsibility of the serialiser, and depends on the use cases.

## The `null` operator and the `if` constraint
With `if` constraints, the absence of a value needs to be considered in order to understand how the generator will behave. Remember, every set contains the empty set, unless excluded by way of the `not(is null)` constraint, for more details see [set restriction and generation](SetRestrictionAndGenerarion.md).

Consider the following if constraint:

```
{
    "if": {
        {
            "field": "field1",
            "is": "equalTo",
            "value": 5
        }
    },
    "then": {
        {
            "field": "field2",
            "is": "equalTo",
            "value": "a"
        }
    }
}
```

The generator will complete the `if` constraint as follows, to ensure the constraint is fully balanced:

```
{
    "if": {
        {
            "field": "field1",
            "is": "equalTo",
            "value": 5
        }
    },
    "then": {
        {
            "field": "field2",
            "is": "equalTo",
            "value": "a"
        }
    },
    "else": {
        {
            "not": {
                "field": "field1",
                "is": "equalTo",
                "value": 5
            }
        }
    }
}
```

This expression does not prevent the consequence (the `then` constraints) from being considered when `field1` has no value. Equally it does not say anything about the alternative consequence (the `else` constraints). As such both outcomes can be considered at any time.

The solution to this is to express the `if` constraint as follows. This is not 'auto completed' for profiles as it would remove functionality that may be intended, it must be explicitly included in the profile.

```
{
    "if": {
        "allOf": [
            {
                "field": "field1",
                "is": "equalTo",
                "value": 5
            },
            {
                "not": {
                    "field": "field1",
                    "is": "null"
                }
            }
        ]
    },
    "then": {
        {
            "field": "field2",
            "is": "equalTo",
            "value": "a"
        }
    }
}
```

The generator will complete the `if` constraint as follows, to ensure the constraint is fully balanced:

```
{
    "if": {
        "allOf": [
            {
                "field": "field1",
                "is": "equalTo",
                "value": 5
            },
            {
                "not": {
                    "field": "field1",
                    "is": "null"
                }
            }
        ]
    },
    "then": {
        {
            "field": "field2",
            "is": "equalTo",
            "value": "a"
        }
    },
    "else": {
        "anyOf": [
            {
                "not": {
                    "field": "field1",
                    "is": "equalTo",
                    "value": 5
                }
            },
            {
                "field": "field1",
                "is": "null"
            }
        ]
    }
}
```

If this case the `then` constraints will only be value when `field1` has a value, otherwise it will fire in either of the `else` cases, but nevertheless is confined to this scope.

### Examples:

* A field that is non-nullable (in SQL terminology)<br />
`field1 ofType string and field1 not(is null)`

* A field that is nullable (in SQL terminology)<br />
`field1 ofType string`

* A field that has no value (null in SQL terminology)<br />
`field1 is null`

### Violations
Violations are a special case for the `null` operator, see [Deliberate Violation](DeliberateViolation.md) for more details.