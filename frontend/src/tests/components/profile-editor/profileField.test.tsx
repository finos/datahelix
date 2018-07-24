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

});