import {Action} from "redux";
import {Reducer} from 'redux-testkit';
import {IProfileState} from "../../../redux/state/IAppState";
import profileReducer from '../../../redux/reducers/profileReducer';

describe('Profile reducer', () => {
    let emptyState : IProfileState = { fields: [] };
    it('Should handle undefined state', () => {
        let mockAction : Action = { type : 'MOCK_ACTION' };
        Reducer(profileReducer).withState(undefined).expect(mockAction).toReturnState(emptyState);
    });
});