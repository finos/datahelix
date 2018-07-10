import * as React from "react";
import {Button, Form, Header, Modal} from "semantic-ui-react";

export interface IProps {
	title: string;
	isOpen?: boolean;

	onClose?: () => void;
	onSubmit?: (filepath: string) => void;
}

export default class extends React.Component<IProps, {}> {
	private readonly inputRef = React.createRef<HTMLInputElement>();

	constructor(props: IProps) {
		super(props);
	}

	public render(): React.ReactNode
	{
		return (
			<Modal
				open={this.props.isOpen === true}
				size="tiny"
				onClose={this.onClose}>

				<Header content={this.props.title} />
				<Modal.Content>
					<Form onSubmit={this.onSubmit}>
						<Form.Field>
							<label>File path</label>
							<input type="text" ref={this.inputRef} />
						</Form.Field>
					</Form>
				</Modal.Content>
				<Modal.Actions>
					<Button
						type="submit"
						onClick={this.onSubmit}>
						Go!
					</Button>
				</Modal.Actions>
			</Modal>
		);
	}

	private readonly onSubmit = () => {
		if (!this.props.onSubmit || !this.inputRef.current || !this.inputRef.current.value)
			return;

		this.props.onSubmit(this.inputRef.current.value);
	}

	private readonly onClose = () => {
		if (!this.props.onClose)
			return;

		this.props.onClose();
	}
}
