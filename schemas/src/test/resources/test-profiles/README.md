# Manual DataHelix Profile Schema Testing

1. `profile-test-empty.json` - _invalid_\
    This tests that an empty but valid json file fails validation
    
1. `profile-test-fields-array-empty.json` - _invalid_\
   shows that the fields array must contain at least one field
        
1. `profile-test-missing-constraint.json` - _invalid_\
    shows that a rule must contain at least one constraint

1. `profile-test-no-constraints.json` - _valid_\
    verifies that we can have an unconstrained profile. i.e one with no rules
    
1. `profile-test-no-fields.json` - _invalid_\
    This profile has a valid `schemaVersion` and `rules` section, but is missing the **`fields`** section
    
1. `profile-test-no-rules.json` - _invalid_\
    This profile has a valid `schemaVersion` and `fields` section, but is missing the **`rules`** section
    
1. `profile-test-no-version.json` - _invalid_\
    This profile has a valid `rules` and `fields` section, but is missing the **`schemaVersion`** section
    
1. `profile-test-rule-array-empty.json` - _valid_\
    shows that we can have a rules array that contains no elements 
    
1. `profile-test-simple-anyof-allof.json` - _valid_\
    Shows that `anyOf` and `allOf` can contain a list is simple constraints
    
1. `profile-test-simple-anyof-allof-errors.json` - _invalid_\
    test that `field - is - value` has the correct structure inside anyOf and allOf
    
1. `profile-test-simple-data-constraints.json` - _valid_\
    This profile shows that the simple data constraints (e.g. `"field":"name", "is": "longerThan", "value":  7`) validate correctly
    
1. `profile-test-simple-data-constraints-errors.json` - _invalid_\
    proves that the\
    * schemaVersion can only be included once
    * fieldnames need to be unique in the `fields` array
    * constraint object cannot be empty
    * `field - is - value` has the correct structure
    * `field - is - null` does not have a `value`
    
1. `profile-test-simple-data-constraints-nested-1level.json` - _valid_\
    proves that constraints can be nested one level deep inside an `anyOf` and `allOf`
    
1. `profile-test-simple-data-constraints-nested-2level.json` - _valid_\
    proves that constraints can be nested two level deep inside an `anyOf` and `allOf`
    
1. `profile-test-simple-data-constraints-nested-7level.json` - _valid_\
    proves that constraints can be nested seven level deep inside an `anyOf` and `allOf`

1. `profile-test-simple-data-constraints-not.json` - _valid_\
    shows that the simple data constraints can by negated by including them in a `not`
    
1. `profile-test-simple-if.json` - _valid_\
    proves that simple `if -then` and `if - then - else` validate 
    
1. `profile-test-simple-if-errors.json` - _invalid_\
    proves that the structure of an `if` must meet the specifications e.g. only one `else` clause, `if - else` without a `then` etc...
    
1. `profile-test-simple-inset.json` - _valid_\
    proves that `field - is - inset` can contain strings, numbers or datetimes and any combination of the three
        
1. `profile-test-simple-regex.json` - _valid_\
    proves that regex strings are validated
    
1. `profile-test-simple-regex-errors.json` - _invalid_\
    prove that numbers and dates are not allowed as a regex value
    
1. `profile-test-simple-datetimes.json` - _valid_\
    proves that valid datetime constraints are allowed
    
1. `profile-test-simple-datetimes-errors.json` - _invalid_\
    proves that invalid datetime constraints are not allowed


