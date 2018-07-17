import { Action } from "redux";
import { Reducer } from 'redux-testkit';
import Actions from "../../../redux/actions";
import profileReducer from '../../../redux/reducers/profileReducer';
import profileReducerBase from '../../../redux/reducers/profileReducerBase';
import { FieldKinds, IFieldState, IProfileState } from "../../../redux/state/IAppState";

describe('Profile reducer', () => {
    const emptyState: IProfileState = { fields: [] };
    const genericFieldState: IFieldState = { id: 'generic', name: '', nullPrevalence: 0, restrictions: { kind: FieldKinds.Unclassified } };
    const mockAction: Action = { type: 'MOCK_ACTION' };

    it('Should handle undefined state', () => {
        Reducer(profileReducer).withState(undefined).expect(mockAction).toReturnState(emptyState);
    });
    it('Should handle CLEAR_CURRENT_PROFILE action', () => {
        // We are not creating a new Action because we prefer the test to not understand anything about tha action structure, etc.
        const cleanProfile = Actions.Profiles.ClearCurrentProfile.create({});
        Reducer(profileReducer).withState(genericFieldState).expect(cleanProfile).toReturnState(emptyState);
    });
    it('Should handle SET_CURRENT_PROFILE action', () => {
        const filledInFieldState: IFieldState = { id: '1', name: 'SomeField', nullPrevalence: 0.5, restrictions: { kind: FieldKinds.String, allowableCharacters: "test", minimumLength: 1, maximumLength: 10 } };
        const filledInProfileState: IProfileState = { fields: [filledInFieldState] };
        const filledInState = { newProfile: filledInProfileState };
        const setProfile = Actions.Profiles.SetCurrentProfile.create(filledInState);
        Reducer(profileReducer).withState(filledInState).expect(setProfile).toReturnState(filledInProfileState);
    });
    it('Should handle other actions by calling a fallback function', () => {
        const mockFunction = jest.fn((state: IProfileState, action: Action<any>): IProfileState => state);
        const result = profileReducerBase(emptyState, mockAction, mockFunction);
        expect(mockFunction.mock.calls.length).toBe(1);
        expect(result).toEqual(emptyState);
    });
});
