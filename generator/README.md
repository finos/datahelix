## Rule violation

We have a clear idea of how to generate test data that satisfies all rules, but additionally we wish to generate test data which _violates_ rules.  
i.e. data which mostly complies with the rule, but deliberately fails to meet one constraint/sub-constraint.  
This helps you test "would my program notice when you submit invalid data".

Each rule has one constraint (multiple constraints may be composed using AND(A, B, ...)).  
The constraint upon that rule must be one of the following:

```
a // atomic constraint. supports negation.
A // any of [OR, AND, atomic constraint]
OR(A, B, ...)  // exactly one of these constraints applies
AND(A, B, ...) // all of these constraints apply
```

So, consider this constraint, which describes "rule #1":

```
  Country = US
OR
    Country = UK
  AND
    x = 5
```

We can use `😧(X)` to generate ways to violate constraint X.

```
😧(a) = ¬a
😧(AND(A, B)) = 😧(A), B
               = A, 😧(B)
               = 😧(A), 😧(B) // bonus (every possible combination that fails it)
😧(OR(A, B)) = 😧(A), 😧(B)

// you may not need to implement these, depending on what guarantees you have on your input
😧(AND(A, B, C)) = 😧(AND(AND(A, B), C))
😧(OR(A, B, C)) = 😧(OR(OR(A, B), C))
```

Currently our plan is to ignore the "bonus" solution to the violation of `AND()`, because tests are more useful if they change fewer things.

### If, If/else

```
  IF (Country == US)
    City IN ['Boston', 'Super Boston']
AND
    Currency = USD
  IFELSE(Country == US)
    International = "true"
```

Keep the precondition true, but violate the consequences.

```
😧(IF(A, B)) = IF(A, 😧(B))
😧(IFELSE(A, B, C)) = IFELSE(A, 😧(B), C)
                    = IFELSE(A, B, 😧(C))
```
