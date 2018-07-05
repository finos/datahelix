import {saveAs} from "file-saver";
import {Middleware} from "redux";

import { mostRecentFormat } from "../../profileSchemas";
import Actions from "../actions";

const sideEffectsMiddleware: Middleware = api => next => action =>
{
	if (Actions.ExportProfile.is(action)) {
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

	if (Actions.StartGeneratingData.is(action))
	{
		alert("Not yet!");
		return;
	}

	return next(action);
}

export default sideEffectsMiddleware;
