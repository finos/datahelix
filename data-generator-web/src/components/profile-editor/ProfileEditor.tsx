import * as React from "react";
import {Button, Form, Grid, Header, Icon} from "semantic-ui-react";

import Actions from "../../redux/actions";
import {dispatchesBasicActionOnClick} from "../dispatchesActionOnClick";

interface IProps
{
	children: React.ReactNode[];
}

const AddEmptyFieldButton = dispatchesBasicActionOnClick(Actions.Fields.AddBlankField, Button);

const ProfileEditor = ({children}: IProps) => (
	<>
		<Header
			dividing={true}
			style={{marginTop: "0"}} as="h1">
			Unnamed profile
		</Header>

		<Form fluid="true">
			<Grid columns={3} divided="vertically">
				{ children.length > 0
					? children
					: <Grid.Row>
						<Grid.Column width={16}>
							<span style={{ fontSize: "larger", color: "darkgrey", fontStyle: "italic" }}>
								No fields defined yet
							</span>
						</Grid.Column>
					</Grid.Row>
				}

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
