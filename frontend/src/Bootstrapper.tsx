import * as React from "react";
import * as ReactDOM from "react-dom";
import {Provider} from "react-redux";
import {applyMiddleware, createStore, Store} from "redux";
import { composeWithDevTools } from "redux-devtools-extension";

import App from "./components/App";
import sideEffectsMiddleware from "./redux/middleware/SideEffectsMiddleware";
import appReducer from "./redux/reducers/appReducer";
import {IAppState} from "./redux/state/IAppState";
import registerServiceWorker from './registerServiceWorker';

export default class Bootstrapper
{
	public static start(
		rootNode?: HTMLElement,
		shouldRegisterServiceWorker?: boolean): void
	{
		const defaultState: IAppState = {
			currentProfile: { fields: [] }
		};

		const store: Store<IAppState> = createStore(
			appReducer,
			defaultState,
			composeWithDevTools(
				applyMiddleware(
					sideEffectsMiddleware)));

		ReactDOM.render(
			<Provider store={store}>
				<App />
			</Provider>,
			rootNode || document.querySelector("[data-react-root]"));

		if (shouldRegisterServiceWorker !== false)
			registerServiceWorker();
	}
}
