import {Action} from "redux";
import generateUniqueString from "../../util/generateUniqueString";
import Actions from "../actions";
import {
	AnyFieldRestriction,
	AnyFieldRestrictionsPatch,
	FieldKinds,
	IEnumMember, IEnumRestrictions,
	IFieldState,
	IFieldStatePatch
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
			[FieldKinds.Enum]: <IEnumRestrictions>{ kind: FieldKinds.Enum, members: [] }
		};

		return modifyItemById(
			oldState,
			action.fieldId,
			f => f.restrictions.kind === action.newKind
				? f // no need to do anything - we're already the right kind
				: {
					...f,
					restrictions: fieldKindToDefaultInitialState[action.newKind] || { kind: action.newKind }
				});
	}

	if (Actions.Fields.UpdateField.is(action))
	{
		return modifyItemById(
			oldState,
			action.fieldId,
			f => FieldPatching.applyPatch(f, action.newValues));
	}

	if (
		Actions.Fields.Enums.AppendBlankEnumMember.is(action) ||
		Actions.Fields.Enums.DeleteEnumMember.is(action) ||
		Actions.Fields.Enums.ChangeEnumMember.is(action))
	{
		return modifyItemById(
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
	if (Actions.Fields.Enums.AppendBlankEnumMember.is(action)) {
		return {
			...oldState,
			members: [
				...oldState.members,
				{
					id: generateUniqueString(),
					name: "",
					prevalence: oldState.members.length === 0 ? 1 : 0
				}
			]
		};
	}

	if (Actions.Fields.Enums.DeleteEnumMember.is(action)) {
		return {
			...oldState,
			members: rebalanceEnumMemberPrevalences(
				oldState.members.filter(member => member.id !== action.memberId),
				action.memberId)
		};
	}

	if (Actions.Fields.Enums.ChangeEnumMember.is(action)) {
		const unrebalancedNewValues = modifyItemById(
			oldState.members,
			action.memberId,
			member => ({
				...member,
				prevalence: oldState.members.length > 1
					? action.prevalence || member.prevalence
					: member.prevalence,
				name: action.name || member.name
			}));

		return {
			...oldState,
			members: action.prevalence !== undefined
				? rebalanceEnumMemberPrevalences(unrebalancedNewValues, action.memberId)
				:  unrebalancedNewValues
		};
	}

	return oldState;
}

function rebalanceEnumMemberPrevalences(
	members: IEnumMember[],
	invariantMemberId: string) // eg, don't rebalance prevalence on something the user just edited
	: IEnumMember[]
{
	const prevalanceTotals = members.reduce(
		(prevTotals, enumMember) => {
			const newTotals = { ...prevTotals };

			if (enumMember.id === invariantMemberId)
				newTotals.invariantTotal += enumMember.prevalence;
			else
				newTotals.variantTotal += enumMember.prevalence;

			return newTotals;
		},
		{ variantTotal: 0, invariantTotal: 0 });

	const distanceFromBalance = 1 - (prevalanceTotals.invariantTotal + prevalanceTotals.variantTotal);
	if (distanceFromBalance === 1)
		return members;

	const getRebalanceAmount: (n: number) => number =
		prevalanceTotals.variantTotal === 0
			? (p: number) => (1 / (members.length - 1))
			: (p: number) => p / prevalanceTotals.variantTotal;

	return members.map(e => ({
		...e,
		prevalence: e.id === invariantMemberId
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

function modifyItemById<T extends { id: string }>(
	oldEntries: T[],
	idOfItemToModify: string,
	mutateFunc: (f: T) => T)
	: T[]
{
	return oldEntries.map(f =>
		f.id !== idOfItemToModify
			? f
			: mutateFunc(f))
}
