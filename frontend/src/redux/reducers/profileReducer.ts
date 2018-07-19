import { Action } from "redux";
import { IProfileState } from "../state/IAppState";
import fieldsReducer from "./fieldsReducer";
import profileReducerBase from './profileReducerBase'

export default function profileReducer(
	oldState: IProfileState | undefined,
	action: Action)
	: IProfileState {
	return profileReducerBase(oldState, action, fieldsReducer);
}
