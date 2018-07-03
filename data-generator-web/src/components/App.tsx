import * as React from 'react';

import ClearProfileButton from "./ClearProfileButton";
import CurrentProfileEditor from './CurrentProfileEditor';
import ExportProfileButton from "./ExportProfileButton";
import {Button, Container, Header} from "semantic-ui-react";

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
				<CurrentProfileEditor />
			</Container>
		);
	}
}

export default App;
