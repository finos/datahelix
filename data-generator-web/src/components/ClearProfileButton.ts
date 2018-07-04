import {connect} from "react-redux";
import {Button, ButtonProps} from "semantic-ui-react";

import {ClearCurrentProfile} from "../redux/actions/Actions";

const WrappedComponent = connect<ButtonProps, ButtonProps, ButtonProps>(
	undefined,
	dispatch => ({
		onClick: () => dispatch(ClearCurrentProfile.create({}))
	}))
	(Button);

export default WrappedComponent;
