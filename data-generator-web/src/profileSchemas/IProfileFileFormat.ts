import {IProfileState} from "../redux/state/IAppState";

interface IProfileFileFormat
{
	canDeserialiseFrom(rawObject: {}): boolean;

	serialiseProfile(state: IProfileState): {};
	deserialiseProfile(rawObject: {}): IProfileState;
}

export default IProfileFileFormat;
