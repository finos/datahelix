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
const examplesContentUrl = "https://api.github.com/repos/finos/datahelix/contents/examples/";
const markdownConverter = new showdown.Converter();
const readmeBaseUrl = "https://github.com/finos/datahelix/tree/master/examples/";

new ClipboardJS("#copyButton");

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

  const listElement = document.createElement("div");
  listElement.className = "example-category-group";
  categoryElement.appendChild(listElement);

  categoryMap[categoryName] = listElement;
  return listElement;
}

const displayReadmeMarkdown = (markdown) => {
  const html = markdownConverter.makeHtml(markdown);

  const readmeHeading = "<p>Click <i>Run</i> above to execute this example.</p>";
  const readmeFooter = "";
  generatorOutput.innerHTML = readmeHeading + html + readmeFooter;
}

const renderExample = (profileExample) => {
  hideAlert();
  isLoading(true);
  window.location.hash = profileExample.profileDirectory;

  const profilePath = examplesContentUrl + profileExample.profileDirectory;
  const profileContentPath = profilePath + "/profile.json";
  const readmeContentPath = profilePath + "/README.md";

  editor.setValue("Loading profile...");
  generatorOutput.innerHTML = "Loading profile...";

  fetch(profileContentPath)
    .then(response => {
      response.json().then(apiData => {
        if (apiData.encoding === "base64") {
          editor.setValue(atob(apiData.content));
        } else {
          throw new Error("Unknown encoding for profile content: " + apiData.encoding);
        }
      }, err => {
        editor.setValue("Error reading profile content. " + err.message);
      })
    }, err => {
      editor.setValue("Error getting profile content." + err.message);
    })
    .then(() => {
      isLoading(false);
    });

    fetch(readmeContentPath)
    .then(response => {
      response.json().then(apiData => {
        if (apiData.encoding === "base64") {
          displayReadmeMarkdown(atob(apiData.content));
        } else {
          throw new Error("Unknown encoding for profile content: " + apiData.encoding);
        }
      }, err => {
        generatorOutput.innerHTML = "Error loading readme. " + err.message;
      })
    }, err => {
      generatorOutput.innerHTML = "Error getting readme. " + err.message;
    });
}

const loadExamples = () => {
  fetch(examplesRootUrl).then(response => {
    editor.setValue("Loading examples...");

    response.json().then(profileExamples => {
      const categoryMap = {};
      const profileMap = {};
      let defaultExample = null;

      profileExamples.sort((a, b) => {
        const categoryComparison = a.category.localeCompare(b.category);
        return categoryComparison === 0
          ? a.description.localeCompare(b.description)
          : categoryComparison;
      });

      profileExamples.forEach(profileExample => {
          const category = categoryMap[profileExample.category] || createCategory(profileExample.category, categoryMap);

          profileMap[profileExample.profileDirectory] = profileExample;
          const profileElement = document.createElement("button");
          profileElement.className = "dropdown-item";
          profileElement.type = "button";
          profileElement.innerText = profileExample.description;
          profileElement.setAttribute("data-profile-path", profileExample.profileDirectory);
          profileElement.addEventListener("click", () => {
              renderExample(profileExample);
          });

          category.appendChild(profileElement);
          if (profileExample.default) {
            defaultExample = defaultExample || profileElement;
          }
      });

      if (window.location.hash) {
        const encoded = window.location.href.split("#")[1];
        try {
          editor.setValue(atob(decodeURIComponent(encoded)));
        } catch (e) {
          renderExample({
            profileDirectory: encoded
          });
        }
      } else if (defaultExample) {
        defaultExample.click();
      }

      if (profileExamples.length === 0) {
        editor.setValue("No examples loaded.");
      }
    }, jsonErr => {
      editor.setValue("Error loading examples: " + jsonErr)
    });
  }, requestErr => {
    editor.setValue("Error requesting examples: " + requestErr)
  });
};

generatorOutput.addEventListener("click", (e) => {
  if (e.target.tagName !== "A" || !e.target.getAttribute("href")) {
    return;
  }

  const href = e.target.getAttribute("href");
  if (href.indexOf("https://") !== 0 && href.indexOf("http://") !== 0) {
    //link to another example?
    const exampleName = href.replace(/[\.\/]/g, "");
    if (exampleName) {
      e.preventDefault();
      renderExample({
        profileDirectory: exampleName
      });
      return false;
    }

    e.target.setAttribute("href", readmeBaseUrl + "someExample/" + href);
  }

  if (href.indexOf("https://finos.github.io/datahelix/playground/#") !== 0) {
    e.target.setAttribute("target", "_blank");
  }
})

loadExamples();