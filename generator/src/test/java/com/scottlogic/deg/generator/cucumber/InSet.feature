Feature: Testing if a field's value is in the set values

    Scenario: Satisfied if field's value is in the set values
      Given the following set of values exists:
      | "X_092", "X_094" |
      Then expect "X_092" or "X_094"





