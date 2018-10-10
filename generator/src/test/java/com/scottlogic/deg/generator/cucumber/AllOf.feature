Feature: Constraint only satisfied if all inner constraints are satisfied

  Scenario: Tests textual constraints
    Given the following fields exist:
      | foo |
      | price |

    And there is a constraint:
    """
       { "allOf": [
       { "field": "foo", "is": "equalTo", "value": "Test0" },
        { "field": "foo", "is": "ofType", "value": "string" },
        { "field": "price", "is": "equalTo", "value": 5 }

         ]}
    """

    Then the following data should be generated:
      | foo   | price |
      | Test0 |   5    |


    