import * as React from "react";
import {connect} from "react-redux";

import {selectCurrentProfileFields} from "../redux/selectors/selectFieldLookup";
import {IAppState} from "../redux/state/IAppState";

import ProfileEditor from "./ProfileEditor";
import ProfileField from "./ProfileField";

function mapStateToProps(state: IAppState): any
{
	const fields = selectCurrentProfileFields(state);

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
