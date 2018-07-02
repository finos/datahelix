import * as React from "react";

import AddFieldButton from "./AddFieldButton";

interface IProps
{
	children: React.ReactNode[];
}

const ProfileEditor = ({children}: IProps) =>
	<div style={{marginTop: "5px"}}>
		{ children }
		<AddFieldButton />
	</div>

export default ProfileEditor;
