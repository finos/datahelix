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
        let cleanProfile = Actions.Profiles.ClearCurrentProfile.create({});
        Reducer(profileReducer).withState(genericFieldState).expect(cleanProfile).toReturnState(emptyState);
    })
});