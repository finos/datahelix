import * as React from "react";

export const ProfileEditor = ({children}: any) =>
	<div style={{marginTop: "5px"}}>
		{ children }
	</div>

export const ProfileField = ({name, children}: any) =>
	<div style={{overflow: "auto", marginBottom: "2em"}}>
		<input type="text" value={name} style={ { float: "left", width: "30%" } } />
		<label style={{width: "10%", lineHeight: "13px", textAlign: "center"}}>
			<span style={{fontSize: "x-small"}}>nullable?</span><br/>
			<input type="checkbox" />
		</label>
		<div style={{float: "right", width: "60%"}}>
			{ children }
		</div>
	</div>
