import { connect } from "react-redux";

import Actions from "../../../redux/actions/index";
import {
	FieldKinds,
	IAppState,
	INumericRestrictions, IRestrictions, IRestrictionsPatch,
	IStringRestrictions, ITemporalRestrictions
} from "../../../redux/state/IAppState";

import {ComponentType} from "react";
import selectFieldLookup from "../../../redux/selectors/selectFieldLookup";
import {IProps as IInputProps, NumericInput, StringInput} from "./Inputs";

interface IProps
{
	fieldId: string;
}

function createReduxMappedInput<
		TFieldKind extends FieldKinds,
		TRestriction extends IRestrictions<TFieldKind>,
		TValue extends number | string>(
	component: ComponentType<IInputProps<TValue>>,
	getValue: (restriction: TRestriction) => TValue | null,
	getRestrictionPatch: (newValue: TValue | null) => IRestrictionsPatch<TFieldKind, TRestriction>)
{
	return connect<IInputProps<TValue>, IInputProps<TValue>, IProps, IAppState>(
		(state, ownProps) => {
			const field = selectFieldLookup(state)[ownProps.fieldId];

			return { value: getValue(field.restrictions as TRestriction) };
		},
		(dispatch, ownProps: IProps) => {
			return {
				onChange: newValue => dispatch(Actions.Fields.UpdateField.create({
					fieldId: ownProps.fieldId,
					newValues: {
						restrictions: getRestrictionPatch(newValue)
					} as any // because I spent half an hour wrestling with the typings :(
				}))
			}
		}
	)(component);
}

export const StandardDeviationRestriction = createReduxMappedInput(
	NumericInput,
	(r: INumericRestrictions) => r.stdDev,
	v => ({ kind: FieldKinds.Numeric, stdDev: v }));

export const MeanFieldRestriction = createReduxMappedInput(
	NumericInput,
	(r: INumericRestrictions) => r.meanAvg,
	v => ({ kind: FieldKinds.Numeric, meanAvg: v }));

export const MinimumValueFieldRestriction = createReduxMappedInput(
	NumericInput,
	(r: INumericRestrictions) => r.minimumValue,
	v => ({ kind: FieldKinds.Numeric, minimumValue: v }));

export const MaximumValueFieldRestriction = createReduxMappedInput(
	NumericInput,
	(r: INumericRestrictions) => r.maximumValue,
	v => ({ kind: FieldKinds.Numeric, maximumValue: v }));


export const MinimumStringLengthFieldRestriction = createReduxMappedInput(
	NumericInput,
	(r: IStringRestrictions) => r.minimumLength,
	v => ({ kind: FieldKinds.String, minimumLength: v }));

export const MaximumStringLengthFieldRestriction = createReduxMappedInput(
	NumericInput,
	(r: IStringRestrictions) => r.maximumLength,
	v => ({ kind: FieldKinds.String, maximumLength: v }));

export const AllowableCharactersFieldRestriction = createReduxMappedInput(
	StringInput,
	(r: IStringRestrictions) => r.allowableCharacters,
	v => ({ kind: FieldKinds.String, allowableCharacters: v }));


export const TemporalRangeStartFieldRestriction = createReduxMappedInput(
	StringInput,
	(r: ITemporalRestrictions) => r.minimum,
	v => ({ kind: FieldKinds.Temporal, minimum: v }));

export const TemporalRangeEndFieldRestriction = createReduxMappedInput(
	StringInput,
	(r: ITemporalRestrictions) => r.maximum,
	v => ({ kind: FieldKinds.Temporal, maximum: v }));