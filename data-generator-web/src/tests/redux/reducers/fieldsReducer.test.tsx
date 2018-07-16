import {Action} from "redux";
import {Reducer} from 'redux-testkit';
import {IProfileState, IFieldState, FieldKinds} from "../../../redux/state/IAppState";
import Actions from "../../../redux/actions";
import fieldsReducer from '../../../redux/reducers/fieldsReducer';

describe('Fields reducer', () => {    
    let genericFieldState : IFieldState = { id: 'generic', name : '', nullPrevalence: 0, restrictions : { kind: FieldKinds.Unclassified } };

    it('Should handle undefined state', () => {
        let mockAction : Action = { type : 'MOCK_ACTION' };
        Reducer(fieldsReducer).withState(undefined).expect(mockAction).toReturnState([]);
    });
    it('Should handle ADD_BLANK_FIELD action', () => {
        let addBlankFieldAction = Actions.Fields.AddBlankField.create({});
        let newBlankState : IFieldState = { id: "0", name: "", nullPrevalence: 0, restrictions: { kind: FieldKinds.Unclassified } };  
        let expectedResult =  [genericFieldState, newBlankState];
        Reducer(fieldsReducer).withState([genericFieldState]).expect(addBlankFieldAction).toReturnState(expectedResult);
    });
    it('Should handle DELETE_FIELD action', () => {
        let deleteFieldAction = Actions.Fields.DeleteField.create({fieldId : genericFieldState.id});
        Reducer(fieldsReducer).withState([genericFieldState]).expect(deleteFieldAction).toReturnState([]);
    });
    
});