import * as React from "react";

import AddFieldButton from "./AddEmptyFieldButton";
import {Form, Grid} from "semantic-ui-react";

interface IProps
{
	children: React.ReactNode[];
}

const ProfileEditor = ({children}: IProps) =>
	<Form fluid>
		<Grid columns={3} divided='vertically'>
			{ children }

			<Grid.Row>
				<Grid.Column width={1}>
					<AddFieldButton />
				</Grid.Column>
			</Grid.Row>
		</Grid>
	</Form>

export default ProfileEditor;
