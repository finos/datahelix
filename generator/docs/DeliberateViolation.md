# Deliberate violation

## Algorithm

1. For each rule `R`:
    1. Create a modified version of the decision tree where one rule's content is replaced with (TBD - as an easy first pass, just replace it with a `NOT`-ed equivalent)..
    2. Generate data _(enhancement: using a combination strategy that only attempts to be exhaustive with respect to fields affected by `R`?)_.

## Output

In the case of generating test cases through the command line tool, we output:
- Numerous data files, each containing one or more entries
- A manifest file listing the data files

### Manifest

```javascript
[
  {
    "filepath": "001.csv",
    "violatedRules": []
  },
  {
    "filepath": "002.csv",
    "violatedRules": [ "Price field should not accept nulls" ]
  }
]
```
