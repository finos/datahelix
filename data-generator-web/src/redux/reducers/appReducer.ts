import {Action} from "redux";

import {
	IAppState
} from "../state/IAppState";
import profileReducer from "./profileReducer";


export default function appReducer(
	oldState: IAppState | undefined,
	action: Action)
	: IAppState
{
	return {
		currentProfile: profileReducer(
			oldState ? oldState.currentProfile : undefined,
			action)
	}
}
