/**
 * Created by kevmi on 26/01/2017.
 */

$(document).ready(function () {
    const dataTable = $('#datasets').dataTable(
        {
            "bFilter": true,
            "sDom": "ltpi"
        }
    );
    $("#searchbox").keyup(function () {
        dataTable.fnFilter(this.value);
    });
    $("#selectbox").change(function () {
        dataTable.fnFilter(this.value, 2, false, true, true, false);
    });
});
function modif(dataset) {
    document.location.href = '/GraphMatching/FicheDataset?dataset=' + dataset;
};