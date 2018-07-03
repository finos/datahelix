import * as React from "react";

import DeleteFieldButton from "./DeleteFieldButton";
import {Button, Form, Grid, Icon, Input, List, Segment} from "semantic-ui-react";

interface IProps
{
	id: string;
	name?: string;
	children: React.ReactNode[];
}

const ProfileField = ({id, name, children}: IProps) =>
	<Grid.Row>
		<Grid.Column width={1}>
			<DeleteFieldButton fieldId={id} />
		</Grid.Column>

		<Grid.Column width={6}>
			<Input fluid value={name} placeholder='Field name' />

			<label style={{lineHeight: "13px", textAlign: "center"}}>
				<span style={{fontSize: "x-small"}}>nullable?</span><br/>
				<input type="checkbox" />
			</label>
		</Grid.Column>
		{/*<input type="text" value={name} style={{ flex: "0 1 20%" }} placeholder="Field name" />*/}

		<Grid.Column width={9}>
			{ children }
		</Grid.Column>
	</Grid.Row>

export default ProfileField;
