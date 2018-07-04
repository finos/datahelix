import {connect} from "react-redux";
import {Button, ButtonProps} from "semantic-ui-react";

import {ExportProfile} from "../redux/actions/Actions";

const WrappedComponent = connect<ButtonProps, ButtonProps, ButtonProps>(
	undefined,
	dispatch => ({
		onClick: () => dispatch(ExportProfile.create({}))
	}))
	(Button);

export default WrappedComponent;
