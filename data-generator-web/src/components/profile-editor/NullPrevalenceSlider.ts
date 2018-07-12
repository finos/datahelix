import {connect} from "react-redux";

import Actions from "../../redux/actions";
import selectFieldLookup from "../../redux/selectors/selectFieldLookup";
import SliderWithValue, { IProps as SliderWithValueProps } from "./SliderWithValue";

interface IProps
{
	fieldId: string;
}

const WrappedComponent =
	connect<SliderWithValueProps, SliderWithValueProps, IProps, SliderWithValueProps>(
		(state, ownProps) => {
			const field = selectFieldLookup(state)[ownProps.fieldId];

			return { value: field.nullPrevalence };
		},
		(dispatch, ownProps: IProps) => {
			return {
				onChange: newValue => dispatch(Actions.Fields.UpdateField.create({
					fieldId: ownProps.fieldId,
					newValues: {
						nullPrevalence: newValue
					}
				}))
			}
		},
		(s, d) => ({ ...s, ...d })
	)(SliderWithValue);

export default WrappedComponent;
