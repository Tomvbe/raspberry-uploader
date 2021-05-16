function postUploadFile(data, sequenceId) {
    $.ajax({
        url: 'upload',
        type: 'POST',

        // Form data
        data,

        // Tell jQuery not to process data or worry about content-type
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
                        const progressId = '#progressId' + sequenceId;
                        // $('progress').attr({
                        $(progressId).attr({
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
    let sequenceId = 0;
    Array.from(files).forEach(file => {
        const data = new FormData($('form')[0]);
        data.set('files', file);
        postUploadFile(data, sequenceId++);
    })
}

function createUploadTable(droppedFiles) {
    const builder = createTableBuilder();
    builder.addHeaders(['Size', 'Name', 'Progress']);

    let idSequence = 0;
    Array.from(droppedFiles).forEach(file => {
        builder.addRow([
            Math.floor(file.size / 1000000) + ' MB',
            file.name,
            '<progress id=progressId' + idSequence++ + '></progress>'
        ])
    })

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
