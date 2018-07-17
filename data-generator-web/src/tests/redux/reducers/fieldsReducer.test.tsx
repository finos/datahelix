import { Action } from "redux";
import Actions from "../../../redux/actions";
import fieldsReducer from '../../../redux/reducers/fieldsReducer';
import { FieldKinds, IFieldState } from "../../../redux/state/IAppState";
import { Reducer } from 'redux-testkit';


describe('Fields reducer', () => {
    const genericFieldState: IFieldState = { id: 'generic', name: '', nullPrevalence: 0, restrictions: { kind: FieldKinds.Unclassified } };

    it('Should handle undefined state', () => {
        const mockAction: Action = { type: 'MOCK_ACTION' };
        Reducer(fieldsReducer).withState(undefined).expect(mockAction).toReturnState([]);
    });
    it('Should handle ADD_BLANK_FIELD action', () => {
        const addBlankFieldAction = Actions.Fields.AddBlankField.create({});
        const newBlankState: IFieldState = { id: "0", name: "", nullPrevalence: 0, restrictions: { kind: FieldKinds.Unclassified } };
        const expectedResult = [genericFieldState, newBlankState];
        Reducer(fieldsReducer).withState([genericFieldState]).expect(addBlankFieldAction).toReturnState(expectedResult);
    });
    it('Should handle DELETE_FIELD action', () => {
        const deleteFieldAction = Actions.Fields.DeleteField.create({ fieldId: genericFieldState.id });
        Reducer(fieldsReducer).withState([genericFieldState]).expect(deleteFieldAction).toReturnState([]);
    });
    it('Should handle UPDATE_FIELD ation', () => {
        const modifiedStateFields = { name: 'Updated name', nullPrevalence: 0.5 };
        const updateFieldAction = Actions.Fields.UpdateField.create({ fieldId: genericFieldState.id, newValues: modifiedStateFields });
        const expectedState = [{ ...genericFieldState, name: modifiedStateFields.name, nullPrevalence: modifiedStateFields.nullPrevalence }];
        Reducer(fieldsReducer).withState([genericFieldState]).expect(updateFieldAction).toReturnState(expectedState);
    });
});