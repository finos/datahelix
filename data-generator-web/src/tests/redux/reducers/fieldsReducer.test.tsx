import { Action } from "redux";
import { Reducer } from 'redux-testkit';
import Actions from "../../../redux/actions";
import fieldsReducer from '../../../redux/reducers/fieldsReducer';
import { FieldKinds } from "../../../redux/state/IAppState";
import { IFieldState } from "../../../redux/state/IAppState";

describe('Fields reducer', () => {
    const allKinds = [FieldKinds.Enum, FieldKinds.Numeric, FieldKinds.String, FieldKinds.Temporal, FieldKinds.Unclassified];
    const genericFieldState: IFieldState = { id: 'generic', name: '', nullPrevalence: 0, restrictions: { kind: FieldKinds.Unclassified } };
    const enumFieldState : IFieldState = { id: 'generic', name: '', nullPrevalence: 0, restrictions: { kind: FieldKinds.Enum, members : [] } };
    const numericFiledState : IFieldState = { id: 'generic', name: '', nullPrevalence: 0, restrictions: { kind: FieldKinds.Numeric, minimumValue : null, maximumValue : null, stdDev: null, meanAvg : null } };
    const stringFieldState : IFieldState = { id: 'generic', name: '', nullPrevalence: 0, restrictions: { kind: FieldKinds.String, minimumLength : null, maximumLength : null, allowableCharacters : null } };
    const temporalFieldState : IFieldState = { id: 'generic', name: '', nullPrevalence: 0, restrictions: { kind: FieldKinds.Temporal, minimum : null, maximum : null } };
    const allFieldStates = [genericFieldState, enumFieldState, numericFiledState, stringFieldState, temporalFieldState];

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
    
    allFieldStates.forEach(originFieldState => {
        const originalKindName = FieldKinds[originFieldState.restrictions.kind];
        allKinds.forEach(destinationKind => {
            const destinationKindName = FieldKinds[destinationKind];
            it(`Should handle CHANGE_FIELD_KIND action from ${originalKindName} to ${destinationKindName}`, () => {
                const changeFieldKindAction = Actions.Fields.ChangeFieldKind.create({fieldId: genericFieldState.id, newKind: destinationKind});
                const result = fieldsReducer([originFieldState], changeFieldKindAction);
                expect(result.length).toBe(1);
                expect(result[0].restrictions.kind).toEqual(destinationKind);
            });
        });
    });
});