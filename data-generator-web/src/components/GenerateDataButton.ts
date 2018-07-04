import {connect} from "react-redux";
import {Dispatch} from "redux";

import {Button, ButtonProps} from "semantic-ui-react";
import Actions from "../redux/actions";

function mapDispatchToProps(dispatch: Dispatch): ButtonProps
{
	return {
		onClick: () => {
			dispatch(Actions.StartGeneratingData.create({}));
		}
	};
}

const WrappedComponent = connect<ButtonProps, ButtonProps, ButtonProps>(
	undefined,
	mapDispatchToProps)
(Button);

export default WrappedComponent;
