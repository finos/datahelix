import * as React from "react";
import {connect} from "react-redux";

import {
	ProfileEditor,
	ProfileField
} from "./ProfileEditor";

import {FieldKinds, IAppState} from "../redux/state/IAppState";
import {
	AllowableCharactersFieldRestriction,
	MaximumStringLengthFieldRestriction,
	MaximumValueFieldRestriction,
	MeanFieldRestriction, MinimumStringLengthFieldRestriction, MinimumValueFieldRestriction,
	StandardDeviationFieldRestriction
} from "./field-restrictions/StandardDeviationFieldRestriction";

function mapStateToProps(state: IAppState): any
{
	const fields = state.currentProfile ? state.currentProfile.fields : [];

	const children = fields.map(f =>
		<ProfileField name={f.name} key={f.id}>
			{ f.restrictions.kind === FieldKinds.String &&
			<>
				<AllowableCharactersFieldRestriction fieldId={f.id} />
				<MinimumStringLengthFieldRestriction fieldId={f.id} />
				<MaximumStringLengthFieldRestriction fieldId={f.id} />
			</>}
			{ f.restrictions.kind === FieldKinds.Numeric &&
			<>
				<StandardDeviationFieldRestriction fieldId={f.id} />
				<MeanFieldRestriction fieldId={f.id} />
				<MinimumValueFieldRestriction fieldId={f.id} />
				<MaximumValueFieldRestriction fieldId={f.id} />
			</>}
		</ProfileField>);

	return { children };
}

const WrappedComponent = connect(mapStateToProps)(ProfileEditor);

export default WrappedComponent;
