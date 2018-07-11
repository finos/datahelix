import generateUniqueString from "../../util/generateUniqueString";
import {FieldKinds, IFieldStatePatch, IProfileState, ModalId} from "../state/IAppState";
import {MappingActionType, SimpleActionType} from "./ActionType";

export namespace Profiles {
	/** Replace the current profile with the specified one */
	export const SetCurrentProfile = new SimpleActionType<{
		newProfile: IProfileState
	}>(
		"SET_CURRENT_PROFILE"
	);

	/** Replace the current profile with an empty profile */
	export const ClearCurrentProfile = new SimpleActionType<{}>(
		"CLEAR_CURRENT_PROFILE");

	/** Initiate the process by which the user imports a profile from file */
	export const TriggerImportProfile = new SimpleActionType<{}>(
		"TRIGGER_IMPORT_PROFILE"); // caught by SideEffectsMiddleware

	/** Initiate the process by which the user exports the current profile to a file */
	export const TriggerExportProfile = new SimpleActionType<{}>(
		"TRIGGER_EXPORT_PROFILE"); // caught by SideEffectsMiddleware
}

export namespace Fields {
	/** Update a field by ID, according to a supplied 'patch' object */
	export const UpdateField = new SimpleActionType<{
		fieldId: string,
		newValues: IFieldStatePatch
	}>("UPDATE_FIELD");

	/** Switch a field's kind (and set up a new blank set of restrictions) */
	export const ChangeFieldKind = new SimpleActionType<{
		fieldId: string,
		newKind: FieldKinds
	}>("CHANGE_FIELD_KIND");

	/** Delete a specific field, targetted by ID */
	export const DeleteField = new SimpleActionType<{
		fieldId: string
	}>("DELETE_FIELD");

	/** Add a new blank field to the profile */
	export const AddBlankField = new MappingActionType<
		{},
		{ fieldId: string }>
	(
		"ADD_BLANK_FIELD",
		_ => ({ fieldId: generateUniqueString() })
	);

	export namespace Enums {
		/** Add a new blank enum member to specified enum field */
		export const AppendBlankEnumMember = new MappingActionType<
			{ fieldId: string },
			{ fieldId: string, newMemberId: string }>(
			"APPEND_BLANK_ENUM_MEMBER",
			args => ({ fieldId: args.fieldId, newMemberId: generateUniqueString() }));

		/** Change a specific enum member belonging to a specific field */
		export const ChangeEnumMember = new SimpleActionType<{
			fieldId: string,
			memberId: string
			name?: string | null,
			prevalence?: number | null
		}>(
			"CHANGE_ENUM_MEMBER");

		/** Delete a specific enum member from the field */
		export const DeleteEnumMember = new SimpleActionType<{
			fieldId: string,
			memberId: string
		}>(
			"DELETE_ENUM_MEMBER");
	}
}

export namespace Modals {
	/** Open a specific modal by ID */
	export const OpenModal = new SimpleActionType<{ modalId: ModalId }>(
		"OPEN_MODAL"); // caught by SideEffectsMiddleware

	/** Close whatever modal is currently open */
	export const CloseModal = new SimpleActionType<{}>(
		"CLOSE_MODAL");
}

/** Initiate the generation process (placeholder) */
export const StartGeneratingData = new SimpleActionType<{}>(
	"START_GENERATING_DATA"); // caught by SideEffectsMiddleware

/** Begin profiling from a file located at specified path */
export const StartProfilingDataFromFile = new SimpleActionType<{ filePath: string }>(
	"START_PROFILING_FROM_FILE"); // caught by SideEffectsMiddleware

/** Begin profiling from a database table (placeholder) */
export const StartProfilingDataFromDatabase = new SimpleActionType<{}>(
	"START_PROFILING_FROM_DATABASE"); // caught by SideEffectsMiddleware
