import * as React from "react";
import {connect} from "react-redux";

import ProfileEditor from "./ProfileEditor";

import {FieldKinds, IAppState} from "../redux/state/IAppState";
import {
	AllowableCharactersFieldRestriction,
	MaximumStringLengthFieldRestriction,
	MaximumValueFieldRestriction,
	MeanFieldRestriction, MinimumStringLengthFieldRestriction, MinimumValueFieldRestriction,
	SpecificFieldRestrictions
} from "./field-restrictions/SpecificFieldRestrictions";
import ProfileField from "./ProfileField";

function mapStateToProps(state: IAppState): any
{
	const fields = state.currentProfile ? state.currentProfile.fields : [];

	const children = fields.map(f =>
		<ProfileField name={f.name} id={f.id} key={f.id}>
			{ f.restrictions && f.restrictions.kind === FieldKinds.String &&
			<>
				<AllowableCharactersFieldRestriction fieldId={f.id} />
				<MinimumStringLengthFieldRestriction fieldId={f.id} />
				<MaximumStringLengthFieldRestriction fieldId={f.id} />
			</>}
			{ f.restrictions && f.restrictions.kind === FieldKinds.Numeric &&
			<>
				<SpecificFieldRestrictions fieldId={f.id} />
				<MeanFieldRestriction fieldId={f.id} />
				<MinimumValueFieldRestriction fieldId={f.id} />
				<MaximumValueFieldRestriction fieldId={f.id} />
			</>}
		</ProfileField>);

	return { children };
}

const WrappedComponent = connect(mapStateToProps)(ProfileEditor);

export default WrappedComponent;
