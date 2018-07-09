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

export type AnyFieldRestriction = INumericRestrictions | IEnumRestrictions | IStringRestrictions | IUnclassifiedRestrictions;

export interface IRestrictions <T extends FieldKinds> {
	readonly kind: T;
}

export interface INormalDistributionRestrictions
{
	readonly meanAvg: number | null;
	readonly stdDev: number | null;
	readonly minimumValue: number | null;
	readonly maximumValue: number | null;
}

export interface INumericRestrictions extends IRestrictions<FieldKinds.Numeric>, INormalDistributionRestrictions
{}

export interface IEnumRestrictions extends IRestrictions<FieldKinds.Enum> {
	readonly enumValues: IEnumValue[];
}

export interface IEnumValue {
	readonly id: string;
	readonly name: string;
	readonly prevalence: number;
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
	Enum,
	Temporal
}


// rule: If patches have restrictions, they must have a kind property
// (we previously defined patches as DeepPartial<IFieldState> but kinds shouldn't really be optional)

export type IFieldStatePatch =
	Partial<IFieldState> & { restrictions?: AnyFieldRestrictionsPatch }

export type AnyFieldRestrictionsPatch =
	Partial<AnyFieldRestriction> & Pick<AnyFieldRestriction, "kind">

export type IRestrictionsPatch<U extends FieldKinds, T extends IRestrictions<U>> =
	Partial<T> & Pick<T, "kind">

