import {connect} from "react-redux";
import {Dispatch} from "redux";

import * as React from "react";
import {Button, ButtonProps, InputProps} from "semantic-ui-react";
import {DeleteField} from "../redux/actions/Actions";

interface IProps extends ButtonProps
{
	fieldId: string;
}

function mapDispatchToProps(dispatch: Dispatch, ownProps: IProps): ButtonProps
{
	return {
		onClick: () => {
			dispatch(DeleteField.create({ fieldId: ownProps.fieldId }));
		}
	};
}

const WrappedComponent = connect<ButtonProps, ButtonProps, IProps>(
	undefined,
	mapDispatchToProps,
	(
		s: InputProps,
		d: InputProps,
		{ fieldId, ...rest }: IProps
	) => ({ ...s, ...d, ...rest })) // don't pass fieldId prop down (Redux passes ownProps down by default)
	(Button);

export default WrappedComponent;
