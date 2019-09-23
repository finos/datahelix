Feature: Correct Constraint Types, validation exceptions should be raised if the wrong typed constraint is applied to a field

Scenario: Running a 'inSet' request alongside a contradicting ofType = string should produce null
Given there is a field foo
And foo has type "string"
And foo is in set:
| 1 |
| 2 |
| 3 |
And foo has type "string"
Then the profile is invalid because "Field \[foo\]: is type STRING , but you are trying to apply a inSet constraint which requires NUMERIC"
