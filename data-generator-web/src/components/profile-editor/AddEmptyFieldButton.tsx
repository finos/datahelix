import * as React from "react";
import {connect} from "react-redux";
import {Button, ButtonProps} from "semantic-ui-react";

import Actions from "../../redux/actions/index";

const WrappedComponent = connect<ButtonProps, ButtonProps, ButtonProps>(
	undefined,
	dispatch => ({
		onClick: () => dispatch(Actions.Fields.AddBlankField.create({}))
	}))
	(Button);

export default WrappedComponent;
