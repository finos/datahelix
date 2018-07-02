import * as React from "react";
import {ChangeEvent} from "react";

export interface IProps
{
	title?: string;
	onChange?: (newValue: number) => void;
}

const NumericFieldRestriction = ({title, onChange}: IProps) => {
	const onChangeWithConversion =  (e: ChangeEvent<HTMLInputElement>) => {
		const parsedNumber = parseFloat(e.target.value);

		if (onChange && !Number.isNaN(parsedNumber))
			onChange(parsedNumber);
	};

	return (
		<div>
			<label>{title}
				<input type="numeric" step={0.01} onChange={onChangeWithConversion}/>
			</label>
		</div>
	);
}

export default NumericFieldRestriction;
