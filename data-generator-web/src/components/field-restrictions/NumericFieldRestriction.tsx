import * as React from "react";
import {Form, Input} from "semantic-ui-react";
import {InputOnChangeData} from "semantic-ui-react/dist/commonjs/elements/Input/Input";
import {SyntheticEvent} from "react";

export interface IProps
{
	title?: string;
	onChange?: (newValue: number) => void;
}

const NumericFieldRestriction = ({title, onChange}: IProps) => {
	const onChangeWithConversion =  (e: SyntheticEvent<HTMLInputElement>, data: InputOnChangeData) => {
		const parsedNumber = parseFloat(data.value);

		if (onChange && !Number.isNaN(parsedNumber))
			onChange(parsedNumber);
	};

	return (
		<Form.Field>
			<label>{title}</label>
			<Input fluid type="number" onChange={onChangeWithConversion} />
		</Form.Field>
	)
}

export default NumericFieldRestriction;
