import {connect} from "react-redux";
import {Button, ButtonProps} from "semantic-ui-react";

import Actions from "../redux/actions";

const WrappedComponent = connect<ButtonProps, ButtonProps, ButtonProps>(
	undefined,
	dispatch => ({
		onClick: () => dispatch(Actions.Profiles.ExportProfile.create({}))
	}))
	(Button);

export default WrappedComponent;
