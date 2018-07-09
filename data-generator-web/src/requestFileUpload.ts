let currentHiddenFileInputElement: HTMLInputElement | null = null;

export default function(): Promise<File[]>
{
	if (currentHiddenFileInputElement) // do we have an element lingering...? not sure when that would happen, but should avoid infinite buildup
		document.body.removeChild(currentHiddenFileInputElement);

	return new Promise((resolve, reject) => {
		const hiddenFileInputElement = document.createElement("input");
		hiddenFileInputElement.style.display = "none";
		hiddenFileInputElement.type = "file";

		const removeElementFromDocument = () => document.body.removeChild(hiddenFileInputElement);

		hiddenFileInputElement.onchange = () => {
			if (!hiddenFileInputElement.files || hiddenFileInputElement.files.length === 0) {
				reject("No files selected");
				return;
			}

			const selectedFiles = Array.from(hiddenFileInputElement.files);

			removeElementFromDocument();

			resolve(selectedFiles);
		};
		hiddenFileInputElement.onerror = (ev: ErrorEvent) => {
			removeElementFromDocument();

			reject(ev.message);
		};

		hiddenFileInputElement.click();

		currentHiddenFileInputElement = hiddenFileInputElement;

		document.body.appendChild(hiddenFileInputElement);
	});
}
