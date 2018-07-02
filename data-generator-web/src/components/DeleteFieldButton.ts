import {connect} from "react-redux";
import {Dispatch} from "redux";

import {DeleteField} from "../redux/actions/Actions";
import Button, { IProps as ButtonProps } from "./Button";

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
		title: "X",
	};
}

const WrappedComponent = connect(undefined, mapDispatchToProps)(Button);

export default WrappedComponent;
