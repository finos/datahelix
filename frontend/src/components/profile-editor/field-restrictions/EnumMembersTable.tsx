import * as React from "react";
import {Icon, Input, Table} from "semantic-ui-react";
import SliderWithValue from "../SliderWithValue";

export interface IProps
{
	members: Array<{
		id: string,
		name: string;
		prevalence: number;
	}>;

	onNameChange?: (enumMemberId: string, newName: string) => void;
	onPrevalenceChange?: (enumMemberId: string, newValue: number) => void;
	onMemberDelete?: (enumMemberId: string) => void;
	onMemberAdd?: () => void;
}

export default class EnumMembersTable extends React.Component<IProps, {}>
{
	constructor(props: IProps)
	{
		super(props);
	}

	public render(): React.ReactNode {
		return (
			<Table celled={true} fixed={true} definition={true} compact={true}>
				<Table.Header fullWidth={true}>
					<Table.Row>
						<Table.HeaderCell width={1}/>
						<Table.HeaderCell width={5}>Name</Table.HeaderCell>
						<Table.HeaderCell width={6}>Prevalence</Table.HeaderCell>
					</Table.Row>
				</Table.Header>
				<Table.Body>
					{
						this.props.members.map(m =>
							<Table.Row key={m.id}>
								<Table.Cell textAlign="center">
									<Icon
										name="trash"
										style={{cursor: "pointer"}}
										// tslint:disable:jsx-no-lambda
										onClick={() => this.onDeleteButtonClick(m.id)} />
								</Table.Cell>
								<Table.Cell>
									<Input
										value={m.name}
										fluid={true}
										// tslint:disable:jsx-no-lambda
										onChange={(e, data) => this.onNameChange(m.id, data.value)}/>
								</Table.Cell>
								<Table.Cell>
									<SliderWithValue
										value={m.prevalence}
										// tslint:disable:jsx-no-lambda
										onChange={newValue => this.onPrevalenceChange(m.id, newValue)} />
								</Table.Cell>
							</Table.Row>)
					}

					<Table.Row>
						<Table.Cell textAlign="center">
							<Icon
								name="plus"
								style={{cursor: "pointer"}}
								onClick={this.onAddButtonClick} />
						</Table.Cell>
						<Table.Cell disabled={true}/>
						<Table.Cell disabled={true}/>
					</Table.Row>
				</Table.Body>

			</Table>
		)
	}

	private readonly onNameChange = (memberId: string, newName: string) =>
	{
		if (this.props.onNameChange)
			this.props.onNameChange(memberId, newName);
	};

	private readonly onPrevalenceChange = (memberId: string, newPrevalence: number) =>
	{
		if (this.props.onPrevalenceChange)
			this.props.onPrevalenceChange(memberId, newPrevalence);
	};

	private readonly onAddButtonClick = () =>
	{
		if (this.props.onMemberAdd)
			this.props.onMemberAdd();
	}

	private readonly onDeleteButtonClick = (id: string) =>
	{
		if (this.props.onMemberDelete)
			this.props.onMemberDelete(id);
	}
}
