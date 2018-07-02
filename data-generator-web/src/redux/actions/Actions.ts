import DeepPartial from "../../util/DeepPartial";
import {IFieldState} from "../state/IAppState";
import { SimpleActionType } from "./ActionType";

export const ClearCurrentProfile = new SimpleActionType<{}>("CREATE_CURRENT_PROFILE");

export const UpdateField = new SimpleActionType<{
	fieldId: string,
	newValues: DeepPartial<IFieldState>
}>("UPDATE_FIELD");
