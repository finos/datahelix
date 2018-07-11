/** The root state of the application */
export interface IAppState
{
	/** The profile currently selected for display in the editor */
	readonly currentProfile?: IProfileState;

	/** The ID of the modal dialog (if any) that's currently displayed */
	readonly currentModal?: ModalIds;
}

/** The IDs of all modals known to/managed by Redux */
export type ModalIds = "start_profiling_from_file";

export interface IProfileState
{
	readonly fields: IFieldState[];
}

export interface IFieldState
{
	/** The internal ID of the field; not exposed by interface or included in profile files */
	readonly id: string;
	readonly name: string;
	/** What proportion of this field is nulls. 0 <= x <= 1 */
	readonly nullPrevalence: number;

	/** A description of the content of the field (eg, numeric normal distribution etc) */
	readonly restrictions: AnyFieldRestriction;
}

/** The union of all possible types of restrictions */
export type AnyFieldRestriction = INumericRestrictions | IEnumRestrictions | IStringRestrictions | ITemporalRestrictions | IUnclassifiedRestrictions;

/**
 * A barebones description of what a restriction object looks like.
 * Looser version of AnyFieldRestriction, mostly for use in eg generics
 */
export interface IRestrictions <T extends FieldKinds> {
	readonly kind: T;
}

/** Restriction: Numeric data in a range-clamped normal distribution */
export interface INumericRestrictions extends IRestrictions<FieldKinds.Numeric> {
	readonly kind: FieldKinds.Numeric;

	readonly meanAvg: number | null;
	readonly stdDev: number | null;
	readonly minimumValue: number | null;
	readonly maximumValue: number | null;
}

/** Restriction: Weighted selection from a set of defined options */
export interface IEnumRestrictions extends IRestrictions<FieldKinds.Enum> {
	readonly kind: FieldKinds.Enum;

	readonly members: IEnumMember[];
}

export interface IEnumMember {
	/** Internal-only ID */
	readonly id: string;
	readonly name: string;
	readonly prevalence: number;
}

/** Restriction: Garbled string data, completely randomly generated character-by-character */
export interface IStringRestrictions extends IRestrictions<FieldKinds.String> {
	readonly kind: FieldKinds.String;

	readonly allowableCharacters: string | null;
	readonly minimumLength: number | null;
	readonly maximumLength: number | null;
}

/** Functionally pointless, but more descriptive than just "string" */
export type DateTimeString = string;

/** Restriction: Timestamps uniformly distributed across a range */
export interface ITemporalRestrictions extends IRestrictions<FieldKinds.Temporal> {
	readonly kind: FieldKinds.Temporal;

	readonly minimum: DateTimeString | null;
	readonly maximum: DateTimeString | null;
}

/** Placeholder when no real restriction is known - not sure it's used in practice */
export interface IUnclassifiedRestrictions extends IRestrictions<FieldKinds.Unclassified> {
	readonly kind: FieldKinds.Unclassified;
}

export enum FieldKinds
{
	Unclassified,
	String,
	Numeric,
	Enum,
	Temporal
}


/**
 * An object that can be applied over a field state to effect specific modifications.
 *
 * @remarks
 * This is almost the same as DeepPartial<IFieldState>, but it has the additional restriction that
 * if a restriction is specified, it MUST have a kind property
 */
export type IFieldStatePatch =
	Partial<IFieldState> & { restrictions?: AnyFieldRestrictionsPatch }

export type AnyFieldRestrictionsPatch =
	Partial<AnyFieldRestriction> & Pick<AnyFieldRestriction, "kind">

export type IRestrictionsPatch<U extends FieldKinds, T extends IRestrictions<U>> =
	Partial<T> & Pick<T, "kind">
