/**
 * Created by kevmi on 26/01/2017.
 */

$(document).ready(function () {
    const dataTable = $('#tests').dataTable(
        {
            "bFilter": true,
            "sDom": "ltpi"
        }
    );
    $("#searchbox").keyup(function () {
        dataTable.fnFilter(this.value);
    });
    $("#selectboxMethode").change(function () {
        dataTable.fnFilter(this.value, 4, false, false, true, false);
    });
    $("#selectboxDataset").change(function () {
        dataTable.fnFilter(this.value, 5, false, false, true, false);
    });
});
function modif(test) {
    document.location.href = '/GraphMatching/FicheTest?test=' + test;
};

function getExec(id, nom) {
    const param = "<form action='/GraphMatching/NewExecution' method='post'>" +
        "<fieldset>" +
        "<legend>Nouvelle exécution du test " + nom + " :</legend>" +
        "<label for='nom' class='label'>Nom : </label>" +
        "<input type='text' id ='nom' name ='nom' required/></br></br>" +
        "<label for='description'  class='label'>Description : </label>" +
        "<textarea type='text' id='description' name='description'></textarea>" +
        "<input type='hidden' id='id' name='id' value='" + id + "'/>" +
        "<input type='submit' value='Exécuter le test' id='enregistrer'/>" +
        "</fieldset>" +
        "</from>";
    document.getElementById('exec').innerHTML = param;
}