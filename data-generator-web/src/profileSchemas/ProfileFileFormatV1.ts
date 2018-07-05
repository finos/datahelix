import {
	AnyFieldRestriction,
	FieldKinds,
	IFieldState,
	INumericRestrictions,
	IProfileState,
	IStringRestrictions
} from "../redux/state/IAppState";
import generateUniqueString from "../util/generateUniqueString";
import IProfileFileFormat from "./IProfileFileFormat";

export default class ProfileFileFormatV1 implements IProfileFileFormat
{
	private static readonly schemaVersionId = "v1";

	private static readonly fieldKindToSerialisedKind: { [fieldKind: number]: Dtos.AllFieldKinds } = {
		[FieldKinds.Numeric]: "numeric",
		[FieldKinds.String]: "text"
	};

	public canDeserialiseFrom(rawObject: {}): boolean {
		return (rawObject as Dtos.IProfile).schemaVersion === ProfileFileFormatV1.schemaVersionId;
	}

	public serialiseProfile(state: IProfileState): {} {
		return <Dtos.IProfile>{
			schemaVersion: ProfileFileFormatV1.schemaVersionId,
			fields:
				state.fields.map<Dtos.IField>(f =>
					({
						name: f.name,
						kind: ProfileFileFormatV1.fieldKindToSerialisedKind[f.restrictions.kind],
						nullPrevalence: f.nullPrevalence,
						distribution: serialiseRestriction(f.restrictions)
					}))
		};
	}

	public deserialiseProfile(rawObject: {}): IProfileState	{
		const typedRawObject = rawObject as Dtos.IProfile;
		return {
			fields:
				typedRawObject.fields.map<IFieldState>(f =>
					({
						id: generateUniqueString(),
						name: f.name,
						nullPrevalence: f.nullPrevalence,
						restrictions: deserialiseRestriction(f)
					}))
		};
	}
}


function deserialiseRestriction(field: Dtos.IField): AnyFieldRestriction
{
	if (field.kind === "text" && field.distribution.kind === "perCharacterRandom")
	{
		return <IStringRestrictions>{
			kind: FieldKinds.String,
			allowableCharacters: field.distribution.alphabet,
			minimumLength: field.distribution.lengthMin,
			maximumLength: field.distribution.lengthMax
		}
	}

	if (field.kind === "numeric" && field.distribution.kind === "normal")
	{
		return <INumericRestrictions>{
			kind: FieldKinds.Numeric,
			meanAvg: field.distribution.meanAvg,
			stdDev: field.distribution.stdDev,
			minimumValue: field.distribution.min,
			maximumValue: field.distribution.max
		}
	}

	throw new Error("Can't deserialise distribution");
}

function serialiseRestriction(restriction: AnyFieldRestriction): Dtos.AnyDistribution
{
	if (isNumericRestrictions(restriction))
	{
		return <Dtos.INormalDistribution> {
			kind: "normal",
			meanAvg: restriction.meanAvg,
			stdDev: restriction.stdDev,
			min: restriction.minimumValue,
			max: restriction.maximumValue
		}
	}

	if (isStringRestrictions(restriction))
	{
		return <Dtos.IRandomCharactersDistribution> {
			kind: "perCharacterRandom",
			alphabet: restriction.allowableCharacters,
			lengthMin: restriction.minimumLength,
			lengthMax: restriction.maximumLength
		}
	}

	throw new Error("Unable to serialise restriction of type: " + restriction.kind);
}



function isNumericRestrictions(field: AnyFieldRestriction): field is INumericRestrictions {
	return field.kind === FieldKinds.Numeric;
}

function isStringRestrictions(field: AnyFieldRestriction): field is IStringRestrictions {
	return field.kind === FieldKinds.String;
}

namespace Dtos {
	export interface IProfile {
		schemaVersion: string;
		fields: IField[];
	}

	export type AllFieldKinds = "numeric" | "text" | "enum" | "temporal";

	export interface IField {
		name: string;
		kind: AllFieldKinds;
		nullPrevalence: number;
		distribution: AnyDistribution;
		//format
	}

	export type AnyDistribution = INormalDistribution | IRandomCharactersDistribution | ISetDistribution;

	export interface INormalDistribution {
		kind: "normal";
		meanAvg: number;
		stdDev: number;
		min: number;
		max: number;
	}

	export interface IRandomCharactersDistribution {
		kind: "perCharacterRandom";
		alphabet: string;
		lengthMin: number;
		lengthMax: number;
	}

	export interface ISetDistribution {
		kind: "set";
		members: ISetMember[];
	}

	export interface ISetMember {
		name: string;
		prevalence: number;
	}
}
