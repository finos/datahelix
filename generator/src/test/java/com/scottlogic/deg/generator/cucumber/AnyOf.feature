Feature: Values can be specified by using any of to set multiple constraints

Scenario: User requires to create a field with strings that conform to one or many constraints
     Given there is a field foo
       And And there are constraints:
       """
       { "anyOf": [
       { "field": "foo", "is": "equalTo", "value": "Test0" },
       { "field": "foo", "is": "inSet", "values": ["Test1", "Test2", "Test3", "Test4", "Test5"] },
       { "field": "foo", "is": "null" },
       { "field": "foo", "is": "matchingRegex", "value": "[a-b]" },
       ]}
       """
       And foo is not null
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
       And And there are constraints:
       """
       { "anyOf": [
       { "field": "foo", "is": "equalTo", "value": "Test0" },
       { "field": "foo", "is": "inSet", "values": ["Test1", "Test2", "Test3", "Test4", "Test5"] },
       { "field": "foo", "is": "null" },
       { "field": "foo", "is": "matchingRegex", "value": "[a-b]" },
       ]}
       """
       And And there are constraints:
       """
       { "anyOf": [
       { "field": "foo", "is": "equalTo", "value": "Test6" },
       { "field": "foo", "is": "inSet", "values": ["Test7", "Test8", "Test9"] },
       { "field": "foo", "is": "matchingRegex", "value": "[c-d]" },
       ]}
       """
       And foo is not null
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