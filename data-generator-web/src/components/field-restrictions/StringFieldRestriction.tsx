import * as React from "react";
import {ChangeEvent, SyntheticEvent} from "react";
import {Form, Input} from "semantic-ui-react";
import {InputOnChangeData} from "semantic-ui-react/dist/commonjs/elements/Input/Input";

export interface IProps
{
	title?: string;
	onChange?: (newValue: string) => void;
}

const StringFieldRestriction = ({title, onChange}: IProps) => {
	const onChangeWithConversion = (e: SyntheticEvent<HTMLInputElement>, data: InputOnChangeData) => {
		if (onChange)
			onChange(data.value);
	}

	return (
		<Form.Field>
			<label>{title}</label>
			<Input fluid onChange={onChangeWithConversion} />
		</Form.Field>
	)
}

export default StringFieldRestriction;
