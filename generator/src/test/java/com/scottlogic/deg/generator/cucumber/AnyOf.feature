Feature: Values can be specified by using any of to set multiple constraints

Scenario: User requires to create a field with strings that conform to one or many constraints
     Given there is a field foo
       And there is a constraint:
       """
       { "anyOf": [
       { "field": "foo", "is": "equalTo", "value": "Test0" },
       { "field": "foo", "is": "inSet", "values": ["Test1", "Test2", "Test3", "Test4", "Test5"] },
       { "field": "foo", "is": null },
       { "field": "foo", "is": "matchingRegex", "value": "[a-b]{5}" }
       ]}
       """
       And foo is of type "string"
       And foo is of length 5
     Then the following data should be generated:
       | foo   |
       | null  |
       | Test0 |
       | Test1 |
       | Test2 |
       | Test3 |
       | Test4 |
       | Test5 |
       | aaaaa |
       | abaaa |
       | abbaa |
       | abbba |
       | abbbb |
       | aabaa |
       | aabba |
       | aabbb |
       | aaaba |
       | aaabb |
       | aaaab |
       | baaaa |
       | bbaaa |
       | bbbaa |
       | bbbba |
       | bbbbb |

Scenario: User requires to create a field with strings that conform to multiple sets of one or many constraints
     Given there is a field foo
       And there is a constraint:
       """
       { "anyOf": [
       { "field": "foo", "is": "equalTo", "value": "Test0" },
       { "field": "foo", "is": "inSet", "values": ["Test1", "Test2", "Test3", "Test4", "Test5"] },
       { "field": "foo", "is": "null" },
       { "field": "foo", "is": "matchingRegex", "value": "[a-b]{5}" }
       ]}
       """
       And there is a constraint:
       """
       { "anyOf": [
       { "field": "foo", "is": "equalTo", "value": "Test6" },
       { "field": "foo", "is": "inSet", "values": ["Test7", "Test8", "Test9"] },
       { "field": "foo", "is": "matchingRegex", "value": "[c-d]{5}" }
       ]}
       """
       And foo is of type "string"
       And foo is of length 5
     Then the following data should be generated:
       | foo   |
       | null  |
       | Test0 |
       | Test1 |
       | Test2 |
       | Test3 |
       | Test4 |
       | Test5 |
       | aaaaa |
       | abaaa |
       | abbaa |
       | abbba |
       | abbbb |
       | aabaa |
       | aabba |
       | aabbb |
       | aaaba |
       | aaabb |
       | aaaab |
       | baaaa |
       | bbaaa |
       | bbbaa |
       | bbbba |
       | bbbbb |
       | Test6 |
       | Test7 |
       | Test8 |
       | Test9 |
       | ccccc |
       | cdccc |
       | cddcc |
       | cdddc |
       | cdddd |
       | ccdcc |
       | ccddc |
       | ccddd |
       | cccdc |
       | cccdd |
       | ccccd |
       | dcccc |
       | ddccc |
       | dddcc |
       | ddddc |
       | ddddd |

Scenario: User requires to create a field with numbers that conform to one or many constraints
     Given there is a field foo
       And there is a constraint:
       """
       { "anyOf": [
       { "field": "foo", "is": "greaterThan", "value": 0 },
       { "field": "foo", "is": "greaterThanOrEqualTo", "value": 2 },
       { "field": "foo", "is": "lessThan", "value": 10 },
       { "field": "foo", "is": "lessThanOrEqualTo", "value": 8 },
       { "field": "foo", "is": "granularTo", "value": 1 },
       { "field": "foo", "is": "null" }
       ]}
       """
       And foo is of type "numeric"
     Then the following data should be generated:
       | foo  |
       | null |
       | 2    |
       | 3    |
       | 4    |
       | 5    |
       | 6    |
       | 7    |
       | 8    |

  Scenario: User requires to create a field with numbers that conform to multiple sets of one or many constraints
    Given there is a field foo
       And there is a constraint:
       """
       { "anyOf": [
       { "field": "foo", "is": "greaterThan", "value": 0 },
       { "field": "foo", "is": "greaterThanOrEqualTo", "value": 2 },
       { "field": "foo", "is": "lessThan", "value": 10 }
       ]}
       """
       And there is a constraint:
       """
       { "anyOf": [
       { "field": "foo", "is": "lessThanOrEqualTo", "value": 8 },
       { "field": "foo", "is": "granularTo", "value": 1 },
       { "field": "foo", "is": "null" }
       ]}
       """
    And foo is of type "numeric"
    Then the following data should be generated:
      | foo  |
      | null |
      | 2    |
      | 3    |
      | 4    |
      | 5    |
      | 6    |
      | 7    |
      | 8    |

Scenario: User requires to create a field with dates that conform to one or many constraints
     Given there is a field foo
       And there is a constraint:
       """
       { "anyOf": [
       { "field": "foo", "is": "after", "value": "2018-10-01" },
       { "field": "foo", "is": "afterOrAt", "value": "2018-10-02" },
       { "field": "foo", "is": "before", "value": "2018-10-10" },
       { "field": "foo", "is": "beforeOrAt", "value": "2018-10-08" },
       { "field": "foo", "is": "null" }
       ]}
       """
       And foo is of type "temporal"
       And foo is formatted as "%tF"
     Then the following data should be generated:
       | foo        |
       | null       |
       | 2018-10-02 |
       | 2018-10-03 |
       | 2018-10-04 |
       | 2018-10-05 |
       | 2018-10-06 |
       | 2018-10-07 |
       | 2018-10-08 |

Scenario: User requires to create a field with dates that conform to multiple sets of constraints
     Given there is a field foo
       And there is a constraint:
       """
       { "anyOf": [
       { "field": "foo", "is": "after", "value": "2018-10-01" },
       { "field": "foo", "is": "afterOrAt", "value": "2018-10-02" }
       ]}
       """
       And there is a constraint:
       """
       { "anyOf": [
       { "field": "foo", "is": "before", "value": "2018-10-10" },
       { "field": "foo", "is": "beforeOrAt", "value": "2018-10-08" },
       { "field": "foo", "is": "null" }
       ]}
       """
       And foo is of type "temporal"
       And foo is formatted as "%tF"
     Then the following data should be generated:
       | foo        |
       | null       |
       | 2018-10-02 |
       | 2018-10-03 |
       | 2018-10-04 |
       | 2018-10-05 |
       | 2018-10-06 |
       | 2018-10-07 |
       | 2018-10-08 |