Feature: User can specify that a value either matches or contains a specified regex

  Background:
    Given the generation strategy is full

  Scenario: User using matchingRegex operator to provide an exact set of values
    Given there is a field foo
    And foo is matching regex /a{1,3}/
    And foo is anything but null
    Then the following data should be generated:
      | foo   |
      | "a"   |
      | "aa"  |
      | "aaa" |