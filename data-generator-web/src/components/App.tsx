import * as React from 'react';
import {
	Container,
	Rail,
	Segment
} from "semantic-ui-react";

import CurrentProfileQuickJumpMenu from "./menu/CurrentProfileQuickJumpMenu";
import MainMenu from "./menu/MainMenu";
import CurrentProfileEditor from './profile-editor/CurrentProfileEditor';

class App extends React.Component<{}, {}> {
	constructor(props: {}) {
		super(props);
	}

	public render(): React.ReactNode {
		return (
			<Container style={{ marginTop: "1em" }}>
				<Segment>
					<Rail position="left">
						<MainMenu />
					</Rail>

					<Rail position="right">
						<CurrentProfileQuickJumpMenu />
					</Rail>

					<CurrentProfileEditor />
				</Segment>
			</Container>
		);
	}
}

export default App;
