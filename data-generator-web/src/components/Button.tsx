import * as React from "react";

export interface IProps
{
	title?: string;
	onClick?: () => void;
}

const Button = ({title, onClick}: IProps) =>
	<input type="button" onClick={onClick} value={title} style={ {cursor: "pointer" }} />

export default Button;
