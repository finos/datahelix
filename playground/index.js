const generatorEndpoint = "https://1551npsbdh.execute-api.eu-west-2.amazonaws.com/api/generator";
const runButton = document.getElementById("run");
const generatorOutput = document.getElementById("output");
const profileArea = document.getElementById("profile");
const editorPanel = document.getElementById("editor-panel");
const copyUrl = document.getElementById("copyUrl");
const spinner = document.getElementById("spinner");

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

copyUrl.addEventListener("click", () =>
  navigator.clipboard.writeText(shareUrl.value)
);

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
  } catch {
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
    .then(response => response.json())
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