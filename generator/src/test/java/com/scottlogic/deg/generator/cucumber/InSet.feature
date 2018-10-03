Feature: Testing if a field's value is in the set values

    Scenario:
      Given there is a field
      And type is inSet [ "X_092", "X_094" ]
      Then expect exactly:
      | type |
      | "X_092"|
      | "X_094" |

