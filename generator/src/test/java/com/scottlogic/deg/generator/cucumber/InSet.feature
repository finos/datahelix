Feature: Testing if a field's value is in the set values

    Scenario:
      Given there is a field foo
      And foo is in set [ "X_092", "X_094" ]
      And foo is not null
      Then expect exactly:
      | foo |
      | "X_092"|
      | "X_094" |

