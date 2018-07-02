import {Action} from "redux";

import Actions from "../actions";
import {IAppState, IProfileState} from "../state/IAppState";


export default function appReducer(
	oldState: IAppState | undefined,
	action: Action)
	: IAppState  | undefined
{
	if (!oldState)
		return oldState;

	return {
		currentProfile: profileReducer(oldState.currentProfile, action)
	}
}

function profileReducer(
	oldState: IProfileState | undefined,
	action: Action)
	: IProfileState | undefined
{
	if (!oldState)
		return oldState;

	if (Actions.AddBlankField.is(action))
	{
		return {
			fields: [
				...oldState.fields,
				{ id: action.fieldId, nullPrevalence: 0 }
			]
		};
	}

	if (Actions.DeleteField.is(action))
	{
		return {
			fields: [
				...oldState.fields.filter(f => f.id !== action.fieldId)
			]
		}
	}

	if (Actions.ClearCurrentProfile.is(action))
	{
		return { fields: [] };
	}

	return oldState;
}
