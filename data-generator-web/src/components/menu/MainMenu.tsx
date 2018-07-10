import * as React from "react";
import {Action} from "redux";
import {Image, Menu, MenuItem} from "semantic-ui-react";

import Actions from "../../redux/actions";
import {ActionType} from "../../redux/actions/ActionType";
import {dispatchesActionOnClick, dispatchesBasicActionOnClick} from "../dispatchesActionOnClick";
import StartProfilingFromFilePathModal from "./StartProfilingFromFilePathModal";

import logoUrl from "../../logo.svg";

function triggersAction<TActionParameters>(title: string, actionType: ActionType<{}, any>): React.ReactNode {
	const DecoratedMenuItem = dispatchesBasicActionOnClick(actionType, MenuItem);
	return <DecoratedMenuItem content={title} />;
}

function triggersActionWithFunc<TActionParameters>(title: string, actionFunc: () => Action): React.ReactNode {
	const DecoratedMenuItem = dispatchesActionOnClick(actionFunc, MenuItem);
	return <DecoratedMenuItem content={title} />;
}

const MainMenu = () =>
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
					{ triggersActionWithFunc("From file", () => Actions.Modals.OpenModal.create({ modalId: "start_profiling_from_file" }))}
					<StartProfilingFromFilePathModal />

					{ triggersAction("From database", Actions.StartProfilingDataFromDatabase)}
				</Menu.Menu>
			</Menu.Item>

			{ triggersAction("Generate Data", Actions.StartGeneratingData) }
		</Menu>
	);

export default MainMenu;
