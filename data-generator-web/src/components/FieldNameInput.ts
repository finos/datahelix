import {connect} from "react-redux";
import {Dispatch} from "redux";

import {Input, InputProps} from "semantic-ui-react";
import Actions from "../redux/actions";
import selectFieldLookup from "../redux/selectors/selectFieldLookup";
import {IAppState} from "../redux/state/IAppState";

interface IProps extends InputProps
{
	fieldId: string;
}

function mapStateToProps(state: IAppState, ownProps: IProps): InputProps
{
	const fieldState = selectFieldLookup(state)[ownProps.fieldId];

	return {
		value: fieldState.name
	};
}

function mapDispatchToProps(dispatch: Dispatch, ownProps: IProps): InputProps
{
	return {
		onChange: (event, data) => {
			dispatch(Actions.Fields.UpdateField.create({
				fieldId: ownProps.fieldId,
				newValues: { name: data.value }
			}));
		}
	};
}

const WrappedComponent = connect(
	mapStateToProps,
	mapDispatchToProps,
	(
		s: InputProps,
		d: InputProps,
		{ fieldId, ...rest }: IProps
	) => ({ ...s, ...d, ...rest })) // don't pass fieldId prop down (Redux passes down by default)
(Input);

export default WrappedComponent;
