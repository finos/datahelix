import * as React from 'react';
import * as ReactDOM from 'react-dom';
import {Provider} from "react-redux";
import {createStore, StoreEnhancer} from "redux";

import App from './components/App';
import appReducer from "./redux/reducers/appReducer";
import {FieldKinds, IAppState} from "./redux/state/IAppState";
import registerServiceWorker from './registerServiceWorker';

function retrieveReduxDevtoolsMiddleware(): StoreEnhancer | undefined
{
	// tslint:disable-next-line:no-string-literal
	const middlewareCreator: () => StoreEnhancer = (window as any)["__REDUX_DEVTOOLS_EXTENSION__"];
	return middlewareCreator && middlewareCreator();
}

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
			retrieveReduxDevtoolsMiddleware());

		ReactDOM.render(
			<Provider store={store}>
				<App />
			</Provider>,
			document.getElementById('root') as HTMLElement
		);
		registerServiceWorker();
	}
}
