import generateUniqueString from "../../util/generateUniqueString";
import {FieldKinds, IFieldStatePatch, IProfileState} from "../state/IAppState";
import {MappingActionType, SimpleActionType} from "./ActionType";


export const SetCurrentProfile = new SimpleActionType<{
	newProfile: IProfileState
}>(
	"SET_CURRENT_PROFILE"
);

export const ClearCurrentProfile = new SimpleActionType<{}>(
	"CLEAR_CURRENT_PROFILE");

export const StartGeneratingData = new SimpleActionType<{}>(
	"START_GENERATING_DATA");



export const UpdateField = new SimpleActionType<{
	fieldId: string,
	newValues: IFieldStatePatch
}>("UPDATE_FIELD");

export const ChangeFieldKind = new SimpleActionType<{
	fieldId: string,
	newKind: FieldKinds
}>("CHANGE_FIELD_KIND");

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

export const StartImportProfile = new SimpleActionType<{}>(
	"START_IMPORT_PROFILE"); // caught by SideEffectsMiddleware

export const ExportProfile = new SimpleActionType<{}>(
	"EXPORT_PROFILE"); // caught by SideEffectsMiddleware
