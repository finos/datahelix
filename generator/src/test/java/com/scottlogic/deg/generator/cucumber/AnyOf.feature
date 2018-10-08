Feature: Values can be specified by using any of to set multiple constraints

Scenario: User requires to create a field with strings that conform to one or many constraints
     Given there is a field foo
       And And there are constraints:
       """
       { "anyOf": [
       { "field": "foo", "is": "equalTo", "value": "Test0" },
       { "field": "foo", "is": "inSet", "values": ["Test1", "Test2", "Test3", "Test4", "Test5"] },
       { "field": "foo", "is": "null" },
       { "field": "foo", "is": "matchingRegex", "value": "[a-c]" },
       ]}
       """
       And foo is not null
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
       | a     |
       | b     |
       | c     |