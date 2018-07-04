import {Action} from "redux";

import {
	IAppState
} from "../state/IAppState";
import profileReducer from "./profileReducer";


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
