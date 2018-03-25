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

function modif(id, paramsHeuristique) {
	if (paramsHeuristique.length>2){
		document.location.href = '/GraphMatching/FicheMethodeHeuristique?methode=' + id;
	}else{
		document.location.href = '/GraphMatching/FicheMethodeExacte?methode=' + id;
	}
};