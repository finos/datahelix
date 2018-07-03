import * as React from "react";
import {connect} from "react-redux";
import {Dispatch} from "redux";

import {Button, ButtonProps, Icon} from "semantic-ui-react";
import {AddBlankField} from "../redux/actions/Actions";

function mapDispatchToProps(dispatch: Dispatch): ButtonProps
{
	return {
		onClick: () => {
			dispatch(AddBlankField.create({}));
		},
		icon: true,
		content: <Icon name="plus" />
	};
}

const WrappedComponent = connect(undefined, mapDispatchToProps)(Button);

export default WrappedComponent;
