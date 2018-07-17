import { Action } from "redux";
import Actions from "../../../redux/actions";
import profileReducer from '../../../redux/reducers/profileReducer';
import profileReducerBase from '../../../redux/reducers/profileReducerBase';
import { FieldKinds, IFieldState, IProfileState } from "../../../redux/state/IAppState";

describe('Profile reducer', () => {
    const emptyState: IProfileState = { fields: [] };
    const genericFieldState: IFieldState = { id: 'generic', name: '', nullPrevalence: 0, restrictions: { kind: FieldKinds.Unclassified } };
    const mockAction: Action = { type: 'MOCK_ACTION' };
    const genericStateProfile: IProfileState = { fields: [genericFieldState] };

    it('Should handle undefined state', () => {
        // Act
        const result = profileReducer(undefined, mockAction);

        // Assert
        expect(result).toEqual(emptyState);
    });
    it('Should handle CLEAR_CURRENT_PROFILE action', () => {
        // Arrange
        // We are not creating a new Action because we prefer the test to not understand anything about tha action structure, etc.
        const cleanProfile = Actions.Profiles.ClearCurrentProfile.create({});

        // Act
        const result = profileReducer(genericStateProfile, cleanProfile);

        // Assert
        expect(result).toEqual(emptyState);
    });
    it('Should handle SET_CURRENT_PROFILE action', () => {
        // Arrange
        const filledInFieldState: IFieldState = { id: '1', name: 'SomeField', nullPrevalence: 0.5, restrictions: { kind: FieldKinds.String, allowableCharacters: "test", minimumLength: 1, maximumLength: 10 } };
        const filledInProfileState: IProfileState = { fields: [filledInFieldState] };
        const filledInState = { newProfile: filledInProfileState };
        const setProfile = Actions.Profiles.SetCurrentProfile.create(filledInState);

        // Act
        const result = profileReducer(genericStateProfile, setProfile);

        // Assert
        expect(result).toEqual(filledInProfileState);
    });
    it('Should handle other actions by calling a fallback function', () => {
        // Arrange
        const mockFunction = jest.fn((state: IProfileState, action: Action<any>): IProfileState => state);

        // Act
        const result = profileReducerBase(emptyState, mockAction, mockFunction);

        // Assert
        expect(mockFunction.mock.calls.length).toBe(1);
        expect(result).toEqual(emptyState);
    });
});
