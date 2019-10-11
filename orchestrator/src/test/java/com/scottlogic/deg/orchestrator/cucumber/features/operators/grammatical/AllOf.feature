Feature: User can specify that data must be created to conform to each of multiple specified constraints.

  Background:
    Given the generation strategy is full

  Scenario: Running an 'allOf' request that contains a valid nested allOf request should be successful
    Given there is a field foo
    And All Of the next 2 constraints
    And All Of the next 2 constraints
      And foo is matching regex "[a-b]{2}"
      And foo is of length 2
    And foo is shorter than 3
    And foo has type "string"
    Then the following data should be generated:
      | foo  |
      | "aa" |
      | "ab" |
      | "bb" |
      | "ba" |
      | null |

  Scenario: Running an 'allOf' request that contains an invalid nested allOf request should generate null
    Given there is a field foo
    And foo has type "string"
    And All Of the next 2 constraints
    And All Of the next 2 constraints
      And foo is matching regex "[a-k]{3}"
      And foo is matching regex "[1-5]{3}"
    And foo is longer than 4
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running a 'allOf' request that includes multiple values within the same statement should be successful
    Given there is a field foo
    And foo has type "string"
    And All Of the next 2 constraints
    And foo is equal to "Test01"
    And foo is equal to "Test01"
    Then the following data should be generated:
      | foo      |
      | "Test01" |

  Scenario: User attempts to combine two constraints that only intersect at the empty set within an allOf operator should not generate data
    Given there is a field foo
    And foo has type "string"
    And All Of the next 2 constraints
    And foo is equal to "Test01"
    And foo is equal to "5"
    Then no data is created
