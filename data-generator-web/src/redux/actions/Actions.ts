import generateUniqueString from "../../util/generateUniqueString";
import {FieldKinds, IFieldStatePatch, IProfileState} from "../state/IAppState";
import {MappingActionType, SimpleActionType} from "./ActionType";

export namespace Profiles {
	export const SetCurrentProfile = new SimpleActionType<{
		newProfile: IProfileState
	}>(
		"SET_CURRENT_PROFILE"
	);

	export const ClearCurrentProfile = new SimpleActionType<{}>(
		"CLEAR_CURRENT_PROFILE");

	export const ExportProfile = new SimpleActionType<{}>(
		"EXPORT_PROFILE"); // caught by SideEffectsMiddleware
}

export namespace Fields {
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

	export namespace Enums {
		export const CreateBlankEnumEntry = new SimpleActionType<{ fieldId: string }>(
			"CREATE_BLANK_ENUM_ENTRY");

		export const ChangeEnumEntry = new SimpleActionType<{
			fieldId: string,
			entryId: string
			name?: string | null,
			prevalence?: number | null
		}>(
			"CHANGE_ENUM_ENTRY");

		export const DeleteEnumEntry = new SimpleActionType<{
			fieldId: string,
			entryId: string
		}>(
			"DELETE_ENUM_ENTRY");
	}
}

export const StartGeneratingData = new SimpleActionType<{}>(
	"START_GENERATING_DATA");
