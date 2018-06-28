import * as React from 'react';
import * as ReactDOM from 'react-dom';
import App from './components/App';
import registerServiceWorker from './registerServiceWorker';

export default class Bootstrapper
{
	public start()
	{
		ReactDOM.render(
			<App />,
			document.getElementById('root') as HTMLElement
		);
		registerServiceWorker();
	}
}
