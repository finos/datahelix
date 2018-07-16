import * as React from "react";
import * as ReactDOM from "react-dom";
import Bootstrapper from "../Bootstrapper";

it("Should bootstrap without crashing", () => {
	const rootElement = document.createElement('div');

	Bootstrapper.start(
		rootElement,
		false); //don't test the service worker since it's generated code

	ReactDOM.unmountComponentAtNode(rootElement);
});
