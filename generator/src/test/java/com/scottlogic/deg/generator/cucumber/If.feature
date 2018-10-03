Feature: Values can be specified by using if, then and else constraints

  Scenario: If and then satisfied
    Given: there are fields:
      | product_code | price |
    And product_code is in "a", "b"
    And there is a constraint:
      {
        "if": { "field": "product_code", "is": "equalTo", "value": "a" },
        "then": { "field": "price", "is": "equalTo", "value": 3 }
        "else": { "field": "price", "is": "equalTo", "value": 5 }
      }
    Then expect exactly unordered
      | product_code | price |
      | a            | 3     |
      | b            | 5      |

    # add some edge cases, for example if there is product_code c which does not match any of the values

  Scenario: If and then satisfied
    Given: the following fields exist:
  | product_code | price |
    And product_code is in a, b
    And price is in 10, 20
    And there is a constraint:
  {
  "if": { "field": "product_code", "is": "equalTo", "value": "a" },
  "then": { "field": "price", "is": "equalTo", "value": 10 }
  }
    Then expect exactly
      | product_code | price |
      | a            | 10     |
      | b            | 10     |
      | b            | 20     |



  { "if": { "field": "product_code", "is": "null" },
    "then": { "field": "price", "is": "equalTo", "value": 3 } },

  { "field": "price", "is": "ofType", "value": "numeric" }


  { "if": { "field": "product_code", "is": "null" },
    "then": {
      "allOf": [
        { "field": "price", "is": "equalTo", "value": 3 },
        { "field": "price", "is": "ofType", "value": "numeric" }
      ]
    }
  }
