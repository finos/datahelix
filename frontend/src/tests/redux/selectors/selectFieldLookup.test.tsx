import selectFieldLookup from '../../../redux/selectors/selectFieldLookup';
import {FieldKinds, IAppState, IFieldState} from "../../../redux/state/IAppState";

describe('Field lookup selector', () => {
	it('Should return a valid lookup', () => {
		// Arrange
		const firstGenericFieldState: IFieldState = { id: 'first', name: 'firstName', nullPrevalence: 0, restrictions: { kind: FieldKinds.Unclassified } };
		const secondGenericFieldState: IFieldState = { id: 'second', name: 'secondName', nullPrevalence: 0, restrictions: { kind: FieldKinds.Unclassified } };
		const allFields : IFieldState[] = [firstGenericFieldState, secondGenericFieldState];
		const appState : IAppState = { 
			currentProfile : {
				fields : allFields
			}
		};
		const expectedResult = { 'first' : firstGenericFieldState, 'second' : secondGenericFieldState };

		// Act
		const result = selectFieldLookup(appState);

		// Assert
		expect(result).toEqual(expectedResult);
	});

	it('Should handle empty arrays', () => {
		// Arrange
		const appState : IAppState = { 
			currentProfile : {
				fields : []
			}
		};
		const expectedResult = {};

		// Act
		const result = selectFieldLookup(appState);

		// Assert
		expect(result).toEqual(expectedResult);
	});
});
