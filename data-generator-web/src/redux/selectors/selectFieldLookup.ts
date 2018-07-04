import {createSelector} from "reselect";
import {IAppState, IFieldState, IProfileState} from "../state/IAppState";

export const selectCurrentProfileFields = createSelector(
	(state: IAppState) => state.currentProfile,
	(currentProfile?: IProfileState) => currentProfile ? currentProfile.fields : []);

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
