import * as React from "react";
import {Image, Menu, MenuItem, MenuItemProps} from "semantic-ui-react";

import Actions from "../../redux/actions";
import dispatchesActionOnClick from "../dispatchesActionOnClick";

import logoUrl from "../../logo.svg";

const ClearProfileMenuItem = dispatchesActionOnClick<MenuItemProps>(
	() => Actions.Profiles.ClearCurrentProfile.create({}),
	MenuItem);

const GenerateDataMenuItem = dispatchesActionOnClick<MenuItemProps>(
	() => Actions.StartGeneratingData.create({}),
	MenuItem);

const ExportProfileMenuItem = dispatchesActionOnClick<MenuItemProps>(
	() => Actions.Profiles.TriggerExportProfile.create({}),
	MenuItem);

const ImportProfileMenuItem = dispatchesActionOnClick<MenuItemProps>(
	() => Actions.Profiles.TriggerImportProfile.create({}),
	MenuItem);


const SidebarMenu = () =>
	(
		<Menu vertical={true} fluid={true}>
			<Menu.Item>
				<Image src={logoUrl} fluid={true} />
			</Menu.Item>

			<Menu.Item>
				File
				<Menu.Menu>
					<ClearProfileMenuItem content="New Profile" />
					<ImportProfileMenuItem content="Import..." />
					<ExportProfileMenuItem content="Export..." />
				</Menu.Menu>
			</Menu.Item>

			<Menu.Item>
				Profile
				<Menu.Menu>
					<Menu.Item>From file</Menu.Item>
					<Menu.Item>From database</Menu.Item>
				</Menu.Menu>
			</Menu.Item>

			<GenerateDataMenuItem content="Generate Data" />
		</Menu>
	);

export default SidebarMenu;
