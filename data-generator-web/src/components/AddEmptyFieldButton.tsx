import * as React from "react";
import {connect} from "react-redux";
import {Dispatch} from "redux";

import {AddBlankField} from "../redux/actions/Actions";
import {Button, ButtonProps, Icon} from "semantic-ui-react";

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
