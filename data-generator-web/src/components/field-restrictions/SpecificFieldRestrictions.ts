import {
	connect,
	MapDispatchToPropsFunction
} from "react-redux";
import {Dispatch} from "redux";
import * as React from "react";

import {UpdateField} from "../../redux/actions/Actions";
import {AnyFieldRestriction, FieldKinds} from "../../redux/state/IAppState";
import NumericFieldRestriction from "./NumericFieldRestriction";
import StringFieldRestriction from "./StringFieldRestriction";

interface IProps
{
	fieldId: string;
}

function getMapDispatchToProps<T>(
	getRestriction: (value: T) => Partial<AnyFieldRestriction>)
	: MapDispatchToPropsFunction<{onChange: (newValue: T) => void}, IProps>
{
	return (dispatch: Dispatch, ownProps: IProps) =>
	{
		return {
			onChange: newValue => dispatch(UpdateField.create({
				fieldId: ownProps.fieldId,
				newValues: {
					restrictions: getRestriction(newValue)
				}
			}))
		}
	}
}

function createNumericFieldRestriction(
	title: string,
	getRestriction: (value: number) => Partial<AnyFieldRestriction>)
	: React.ComponentClass<IProps>
{
	return connect(
		() => ({ title }), // bit funky since the output is static and not a function of redux state - is there a better way to do this?
		getMapDispatchToProps(getRestriction))
		(NumericFieldRestriction);
}

function createStringFieldRestriction(
	title: string,
	getRestriction: (value: string) => Partial<AnyFieldRestriction>)
	: React.ComponentClass<IProps>
{
	return connect(
		() => ({ title }), // bit funky since the output is static and not a function of redux state - is there a better way to do this?
		getMapDispatchToProps(getRestriction))
		(StringFieldRestriction);
}

export const SpecificFieldRestrictions = createNumericFieldRestriction(
	"Standard Deviation",
	newValue => ({ kind: FieldKinds.Numeric, stdDev: newValue }));

export const MeanFieldRestriction = createNumericFieldRestriction(
	"Mean",
	newValue => ({ kind: FieldKinds.Numeric, meanAvg: newValue }));

export const MinimumValueFieldRestriction = createNumericFieldRestriction(
	"Minimum Value",
	newValue => ({ kind: FieldKinds.Numeric, minimumValue: newValue }));

export const MaximumValueFieldRestriction = createNumericFieldRestriction(
	"Maximum Value",
	newValue => ({ kind: FieldKinds.Numeric, maximumValue: newValue }));


export const MinimumStringLengthFieldRestriction = createNumericFieldRestriction(
	"Minimum String Length",
	newValue => ({ kind: FieldKinds.String, minimumLength: newValue }));

export const MaximumStringLengthFieldRestriction = createNumericFieldRestriction(
	"Maximum String Length",
	newValue => ({ kind: FieldKinds.String, maximumLength: newValue }));

export const AllowableCharactersFieldRestriction = createStringFieldRestriction(
	"Allowable Characters",
	newValue => ({ kind: FieldKinds.String, allowableCharacters: newValue }));
