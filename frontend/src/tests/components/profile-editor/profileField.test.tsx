import { mount } from 'enzyme';
import * as Enzyme from 'enzyme';
import * as Adapter from 'enzyme-adapter-react-16';
import * as React from "react";
import { Provider } from "react-redux";
import configureStore from 'redux-mock-store';
import ProfileField from "../../../components/profile-editor/ProfileField";
import { FieldKinds, IAppState, IEnumMember, IFieldState  } from "../../../redux/state/IAppState";

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
		const numericFieldState: IFieldState = { id: 'numericA', name: 'First numeric field', nullPrevalence: 0.2, restrictions: { kind: FieldKinds.Numeric, minimumValue, maximumValue, meanAvg, stdDev } };
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

	it("Should display String field", () => {
		// Arrange
		const minimumLength = 1;
		const maximumLength = 50;
		const allowableCharacters = "n";
		const stringFieldState: IFieldState = { id: 'stringA', name: 'First string field', nullPrevalence: 0.1, restrictions: { kind: FieldKinds.String, minimumLength, maximumLength, allowableCharacters } };
		const childrenFieldStates : IFieldState[] = [stringFieldState];

		initialState = {...initialState, currentProfile : {
			fields : childrenFieldStates
		}};

		store = mockStore(initialState);

		// Act
		wrapper = mount(
			<Provider store={store} >
				<ProfileField id={stringFieldState.id} name={stringFieldState.name} kind={stringFieldState.restrictions.kind} />
			</Provider>
		);

		// Assert
		expect(wrapper.find('div[id]')).toHaveLength(1);
		expect(wrapper.find(`input[placeholder="Field name"][value="${stringFieldState.name}"]`)).toHaveLength(1);
		expect(wrapper.find(`div[aria-valuenow=${stringFieldState.nullPrevalence}]`)).toHaveLength(1);
		expect(wrapper.find(`input[value=${minimumLength}]`)).toHaveLength(1);
		expect(wrapper.find(`input[value=${maximumLength}]`)).toHaveLength(1);
		expect(wrapper.find(`input[value="${allowableCharacters}"]`)).toHaveLength(1);
	});

	it("Should display enumeration field", () => {
		// Arrange
		const firstMember : IEnumMember = { id:'someValue', name:'someValue', prevalence:0.95 };
		const secondMember : IEnumMember = { id:'someOtherValue', name:'someOtherValue', prevalence:0.05 };
		const enumMembers : IEnumMember[] = [ firstMember, secondMember ];
		const enumerationFieldState: IFieldState = { id: 'enumerationA', name: 'First enumeration field', nullPrevalence: 0.4, restrictions: { kind: FieldKinds.Enum, members: enumMembers } };
		const childrenFieldStates : IFieldState[] = [enumerationFieldState];

		initialState = {...initialState, currentProfile : {
			fields : childrenFieldStates
		}};

		store = mockStore(initialState);

		// Act
		wrapper = mount(
			<Provider store={store} >
				<ProfileField id={enumerationFieldState.id} name={enumerationFieldState.name} kind={enumerationFieldState.restrictions.kind} />
			</Provider>
		);

		// Assert
		expect(wrapper.find('div[id]')).toHaveLength(1);
		expect(wrapper.find(`input[placeholder="Field name"][value="${enumerationFieldState.name}"]`)).toHaveLength(1);
		expect(wrapper.find(`div[aria-valuenow=${enumerationFieldState.nullPrevalence}]`)).toHaveLength(1);
		// 3 rows, one being the footer
		expect(wrapper.find(`tbody tr`)).toHaveLength(3);
		expect(wrapper.find(`table div[aria-valuenow=${firstMember.prevalence}]`)).toHaveLength(1);
		expect(wrapper.find(`table input[value="${firstMember.name}"]`)).toHaveLength(1);
		expect(wrapper.find(`table div[aria-valuenow=${secondMember.prevalence}]`)).toHaveLength(1);
		expect(wrapper.find(`table input[value="${secondMember.name}"]`)).toHaveLength(1);
	});

	it("Should display temporal field", () => {
		// Arrange
		const minimum = '01/01/2002';
		const maximum = '02/02/2018';
		const temporalFieldState: IFieldState = { id: 'temporalA', name: 'First temporal field', nullPrevalence: 0.44, restrictions: { kind: FieldKinds.Temporal, minimum, maximum } };
		const childrenFieldStates : IFieldState[] = [temporalFieldState];

		initialState = {...initialState, currentProfile : {
			fields : childrenFieldStates
		}};

		store = mockStore(initialState);

		// Act
		wrapper = mount(
			<Provider store={store} >
				<ProfileField id={temporalFieldState.id} name={temporalFieldState.name} kind={temporalFieldState.restrictions.kind} />
			</Provider>
		);

		// Assert
		expect(wrapper.find('div[id]')).toHaveLength(1);
		expect(wrapper.find(`input[placeholder="Field name"][value="${temporalFieldState.name}"]`)).toHaveLength(1);
		expect(wrapper.find(`div[aria-valuenow=${temporalFieldState.nullPrevalence}]`)).toHaveLength(1);
		expect(wrapper.find(`input[value="${minimum}"]`)).toHaveLength(1);
		expect(wrapper.find(`input[value="${maximum}"]`)).toHaveLength(1);
	});

});