Feature: Values can be specified by using any of to set multiple constraints

  Background:
    Given the generation strategy is full
    And there is a field foo

  Scenario: User requires to create a field with strings that conform to one or many constraints
    Given there is a constraint:
      """
      { "anyOf": [
        { "field": "foo", "is": "equalTo", "value": "Test0" },
        { "field": "foo", "is": "inSet", "values": ["Test1", "Test2", "Test3", "Test4", "Test5"] },
        { "field": "foo", "is": "matchingRegex", "value": "[a-b]{4}" }
      ]}
      """
    And foo has type "string"
    And foo is anything but null
    Then the following data should be generated:
      | foo     |
      | "Test0" |
      | "Test1" |
      | "Test2" |
      | "Test3" |
      | "Test4" |
      | "Test5" |
      | "aaaa"  |
      | "aaba"  |
      | "aabb"  |
      | "aaab"  |
      | "abaa"  |
      | "abba"  |
      | "abbb"  |
      | "abab"  |
      | "baaa"  |
      | "baba"  |
      | "babb"  |
      | "baab"  |
      | "bbaa"  |
      | "bbba"  |
      | "bbbb"  |
      | "bbab"  |

  Scenario: When user requires creation of a field with strings that contain multiple contradictory sets of one or many constraints no data should be generated
    Given there is a constraint:
      """
      { "anyOf": [
        { "field": "foo", "is": "equalTo", "value": "Test0" },
        { "field": "foo", "is": "inSet", "values": ["Test1", "Test2", "Test3", "Test4", "Test5"] },
        { "field": "foo", "is": "matchingRegex", "value": "[a-b]{4}" }
      ]}
      """
    And there is a constraint:
      """
      { "anyOf": [
        { "field": "foo", "is": "equalTo", "value": "Test6" },
        { "field": "foo", "is": "inSet", "values": ["Test7", "Test8", "Test9"] }
      ]}
      """
    And foo has type "string"
    And foo is anything but null
    Then no data is created

  Scenario: Running an 'anyOf' request that contains a valid nested anyOf request should be successful
    Given there is a constraint:
      """
      { "anyOf": [
        { "field": "foo", "is": "ofLength", "value": 1 },
        { "anyOf":  [
          { "field": "foo", "is": "ofLength", "value": 3 },
          { "field": "foo", "is": "ofLength", "value": 5 }
        ]}
      ]}
      """
    And foo is in set:
      | "1"     |
      | "22"    |
      | "333"   |
      | "4444"  |
      | "55555" |
    And foo has type "string"
    And foo is anything but null
    Then the following data should be generated:
      | foo     |
      | "1"     |
      | "333"   |
      | "55555" |
    And the following data should not be included in what is generated:
      | "22"   |
      | "4444" |

  Scenario: Running an 'anyOf' request that contains a valid nested allOf request should be successful
    Given there is a constraint:
      """
      { "anyOf": [
        { "field": "foo", "is": "ofLength", "value": 1 },
        { "allOf": [
          { "field": "foo", "is": "longerThan", "value": 3 },
          { "field": "foo", "is": "shorterThan", "value": 5 }
        ]}
      ]}
      """
    And foo is in set:
      | "1"     |
      | "22"    |
      | "333"   |
      | "4444"  |
      | "55555" |
    And foo has type "string"
    And foo is anything but null
    Then the following data should be generated:
      | foo    |
      | "1"    |
      | "4444" |
    And the following data should not be included in what is generated:
      | "22"    |
      | "333"   |
      | "55555" |

  Scenario: Running an 'anyOf' request that contains an invalid nested anyOf request should fail with an error message
    Given there is a constraint:
      """
      { "anyOf": [
        { "field": "foo", "is": "ofLength", "value": 1 },
        { "anyOf": [
          { "field": "foo", "is": "ofLength", "value": -1 }
        ]}
      ]}
      """
    And foo has type "string"
    And foo is anything but null
    Then the profile is invalid because "Field \[foo\]: ofLength constraint must have an operand/value >= 0, currently is -1"
    And no data is created

  Scenario: Running an 'anyOf' request that contains an invalid nested allOf request should fail with an error message
    Given there is a constraint:
      """
      { "anyOf": [
        { "field": "foo", "is": "ofLength", "value": 1 },
        { "allOf": [
          { "field": "foo", "is": "ofLength", "value": -1 }
        ]}
      ]}
      """
    And foo has type "string"
    And foo is anything but null
    Then the profile is invalid because "Field \[foo\]: ofLength constraint must have an operand/value >= 0, currently is -1"
    And no data is created

  Scenario: Running an 'anyOf' request that contains an valid nested allOf request should generate data
    Given there is a constraint:
      """
      { "anyOf": [
        { "field": "foo", "is": "ofLength", "value": 1 },
        { "allOf": [
          { "field": "foo", "is": "longerThan", "value": 3 },
          { "field": "foo", "is": "shorterThan", "value": 2 }
        ]}
      ]}
      """
    And foo has type "string"
    And foo is in set:
      | "a"  |
      | "aa" |
      | "9"  |
      | "a1" |
      | "B"  |
    And foo is anything but null
    Then the following data should be generated:
      | foo |
      | "a" |
      | "9" |
      | "B" |
