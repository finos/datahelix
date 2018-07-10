import * as React from "react";

import {Form, Grid, Header, Icon} from "semantic-ui-react";
import AddEmptyFieldButton from "./AddEmptyFieldButton";

interface IProps
{
	children: React.ReactNode[];
}

const ProfileEditor = ({children}: IProps) => (
	<>
		<Header dividing={true} style={{marginTop: "0"}} as="h1">Profile</Header>

		<Form fluid="true">
			<Grid columns={3} divided="vertically">
				{ children.length === 0 &&
					<Grid.Row>
						<Grid.Column width={16}>
							<span style={{ fontSize: "larger", color: "darkgrey", fontStyle: "italic" }}>
								No fields defined yet
							</span>
						</Grid.Column>
					</Grid.Row>}

				{ children }

				<Grid.Row>
					<Grid.Column width={16}>
						<AddEmptyFieldButton icon={true}>
							<Icon name="plus" />
						</AddEmptyFieldButton>
					</Grid.Column>
				</Grid.Row>
			</Grid>
		</Form>
	</>);

export default ProfileEditor;
