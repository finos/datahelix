# Field Fixing strategies

The generator in 'reductive' mode can be said to operate in the following way:
1. Pick a field
2. Fix that field's value
3. Eliminate any options that cannot be generated based on this value
4. Repeat until all fields are fixed with values

One area that _could_ affect performance is the order in which we pick fields to fix values. The way in which we determine this order is known as a **fix field strategy**.

Our current default is the **hierarchical dependency** strategy.

## Hierarchical Dependency

This strategy picks fields to fix using a combination of factors. It sorts a list of fields into an order as follows: 
- **First** favour picking fields that are **not dependent** on other fields
- **Then**  favour picking fields that **constrain** other fields
- **Then** use the [set based strategy](#set-based)
- **Then** compare fields alphabetically by name

If a field is **independent** of any other fields they can _co-vary_. This means that the field's value cannot cause another fields value to contradict.

Overall this strategy assumes some **hierarchical** categorisation of fields where we attempt to pick the most **constraining** fields first. 

The idea behind this is that:
- The field picked **reduces** the other fields remaining possible values 
- The field picked is known to be less dependent than other fields and should be less likely to require backtracking

### Examples
Consider the following statements:
- There are 4 fields in a profile A, B, C and D
- Field B's value is dependent on Field A
- Field C and D's values are dependent on Field B

From this we can also deduce:
> Field C and D are also indirectly dependent on Field A

In this example fields would be picked in the following order:
1. A
2. B
3. C
4. D

#### Explanation
- A is picked first as it has no dependencies on other fields, but directly constrains B and indirectly constrains C and D
- B is picked next as it constrains C and D, but is dependent upon A
- Neither C and D constrain any other field and are both constrained equally by B. Therefore C is only chosen over D because of alphabetical ordering 

## Set Based

This strategy picks fields based on the following:
 - **First** favour picking fields that are always **constrained** by a finite set
 - **Then**  favour picking fields where the sets are smaller
 
Currently this is also used as a further step within the Hierarchical Dependency strategy
 
## Ranked Constraint
 
This strategy does the following:
1. Get all the atomic constraints in the tree and group them by field
2. Score each constraint by it's precision (e.g. an equalTo is precise, a set of 2 values is half as precise)
3. Sort the fields by score into a ranking
4. Pick the field with the highest rank (greatest precision)

This differs in implementation to the above strategies in that it is **tree based** and not **profile based**.

This means two things:
- It runs based on a Decision Tree rather than a Profile object
- It is dynamic and can run multiple times within the generation run time