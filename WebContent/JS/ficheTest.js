/**
 * Created by kevmi on 26/01/2017.
 */
$(document).ready(function () {
    const methodes = $('#methodes').DataTable({
        columnDefs: [{
            orderable: false,
            className: 'select-checkbox',
            targets: 0
        }],
        select: {
            style: 'multi',
            selector: 'td:first-child',
        },
        "sDom": '<ti>',
        "scrollY": "300px",
        "scrollCollapse": true,
        "paging": false,
        "bSort": false

    });
    const datasets = $('#datasets').DataTable({
        columnDefs: [{
            orderable: false,
            className: 'select-checkbox',
            targets: 0
        }],
        select: {
            style: 'multi',
            selector: 'td:first-child',
        },
        "sDom": '<ti>',
        "scrollY": "300px",
        "scrollCollapse": true,
        "paging": false,
        "bSort": false
    });
    //selection des lignes pour modification test
    methodes.rows('.memo').select();
    datasets.rows('.memo').select();
});

function addParam() {
    const param = "<div></br></br>" +
        "<label for='nomsParam[]'> Nom : </label>" +
        "<input type='text' name='nomsParam[]' id='nomsParam[]'/> " +
        "<label for='valeursParam[]'>Valeur : </label>" +
        "<input type='text' name='valeursParam[]' id='valeursParam[]'/> " +
        "<input class='deleteParam' type='button' value='Supprimer'/></div>";
    $('#paramSup').append(param);
}

function addParamHeuristique(nomtype) {
    const paramHeuristique = "<div></br></br>" +
    "<label for='nom' class='label'>"+ nomtype +" :</label>" +
    "<input type='text' name='valeur' id='valeur' required='required'/> "
    $('#paramNecH').append(paramHeuristique);
}
 
function getParams(id, nom, paramsHeuristique) {
	//alert("start");
	var newParamsHeuristique = paramsHeuristique.substring(1,paramsHeuristique.length-1);
	paramsHeuristiqueCut = newParamsHeuristique.split(",");
	/*for(var i = 0 ; i < paramsHeuristiqueCut.length ; i++){		
		//alert(paramsHeuristiqueCut[i]);
	}*/
	if(paramsHeuristique.length>2){
		//alert( paramsHeuristiqueCut.length);
		document.getElementById('divParamNew').style.display="";
		const param = 		
			"<div id='divParamH'>"+
				"<fieldset id='paramNecH'>"+
					"<legend>Veuillez entrer les valeurs de paramètre nécessaires pour " + nom + " :</legend>" +
					//"<input type='button' value='任意添加' onclick='addParamHeuristique()'>"+//测试按钮
						"<c:forEach items='${params.keySet()}' var='key'>"+             
							"<div>"+
								"<br/><br/>"+  
								"<label for='nom' class='label'>" +  paramsHeuristiqueCut[0] + " : " + "</label>" +
								"<input type='text' name='valeur' id='valeur' required='required' value=''${key}'/>"+      
							"</div>"+
						"</c:forEach>"+
				"</fieldset>"+
			"</div>";
		
		document.getElementById('divParamNew').innerHTML = param;
		for (var i=0;i<paramsHeuristiqueCut.length-1;i++)
			addParamHeuristique(paramsHeuristiqueCut[i+1]);
	}
	else
	{
		document.getElementById('divParamNew').style.display="none";
	}
}

$(document).on("click", ".deleteParam", function(){
    $(this).closest("div").remove();
});

function getSelected() {
    const selectedMethodes = $('#methodes').DataTable().rows({selected: true}).nodes();
    for (var i = 0; i < selectedMethodes.length; i++) {
        $('#form').append("<input type='hidden' name='selectedMethode[]' value='" + selectedMethodes[i].id + "'/>");
    }
    const selectedData = $('#datasets').DataTable().rows({selected: true}).nodes();
    for (var i = 0; i < selectedData.length; i++) {
        $('#form').append("<input type='hidden' name='selectedData[]' value='" + selectedData[i].id + "'/>");
    }
}
function validForm() {
    const selectedMethodes = $('#methodes').DataTable().rows({selected: true}).nodes();
    if (selectedMethodes.length < 2) {
        alert("Vous devez sélectionner au moins deux modèles pour les comparer !");
        return false;
    }
    const selectedData = $('#datasets').DataTable().rows({selected: true}).nodes();
    if (selectedData.length < 1) {
        alert("Vous devez sélectionner au moins un dataset ou un subset !");
        return false;
    }
}

