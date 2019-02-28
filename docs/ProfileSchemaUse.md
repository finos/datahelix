# DataHelix Profile Schema Usage

The JSON schema for the DataHelix data profile is stored in the file `datahelix.schema.json` in the `json` directory

To use this in an editor we currenlty set up the editor to validate all json files under the json directory against the `datahelix.schema.json` schema file.

to setup IntelliJ to validate json files against the schema follow these steps:

1. open IntelliJ
1. select `File` -> `Settings` -> `Languages & Frameworks` -> `Schemas and DTDs'
1. select `JSON Schema Mappings`
1. press the `+` button to add a new schema mapping
1. give the mapping a name (e.g. `DataHelix Profile Schema`)
1. for `Schema file or URL:` select the local schema file (e.g. `<project root>/datahelix/json/datahelix.schema.json`)
1. make sure the `Schema version:` is set to `JSON schema version 7`
1. press the `+` button to add a new mapping
1. select `Add Directory`
1. select the `json` directory
1. press okay

now when you open a json file from the `json` directory, it will be automattically validated against the DataHelix profile schema.