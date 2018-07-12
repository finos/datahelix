import {IProfileState} from "../redux/state/IAppState";

/** When we say 'serialise' here, we mean converting to POJOs - outputting as eg JSON is a separate responsibility */
interface IProfileFileFormat
{
	canDeserialiseFrom(rawObject: {}): boolean;

	serialiseProfile(state: IProfileState): {};
	deserialiseProfile(rawObject: {}): IProfileState;
}

export default IProfileFileFormat;
