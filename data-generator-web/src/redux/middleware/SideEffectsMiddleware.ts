import {Middleware} from "redux";

import Actions from "../actions";
import {FieldKinds} from "../state/IAppState";

const sideEffectsMiddleware: Middleware = api => next => action =>
{
	if (Actions.ExportProfile.is(action))
	{
		alert(
			JSON.stringify(
				api.getState().currentProfile,
				null,
				"  "));

		return;
	}

	if (Actions.StartImportProfile.is(action))
	{
		next(Actions.SetCurrentProfile.create({
			newProfile: {
				fields: [
					{
						id: "aaaa",
						name: "description",
						nullPrevalence: 0,
						restrictions: {
							kind: FieldKinds.String,
							allowableCharacters: "abcdkz"
						}
					},
					{
						id: "bbbb",
						name: "price",
						nullPrevalence: 0,
						restrictions: {
							kind: FieldKinds.Numeric,
							meanAvg: 1,
							stdDev: 1
						}
					}
				]
			}
		}));
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
