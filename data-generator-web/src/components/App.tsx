import * as React from 'react';

import Button from "./Button";
import ClearProfileButton from "./ClearProfileButton";
import CurrentProfileEditor from './CurrentProfileEditor';
import ExportProfileButton from "./ExportProfileButton";

class App extends React.Component {
	public render(): React.ReactNode {
		return (
			<main style={{ border: "1px solid black", maxWidth: "1024px" }}>
				<header>
					<h1>Data Generator</h1>
				</header>
				<div>
					<ClearProfileButton />
					<Button title="Load Profile" />
					<ExportProfileButton />
					<Button title="Generate Data" />
				</div>
				<CurrentProfileEditor />
			</main>
		);
	}
}

export default App;
