import * as React from "react";
import {connect} from "react-redux";
import {Dispatch} from "redux";
import {ActionType} from "../redux/actions/ActionType";

interface IClickableProps
{
	onClick?: (...args: any[]) => void
}

export default function dispatchesActionOnClick<T extends IClickableProps>(
	actionType: ActionType<{}, any>,
	subElement: React.ComponentType<T>)
	: React.ComponentType<T>
{
	return connect<{}, {}, T, T>(
		undefined,
		(dispatch: Dispatch) => (<T><any>{
			onClick: () => { dispatch(actionType.create({})) }
		}))
	(subElement);
}
