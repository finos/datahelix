Feature: Hard contradictions produce nothing, soft contradictions produce only null.

Background:
  Given the generation strategy is RANDOM
  And there is a field foo

### Hard Contradictions ###
Scenario: Contradicting types, disallowing 'null', should not produce any data.
  Given foo is anything but null
  And foo is of type "string"
  And foo is of type "integer"
  Then no data is created

@ignore #issue #817
Scenario: Contradicting inferred and actual types should not produce any data.
  Given foo is anything but null
  And foo is of type "integer"
  And foo is less than 1
  And foo is greater than 0
  Then no data is created

### Soft Contradictions ###
Scenario: Contradicting types, but allowing 'null', should produce only 'null' values.
  Given foo is of type "string"
    And foo is of type "integer"
    And the generator can generate at most 2 rows
  Then the following data should be generated:
    | foo  |
    | null |
    | null |
