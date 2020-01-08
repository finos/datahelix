const generatorEndpoint = "https://1551npsbdh.execute-api.eu-west-2.amazonaws.com/api/generator";
const runButton = document.getElementById("run");
const generatorOutput = document.getElementById("output");
const profileArea = document.getElementById("profile");
const editorPanel = document.getElementById("editor-panel");
const spinner = document.getElementById("spinner");
const shareUrl = document.getElementById("shareUrl");
const examplesDisplay = document.getElementById("examples");
const examplesButton = document.getElementById("examplesButton");
const examplesRootUrl = "examples.json";

new ClipboardJS("#copyButton");

// decode the hash into a profile
if (window.location.hash) {
  const encoded = window.location.href.split("#")[1];
  profileArea.value = atob(decodeURIComponent(encoded));
}

// if the share button is pressed, encode the profile 
$("#shareModal").on("show.bs.modal", () => {
  const baseUrl = window.location.href.split("#")[0];
  const code = encodeURIComponent(btoa(editor.getValue()));
  shareUrl.value = `${baseUrl}#${code}`;
});

// configure the editor
const editor = CodeMirror.fromTextArea(profileArea, {
  mode: { name: "javascript", json: true },
  lineNumbers: true
});

const showAlert = message => {
  const template = document.querySelector('#error-popup-template');
  const alert = document.importNode(template.content, true);
  alert.querySelector(".content").innerText = message;
  editorPanel.prepend(alert);
}

const hideAlert = () => {
  const alert = document.getElementById("error-popup");
  if (alert) {
    alert.remove();
  }
}

const isLoading = (loading) => {
  if (loading) {
    runButton.disabled = true;
    spinner.style.display = "";
  } else {
    runButton.disabled = false;
    spinner.style.display = "none";
  }
}

// takes an array of items and creates an HTML snippet with each item 
// hosted within the given element type.
const mapToElements = (arr, element, fn = v => v) => 
    arr.map(value => "<" + element + ">" + fn(value) + "</" + element + ">").join("");

runButton.addEventListener("click", () => {
  generatorOutput.innerHTML = "";
  const profile = editor.getValue();

  hideAlert();

  try {
    JSON.parse(profile);
  } catch (error) {
    showAlert("Profile is not valid JSON");
    return;
  }

  isLoading(true);
  fetch(generatorEndpoint, {
    method: 'POST',
    mode: 'cors',
    headers: {
      'Content-Type': 'text/plain'
    },
    body: profile
  })
    .then(response => {
      if (!response.ok) {
        throw new Error("The endpoint returned an error");
      }
      return response.json()
    })
    .then(responseJson => {

      if (responseJson.errorMessage) {
        showAlert(responseJson.errorMessage);
        return;
      }

      const data = responseJson.generatedData;

      const columns = data[0];
      const rows = data.slice(1);

      const table = "<thead><tr>" + mapToElements(columns, "th") + "</tr></thead>" +
        "<tbody>" + mapToElements(rows, "tr", row => mapToElements(row, "td")) + "</tbody>";

      generatorOutput.innerHTML = table;
    })
    .catch(() => {
      showAlert("There was a problem in reaching the DataHelix API endpoint");
    })
    .finally(() => isLoading(false));
});

const createCategory = (categoryName, categoryMap) => {
  const categoryElement = document.createElement("div");
  categoryElement.className = "example-category";

  const categoryNameElement = document.createElement("h4");
  categoryNameElement.innerText = categoryName;
  categoryElement.appendChild(categoryNameElement);

  examplesDisplay.appendChild(categoryElement);

  const listElement = document.createElement("ol");
  categoryElement.appendChild(listElement);

  categoryMap[categoryName] = listElement;
  return listElement;
}

const showExamples = (show) => {
  examplesDisplay.style.display = (examplesDisplay.style.display === "none" || show)
    ? ""
    : "none";
}

const hideExamples = () => {
  examplesDisplay.style.display = "none";
}

const loadExamples = () => {
  fetch(examplesRootUrl).then(resonse => {
    editor.setValue("Loading examples...");
    hideExamples();

    resonse.json().then(profileExamples => {
      const categoryMap = {};
      let firstExample = null;

      profileExamples.sort((a, b) => {
        const categoryComparison = a.category.localeCompare(b.category);
        return categoryComparison === 0
          ? a.description.localeCompare(b.description)
          : categoryComparison;
      });

      profileExamples.forEach(profileExample => {
          const category = categoryMap[profileExample.category] || createCategory(profileExample.category, categoryMap);

          const profileElement = document.createElement("li");
          profileElement.innerText = profileExample.description;
          profileElement.addEventListener("click", () => {
              hideExamples();
              editor.setValue(JSON.stringify(profileExample.profile, null, 2));
          });

          category.appendChild(profileElement);
          firstExample = firstExample || profileElement;
      });

      if (profileExamples.length > 0) {
        firstExample.click();

        examplesButton.addEventListener("click", () => {
          showExamples();
        })
      } else {
        editor.setValue("No examples loaded.");
      }
    }, jsonErr => {
      editor.setValue("Error loading examples: " + jsonErr)
    });
  }, requestErr => {
    editor.setValue("Error requesting examples: " + requestErr)
  });
};

loadExamples();