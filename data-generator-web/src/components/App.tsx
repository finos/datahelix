import * as React from 'react';
import {Container, Divider, Header} from "semantic-ui-react";

import ClearProfileButton from "./ClearProfileButton";
import CurrentProfileEditor from './CurrentProfileEditor';
import ExportProfileButton from "./ExportProfileButton";
import GenerateDataButton from "./GenerateDataButton";
import ImportProfileButton from "./ImportProfileButton";

class App extends React.Component {
	public render(): React.ReactNode {
		return (
			<Container>
				<Header as='h1'>Data Generator</Header>

				<div>
					<ClearProfileButton content="Clear Profile" />
					<ImportProfileButton content="Import Profile" />
					<ExportProfileButton content="Export Profile" />
					<GenerateDataButton content="Generate Data" />
				</div>

				<Divider />

				<CurrentProfileEditor />
			</Container>
		);
	}
}

export default App;
