import * as React from 'react';

import {Button, Container, Divider, Header} from "semantic-ui-react";
import ClearProfileButton from "./ClearProfileButton";
import CurrentProfileEditor from './CurrentProfileEditor';
import ExportProfileButton from "./ExportProfileButton";

class App extends React.Component {
	public render(): React.ReactNode {
		return (
			<Container>
				<Header as='h1'>Data Generator</Header>

				<div>
					<ClearProfileButton />
					<Button>Import Profile</Button>
					<ExportProfileButton />
					<Button>Generate Data</Button>
				</div>

				<Divider />

				<CurrentProfileEditor />
			</Container>
		);
	}
}

export default App;
