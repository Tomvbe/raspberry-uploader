const isExistingDirectoryYes = "#isExistingDirectoryYes";
const isExistingDirectoryNo = "#isExistingDirectoryNo";
const newDirectoryInput = "#newDirectoryInput";
const existingDirectoryInput = "#existingDirectoryInput";
const newDirectory = "#newDirectory";
const existingDirectory = "#existingDirectory";

window.onload = function() {
    updateDirectoryList(document.getElementById("type").value);
    createDirectorySelectionVisibilityListener();
    activateNotifications();
}

function areMandatoryFieldsValid() {
    if ($(isExistingDirectoryNo).is(":checked")) {
        if(!$(newDirectoryInput).val()) {
            alert("PLEASE SELECT A DIRECTORY.")
            return false;
        }
    }

    return true;
}

function upload() {
    // To avoid duplicate upload calls by double-clicking.
    disableButtonTemporarily('#uploadButton');

    if (!areMandatoryFieldsValid()) {
        return;
    }

    const files = document.getElementById('dropper').files;
    let fileCountToProcess = files.length;
    uploadFiles(files);

    function uploadFiles(files) {
        Array.from(files).forEach(file => {
            const data = new FormData($('form')[0]);
            // noinspection JSCheckFunctionSignatures
            data.set('files', file);
            data.set('directory', $(isExistingDirectoryYes).is(":checked") ? $(existingDirectoryInput).val() : $(newDirectoryInput).val());
            postUploadFile(data, convertFileNameToId(file.name));
        })
    }

    function postUploadFile(data, fileName) {
        $.ajax({
            url: 'ajax/upload',
            type: 'POST',
            data,
            cache: false,
            contentType: false,
            processData: false,
            xhr: () => monitorUploadProcess(fileName),
            error: (xhr) => handleError(xhr)
        });
    }

    function handleError(xhr) {
        $('.errorContainer')[0].innerHTML = xhr?.responseJSON?.trace;
    }

    function monitorUploadProcess(fileName) {
        let myXhr = $.ajaxSettings.xhr();
        if (myXhr.upload) {
            // For handling the progress of the upload
            myXhr.upload.addEventListener('progress', function (e) {
                if (e.lengthComputable) {
                    $('#' + fileName).attr({
                        value: e.loaded,
                        max: e.total,
                    });
                }
                if (e.loaded >= e.total && --fileCountToProcess === 0) {
                    new Notification("New message incoming", {
                        body: "Upload finished!",
                    })
                }
            }, false);
        }
        return myXhr;
    }
}

function disableButtonTemporarily(buttonId, disableTimeMs = 5000) {
    $(buttonId).attr('disabled', true);
    setTimeout(() => $('#uploadButton').attr('disabled', false), disableTimeMs);
}

/**
 * jquery id selector can't handle any special chars not even base64 (contains '=', '/' and '+').
 */
function convertFileNameToId(name) {
    let id = '';
    for (let i = 0; i < name.length; i++) {
        id += name.charCodeAt(i);
    }
    return id;
}

function createTableRowForFile(file) {
    return [
        Math.floor(file.size / 1000000) + ' MB',
        file.name,
        '<progress id=' + convertFileNameToId(file.name) + '></progress>'
    ]
}

function createUploadTable(files) {
    const builder = createTableBuilder();

    builder.addHeaders(['Size', 'Name', 'Progress']);
    Array.from(files).forEach(file => builder.addRow(createTableRowForFile(file)));

    document.getElementById('selectedFiles').innerHTML = builder.build();
}

function createTableBuilder() {

    const headers = [];
    const rows = [];

    function addHeaders(headersInput) {
        headersInput.forEach(header => headers.push('<th>' + header + '</th>'));
        return this;
    }

    function addRow(row) {
        rows.push('<tr>' + convertToTdRow(row).join('') + '</tr>');
        return this;
    }

    function convertToTdRow(row) {
        return row.map(value => '<td>' + value + '</td>');
    }

    function build() {
        return '<table>' + headers.join('') + rows.join('') + '</table>'
    }

    return {
        addHeaders,
        addRow,
        build
    }
}

function createDirectorySelectionVisibilityListener() {

    function showSelectExistingDirectory() {
        $(existingDirectory).show();
        $(newDirectory).hide();
        updateDirectoryList(document.getElementById("type").value);
    }

    function showSelectNewDirectory() {
        $(existingDirectory).hide();
        $(newDirectory).show();
    }

    $(isExistingDirectoryYes).on('change', function () {
        this.checked ? showSelectExistingDirectory() : showSelectNewDirectory();
    });

    $(isExistingDirectoryNo).on('change', function () {
        this.checked ? showSelectNewDirectory() : showSelectExistingDirectory();
    });
}

function updateDirectoryList(mediaType) {
    getDirectoryList(mediaType)
}

function getDirectoryList(type) {

    function removeOptions(selectElement) {
        let i, L = selectElement.options.length - 1;
        for(i = L; i >= 0; i--) {
            selectElement.remove(i);
        }
    }

    $.get( "directory/list", { type } )
        .done(data => {

            const select = document.getElementById("existingDirectoryInput");
            removeOptions(select);

            data.forEach(dir => {
                const el = document.createElement("option");
                el.textContent = dir;
                el.value = dir;
                select.appendChild(el);
            })
        });
}

function activateNotifications() {
    if (Notification.permission !== "denied") {
        Notification.requestPermission().then(permission => {
            console.log(permission);
        });
    }
}

