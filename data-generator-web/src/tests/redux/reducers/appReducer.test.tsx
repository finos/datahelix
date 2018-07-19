import {Action} from "redux";
import appReducer from "../../../redux/reducers/appReducer";

it('Should return state', () => {
	// Arrange
	const mockAction: Action = { type: 'MOCK_ACTION' };
	const emptyAppState = {
		currentProfile: { fields : [] },
		currentModal: undefined
	};

	// Act
	const result = appReducer(undefined, mockAction);

	// Assert
	expect(result).toEqual(emptyAppState);
});
