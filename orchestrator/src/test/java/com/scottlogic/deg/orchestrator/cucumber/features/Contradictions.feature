Feature: Hard contradictions produce nothing, soft contradictions produce only null.

  Background:
    Given the generation strategy is RANDOM
    And there is a field foo

### Hard Contradictions ###
  Scenario: Contradicting types, disallowing 'null', should not produce any data.
    Given foo is anything but null
    And foo has type "string"
    And foo has type "integer"
    Then no data is created

  Scenario: Contradicting inferred and actual types should not produce any data.
    Given foo is anything but null
    And foo has type "integer"
    And foo is less than 1
    And foo is greater than 0
    Then no data is created
