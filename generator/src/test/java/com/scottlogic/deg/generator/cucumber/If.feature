Feature: Values can be specified by using if, then and else constraints

  Scenario: If and then satisfied
    Given the following fields exist:
      | foo |
      | price |
    And foo is of type "string"
    And price is of type "numeric"
    And foo is in set ["a", "b"]
    And there is a constraint:
    """
      {
        "if": { "field": "foo", "is": "equalTo", "value": "a" },
        "then": { "field": "price", "is": "equalTo", "value": 3 },
        "else": { "field": "price", "is": "equalTo", "value": 5 }
      }
      """
    Then the following data should be generated:
      | foo | price |
      |a|3|
      |b| 5|


#
#  Scenario: If and then satisfied
#    Given the following fields exist:
#  | product_code | price |
#    And product_code is in set [ "a", "b"]
#    And price is in set {10, 20]
#    And there is a constraint:
#    """
#  {
#  "if": { "field": "product_code", "is": "equalTo", "value": "a" },
#  "then": { "field": "price", "is": "equalTo", "value": 10 }
#  }
#  """
#    Then the following data should be generated
#      | product_code | price |
#      | a            | 10     |
#      | b            | 10     |
#      | b            | 20     |
#
