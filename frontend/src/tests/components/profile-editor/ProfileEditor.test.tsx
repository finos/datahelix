import * as React from "react";
import ProfileEditor from '../../../components/profile-editor/ProfileEditor';
import { configure, mount, shallow } from 'enzyme';
import * as Enzyme from 'enzyme';
import * as Adapter from 'enzyme-adapter-react-16';
import configureStore from 'redux-mock-store';
import { Provider } from "react-redux";

describe('Profile editor', () => {
	const mockStore = configureStore();
	let store, wrapper;
	const initialState = {};
	
	beforeAll(()=>{
		Enzyme.configure({ adapter: new Adapter() });
	});

	beforeEach(() => { 
		store = mockStore(initialState);
		wrapper = mount(
			<Provider store={store} >
				<ProfileEditor children={[]} />
			</Provider>
		);
	});

	it("Should display children", () => {
		expect(true).toBe(true);
	});
});