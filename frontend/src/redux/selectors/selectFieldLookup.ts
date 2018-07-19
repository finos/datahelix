import {createSelector} from "reselect";
import {IAppState, IFieldState, IProfileState} from "../state/IAppState";

/** Selects (if possible) the fields belonging to the current profile. Defaults to empty array */
export const selectCurrentProfileFields = createSelector(
	(state: IAppState) => state.currentProfile,
	(currentProfile?: IProfileState) => currentProfile ? currentProfile.fields : []);

/** Selects a lookup from field ID to field object, within the current profile */
const selectFieldLookup = createSelector(
	selectCurrentProfileFields,
	(fields: IFieldState[]) =>
	{
		const map: { [fieldId: string ]: IFieldState } = {};

		for (const f of fields)
		{
			map[f.id] = f;
		}

		return map;
	});

export default selectFieldLookup;
