import {Action} from "redux";
import Actions from "../actions";
import {ModalId} from "../state/IAppState";

export default function modalReducer(
	oldState: ModalId | undefined,
	action: Action)
	: ModalId | undefined
{
	if (Actions.Modals.OpenModal.is(action)) {
		return action.modalId;
	}

	if (Actions.Modals.CloseModal.is(action)) {
		return undefined;
	}

	return oldState;
}