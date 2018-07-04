import * as React from "react";
import {connect} from "react-redux";
import {Button, ButtonProps} from "semantic-ui-react";

import {AddBlankField} from "../redux/actions/Actions";

const WrappedComponent = connect<ButtonProps, ButtonProps, ButtonProps>(
	undefined,
	dispatch => ({
		onClick: () => dispatch(AddBlankField.create({}))
	}))
	(Button);

export default WrappedComponent;
