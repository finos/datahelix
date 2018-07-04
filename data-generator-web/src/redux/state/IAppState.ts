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
	readonly name: string;
	readonly nullPrevalence: number;
	readonly restrictions: AnyFieldRestriction;
}

export type AnyFieldRestriction = INumericRestrictions | IStringEnumRestrictions | IStringRestrictions | IUnclassifiedRestrictions;

export interface IRestrictions <T extends FieldKinds> {
	readonly kind: T;
}

export interface INumericRestrictions extends IRestrictions<FieldKinds.Numeric>{
	readonly meanAvg: number | null;
	readonly stdDev: number | null;
	readonly minimumValue: number | null;
	readonly maximumValue: number | null;
}

export interface IStringEnumRestrictions extends IRestrictions<FieldKinds.Enum> {
	readonly enumValues: IEnumValue[];
}

interface IEnumValue {
	readonly name: string;
	readonly prevalence: number;
	readonly comment: string | null;
}

export interface IStringRestrictions extends IRestrictions<FieldKinds.String> {
	readonly allowableCharacters: string | null;
	readonly minimumLength: number | null;
	readonly maximumLength: number | null;
}

export interface IUnclassifiedRestrictions extends IRestrictions<FieldKinds.Unclassified> {
}

export enum FieldKinds
{
	Unclassified,
	String,
	Numeric,
	Enum
}


// rule: If patches have restrictions, they must have a kind property
// (we previously defined patches as DeepPartial<IFieldState> but kinds shouldn't really be optional)

export type IFieldStatePatch =
	Partial<IFieldState> & { restrictions?: AnyFieldRestrictionsPatch }

export type AnyFieldRestrictionsPatch =
	Partial<AnyFieldRestriction> & Pick<AnyFieldRestriction, "kind">

export type IRestrictionsPatch<U extends FieldKinds, T extends IRestrictions<U>> =
	Partial<T> & Pick<T, "kind">

