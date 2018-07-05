import * as React from "react";
import {Button, ButtonProps} from "semantic-ui-react";

export interface IProps extends ButtonProps
{
	onFileChosen?: (file: File) => void;
}

export default class FileUploadButton extends React.Component<IProps, {}> {
	private readonly inputRef: React.RefObject<HTMLInputElement>;

	constructor(props: IProps) {
		super(props);

		this.inputRef = React.createRef();
	}

	public render(): React.ReactNode
	{
		const { onFileChosen, ...buttonProps } = this.props;

		return (
			<>
				<Button
					{ ...buttonProps }
					onClick={this.onButtonClick} />

				<input
					type="file"
					ref={this.inputRef}
					onChange={this.onFileChosen}
					style={{display: "none"}} />
			</>
		);
	}

	private readonly onButtonClick = () => this.inputRef.current!.click();

	private readonly onFileChosen = () =>
	{
		const fileList = this.inputRef.current!.files || [];

		if (fileList.length === 0)
			return;

		if (this.props.onFileChosen)
			this.props.onFileChosen(fileList[0]);
	};
}
