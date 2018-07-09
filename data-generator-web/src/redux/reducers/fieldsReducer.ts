import {Action} from "redux";
import generateUniqueString from "../../util/generateUniqueString";
import Actions from "../actions";
import {
	AnyFieldRestriction,
	AnyFieldRestrictionsPatch,
	FieldKinds,
	IEnumRestrictions, IEnumValue,
	IFieldState,
	IFieldStatePatch, INumericRestrictions, IStringRestrictions
} from "../state/IAppState";

export default function fieldsReducer(
	oldState: IFieldState[] | undefined,
	action: Action)
	: IFieldState[]
{
	if (!oldState)
		return [];

	if (Actions.Fields.AddBlankField.is(action))
	{
		return [
			...oldState,
			{
				id: action.fieldId,
				name: "",
				nullPrevalence: 0,
				restrictions: { kind: FieldKinds.Unclassified }
			}
		];
	}

	if (Actions.Fields.DeleteField.is(action))
	{
		return oldState.filter(f => f.id !== action.fieldId);
	}

	if (Actions.Fields.ChangeFieldKind.is(action))
	{
		const fieldKindToDefaultInitialState: { [ fieldKinds: number ]: AnyFieldRestriction } = {
			[FieldKinds.Enum]: <IEnumRestrictions>{ kind: FieldKinds.Enum, enumValues: [] }
		};

		return modifyEntry(
			oldState,
			action.fieldId,
			f => ({
				...f,
				restrictions: fieldKindToDefaultInitialState[action.newKind] || { kind: action.newKind }
			}));
	}

	if (Actions.Fields.UpdateField.is(action))
	{
		return modifyEntry(
			oldState,
			action.fieldId,
			f => FieldPatching.applyPatch(f, action.newValues));
	}

	if (
		Actions.Fields.Enums.CreateBlankEnumEntry.is(action) ||
		Actions.Fields.Enums.DeleteEnumEntry.is(action) ||
		Actions.Fields.Enums.ChangeEnumEntry.is(action))
	{
		return modifyEntry(
			oldState,
			action.fieldId,
			f => ({
				...f,
				restrictions: enumRestrictionsReducer(
					f.restrictions as IEnumRestrictions,
					action)
			}));
	}

	return oldState;
}

function enumRestrictionsReducer(
	oldState: IEnumRestrictions,
	action: Action)
	: IEnumRestrictions {
	if (Actions.Fields.Enums.CreateBlankEnumEntry.is(action)) {
		return {
			...oldState,
			enumValues: [
				...oldState.enumValues,
				{
					id: generateUniqueString(),
					name: "",
					prevalence: oldState.enumValues.length === 0 ? 1 : 0
				}
			]
		};
	}

	if (Actions.Fields.Enums.DeleteEnumEntry.is(action)) {
		return {
			...oldState,
			enumValues: rebalanceEnumEntryPrevalences(
				oldState.enumValues.filter(entry => entry.id !== action.entryId),
				action.entryId)
		};
	}

	if (Actions.Fields.Enums.ChangeEnumEntry.is(action)) {
		const unrebalancedNewValues = modifyEntry(
			oldState.enumValues,
			action.entryId,
			entry => ({
				...entry,
				prevalence: oldState.enumValues.length > 1
					? action.prevalence || entry.prevalence
					: entry.prevalence,
				name: action.name || entry.name
			}));

		return {
			...oldState,
			enumValues: action.prevalence !== undefined
				? rebalanceEnumEntryPrevalences(unrebalancedNewValues, action.entryId)
				:  unrebalancedNewValues
		};
	}

	return oldState;
}

function rebalanceEnumEntryPrevalences(
	entries: IEnumValue[],
	invariantEntryId: string) // eg, don't rebalance prevalence on something the user just edited
	: IEnumValue[]
{
	const prevalanceTotals = entries.reduce(
		(prevTotals, enumEntry) => {
			const newTotals = { ...prevTotals };

			if (enumEntry.id === invariantEntryId)
				newTotals.invariantTotal += enumEntry.prevalence;
			else
				newTotals.variantTotal += enumEntry.prevalence;

			return newTotals;
		},
		{ variantTotal: 0, invariantTotal: 0 });

	const distanceFromBalance = 1 - (prevalanceTotals.invariantTotal + prevalanceTotals.variantTotal);
	if (distanceFromBalance === 1)
		return entries;

	const getRebalanceAmount: (n: number) => number =
		prevalanceTotals.variantTotal === 0
			? (p: number) => (1 / (entries.length - 1))
			: (p: number) => p / prevalanceTotals.variantTotal;

	return entries.map(e => ({
		...e,
		prevalence: e.id === invariantEntryId
			? e.prevalence
			: Math.max( // tiny rounding errors can put us below zero, which puts an annoying negative sign on the screen, so clamp to zero
				e.prevalence + distanceFromBalance * getRebalanceAmount(e.prevalence),
				0)
	}));
}

namespace FieldPatching {
	function patchRestrictions(
		base: AnyFieldRestriction,
		patch?: AnyFieldRestrictionsPatch)
		: AnyFieldRestriction {
		if (!patch)
			return base;

		if (base.kind !== patch.kind) {
			throw new Error("Changing type should be done with a separate action so sensible defaults can establish");
		}

		// the work to get the typings happy with this is prohibitive, so just cast
		return {...base, ...patch} as AnyFieldRestriction;
	}

	// originally I used a || b, but that causes unintended results because eg "" is falsy
	function coalesceValues<T>(a: T | undefined, b: T | undefined): T | undefined {
		return a !== undefined ? a : b;
	}

	export function applyPatch(
		base: IFieldState,
		patch: IFieldStatePatch)
		: IFieldState {
		return {
			id: base.id,
			name: coalesceValues(patch.name, base.name),
			nullPrevalence: coalesceValues(patch.nullPrevalence, base.nullPrevalence),
			restrictions: patchRestrictions(base.restrictions, patch.restrictions)
		} as IFieldState;
	}
}

function modifyEntry<T extends { id: string }>(
	oldEntries: T[],
	idOfEntryToModify: string,
	mutateFunc: (f: T) => T)
	: T[]
{
	return oldEntries.map(f =>
		f.id !== idOfEntryToModify
			? f
			: mutateFunc(f))
}
