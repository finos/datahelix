Feature: Values can be specified by using any of to set multiple constraints

Scenario: User requires to create a field with strings that conform to one or many constraints
     Given there is a field foo
       And there is a constraint:
       """
       { "anyOf": [
         { "field": "foo", "is": "equalTo", "value": "Test0" },
         { "field": "foo", "is": "inSet", "values": ["Test1", "Test2", "Test3", "Test4", "Test5"] },
         { "field": "foo", "is": "matchingRegex", "value": "[a-b]{4}" }
       ]}
       """
       And foo is of type "string"
     Then the following data should be generated:
       | foo   |
       | null  |
       | Test0 |
       | Test1 |
       | Test2 |
       | Test3 |
       | Test4 |
       | Test5 |
       | aaaa  |
       | aaba  |
       | aabb  |
       | aaab  |
       | abaa  |
       | abba  |
       | abbb  |
       | abab  |
       | baaa  |
       | baba  |
       | babb  |
       | baab  |
       | bbaa  |
       | bbba  |
       | bbbb  |
       | bbab  |

Scenario: User requires to create a field with strings that conform to multiple sets of one or many constraints
     Given there is a field foo
       And there is a constraint:
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
       And foo is of type "string"
     Then the following data should be generated:
       | foo   |


Scenario: User requires to create a field with numbers that conform to one or many constraints
     Given there is a field foo
       And there is a constraint:
       """
       { "anyOf": [
         { "field": "foo", "is": "greaterThan", "value": 0 },
         { "field": "foo", "is": "greaterThanOrEqualTo", "value": 2 }
       ]}
       """
       And foo is of type "numeric"
       And foo is less than 11
       And foo is granular to 1
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
       | 9    |
       | 10   |

Scenario: User requires to create a field with numbers that conform to multiple sets of one or many constraints
    Given there is a field foo
       And there is a constraint:
       """
       { "anyOf": [
         { "field": "foo", "is": "greaterThan", "value": 8 },
         { "field": "foo", "is": "greaterThanOrEqualTo", "value": 10 }
       ]}
       """
       And there is a constraint:
       """
       { "anyOf": [
         { "field": "foo", "is": "greaterThan", "value": 1 },
         { "field": "foo", "is": "greaterThanOrEqualTo", "value": 2 }
       ]}
       """
    And foo is of type "numeric"
    And foo is less than 20
    And foo is granular to 1
    Then the following data should be generated:
      | foo  |
      | null |
      | 9    |
      | 10   |
      | 11   |
      | 12   |
      | 13   |
      | 14   |
      | 15   |
      | 16   |
      | 17   |
      | 18   |
      | 19   |

Scenario: User requires to create a field with dates that conform to one or many constraints
     Given there is a field foo
       And there is a constraint:
       """
       { "anyOf": [
         { "field": "foo", "is": "after", "value": "2018-10-01T00:00:00.000" },
         { "field": "foo", "is": "afterOrAt", "value": "2018-10-02T00:00:00.000" }
       ]}
       """
       And foo is of type "temporal"
       And foo is before 2018-10-10T00:00:00.000
       And foo is formatted as "%tF"
     Then the following data should be generated:
       | foo        |
       | null       |
       | 2018-10-01 |
       | 2018-10-02 |
       | 2018-10-03 |
       | 2018-10-04 |
       | 2018-10-05 |
       | 2018-10-06 |
       | 2018-10-07 |
       | 2018-10-08 |
       | 2018-10-09 |

Scenario: User requires to create a field with dates that conform to multiple sets of constraints
     Given there is a field foo
       And there is a constraint:
       """
       { "anyOf": [
         { "field": "foo", "is": "after", "value": "2018-10-01T00:00:00.000" },
         { "field": "foo", "is": "afterOrAt", "value": "2018-10-02T00:00:00.000" }
       ]}
       """
       And there is a constraint:
       """
       { "anyOf": [
         { "field": "foo", "is": "after", "value": "2018-10-03T00:00:00.000" },
         { "field": "foo", "is": "afterOrAt", "value": "2018-10-04T00:00:00.000" }
       ]}
       """
       And foo is of type "temporal"
       And foo is before 2018-10-09T00:00:00.000
       And foo is formatted as "%tF"
     Then the following data should be generated:
       | foo        |
       | null       |
       | 2018-10-03 |
       | 2018-10-04 |
       | 2018-10-05 |
       | 2018-10-06 |
       | 2018-10-07 |
       | 2018-10-08 |