import { Action } from "redux";
import Actions from "../actions";
import { IProfileState } from "../state/IAppState";

export default function profileReducerBase(
	oldState: IProfileState | undefined,
	action: Action,
	fallbackReducer: Function)
	: IProfileState {
	if (!oldState)
		return { fields: [] };

	if (Actions.Profiles.SetCurrentProfile.is(action)) {
		return action.newProfile;
	}

	if (Actions.Profiles.ClearCurrentProfile.is(action)) {
		return { fields: [] };
	}

	return {
		fields: fallbackReducer(oldState.fields, action)
	};
}