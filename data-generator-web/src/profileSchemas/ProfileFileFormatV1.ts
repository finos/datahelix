import {
	AnyFieldRestriction,
	FieldKinds, IEnumMember,
	IEnumRestrictions,
	IFieldState,
	INumericRestrictions,
	IProfileState,
	IStringRestrictions, ITemporalRestrictions
} from "../redux/state/IAppState";
import generateUniqueString from "../util/generateUniqueString";
import IProfileFileFormat from "./IProfileFileFormat";

export default class ProfileFileFormatV1 implements IProfileFileFormat
{
	private static readonly schemaVersionId = "v1";

	private static readonly fieldKindToSerialisedKind: { [fieldKind: number]: Dtos.AllFieldKinds } = {
		[FieldKinds.Numeric]: "numeric",
		[FieldKinds.String]: "text",
		[FieldKinds.Enum]: "enum",
		[FieldKinds.Temporal]: "temporal"
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
	if (field.kind === "text" && field.distribution.kind === "perCharacterRandom") {
		return <IStringRestrictions>{
			kind: FieldKinds.String,
			allowableCharacters: field.distribution.alphabet,
			minimumLength: field.distribution.lengthMin,
			maximumLength: field.distribution.lengthMax
		}
	}

	if (field.kind === "numeric" && field.distribution.kind === "normal") {
		return <INumericRestrictions>{
			kind: FieldKinds.Numeric,
			meanAvg: field.distribution.meanAvg,
			stdDev: field.distribution.stdDev,
			minimumValue: field.distribution.min,
			maximumValue: field.distribution.max
		}
	}

	if (field.kind === "enum" && field.distribution.kind === "set") {
		return <IEnumRestrictions>{
			kind: FieldKinds.Enum,
			members: field.distribution.members.map<IEnumMember>(m => ({
				id: generateUniqueString(),
				name: m.name,
				prevalence: m.prevalence
			}))
		}
	}

	if (field.kind === "temporal" && field.distribution.kind === "randomDateWithinRange") {
		return <ITemporalRestrictions>{
			kind: FieldKinds.Temporal,
			minimum: field.distribution.min,
			maximum: field.distribution.max
		}
	}

	throw new Error("Can't deserialise distribution");
}

function serialiseRestriction(restriction: AnyFieldRestriction): Dtos.AnyDistribution
{
	if (restriction.kind === FieldKinds.Numeric) {
		return <Dtos.INormalDistribution> {
			kind: "normal",
			meanAvg: restriction.meanAvg,
			stdDev: restriction.stdDev,
			min: restriction.minimumValue,
			max: restriction.maximumValue
		}
	}

	if (restriction.kind === FieldKinds.String) {
		return <Dtos.IRandomCharactersDistribution> {
			kind: "perCharacterRandom",
			alphabet: restriction.allowableCharacters,
			lengthMin: restriction.minimumLength,
			lengthMax: restriction.maximumLength
		}
	}

	if (restriction.kind === FieldKinds.Enum) {
		return <Dtos.ISetDistribution> {
			kind: "set",
			members: restriction.members.map<Dtos.ISetMember>(member => ({
				name: member.name,
				prevalence: member.prevalence
			}))
		}
	}

	if (restriction.kind === FieldKinds.Temporal) {
		return <Dtos.IRandomDateWithinRangeDistribution> {
			kind: "randomDateWithinRange",
			min: restriction.minimum,
			max: restriction.maximum
		}
	}


	throw new Error("Unable to serialise restriction of type: " + restriction.kind);
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

	export type AnyDistribution = INormalDistribution | IRandomCharactersDistribution | ISetDistribution | IRandomDateWithinRangeDistribution;

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

	export interface IRandomDateWithinRangeDistribution {
		kind: "randomDateWithinRange";
		min: string;
		max: string;
	}

	export interface ISetMember {
		name: string;
		prevalence: number;
	}
}
