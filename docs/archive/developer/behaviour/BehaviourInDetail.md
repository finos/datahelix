# Behaviour in Detail
## Nullness
### Behaviour
Nulls can always be produced for a field, except when a field is explicitly not null. 

### Misleading Examples
|Field is               |Null produced|
|:----------------------|:-----------:|
|Of type X              | ✔ |
|Not of type X          | ✔ |
|In set [X, Y, ...]     | ✔ |
|Not in set [X, Y, ...] | ✔ |
|Equal to X             | ❌ |
|Not equal to X         | ✔ |
|Greater than X         | ✔ |
|Null                   | ✔ |
|Not null               | ❌ |

For the profile snippet:
```
{ "if":
    { "field": "A", "equalTo": 1 },
  "then":
    { "field": "B", "equalTo": 2 }
},
{ "field": "A", "equalTo": 1 }
```

|Allowed value of A|Allowed value of B|
|------------------|------------------|
|1                 |2                 |

## Type Implication
### Behaviour
No operators imply type (except ofType ones). By default, all values are allowed.

### Misleading Examples
Field is greater than number X:

|Values                |Can be produced|
|----------------------|:-------------:|
|Numbers greater than X|✔ |
|Numbers less than X   |❌ |
|Null                  |✔ |
|Strings               |✔ |
|Date-times            |✔ |
