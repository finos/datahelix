import * as React from "react";
import {Checkbox, Form, Grid, Icon} from "semantic-ui-react";

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
import FieldNameInput from "./FieldNameInput";
import FieldTypeDropdown from "./FieldTypeDropdown";

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
			<DeleteFieldButton fieldId={id} icon={true}>
				<Icon name="trash" />
			</DeleteFieldButton>
		</Grid.Column>

		<Grid.Column width={4}>
			<Form.Field>
				<label>Field Name</label>
				<FieldNameInput fieldId={id} fluid={true} placeholder='Field name' />
			</Form.Field>

			<Checkbox label='Nullable?' />
		</Grid.Column>

		<Grid.Column width={3}>
			<Form.Field>
				<label>Field Type</label>
				<FieldTypeDropdown
					fieldId={id}
					placeholder="Select..."
					fluid={true} />
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
