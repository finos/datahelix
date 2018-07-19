import {connect} from "react-redux";
import {Dispatch} from "redux";

import Actions from "../../redux/actions";
import {IAppState} from "../../redux/state/IAppState";
import FilePathModel, {IProps as FilePathModelProps} from "./FilePathModal";

function mapStateToProps(state: IAppState): FilePathModelProps
{
	return {
		title: "Start profiling from file",
		isOpen: state.currentModal === "start_profiling_from_file"
	};
}

function mapDispatchToProps(dispatch: Dispatch): Partial<FilePathModelProps>
{
	return {
		onClose: () => dispatch(Actions.Modals.CloseModal.create({})),
		onSubmit: filePath => {
			dispatch(Actions.StartProfilingDataFromFile.create({ filePath }));
		}
	};
}

const WrappedComponent = connect(mapStateToProps, mapDispatchToProps)(FilePathModel);

export default WrappedComponent;
