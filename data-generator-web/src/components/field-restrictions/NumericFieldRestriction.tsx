import * as React from "react";
import {SyntheticEvent} from "react";
import {Form, Input} from "semantic-ui-react";
import {InputOnChangeData} from "semantic-ui-react/dist/commonjs/elements/Input/Input";

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
			<Input fluid={true} type="number" onChange={onChangeWithConversion} />
		</Form.Field>
	)
}

export default NumericFieldRestriction;
