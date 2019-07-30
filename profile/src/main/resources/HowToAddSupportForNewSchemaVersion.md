## How to add support for a new schema version

1. Copy a package in _/profileschema/_ and rename to the new version number.
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
