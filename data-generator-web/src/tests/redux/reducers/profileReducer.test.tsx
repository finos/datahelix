import {Action} from "redux";
import {Reducer} from 'redux-testkit';
import {IProfileState, IFieldState, FieldKinds} from "../../../redux/state/IAppState";
import Actions from "../../../redux/actions";
import profileReducer from '../../../redux/reducers/profileReducer';

describe('Profile reducer', () => {
    let emptyState : IProfileState = { fields: [] };
    let genericFieldState : IFieldState = { id: 'generic', name : '', nullPrevalence: 0, restrictions : { kind: FieldKinds.Unclassified } };
    
    it('Should handle undefined state', () => {
        let mockAction : Action = { type : 'MOCK_ACTION' };
        Reducer(profileReducer).withState(undefined).expect(mockAction).toReturnState(emptyState);
    });
    it('Should handle CLEAR_CURRENT_PROFILE action', () => {
        // We are not creating a new Action because we prefer the test to not understand anything about tha action structure, etc.
        let cleanProfile = Actions.Profiles.ClearCurrentProfile.create({});
        Reducer(profileReducer).withState(genericFieldState).expect(cleanProfile).toReturnState(emptyState);
    });
    it('Should handle SET_CURRENT_PROFILE action', () => {
        let filledInFieldState : IFieldState = { id: '1', name : 'SomeField', nullPrevalence: 0.5, restrictions : { kind: FieldKinds.String, allowableCharacters: "test", minimumLength: 1, maximumLength: 10 } };
        let filledInProfileState : IProfileState = { fields : [ filledInFieldState ] };
        let filledInState = { newProfile : filledInProfileState };
        let setProfile = Actions.Profiles.SetCurrentProfile.create(filledInState);
        Reducer(profileReducer).withState(filledInState).expect(setProfile).toReturnState(filledInProfileState);
    });
    it('Should handle other actions by calling fieldsReducer', () => {
        // TODO: Need to override the dependency in profileReducer
    });
});