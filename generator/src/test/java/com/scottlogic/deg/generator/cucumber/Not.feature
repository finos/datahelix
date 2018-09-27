Scenario: Inner constraint not satisfied
    Given: the following fields exist:
  | name | value |
  And name is in a, b
  And value is in X, Y
  And there is a constraint:
  {
  "not": { "field": "a", "is": "null" } },
{"field": "a", "is": "inSet", "values": [ "X", "Y"] },

{ "not": { "field": "b", "is": "null" } },
{ "field": "b", "is": "inSet", "values": [ "1", "2" ] }

 Then expect

