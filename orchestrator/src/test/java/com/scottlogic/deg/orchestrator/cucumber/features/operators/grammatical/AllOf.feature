Feature: User can specify that data must be created to conform to each of multiple specified constraints.

  Background:
    Given the generation strategy is full

  Scenario: Running an 'allOf' request that contains a valid nested allOf request should be successful
    Given there is a field foo
    And foo has type "string"
    And there is a constraint:
      """
      { "allOf": [
        { "allOf": [
          { "field": "foo", "is": "matchingRegex", "value": "[a-b]{2}" },
          { "field": "foo", "is": "ofLength", "value": 2 }
        ]},
        { "field": "foo", "is": "shorterThan", "value": 3 }
      ]}
      """
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
    And there is a constraint:
      """
      { "allOf": [
        { "allOf": [
          {"field": "foo", "is": "matchingRegex", "value": "[a-k]{3}" },
          {"field": "foo", "is": "matchingRegex", "value": "[1-5]{3}" }
        ]},
        { "field": "foo", "is": "longerThan", "value": 4 }
      ]}
      """
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running a 'allOf' request that includes multiple values within the same statement should be successful
    Given there is a field foo
    And foo has type "string"
    And there is a constraint:
      """
      { "allOf": [
        { "field": "foo", "is": "equalTo", "value": "Test01" },
        { "field": "foo", "is": "equalTo", "value": "Test01" }
      ]}
      """
    Then the following data should be generated:
      | foo      |
      | "Test01" |

  Scenario: User attempts to combine two constraints that only intersect at the empty set within an allOf operator should not generate data
    Given there is a field foo
    And foo has type "string"
    And there is a constraint:
      """
      { "allOf": [
        { "field": "foo", "is": "equalTo", "value": "Test0" },
        { "field": "foo", "is": "equalTo", "value": "5" }
      ]}
      """
    Then no data is created
