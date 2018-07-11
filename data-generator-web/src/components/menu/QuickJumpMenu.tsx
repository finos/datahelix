import * as React from "react";
import {Menu} from "semantic-ui-react";

export interface IProps
{
	readonly fields: Array<{
		id: string,
		name: string
	}>
}

const QuickJumpMenu = ({fields}: IProps) => {
	if (fields.length === 0)
		return null;

	return (
		<Menu vertical={true} fluid={true}>
			<Menu.Item>
				Fields
				<Menu.Menu>
					{
						fields.map(f => (
							<Menu.Item key={f.id} href={`#${f.id}`}>{f.name}</Menu.Item>
						))
					}
				</Menu.Menu>
			</Menu.Item>
		</Menu>
	)
}

export default QuickJumpMenu;
