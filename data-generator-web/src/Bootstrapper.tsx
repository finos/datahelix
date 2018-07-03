import * as React from 'react';
import * as ReactDOM from 'react-dom';
import {Provider} from "react-redux";
import {applyMiddleware, createStore} from "redux";
import { composeWithDevTools } from 'redux-devtools-extension';

import App from './components/App';
import sideEffectsMiddleware from "./redux/middleware/SideEffectsMiddleware";
import appReducer from "./redux/reducers/appReducer";
import {FieldKinds, IAppState} from "./redux/state/IAppState";
import registerServiceWorker from './registerServiceWorker';

export default class Bootstrapper
{
	public start(): void
	{
		const defaultState: IAppState = {
			currentProfile: {
				fields: [
					{
						id: "aaaa",
						name: "description",
						nullPrevalence: 0,
						restrictions: {
							kind: FieldKinds.String
						}
					},
					{
						id: "bbbb",
						name: "price",
						nullPrevalence: 0,
						restrictions: {
							kind: FieldKinds.Numeric,
							meanAvg: 1,
							stdDev: 1
						}
					}
				]
			}
		};

		const store = createStore(
			appReducer,
			defaultState,
			composeWithDevTools(
				applyMiddleware(
					sideEffectsMiddleware)));

		ReactDOM.render(
			<Provider store={store}>
				<App />
			</Provider>,
			document.getElementById('root') as HTMLElement
		);
		registerServiceWorker();
	}
}
