import * as React from "react";
import {connect} from "react-redux";
import {Action, Dispatch} from "redux";

interface IClickableProps
{
	onClick?: (...args: any[]) => void
}

export default function dispatchesActionOnClick<T extends IClickableProps>(
	createAction: () => Action,
	subElement: React.ComponentType<T>)
	: React.ComponentType<T>
{
	return connect<{}, {}, T, T>(
		undefined,
		(dispatch: Dispatch) => (<T><any>{
			onClick: () => { dispatch(createAction()) }
		}))
	(subElement);
}
