import * as React from "react";
import {connect} from "react-redux";

import ProfileEditor from "./ProfileEditor";

import {IAppState} from "../redux/state/IAppState";
import ProfileField from "./ProfileField";

function mapStateToProps(state: IAppState): any
{
	const fields = state.currentProfile ? state.currentProfile.fields : [];

	const children = fields.map(f =>
		<ProfileField
			name={f.name}
			id={f.id}
			key={f.id}
			kind={f.restrictions ? f.restrictions.kind : null} />);

	return { children };
}

const WrappedComponent = connect(mapStateToProps)(ProfileEditor);

export default WrappedComponent;
