import {connect} from "react-redux";

import Actions from "../../../redux/actions/index";
import selectFieldLookup from "../../../redux/selectors/selectFieldLookup";
import {IEnumRestrictions} from "../../../redux/state/IAppState";
import EnumMembersTable, { IProps as IEnumMembersTableProps } from "./EnumMembersTable";

interface IProps
{
	fieldId: string;
}

type Diff<T, U> = T extends U ? never : T;  // Remove types from T that are assignable to U
type Names<T> = keyof T

type Unpick<T, K extends keyof T> = Pick<T, Diff<Names<T>, Names<Pick<T, K>>>>;

type StateSourcedTargetProps = Unpick<IEnumMembersTableProps, "members">;

const WrappedComponent =
	connect<IEnumMembersTableProps, StateSourcedTargetProps, IProps, IEnumMembersTableProps>(
		(state, ownProps) => {
			const field = selectFieldLookup(state)[ownProps.fieldId];
			const restrictions = field.restrictions as IEnumRestrictions;

			return {
				members: restrictions.members.map(v =>	({
					id: v.id,
					name: v.name,
					prevalence: v.prevalence
				})) };
		},
		(dispatch, ownProps) => {
			return {
				onMemberAdd: () => dispatch(
					Actions.Fields.Enums.AppendBlankEnumMember.create(
					{
						fieldId: ownProps.fieldId
					})),
				onMemberDelete: (enumMemberId: string) => dispatch(
					Actions.Fields.Enums.DeleteEnumMember.create({
						fieldId: ownProps.fieldId,
						memberId: enumMemberId
					})),
				onNameChange: (enumMemberId: string, newName: string) => dispatch(
					Actions.Fields.Enums.ChangeEnumMember.create({
						fieldId: ownProps.fieldId,
						memberId: enumMemberId,
						name: newName
					})),
				onPrevalenceChange: (enumMemberId: string, newValue: number) => dispatch(
					Actions.Fields.Enums.ChangeEnumMember.create({
						fieldId: ownProps.fieldId,
						memberId: enumMemberId,
						prevalence: newValue
					}))
			}
		},
		(s, d) => ({ ...s, ...d })
	)(EnumMembersTable);

export default WrappedComponent;
