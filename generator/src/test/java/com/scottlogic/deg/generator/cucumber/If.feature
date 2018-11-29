Feature: Values can be specified by using if, then and else constraints

Background:
     Given the generation strategy is full

Scenario: if constraint is not satisfied, else constraint is
     Given the following fields exist:
       | foo   |
       | price |
       And foo is of type "string"
       And price is of type "numeric"
       And foo is in set:
        | "a" |
        | "b" |
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
       | "a" |  3    |
       | "b" |  5    |

Scenario: User requires both if and then constraint to be satisfied
     Given the following fields exist:
       | productCode |
       | priceTest   |
       And productCode is of type "string"
       And priceTest is of type "numeric"
       And productCode is in set:
          | "a" |
          | "b" |
       And priceTest is in set:
         | 10 |
         | 20 |
       And there is a constraint:
       """
         {
         "if": { "field": "productCode", "is": "equalTo", "value": "a" },
         "then": { "field": "priceTest", "is": "equalTo", "value": 10 }
         }
       """
     Then the following data should be generated:
       | productCode | priceTest |
       | "a"         |    10     |
       | "b"         |    10     |
       | "b"         |    20     |