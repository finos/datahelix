import {Middleware} from "redux";

import Actions from "../actions";

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

	return next(action);
}

export default sideEffectsMiddleware;
