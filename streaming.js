class Buffer {
    reload = null;
    items = null;
    threshold = null;

    constructor(size, reload, threshold) {
        this.reload = reload;
        this.threshold = threshold || Math.round(size / 2);
    }

    next() {
        if (!this.items) {
            this.items = [];
            this.populateBuffer();
            return null;
        }

        let item = this.items.shift();
        if (this.items.length === this.threshold){
            this.populateBuffer();
        }
        return item;
    }

    populateBuffer() {
        this.reload(newItems => {
            if (!this.items || this.items.length === 0) {
                this.items = newItems;
                return;
            }
    

            this.items = this.items.concat(newItems);
        });
    }
}

class Streamer {
    constructor(profile, size, threshold, delay, ui) {
        this.profile = profile;
        this.delay = delay;
        this.refresh = null;
        this.startedStreaming = false;
        this.size = size;
        this.threshold = threshold;
        this.ui = ui;
    }

    reload(callback) {
        const _this = this;

        const loader = document.location.href.indexOf('localhost') === -1
            ? this.playground
            : this.localhost;

        loader(this.profile)
            .then((response) => response.json())
            .then((json) => _this.handleJson(json, callback))
            .catch ((err) => _this.handleError(err));
    }

    handleError(err) {
        if (this.startedStreaming) {
            this.ui.showError('Error getting generated data' + err);
        }
        this.dispose();
    }

    handleJson(json, callback) {
        const showHeader = !this.startedStreaming;
        this.startedStreaming = true;

        if (json.generatedData) {
            const header = json.generatedData.shift();
            if (showHeader) {
                this.ui.showHeader(header);
            }

            callback(json.generatedData);
        } else {
            throw new Error('Invalid generated data');
        }
    }

    playground(profile) {
        return fetch(
            'https://1551npsbdh.execute-api.eu-west-2.amazonaws.com/api/generator',
            { 
                method: 'POST', 
                body: profile,
                mode: 'cors',
                headers: { 'Content-Type': 'text/plain' } 
            });
    }

    localhost() {
        return fetch('http://localhost/datahelix/data.json');
    }

    updateDisplay() {
        const nextItem = this.buffer.next();
        if (nextItem) {
            this.ui.displayItem(nextItem);
        }
    }

    startStreaming() {
        const updateDisplay = this.updateDisplay.bind(this);
        this.buffer = new Buffer(
            this.size,
            this.reload.bind(this),
            this.threshold
        );
        this.refresh = window.setInterval(updateDisplay, this.delay);
    }

    swapProfile(profile) {
        this.dispose();
        if (this.startedStreaming) {
            this.ui.reset();
            this.startedStreaming = false;
        }
        this.profile = profile;
    }

    dispose() {
        window.clearInterval(this.refresh);
        delete this.refresh;
    }
}

class Ui {
    constructor(defaultProfile, displayContainer) {
        this.streamer = new Streamer(defaultProfile, 25, null, 100, this);
        this.displayContainer = displayContainer;
        const heading = this.displayContainer.querySelector('h1');
        heading.title = 'Click to stream a new profile...'
        heading.addEventListener('click', this.loadNewProfile.bind(this));
        this.resultsContainer = document.createElement('div');
        this.resultsContainer.id = 'demo-content';
    }

    showHeader(header) {
        this.resultsContainer.innerHTML = `
        <div id="field-headers">
            ${header.map(column => `<label>${column}</label>`).join('')}
        </div>`;
        this.displayContainer.appendChild(this.resultsContainer);
    }

    showError(error) {
        this.resultsContainer.innerHTML = error;
        this.displayContainer.appendChild(this.resultsContainer);
    }

    reset(){
        this.resultsContainer.innerHTML = '';
        this.displayContainer.removeChild(this.resultsContainer);
    }

    displayItem(item) {
        const row = document.createElement('div');
        row.className = 'dataitem';

        item.forEach(value => {
            const cell = document.createElement('span');
            cell.innerText = value;
            row.appendChild(cell);
        });

        const fieldHeaders = document.getElementById('field-headers');
        if (!fieldHeaders) {
            return;
        }

        const firstDisplayedRow = fieldHeaders.nextElementSibling;
        const rows = fieldHeaders.parentElement;
        if (firstDisplayedRow) {
            rows.insertBefore(row, firstDisplayedRow);
        } else {
            rows.appendChild(row);
        }

        const lastRow = rows.lastElementChild;
        if (rows.children.length > this.streamer.size + 1) {
            rows.removeChild(lastRow);
        }
    }

    openFile(fileType) {
        return new Promise((resolve, reject) => {
            const input = document.createElement("INPUT");
            input.type = "file";
            input.hidden = "true";
    
            if (fileType) {
                input.accept = fileType;
            }
            document.body.appendChild(input);
            input.addEventListener("change", function(e) {
                const files = e.target.files;
                var toLoad = files.length;
                if (!toLoad) {
                    return; //NOTE: Reject isn't called as it makes it consistent with when the 'Cancel' button is clicked in the open dialog, which cannot be detected.
                }
    
                const fileContents = {};        
                const file = files[0];
                const reader = new FileReader();
                reader.onload = function (loader) {
                    const contents = loader.target.result;
                    fileContents[file.name] = {
                        name: file.name,
                        size: file.size,
                        type: file.type,
                        content: contents
                    };
                    toLoad--;
    
                    if (toLoad === 0) {
                        resolve(fileContents);
                    }
                };
                reader.readAsText(file);
            });
    
            input.click();
    
            setTimeout(function() {
                document.body.removeChild(input);
            }, 99000);
        });
    }

    loadNewProfile() {
        this.openFile('.json')
            .then((files => {
                const fileArray = Object.values(files);
                if (fileArray.length) {
                    this.streamer.swapProfile(fileArray[0].content);
                    this.streamer.startStreaming();    
                }
            }).bind(this));
    }

    start(delay) {
        const streamer = this.streamer;

        window.setTimeout(() => {
            streamer.startStreaming();
        }, delay)
    }
}

window.addEventListener('load', () => {
    const profile = `{
    "fields": [
    {
      "name": "execution_time",
      "type": "datetime",
      "nullable": false
    },
    {
      "name": "nyse_stock",
      "type": "faker.stock.nyseSymbol",
      "nullable": false
    },
    {
      "name": "units_held",
      "type": "integer",
      "nullable": false
    },
    {
      "name": "unit_price",
      "type": "decimal",
      "nullable": false
    },
    {
      "name": "country_of_origin",
      "type": "faker.country.name",
      "nullable": false
    },
    {
      "name": "open_date",
      "type": "datetime",
      "nullable": false
    },
    {
      "name": "close_date",
      "type": "datetime",
      "nullable": true
    },
    {
      "name": "is_open",
      "type": "boolean",
      "nullable": false
    }
  ],
  "constraints": [
    {
      "field": "execution_time",
      "equalTo": "NOW"
    },
    {
      "field": "open_date",
      "afterOrAt": "1990-01-01T00:00:00.000"
    },
    {
      "field": "open_date",
      "beforeOrAt": "2025-01-01T00:00:00.000"
    },
    {
      "field": "close_date",
      "equalToField": "open_date",
      "offset": 3,
      "offsetUnit": "years"
    },
    {
      "if": { "allOf": [
          { "field": "open_date", "beforeOrAt": "NOW"},
          { "field": "close_date", "afterOrAt": "NOW"}
        ] },
      "then": { "field": "is_open", "equalTo": true },
      "else": { "field": "is_open", "equalTo": false }
    },
    {
      "field": "open_date",
      "granularTo": "days"
    },
    {
      "field": "close_date",
      "granularTo": "days"
    },
    {
      "field": "unit_price",
      "granularTo": 0.01
    },
    {
      "field": "unit_price",
      "greaterThan": 0
    },
    {
      "field": "unit_price",
      "lessThan": 1000
    },
    {
      "field": "units_held",
      "greaterThanOrEqualTo": 0
    },
    {
      "field": "units_held",
      "lessThan": 1000000
    }
  ]
}`;

    const ui = new Ui(profile, document.querySelector('.jumbotron'));
    ui.start(1000);
});
