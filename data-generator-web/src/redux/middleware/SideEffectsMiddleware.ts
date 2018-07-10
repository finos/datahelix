import {saveAs} from "file-saver";
import {Action, Middleware} from "redux";

import {fileFormatList, mostRecentFormat} from "../../profileSchemas";
import requestFileUpload from "../../requestFileUpload";
import Actions from "../actions";
import {IProfileState} from "../state/IAppState";

const sideEffectsMiddleware: Middleware = api => next => action =>
{
	if (Actions.Profiles.TriggerExportProfile.is(action)) {
		let serialisedProfile: {};

		try {
			serialisedProfile = mostRecentFormat.serialiseProfile(
				api.getState().currentProfile);
		}
		catch(e)
		{
			alert("Failed to create profile file");
			return;
		}

		const serialisedProfileAsJson =
			JSON.stringify(
				serialisedProfile,
				null,
				"  ");

		const blob = new Blob(
			[ serialisedProfileAsJson ],
			{ type: "application/json" });

		saveAs(blob, "profile.json");

		return;
	}

	if (Actions.Profiles.TriggerImportProfile.is(action)) {
		handleTriggerImport()
			.then(newAction => next(newAction))
			.catch((err: string) => {
				alert(`Failed to import file\n\n${err}`);
			});

		return;
	}

	if (Actions.StartGeneratingData.is(action) || Actions.Profiles.TriggerProfileFromFile.is(action) || Actions.Profiles.TriggerProfileFromDatabase.is(action)) {
		alert("Not yet!");
		return;
	}

	return next(action);
}

async function handleTriggerImport(): Promise<Action>
{
	const uploadedFiles = await requestFileUpload();

	const json = await readTextFromBlob(uploadedFiles[0]);

	let newProfileState: IProfileState;

	newProfileState = readProfileFromJson(json);

	return Actions.Profiles.SetCurrentProfile.create({newProfile: newProfileState});
}

function readTextFromBlob(blob: Blob): Promise<string>
{
	const fileReader = new FileReader();

	fileReader.readAsText(blob);

	return new Promise((resolve, reject) =>
	{
		fileReader.onload = () => resolve(fileReader.result);
		fileReader.onerror = () => reject(fileReader.error);
	})
}

function readProfileFromJson(json: string): IProfileState
{
	const rawProfileObject = JSON.parse(json);

	const bestFileFormat = fileFormatList.find(ff => ff.canDeserialiseFrom(rawProfileObject));

	if (!bestFileFormat)
	{
		throw new Error("Can't find matching file schema");
	}

	return bestFileFormat.deserialiseProfile(rawProfileObject);
}



export default sideEffectsMiddleware;
