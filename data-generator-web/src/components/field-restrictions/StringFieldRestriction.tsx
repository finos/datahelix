import * as React from "react";
import {ChangeEvent} from "react";

export interface IProps
{
	title?: string;
	onChange?: (newValue: string) => void;
}

const StringFieldRestriction = ({title, onChange}: IProps) => {
	const onChangeWithConversion =  (e: ChangeEvent<HTMLInputElement>) => {
		if (onChange)
			onChange(e.target.value);
	}

	return (
		<div>
			<label style={{ width: "100%" }}>{title}
				<input type="text" onChange={onChangeWithConversion}/>
			</label>
		</div>
	)
}

export default StringFieldRestriction;
