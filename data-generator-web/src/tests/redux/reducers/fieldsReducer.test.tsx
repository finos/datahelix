import {Action} from "redux";
import {Reducer} from 'redux-testkit';
import {IProfileState, IFieldState, FieldKinds} from "../../../redux/state/IAppState";
import Actions from "../../../redux/actions";
import fieldsReducer from '../../../redux/reducers/fieldsReducer';
import StateHelper from "../../testHelpers/StateHelper";

describe('Fields reducer', () => {    
    it('Should handle undefined state', () => {
        let mockAction : Action = { type : 'MOCK_ACTION' };
        Reducer(fieldsReducer).withState(undefined).expect(mockAction).toReturnState([]);
    });
    
});