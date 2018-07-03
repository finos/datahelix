import {connect} from "react-redux";
import {Dispatch} from "redux";

import {DeleteField} from "../redux/actions/Actions";
import {Button, ButtonProps, Icon} from "semantic-ui-react";
import * as React from "react";

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

const WrappedComponent = connect(undefined, mapDispatchToProps)(Button);

export default WrappedComponent;
