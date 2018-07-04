import Slider from "rc-slider";
import * as React from "react";

export interface IProps
{
	value?: number;
	onChange?: (newValue: number | undefined) => void;
}

export default class SliderWithValue extends React.Component<IProps, {}>
{
	constructor(props: IProps)
	{
		super(props);
	}

	public render() {
		return (
			<div style={{display: "flex"}}>
				<Slider
					min={0} max={1} step={0.01}
					defaultValue={0}
					value={this.props.value}
					style={{ flex: "1 0" }}
					onChange={this.onChange}/>

				<div
					style={{
						flex: "0 0 2.5em",
						textAlign: "right",
						marginLeft: "5px",
						transform: "translate(0, -3px)"
					}}>
					{(this.props.value || 0).toFixed(2)}
				</div>
			</div>
		)
	}

	private readonly onChange =
		(newValue: any) =>
		{
			if (!this.props.onChange)
				return;

			this.props.onChange(newValue as number);
		}
}
