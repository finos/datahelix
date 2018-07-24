import * as React from "react";
import ProfileEditor from '../../../components/profile-editor/ProfileEditor';
import { configure, mount, shallow } from 'enzyme';
import * as Enzyme from 'enzyme';
import * as Adapter from 'enzyme-adapter-react-16';
import configureStore from 'redux-mock-store';
import { Provider } from "react-redux";
import { IFieldState, FieldKinds, IAppState } from "../../../redux/state/IAppState";
import ProfileField from "../../../components/profile-editor/ProfileField";

describe('Profile editor', () => {
	const mockStore = configureStore();
	let store : any, wrapper;
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

	it("Should display children", () => {
		// Arrange
		const genericFieldA : React.ReactNode = <ProfileField name='First generic field' id='genericA' key='genericA' kind={FieldKinds.Unclassified} />;
		const genericFieldB: React.ReactNode = <ProfileField name='Second generic field' id='genericB' key='genericB' kind={FieldKinds.Unclassified} />;
		const childrenFields : React.ReactNode[] = [genericFieldA, genericFieldB];

		const genericFieldStateA: IFieldState = { id: 'genericA', name: 'First generic field', nullPrevalence: 0, restrictions: { kind: FieldKinds.Unclassified } };
		const genericFieldStateB: IFieldState = { id: 'genericB', name: 'Second generic field', nullPrevalence: 0.5, restrictions: { kind: FieldKinds.Unclassified } };
		const childrenFieldStates : IFieldState[] = [genericFieldStateA, genericFieldStateB];

		initialState = {...initialState, currentProfile : {
			fields : childrenFieldStates
		}};

		store = mockStore(initialState);

		// Act
		wrapper = mount(
			<Provider store={store} >
				<ProfileEditor children={childrenFields} />
			</Provider>
		);

		// Assert
		expect(wrapper.find('form div[id]')).toHaveLength(2);
	});

	it("Should handle no children", () => {
		// Arrange
		const childrenFields : React.ReactNode[] = [];
		const childrenFieldStates : IFieldState[] = [];

		initialState = {...initialState, currentProfile : {
			fields : childrenFieldStates
		}};

		store = mockStore(initialState);

		// Act
		wrapper = mount(
			<Provider store={store} >
				<ProfileEditor children={childrenFields} />
			</Provider>
		);

		// Assert
		expect(wrapper.find('form div[id]')).toHaveLength(0);
	});
});