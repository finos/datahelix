import * as React from "react";
import {Input} from "semantic-ui-react";
import {InputOnChangeData} from "semantic-ui-react/dist/commonjs/elements/Input/Input";

export interface IProps<T>
{
	value?: T;
	onChange?: (newValue: T) => void;
}

type InputChangeEventHandler = (event: React.SyntheticEvent<HTMLInputElement>, data: InputOnChangeData) => void;

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
			value={this.props.value} />
	}

	private readonly onChange: InputChangeEventHandler =
		(_, data) =>
		{
			const parsedValue = parseFloat(data.value);

			if (this.props.onChange && !Number.isNaN(parsedValue))
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
			value={this.props.value} />
	}

	private readonly onChange: InputChangeEventHandler =
		(_, data) => { if (this.props.onChange) this.props.onChange(data.value); }
}
