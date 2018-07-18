import Actions from "../../../redux/actions";
import modalReducer from "../../../redux/reducers/modalReducer";
import {ModalId} from "../../../redux/state/IAppState";

describe('Modal reducer', () => {
	it('Should handle OPEN_MODAL by returning the modal id', () => {
		// Arrange
		const oldState : ModalId = ModalId.StartProfilingFromFile;
		const openModalAction = Actions.Modals.OpenModal.create({modalId : oldState});
		const expectedResult = ModalId.StartProfilingFromFile;

		// Act
		const result = modalReducer(oldState, openModalAction);

		// Assert
		expect(result).toEqual(expectedResult);
	});

	it('Should handle CLOSE_MODAL by returning undefined', () => {
		// Arrange
		const oldState : ModalId = ModalId.StartProfilingFromFile;
		const closeModalAction = Actions.Modals.CloseModal.create({});

		// Act
		const result = modalReducer(oldState, closeModalAction);

		// Assert
		expect(result).toBe(undefined);
	});
});
