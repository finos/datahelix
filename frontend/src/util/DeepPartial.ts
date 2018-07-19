// stolen from:
// https://stackoverflow.com/questions/45372227/how-to-implement-typescript-deep-partial-mapped-type-not-breaking-array-properti

/* tslint:disable */
type DeepPartial<T> = {
	[P in keyof T]?:
	T[P] extends Array<infer U>
		? Array<DeepPartial<U>>
		: T[P] extends ReadonlyArray<infer U>
			? ReadonlyArray<DeepPartial<U>>
			: DeepPartial<T[P]>
};
/* tslint:enable */

export default DeepPartial;
