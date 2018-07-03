import * as React from "react";

import DeleteFieldButton from "./DeleteFieldButton";

interface IProps
{
	id: string;
	name?: string;
	children: React.ReactNode[];
}

const ProfileField = ({id, name, children}: IProps) =>
	<div style={{display: "flex", flexDirection: "row", marginBottom: "2em", alignItems: "flex-start"}}>
		<DeleteFieldButton fieldId={id} />

		<input type="text" value={name} style={{ flex: "0 1 20%" }} placeholder="Field name" />

		<label style={{lineHeight: "13px", textAlign: "center"}}>
			<span style={{fontSize: "x-small"}}>nullable?</span><br/>
			<input type="checkbox" />
		</label>

		<div style={{ flex: "1" }}>
			{ children }
		</div>
	</div>

export default ProfileField;
