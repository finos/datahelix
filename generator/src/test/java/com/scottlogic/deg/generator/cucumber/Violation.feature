Feature: User can set the generation strategy to violate mode and see test cases generated

Background:
  Given the generation strategy is interesting
  And there is a field foo
  And the data requested is violating
  And the generator can generate at most 20 rows

Scenario: Running the generator in violate mode for negated equal is successful
   Given there is a constraint:
       """
         {
           "not": { "field": "foo", "is": "equalTo", "value": 8 }
         }
       """
   Then the following data should be generated:
     | foo  |
     | 8    |
     | null |

Scenario: Running the generator in violate mode for negated string type is successful
    Given there is a constraint:
       """
         {
           "not": { "field": "foo", "is": "ofType", "value": "string" }
         }
       """
    Then the following data should be generated:
      | foo            |
      | "Lorem Ipsum"  |
      | null           |

Scenario: Running the generator in violate mode for negated numeric type is successful
    Given there is a constraint:
       """
         {
           "not": { "field": "foo", "is": "ofType", "value": "numeric" }
         }
       """
    Then the following data should be generated:
      | foo         |
      | -2147483648 |
      | 0           |
      | 2147483646  |
      | null        |
