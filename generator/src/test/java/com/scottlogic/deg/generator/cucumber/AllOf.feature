Feature: Constraint only satisfied if all inner constraints are satisfied

  Scenario: Tests textual constraints
    Given: there is a field foo
    And there is a constraint:
    """
       { "allOf": [
       { "field": "foo", "is": "equalTo", "value": "Test0" },
       { "field": "foo", "is": "inSet", "values": ["Test1", "Test2", "Test3", "Test4", "Test5"] },
       { "field": "foo", "is": "null" },
       { "field": "foo", "is": "matchingRegex", "value": "[0-10]" }
       ]}
    """
    Then the following data should be generated:




  Scenario: Tests numeric constraints
    Given: there is a field foo
    And there is a constraint:
    """
       { "allOf": [
       { "field": "foo", "is": "greaterThan", "value": "Test0" },
       { "field": "foo", "is": "inSet", "values": ["Test1", "Test2", "Test3", "Test4", "Test5"] },
       { "field": "foo", "is": "null" },
       { "field": "foo", "is": "matchingRegex", "value": "[0-10]" }
       ]}
    """
    Then the following data should be generated: