import * as React from "react";
import {Input} from "semantic-ui-react";
import {InputOnChangeData} from "semantic-ui-react/dist/commonjs/elements/Input/Input";

export interface IProps<T>
{
	value?: T | null;
	onChange?: (newValue: T | null) => void;
}

type InputChangeEventHandler = (event: React.SyntheticEvent<HTMLInputElement>, data: InputOnChangeData) => void;

function valueOrEmptyString(v: number | string | undefined | null): string | number
{
	return v === null || v === undefined ? "" : v;
}

export class NumericInput extends React.Component<IProps<number>, {}>
{
	constructor(props: IProps<number>)
	{
		super(props);
	}

	public render(): React.ReactNode
	{
		return <Input
			fluid={true}
			type="number"
			onChange={this.onChange}
			value={valueOrEmptyString(this.props.value)} />
	}

	private readonly onChange: InputChangeEventHandler =
		(_, data) =>
		{
			const parsedValue = data.value === ""
				? null
				: parseFloat(data.value);

			if (parsedValue !== null && Number.isNaN(parsedValue))
				return; // don't accept non-numeric values

			if (this.props.onChange)
				this.props.onChange(parsedValue);
		};
}

export class StringInput extends React.Component<IProps<string>, {}>
{
	constructor(props: IProps<string>)
	{
		super(props);
	}

	public render(): React.ReactNode
	{
		return <Input
			fluid={true}
			type="text"
			onChange={this.onChange}
			value={valueOrEmptyString(this.props.value)} />
	}

	private readonly onChange: InputChangeEventHandler =
		(_, data) => { if (this.props.onChange) this.props.onChange(data.value); }
}
