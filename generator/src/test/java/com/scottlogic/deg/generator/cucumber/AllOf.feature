Feature: User can specify that data must be created to conform to each of multiple specified constraints.

Background:
     Given the generation strategy is full

Scenario: Running an 'allOf' request that contains a valid nested allOf request should be successful
     Given there is a field foo
       And there is a constraint:
       """
         { "allOf": [
           { "allOf": [
             { "field": "foo", "is": "matchingRegex", "value": "[a-k]{3}" },
             { "field": "foo", "is": "ofLength", "value": 3 }
           ]},
           { "field": "foo", "is": "ofType", "value": "string" }
         ]}
       """
     Then the following data should be included in what is generated:
       | foo   |
       | "abc" |
       | null  |

  # failing - generates duplicate nulls linked to issue 343 should have been fixed with 354
Scenario: Running an 'allOf' request that contains a valid nested anyOf request should be successful
     Given there is a field foo
       And foo is of type "string"
       And there is a constraint:
       """
         { "allOf": [
           { "anyOf": [
             { "field": "foo", "is": "ofLength", "value": 1 },
             { "field": "foo", "is": "ofLength", "value": 2 }
           ]},
           { "field": "foo", "is": "matchingRegex", "value": "[1]{1,2}" }
         ]}
       """
     Then the following data should be generated:
       | foo  |
       | "1"  |
       | "11" |
       | null |

  # failing should be fixed with ticket 437
Scenario: Running an 'allOf' request that contains an invalid nested allOf request should generate null
     Given there is a field foo
       And there is a constraint:
       """
         { "allOf": [
           { "allOf": [
             {"field": "foo", "is": "matchingRegex", "value": "[a-k]{3}" },
             {"field": "foo", "is": "matchingRegex", "value": "[1-5]{3}" }
           ]},
           { "field": "foo", "is": "ofType", "value": "string" }
         ]}
       """
     Then the following data should be generated:
        | foo  |
        | null |

  #failing - looks to be part of issue 91
Scenario: Running an 'allOf' request that contains soft contradictory restraints in a nested anyOf request should be successful
     Given there is a field foo
       And foo is in set:
        |   5   |
        | "ack" |
       And there is a constraint:
       """
         { "allOf": [
           { "anyOf": [
             {"field": "foo", "is": "matchingRegex", "value": "[a-z]{3}" },
             {"field": "foo", "is": "matchingRegex", "value": "[a-k]{3}" }
           ]},
           { "field": "foo", "is": "ofType", "value": "numeric" },
           { "field": "foo", "is": "equalTo", "value": 5}
         ]}
       """
     Then the following data should be generated:
       | foo  |
       |  5   |
       | null |

Scenario: Running a 'allOf' request that includes multiple values within the same statement should be successful
     Given there is a field foo
       And foo is of type "string"
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

Scenario: User attempts to combine two constraints that only intersect at the empty set within an allOf operator should generate null
    Given there is a field foo
    And there is a constraint:
    """
      { "allOf": [
         { "field": "foo", "is": "equalTo", "value": "Test0" },
         { "field": "foo", "is": "equalTo", "value": 5 }
      ]}
    """
    Then the following data should be generated:
       | foo  |
       | null |

Scenario: Numeric value using the allOf operator
     Given there is a field foo
       And there is a constraint:
       """
         { "allOf": [
            { "field": "foo", "is": "ofType", "value": "numeric" },
            { "field": "foo", "is": "equalTo", "value": 5 }
         ]}
       """
     Then the following data should be generated:
       | foo  |
       | null |
       |  5   |
