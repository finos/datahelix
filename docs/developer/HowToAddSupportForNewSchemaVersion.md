## How to add support for a new schema version

1. Copy a package in _profile/src/main/resources/profileschema/_ and rename to the new version number.
1. Change the _schemaVersion_ const from the old version number to the new one.

### Example
If the file structure currently looks like the below...
```
- profileschema
     |- 0.1
         |- datahelix.schema.json
```
...and the new version is 0.2 then change it to the following:
```
- profileschema
     |- 0.1
         |- datahelix.schema.json
     |- 0.2
         |- datahelix.schema.json
```

Then change the below (in the new file)...
```
...
"schemaVersion": {
  "title": "The version of the DataHelix profile schema",
  "const": "0.1"
},
...
``` 
...to this:
```
...
"schemaVersion": {
  "title": "The version of the DataHelix profile schema",
  "const": "0.2"
},
...
```

You will need to update the test in _ProfileSchemaImmutabilityTests_ to contain the new schema version generated. Old versions should **not** be modified. This is reflected by the test failing if any existing schemas are modified.

If you experience any issues with this test not updating the schema in IntelliJ, it is recommended to invalidate the cache and restart, or to delete the _profile/out_ directory and rebuild. 
