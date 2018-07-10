import * as React from "react";
import {Image, Menu, MenuItem} from "semantic-ui-react";

import Actions from "../../redux/actions";
import dispatchesActionOnClick from "../dispatchesActionOnClick";

import logoUrl from "../../logo.svg";
import {ActionType} from "../../redux/actions/ActionType";

function triggersAction(title: string, actionType: ActionType<{}, any>): React.ReactNode {
	const DecoratedMenuItem = dispatchesActionOnClick(actionType, MenuItem);
	return <DecoratedMenuItem content={title} />;
}

const SidebarMenu = () =>
	(
		<Menu vertical={true} fluid={true}>
			<Menu.Item>
				<Image src={logoUrl} fluid={true} />
			</Menu.Item>

			{ triggersAction("New Profile", Actions.Profiles.ClearCurrentProfile) }

			<Menu.Item>
				File
				<Menu.Menu>
					{ triggersAction("Import...", Actions.Profiles.TriggerImportProfile) }
					{ triggersAction("Export...", Actions.Profiles.TriggerExportProfile) }
				</Menu.Menu>
			</Menu.Item>

			<Menu.Item>
				Start profiling
				<Menu.Menu>
					{ triggersAction("From file", Actions.Profiles.TriggerProfileFromFile)}
					{ triggersAction("From database", Actions.Profiles.TriggerProfileFromDatabase)}
				</Menu.Menu>
			</Menu.Item>

			{ triggersAction("Generate Data", Actions.StartGeneratingData) }
		</Menu>
	);

export default SidebarMenu;
