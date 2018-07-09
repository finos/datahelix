import {Action} from "redux";
import Actions from "../actions";
import {
	AnyFieldRestriction,
	AnyFieldRestrictionsPatch, FieldKinds,
	IFieldState,
	IFieldStatePatch,
	IProfileState
} from "../state/IAppState";
import fieldsReducer from "./fieldsReducer";

export default function profileReducer(
	oldState: IProfileState | undefined,
	action: Action)
	: IProfileState
{
	if (!oldState)
		return { fields: [] };

	if (Actions.Profiles.SetCurrentProfile.is(action))
	{
		return action.newProfile;
	}

	if (Actions.Profiles.ClearCurrentProfile.is(action))
	{
		return { fields: [] };
	}

	return {
		fields: fieldsReducer(oldState.fields, action)
	};
}
