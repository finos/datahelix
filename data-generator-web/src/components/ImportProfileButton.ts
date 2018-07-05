import {connect} from "react-redux";
import {ButtonProps} from "semantic-ui-react";

import {fileFormatList} from "../profileSchemas";
import Actions from "../redux/actions";
import {IProfileState} from "../redux/state/IAppState";
import FileUploadButton, { IProps as FileUploadButtonProps } from "./FileUploadButton";

const WrappedComponent = connect<FileUploadButtonProps, FileUploadButtonProps, ButtonProps>(
	undefined,
	dispatch => ({
		onFileChosen: async file => {
			const json = await readTextFromBlob(file);

			let newProfileState: IProfileState;

			try {
				newProfileState = readProfileFromJson(json);
			}
			catch (e) {
				alert("Failed to load file");
				return;
			}

			dispatch(Actions.SetCurrentProfile.create({newProfile: newProfileState}));
		}
	}))
	(FileUploadButton);

function readTextFromBlob(blob: Blob): Promise<string>
{
	const fileReader = new FileReader();

	fileReader.readAsText(blob);

	return new Promise((resolve, reject) =>
	{
		fileReader.onload = () => resolve(fileReader.result);
		fileReader.onerror = () => reject(fileReader.error);
	})
}

function readProfileFromJson(json: string): IProfileState
{
	const rawProfileObject = JSON.parse(json);

	const bestFileFormat = fileFormatList.find(ff => ff.canDeserialiseFrom(rawProfileObject));

	if (!bestFileFormat)
	{
		throw new Error("Can't find matching file schema");
	}

	return bestFileFormat.deserialiseProfile(rawProfileObject);
}

export default WrappedComponent;
