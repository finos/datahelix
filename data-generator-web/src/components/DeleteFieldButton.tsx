import {connect} from "react-redux";
import {Dispatch} from "redux";

import * as React from "react";
import {Button, ButtonProps, Icon} from "semantic-ui-react";
import {DeleteField} from "../redux/actions/Actions";

interface IProps
{
	fieldId: string;
}

function mapDispatchToProps(dispatch: Dispatch, ownProps: IProps): ButtonProps
{
	return {
		onClick: () => {
			dispatch(DeleteField.create({ fieldId: ownProps.fieldId }));
		},
		icon: true,
		content: <Icon name="trash" />
	};
}

const WrappedComponent = connect(
	undefined,
	mapDispatchToProps,
	(s: ButtonProps, d, _) => ({ ...s, ...d })) // so that ownProps don't get mixed in, as by default
	(Button);

export default WrappedComponent;
