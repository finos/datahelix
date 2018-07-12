import {connect} from "react-redux";
import {createSelector} from "reselect";

import {selectCurrentProfileFields} from "../../redux/selectors/selectFieldLookup";
import {IAppState, IFieldState} from "../../redux/state/IAppState";
import QuickJumpMenu, {IProps as QuickJumpMenuProps} from "./QuickJumpMenu";

export const selectFieldsViewModel = createSelector(
	selectCurrentProfileFields,
	(fields: IFieldState[]) => fields.map(f => ({
		id: f.id,
		name: f.name || "Unnamed field"
	})));

function mapStateToProps(state: IAppState): QuickJumpMenuProps
{
	return { fields: selectFieldsViewModel(state) };
}

const WrappedComponent = connect(mapStateToProps)(QuickJumpMenu);

export default WrappedComponent;
