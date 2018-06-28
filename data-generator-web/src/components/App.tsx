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
	<ul>
		<ProfileField name="Stock Description">
			<StringField />
		</ProfileField>
		<ProfileField name="Buy Price">
			<NumericProfileFieldData />
		</ProfileField>
		<ProfileField name="Sell Price">
			<NumericProfileFieldData />
		</ProfileField>
	</ul>

const ProfileField = ({name, children}: any) =>
	<li style={{overflow: "auto"}}>
		<input type="text" value={name} style={ { float: "left", width: "30%" } } />
		<div style={{float: "right", width: "68%"}}>
			{ children }
		</div>
	</li>

const NumericProfileFieldData = () =>
	<ul>
		<NumericField name="Minimum Value" />
		<NumericField name="Maximum Value" />
		<NumericField name="Standard Deviation" />
		<NumericField name="Average" />
	</ul>


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

const StringField = () => <input type="text" />

export default App;
