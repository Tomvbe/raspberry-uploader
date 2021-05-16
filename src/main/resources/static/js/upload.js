function postUploadFile(data, fileName) {
    $.ajax({
        url: 'ajax/upload',
        type: 'POST',
        data,

        // You *must* include these options!
        cache: false,
        contentType: false,
        processData: false,

        // Custom XMLHttpRequest
        xhr: function () {
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
                }, false);
            }
            return myXhr;
        }
    });
}

function upload() {
    const files = document.getElementById('dropper').files;
    Array.from(files).forEach(file => {
        const data = new FormData($('form')[0]);
        // noinspection JSCheckFunctionSignatures
        data.set('files', file);
        postUploadFile(data, convertFileNameToId(file.name));
    })
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
