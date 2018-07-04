import * as React from "react";

import {Checkbox, Dropdown, Form, Grid, Input} from "semantic-ui-react";
import {FieldKinds} from "../redux/state/IAppState";
import DeleteFieldButton from "./DeleteFieldButton";
import {
	AllowableCharactersFieldRestriction,
	MaximumStringLengthFieldRestriction,
	MaximumValueFieldRestriction,
	MeanFieldRestriction,
	MinimumStringLengthFieldRestriction,
	MinimumValueFieldRestriction,
	StandardDeviationRestriction
} from "./field-restrictions/SpecificFieldRestrictions";

interface IProps
{
	id: string;
	name?: string;
	kind: FieldKinds | null;
}

function withLabel(label: string, component: any): any
{
	return (
		<Form.Field>
			<label>{label}</label>
			{ component }
		</Form.Field>
	)
}

const ProfileField = ({id, name, kind}: IProps) =>
	<Grid.Row>
		<Grid.Column width={1}>
			<DeleteFieldButton fieldId={id} />
		</Grid.Column>

		<Grid.Column width={4}>
			<Form.Field>
				<label>Field Name</label>
				<Input fluid={true} value={name} placeholder='Field name' />
			</Form.Field>

			<Checkbox label='Nullable?' />
		</Grid.Column>

		<Grid.Column width={3}>
			<Form.Field>
				<label>Field Type</label>
				<Dropdown
					placeholder="Select..."
					fluid={true}
					selection={true}
					options={[
						{ text: "Numeric", value: "Numeric" },
						{ text: "String", value: "String" }
					]} />
			</Form.Field>
		</Grid.Column>

		<Grid.Column width={8}>
			{
				kind === FieldKinds.String &&
				<>
					{ withLabel(
						"Allowable Characters",
						<AllowableCharactersFieldRestriction fieldId={id} />) }
					{ withLabel(
						"Minimum Length",
						<MinimumStringLengthFieldRestriction fieldId={id} />) }
					{ withLabel(
						"Maximum Length",
						<MaximumStringLengthFieldRestriction fieldId={id} />) }
				</>
				||
				kind === FieldKinds.Numeric &&
				<>
					{ withLabel(
						"Standard Deviation",
						<StandardDeviationRestriction fieldId={id} />) }
					{ withLabel(
						"Mean",
						<MeanFieldRestriction fieldId={id} />) }
					{ withLabel(
						"Minimum Value",
						<MinimumValueFieldRestriction fieldId={id} />) }
					{ withLabel(
						"Maximum Value",
						<MaximumValueFieldRestriction fieldId={id} />) }
				</>
				||
				null
			}
		</Grid.Column>
	</Grid.Row>

export default ProfileField;
