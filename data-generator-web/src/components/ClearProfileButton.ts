import {connect} from "react-redux";
import {Dispatch} from "redux";

import {ClearCurrentProfile} from "../redux/actions/Actions";
import Button, { IProps as ButtonProps } from "./Button";


function mapDispatchToProps(dispatch: Dispatch): ButtonProps
{
	return {
		onClick: () => {
			dispatch(ClearCurrentProfile.create({}));
		},
		title: "New Profile",
	};
}

const WrappedComponent = connect(undefined, mapDispatchToProps)(Button);

export default WrappedComponent;
