import generateUniqueString from "../../util/generateUniqueString";
import {IFieldStatePatch} from "../state/IAppState";
import {MappingActionType, SimpleActionType} from "./ActionType";



export const ClearCurrentProfile = new SimpleActionType<{}>(
	"CLEAR_CURRENT_PROFILE");

export const UpdateField = new SimpleActionType<{
	fieldId: string,
	newValues: IFieldStatePatch
}>("UPDATE_FIELD");

export const DeleteField = new SimpleActionType<{
	fieldId: string
}>("DELETE_FIELD");

export const AddBlankField = new MappingActionType<
	{},
	{ fieldId: string }>
(
	"ADD_BLANK_FIELD",
	_ => ({ fieldId: generateUniqueString() })
);

export const ExportProfile = new SimpleActionType<{}>(
	"EXPORT_PROFILE"); // caught by SideEffectsMiddleware