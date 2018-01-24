/**
 * Created by kevmi on 26/01/2017.
 */

$(document).ready(function () {
    const dataTable = $('#executions').dataTable(
        {
            "bFilter": true,
            "sDom": "ltpi"
        }
    );
    $("#searchbox").keyup(function () {
        dataTable.fnFilter(this.value);
    });
    $("#selectboxTest").change(function () {
        dataTable.fnFilter(this.value, 2, false, false, true, false);
    });
    $("#selectboxEtat").change(function () {
        dataTable.fnFilter(this.value, 4, false, false, true, false);
    });
});

function info(id) {
    $.get('/GraphMatching/InfosExecution?execution=' + id, function(responseText){
        document.getElementById('infos').innerHTML = responseText;
    });
}