### Profile Validation

The [JSON schema](https://json-schema.org/) for the DataHelix profile is stored in the file `datahelix.schema.json` in the [profileschema](https://github.com/finos/datahelix/tree/master/profile/src/main/resources/profileschema) directory.

We recommend using Visual Studio Code to validate your profiles. To enable it to validate json files against the DataHelix profile schema a `json.schemas` section needs to be added to the `settings.json` file.

To do this:

1. Click on the gear icon at the bottom left of the screen and select `Settings`
1. In the settings windows, click `Extensions` -> `JSON`
1. You should see a section like this:
    ```
    Schemas
    Associate schemas to JSON files in the current project
    Edit in settings.json
    ```
1. Click on the `Edit in settings.json` link and VSCode will open the settings.json file.
1. Add the following snippet to the end of the file (replacing `<datahelix_projectroot>` with the root directory path for the DataHelix project and replacing the `"fileMatch"` value with an appropriate value for your configuration):
    ```
      "json.schemas": [
        {
          "fileMatch": [
            "<datahelix_projectroot>/*"
          ],
          "url": "file:///<datahelix_projectroot>/profile/src/main/resources/profileschema/datahelix.schema.json"
        }
      ]
    ```
    Alternatively you can configure this to any naming convention you want for profile files, for example `"*.profile.json"`.

To verify that the url to the `datahelix.schema.json` is valid you can `ctrl-click` on it and the schema file will open in the editor. If the ` "json.schemas"` snippet already exists, you can add a new object to the JSON array for the DataHelix profile schema.
