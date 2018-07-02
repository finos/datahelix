import * as React from 'react';
import * as ReactDOM from 'react-dom';
import {Provider} from "react-redux";
import {createStore} from "redux";

import App from './components/App';
import appReducer from "./redux/reducers/appReducer";
import {FieldKinds, IAppState} from "./redux/state/IAppState";
import registerServiceWorker from './registerServiceWorker';

export default class Bootstrapper
{
	public start()
	{
		// tslint:disable-next-line:no-string-literal
		const reduxDevtoolsMiddleware = window["__REDUX_DEVTOOLS_EXTENSION__"] && window["__REDUX_DEVTOOLS_EXTENSION__"]();

		const defaultState: IAppState = {
			currentProfile: {
				fields: [
					{
						id: "1",
						name: "description",
						nullPrevalence: 0,
						restrictions: {
							kind: FieldKinds.String
						}
					},
					{
						id: "2",
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
			reduxDevtoolsMiddleware);

		ReactDOM.render(
			<Provider store={store}>
				<App />
			</Provider>,
			document.getElementById('root') as HTMLElement
		);
		registerServiceWorker();
	}
}
