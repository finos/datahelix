import * as React from 'react';

import Button from "./Button";
import ClearProfileButton from "./ClearProfileButton";
import CurrentProfileEditor from './CurrentProfileEditor';

class App extends React.Component {
	public render(): React.ReactNode {
		return (
			<main style={{ border: "1px solid black" }}>
				<header>
					<h1>Data Generator</h1>
				</header>
				<div>
					<ClearProfileButton />
					<Button title="Load Profile" />
					<Button title="Export Profile" />
					<Button title="Generate Data" />
				</div>
				<CurrentProfileEditor />
			</main>
		);
	}
}

export default App;
