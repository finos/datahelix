import * as React from 'react';

class App extends React.Component {
	public render(): React.ReactNode {
		return (
			<main style={{ border: "1px solid black" }}>
				<header>
					<h1>Data Generator</h1>
				</header>
				<div>
					<button>New Profile</button>
					<button>Load Profile</button>
					<button>Export Profile</button>
					<button>Generate Data</button>
				</div>
				<ProfileEditor />
			</main>
		);
	}
}

const ProfileEditor = () =>
	<div style={{marginTop: "5px"}}>
		<ProfileField name="Stock Description">
			<StringProfileFieldData />
		</ProfileField>
		<ProfileField name="Buy Price">
			<NumericProfileFieldData />
		</ProfileField>
		<ProfileField name="Sell Price">
			<NumericProfileFieldData />
		</ProfileField>
	</div>

const ProfileField = ({name, children}: any) =>
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

const NumericProfileFieldData = () =>
	<>
		<NumericField name="Minimum Value" />
		<NumericField name="Maximum Value" />
		<NumericField name="Standard Deviation" />
		<NumericField name="Average" />
	</>

const StringProfileFieldData = () =>
	<>
		<StringField name="Allowable characters" />
		<NumericField name="Minimum Length" />
		<NumericField name="Maximum Length" />
	</>


interface IProps
{
	name: string;
}

const NumericField = ({name}: IProps) =>
	<div>
		<label>{name}
			<input type="number" step={0.01} />
		</label>
	</div>

const StringField = ({name}: any) =>
	<div>
		<label>{name}
			<input type="string" />
		</label>
	</div>

export default App;
