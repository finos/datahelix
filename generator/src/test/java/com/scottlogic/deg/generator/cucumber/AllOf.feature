Feature: User can specify that data must be created to conform to each of multiple specified constraints.

Background:
     Given the generation strategy is full

Scenario: Running an 'allOf' request that contains a valid nested allOf request should be successful
     Given there is a field foo
       And there is a constraint:
       """
         { "allOf": [
           { "field": "foo", "allOf": [
             { "field": "foo", "is": "matchingRegex", "value": "[a-k]{3}" },
             { "field": "foo", "is": "ofLength", "value": 3 }
           ]},
           { "field": "foo", "is": "ofType", "value": "string" }
         ]}
       """
     Then the following data should be included in what is generated:
       | foo   |
       | "abc" |

Scenario: Running an 'allOf' request that contains a valid nested anyOf request should be successful
     Given there is a field foo
       And there is a constraint:
       """
         { "allOf": [
           { "field": "foo", "anyOf": [
             { "field": "foo", "is": "ofLength", "value": 1 },
             { "field": "foo", "is": "ofLength", "value": 2 }
           ]},
           { "field": "foo", "is": "containingRegex", "value": "[1-9]{1}" }
         ]}
       """
     Then the following data should be included in what is generated:
       | foo  |
       | "1"  |
       | "11" |

Scenario: Running an 'allOf' request that contains an invalid nested allOf request should fail with an error message
     Given there is a field foo
       And there is a constraint:
       """
         { "allOf": [
           { "field": "foo", "allOf": [
             {"field": "foo", "is": "matchingRegex", "value": "[a-k]{3}" },
             {"field": "foo", "is": "matchingRegex", "value": "[1-5]{3}" }
           ]},
           { "field": "foo", "is": "ofType", "value": "string" }
         ]}
       """
     Then I am presented with an error message
       And no data is created

@ignore
Scenario: Running an 'allOf' request that contains an invalid nested anyOf request should fail with an error message
     Given there is a field foo
       And there is a constraint:
       """
         { "allOf": [
           { "field": "foo", "anyOf": [
             {"field": "foo", "is": "matchingRegex", "value": "[a-z]{3}" },
             {"field": "foo", "is": "matchingRegex", "value": "[a-k]{3}" }
           ]},
           { "field": "foo", "is": "ofType", "value": "numeric" }
         ]}
       """
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'allOf' request that includes multiple values within the same statement should be successful
     Given there is a field foo
       And there is a constraint:
       """
         { "allOf": [
           { "field": "foo", "is": "equalTo", "value": "Test01" },
           { "field": "foo", "is": "equalTo", "value": "Test01" }
         ]}
       """
     Then the following data should be included in what is generated:
       | foo      |
       | "Test01" |

  Scenario: User attempts to combine contradicting constraints within an allOf operator
    Given there is a field foo
    And there is a constraint:
    """
      { "allOf": [
         { "field": "foo", "is": "equalTo", "value": "Test0" },
         { "field": "foo", "is": "equalTo", "value": 5 }
      ]}
    """
    Then no data is created


  Scenario: Numeric value using the allOf operator
    Given there is a field price

    And there is a constraint:
    """
      { "allOf": [
         { "field": "price", "is": "ofType", "value": "numeric" },
         { "field": "price", "is": "equalTo", "value": 5 }
      ]}
    """

    Then the following data should be generated:
      | price |
      |  5   |


  Scenario: String value using the allOf operator
    Given there is a field foo

    And there is a constraint:
    """
      { "allOf": [
         { "field": "foo", "is": "equalTo", "value": "Test0" },
         { "field": "foo", "is": "ofType", "value": "string" }
      ]}
    """

    Then the following data should be generated:
      | foo   |
      | "Test0" |


