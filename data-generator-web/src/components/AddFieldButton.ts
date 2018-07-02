import {connect} from "react-redux";
import {Dispatch} from "redux";

import {AddBlankField} from "../redux/actions/Actions";
import Button, { IProps as ButtonProps } from "./Button";


function mapDispatchToProps(dispatch: Dispatch): ButtonProps
{
	return {
		onClick: () => {
			dispatch(AddBlankField.create({}));
		},
		title: "+",
	};
}

const WrappedComponent = connect(undefined, mapDispatchToProps)(Button);

export default WrappedComponent;
