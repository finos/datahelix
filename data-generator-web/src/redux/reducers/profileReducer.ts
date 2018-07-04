import {Action} from "redux";
import {
	AnyFieldRestriction,
	AnyFieldRestrictionsPatch, FieldKinds,
	IFieldState,
	IFieldStatePatch,
	IProfileState
} from "../state/IAppState";
import Actions from "../actions";

export default function profileReducer(
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
				{ id: action.fieldId, nullPrevalence: 0, restrictions: { kind: FieldKinds.Unclassified } }
			]
		};
	}

	if (Actions.DeleteField.is(action))
	{
		return {
			fields: oldState.fields.filter(f => f.id !== action.fieldId)
		};
	}

	if (Actions.SetCurrentProfile.is(action))
	{
		return action.newProfile;
	}

	if (Actions.ChangeFieldKind.is(action))
	{
		return {
			fields: modifyField(
				oldState.fields,
				action.fieldId,
				f => ({
					...f,
					restrictions: {
						kind: action.newKind
					} as AnyFieldRestriction
				}))
		};
	}

	if (Actions.UpdateField.is(action))
	{
		return {
			fields: modifyField(
				oldState.fields,
				action.fieldId,
				f => patchFieldState(f, action.newValues))
		};
	}

	if (Actions.ClearCurrentProfile.is(action))
	{
		return { fields: [] };
	}

	return oldState;
}

function patchRestrictions(base: AnyFieldRestriction, patch?: AnyFieldRestrictionsPatch): AnyFieldRestriction
{
	if (!patch)
		return base;

	if (base.kind !== patch.kind)
	{
		throw new Error("Changing type should be done with a separate action so sensible defaults can establish");
	}

	// the work to get the typings happy with this is prohibitive, so just cast
	return { ...base, ...patch } as AnyFieldRestriction;
}

// originally I used a || b, but that causes unintended results because eg "" is falsy
function coalesceValues<T>(a: T | undefined, b: T | undefined): T | undefined
{
	return a !== undefined ? a : b;
}

function patchFieldState(base: IFieldState, patch: IFieldStatePatch): IFieldState
{
	return {
		id: base.id,
		name: coalesceValues(patch.name, base.name),
		nullPrevalence: coalesceValues(patch.nullPrevalence, base.nullPrevalence),
		restrictions: patchRestrictions(base.restrictions, patch.restrictions)
	} as IFieldState;
}

function modifyField(oldFields: IFieldState[], fieldToModifyId: string, mutateFunc: (f: IFieldState) => IFieldState) {
	return oldFields.map(f =>
		f.id !== fieldToModifyId
			? f
			: mutateFunc(f))
}
