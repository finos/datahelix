import { mount } from 'enzyme';
import * as Enzyme from 'enzyme';
import * as Adapter from 'enzyme-adapter-react-16';
import * as React from "react";
import { Provider } from "react-redux";
import configureStore from 'redux-mock-store';
import ProfileField from "../../../components/profile-editor/ProfileField";
import { FieldKinds, IAppState, IFieldState } from "../../../redux/state/IAppState";

describe('Profile field', () => {
	const mockStore = configureStore();
	let store : any;
	let wrapper;
	let initialState : IAppState = {};
	
	beforeAll(()=>{
		Enzyme.configure({ adapter: new Adapter() });
	});

	beforeEach(() => { 
		initialState = {
			currentModal : undefined,
			currentProfile : {  
				fields : []
			}
		};

		store = mockStore(initialState);
	});

	it("Should display generic field", () => {
		// Arrange
		const genericFieldState: IFieldState = { id: 'genericA', name: 'First generic field', nullPrevalence: 0.5, restrictions: { kind: FieldKinds.Unclassified } };
		const childrenFieldStates : IFieldState[] = [genericFieldState];

		initialState = {...initialState, currentProfile : {
			fields : childrenFieldStates
		}};

		store = mockStore(initialState);

		// Act
		wrapper = mount(
			<Provider store={store} >
				<ProfileField id={genericFieldState.id} name={genericFieldState.name} kind={genericFieldState.restrictions.kind} />
			</Provider>
		);

		// Assert
		expect(wrapper.find('div[id]')).toHaveLength(1);
		expect(wrapper.find(`input[placeholder="Field name"][value="${genericFieldState.name}"]`)).toHaveLength(1);
		expect(wrapper.find(`div[aria-valuenow=${genericFieldState.nullPrevalence}]`)).toHaveLength(1);
	});

	it("Should display numeric field", () => {
		// Arrange
		const minimumValue = 1;
		const maximumValue = 3;
		const meanAvg = 2;
		const stdDev = 0.9;
		const numericFieldState: IFieldState = { id: 'numericA', name: 'First numeric field', nullPrevalence: 0.2, restrictions: { kind: FieldKinds.Numeric, minimumValue : minimumValue, maximumValue : maximumValue, meanAvg : meanAvg, stdDev:stdDev } };
		const childrenFieldStates : IFieldState[] = [numericFieldState];

		initialState = {...initialState, currentProfile : {
			fields : childrenFieldStates
		}};

		store = mockStore(initialState);

		// Act
		wrapper = mount(
			<Provider store={store} >
				<ProfileField id={numericFieldState.id} name={numericFieldState.name} kind={numericFieldState.restrictions.kind} />
			</Provider>
		);

		// Assert
		expect(wrapper.find('div[id]')).toHaveLength(1);
		expect(wrapper.find(`input[placeholder="Field name"][value="${numericFieldState.name}"]`)).toHaveLength(1);
		expect(wrapper.find(`div[aria-valuenow=${numericFieldState.nullPrevalence}]`)).toHaveLength(1);
		expect(wrapper.find(`input[value=${minimumValue}]`)).toHaveLength(1);
		expect(wrapper.find(`input[value=${maximumValue}]`)).toHaveLength(1);
		expect(wrapper.find(`input[value=${meanAvg}]`)).toHaveLength(1);
		expect(wrapper.find(`input[value=${stdDev}]`)).toHaveLength(1);
	});

});