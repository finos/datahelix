import {IProfileState, IFieldState, FieldKinds} from "../../redux/state/IAppState";

export default class StateHelper {
    static EmptyState : IProfileState = { fields: [] };
    static GenericFieldState : IFieldState = { id: 'generic', name : '', nullPrevalence: 0, restrictions : { kind: FieldKinds.Unclassified } };
}