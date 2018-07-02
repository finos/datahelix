export interface IAppState
{
	readonly currentProfile?: IProfileState;
}

export interface IProfileState
{
	readonly fields: IFieldState[];

}

export interface IFieldState
{
	readonly id: string;
	readonly name?: string;
	readonly nullPrevalence: number;
	readonly restrictions: AnyFieldRestriction;
}

export type AnyFieldRestriction = INumericRestrictions | IStringEnumRestrictions | IStringRestrictions;

interface IRestrictions <T extends FieldKinds> {
	readonly kind: T;
}

interface INumericRestrictions extends IRestrictions<FieldKinds.Numeric>{
	readonly meanAvg?: number;
	readonly stdDev?: number;
	readonly minimumValue?: number;
	readonly maximumValue?: number;
}

interface IStringEnumRestrictions extends IRestrictions<FieldKinds.Enum> {
	readonly enumValues: IEnumValue[];
}

interface IEnumValue {
	readonly name: string;
	readonly prevalence: number;
	readonly comment?: string;
}

interface IStringRestrictions extends IRestrictions<FieldKinds.String> {
	readonly allowableCharacters?: string;
	readonly minimumLength?: number;
	readonly maximumLength?: number;
}

export enum FieldKinds
{
	String,
	Numeric,
	Enum
}
