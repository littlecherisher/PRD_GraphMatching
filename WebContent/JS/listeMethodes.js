/**
 * Created by kevmi on 26/01/2017.
 */
$(document).ready(function () {
    const dataTable = $('#methodes').dataTable(
        {
            "bFilter": true,
            "sDom": "ltpi"
        }
    );
    $("#searchbox").keyup(function () {
        dataTable.fnFilter(this.value);
    });
});
function modif(methode) {
	/*if (methode.paramsHeuristique != null) {
		document.location.href = '/GraphMatching/FicheMethodeHeuristique?methode=' + methode;
	} else {
		document.location.href = '/GraphMatching/FicheMethodeExacte?methode=' + methode;
	}*/
	document.location.href = '/GraphMatching/FicheMethodeExacte?methode=' + methode;
};