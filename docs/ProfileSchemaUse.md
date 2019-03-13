# DataHelix Profile Schema Usage

The [JSON schema](https://json-schema.org/) for the DataHelix data profile is stored in the file `datahelix.schema.json` in the `json` directory.

The grammar for the schema is documented in [BNF](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form) form in the file [datahelix.profile.bnf](../schemas/src/main/resources/profileschema/0.1/datahelix.profile.bnf) and in syntax diagrams in the file [ProfileGrammar.md](ProfileGrammar.md)

## JetBrains IntelliJ

To use the DataHelix profile JSON schema in IntelliJ we need to  set up the intellij editor to validate all json files under the `json` and/or `examples` directories against the `datahelix.schema.json` schema file.

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

now when you open a json file from the `json` directory in IntelliJ, it will be automatically validated against the DataHelix profile schema.


## Microsoft Visual Studio Code

to enable visual studio code to validate json files against the DataHelix profile schema a `json.schems` section needs to be added to the `settings.json` file.

to do this:

1. click on the gear icon (<img src="../wikiimages/settingsicon.png" width="16" height="16">) at the bottom left of the screen and select `Settings`
1. in the settings windows, click `Extensions` -> `JSON`
1. you should see a section like this:
    ```
    Schemas
    Associate schemas to JSON files in the current project
    Edit in settings.json
    ```
1. click on the `Edit in settings.json` link and VSCode will open the settings.json file.
1. add the following snippet to the end of the file (replacing `<projectroot>` with the root directory path for the DataHelix project:
    ```
      "json.schemas": [
        {
          "fileMatch": [
            "<projectroot>/datahelix/*"
          ],
          "url": "file://<projectroot>/datahelix/schemas/src/main/resources/profileschema/0.1/datahelix.schema.json"
        }
    ```
1. if the above snippet already exists, you can add a new object to the JSON array for the DataHelix profile schema.


