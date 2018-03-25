/**
 * Created by Kai on 24/01/2018.
 */

$(document).ready(function() {
	const
	methodes = $('#methodes').DataTable({
		columnDefs : [ {
			orderable : false,
			className : 'select-checkbox',
			targets : 0
		} ],
		select : {
			style : 'multi',
			selector : 'td:first-child',
		},
		"sDom" : '<ti>',
		"scrollY" : "300px",
		"scrollCollapse" : true,
		"paging" : false,
		"bSort" : false

	});
	const
	datasets = $('#datasets').DataTable({
		columnDefs : [ {
			orderable : false,
			className : 'select-checkbox',
			targets : 0
		} ],
		select : {
			style : 'multi',
			selector : 'td:first-child',
		},
		"sDom" : '<ti>',
		"scrollY" : "300px",
		"scrollCollapse" : true,
		"paging" : false,
		"bSort" : false
	});
	//selection des lignes pour modification test
	methodes.rows('.memo').select();
	datasets.rows('.memo').select();
});

function addParam() {
	const
	param = "<div></br></br>"
			+ "<label for='nomsParam[]'> Nom : </label>"
			+ "<input type='text' name='nomsParam[]' id='nomsParam[]'/> "
			+ "<label for='valeursParam[]'>Valeur : </label>"
			+ "<input type='text' name='valeursParam[]' id='valeursParam[]'/> "
			+ "<input class='deleteParam' type='button' value='Supprimer'/></div>";
	$('#paramSup').append(param);
}

function addParamHeuristique(nomtype, i) {
	const
	paramHeuristique = "<div></br></br>"
			+ "<input type='text' name='nomsParamHeu[]' id='nomsParamHeu[]' style='border:0px;font-size:14px' size='12' readonly='readonly'></input>"
			+ "<input type='text' name='typesParamHeu[]' id='typesParamHeu[]' style='border:0px;font-size:14px' size='1' readonly='readonly'>"
			+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
			+ "</input>"
			+ "<input type='text' name='valeursParamHeu[]' id='valeursParamHeu[]' style='font-size:14px' size='10' required='required'/> ";
	$('#paramNecH').append(paramHeuristique);
	document.getElementById('nomsParamHeu[]').id = "**" + i;
	document.getElementById('typesParamHeu[]').id = "***" + i;
}

function addParamHeuristique2(nomtype, i, Bp) {
	const
	paramHeuristique = "<div></br></br>"
			+ "<input type='text' name='nomsParamHeu[]' id='nomsParamHeu[]' style='border:0px;font-size:14px' size='12' readonly='readonly'></input>"
			+ "<input type='text' name='typesParamHeu[]' id='typesParamHeu[]' style='border:0px;font-size:14px' size='1' readonly='readonly'>"
			+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
			+ "</input>"
			+ "<input type='text' name='valeursParamHeu[]' id='valeursParamHeu[]' style='font-size:14px' size='10' required='required'/> ";
	$('#paramNecH').append(paramHeuristique);
	document.getElementById('nomsParamHeu[]').id = "**" + i;
	document.getElementById('typesParamHeu[]').id = "***" + i;
	document.getElementById('valeursParamHeu[]').id = "##**" + Bp;
	Bp++;
	return Bp;
}

function getParams(id, nom, paramsHeuristique, selectFlag,countFlag) {
	var newParamsHeuristique = paramsHeuristique.substring(1,
			paramsHeuristique.length - 1);
	paramsHeuristiqueCut = newParamsHeuristique.split(",");
	var checkboxSelect = document.getElementById(id).className;
	for (var i = 1; i < paramsHeuristiqueCut.length; i++) {
		paramsHeuristiqueCut[i] = paramsHeuristiqueCut[i].substring(1);
	}
	var paramsHeuristiqueDouble = [];
	for (var k = 0; k < paramsHeuristiqueCut.length; k++) {
		paramsHeuristiqueDouble[k] = [];
	}
	for (var i = 0; i < paramsHeuristiqueCut.length; i++) {
		var a = paramsHeuristiqueCut[i].indexOf('----');
		paramsHeuristiqueDouble[i][0] = paramsHeuristiqueCut[i].substring(0, a);
		paramsHeuristiqueDouble[i][1] = paramsHeuristiqueCut[i]
				.substring(a + 4);
		//paramsHeuristiqueDouble[i][2]=paramsHeuristiqueDouble[i][0].length;
	}
	var divid = "##" + id;
	var selectFlagNew = 0;
	var deleteFlag = 0;
	if (checkboxSelect.indexOf("selected") > -1) //l'opération de suppression de choisir à nouveau
	{
		if (paramsHeuristique.length > 2) //Supprimer la zone de saisie dynamique pour les heuristiques
		{
			var mybox = document.getElementById(divid);
			mybox.parentNode.removeChild(mybox);
			//document.getElementById(divid).style.display = "none";
			//document.getElementById(divid).id = "delete";
		}
		for (var i=1;i<=countFlag;i++)
			//SelectFlag est égal à 0 lorsque toutes les méthodes sont vérifiées de se supprimer
		{		
				var getclassName = document.getElementById(i).className;
				if (getclassName.indexOf("selected") < 0)
				{
					deleteFlag ++;
				}
				
		}
		if(deleteFlag+1 == countFlag)
		{
			selectFlag = 0;
		}
		return selectFlag;
	} 
	else 
	{
		if (paramsHeuristique.length > 2) //Comparaison de différents types de méthodes
			selectFlagNew = 2;//heuristique
		else
			selectFlagNew = 1;//exacte
		//Déterminer si la méthode appartient au même type
		if (selectFlag == 0 || selectFlag == selectFlagNew) {
			if (paramsHeuristique.length > 2) {
				document.getElementById('divParamNew').style.display = "";
				const
				param = "<div id='divParamNew2'>"
						+ "<fieldset id='paramNecH'>"
						+ "<legend id='divParamNew3'>Veuillez entrer les valeurs des paramètres nécessaires pour "
						+ nom
						+ " :</legend>"
						+
						"<c:forEach items='${params.keySet()}' var='key'>"
						+ "<div>"
						+ "<br/><br/>"
						+ "<label>"
						+ "Nom&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
						+ "Type&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
						+ "Valeur"
						+ "</label>"
						+ "<br/><br/>"
						+ "<input type='text' name='nomsParamHeu[]' id='**0' style='border:0px;font-size:14px' size='12' readonly='readonly'></input>"
						+ "<input type='text' name='typesParamHeu[]' id='***0' style='border:0px;font-size:14px' size='1' readonly='readonly'>"
						+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
						+ "</input>"
						+ "<input type='text' name='valeursParamHeu[]' id='valeursParamHeu[]' style='font-size:14px' size='10' required='required' value=''${key}'/>"
						+ "</div>" + "</c:forEach>" + "</fieldset>" + "</div>";	
				document.getElementById('divParamNew').innerHTML = param;
				for (var i = 0; i < paramsHeuristiqueCut.length - 1; i++)
					addParamHeuristique(paramsHeuristiqueCut[i + 1], i + 1);
				const
				paramNew = document.getElementById('divParamNew').innerHTML;
				document.getElementById('divParamNew').innerHTML = "";
				$('#divParamNewLarge').append(paramNew);
				document.getElementById('divParamNew2').id = "##" + id;
				for (var i = 0; i < paramsHeuristiqueCut.length; i++) {
					document.getElementById('**' + i).value = paramsHeuristiqueDouble[i][0];
					document.getElementById('***' + i).value = paramsHeuristiqueDouble[i][1];
					document.getElementById('**' + i).id = 'already';
					document.getElementById('***' + i).id = 'already2';
				}

			} else {
				//Description: Ouvre la ligne de code suivante (à la demande) pour obtenir la fonction: 
				//Après avoir sélectionné une méthode heuristique, si vous choisissez une méthode exacte, 
				//la boîte de saisie des paramètres de l'heuristique précédente disparaît. 
				//À ce moment, cette fonction a été abolie.
				//document.getElementById('divParamNew').style.display = "none";
			}
			return selectFlagNew;
		}
		else
		{
			document.getElementById('#td:'+id).click();
			alert('Les types des méthodes que vous choisissez sont différents, le choix n\'est pas valide!');
			if (paramsHeuristique.length > 2) //Si heuristique, supprimez la zone de saisie dynamique
			{
				var myboxDelete = document.getElementById(divid);
				myboxDelete.parentNode.removeChild(myboxDelete);
			}
			return selectFlag;
		}
	}
}

$(document).on("click", ".deleteParam", function() {
	$(this).closest("div").remove();
});

function getParams2(id, nom, paramsHeuristique, Bp) {
	var newParamsHeuristique = paramsHeuristique.substring(1,
			paramsHeuristique.length - 1);
	paramsHeuristiqueCut = newParamsHeuristique.split(",");
	for (var i = 1; i < paramsHeuristiqueCut.length; i++) {
		paramsHeuristiqueCut[i] = paramsHeuristiqueCut[i].substring(1);
	}
	var paramsHeuristiqueDouble = [];
	for (var k = 0; k < paramsHeuristiqueCut.length; k++) {
		paramsHeuristiqueDouble[k] = [];
	}
	for (var i = 0; i < paramsHeuristiqueCut.length; i++) {
		var a = paramsHeuristiqueCut[i].indexOf('----');
		paramsHeuristiqueDouble[i][0] = paramsHeuristiqueCut[i].substring(0, a);
		paramsHeuristiqueDouble[i][1] = paramsHeuristiqueCut[i]
				.substring(a + 4);
	}
	if (paramsHeuristique.length > 2) {
		document.getElementById('divParamNew').style.display = "";
		const
		param =
		"<div id='divParamNew2'>"
				+ "<fieldset id='paramNecH'>"
				+ "<legend id='divParamNew3'>Veuillez entrer les valeurs des paramètres nécessaires pour "
				+ nom
				+ " :</legend>"
				+
				"<c:forEach items='${params.keySet()}' var='key'>"
				+ "<div>"
				+ "<br/><br/>"
				+ "<label>"
				+ "Nom&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
				+ "Type&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
				+ "Valeur"
				+ "</label>"
				+ "<br/><br/>"
				+ "<input type='text' name='nomsParamHeu[]' id='**0' style='border:0px;font-size:14px' size='12' readonly='readonly'></input>"
				+ "<input type='text' name='typesParamHeu[]' id='***0' style='border:0px;font-size:14px' size='1' readonly='readonly'>"
				+ "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
				+ "</input>"
				+ "<input type='text' name='valeursParamHeu[]' id='##**' style='font-size:14px' size='10' ' value=''${TestMethodeParametre.valeur}'/>"
				+ "</div>" + "</c:forEach>" + "</fieldset>" + "</div>";
		var Bp2 = Bp;
		Bp++;
		document.getElementById('divParamNew').innerHTML = param;
		for (var i = 0; i < paramsHeuristiqueCut.length - 1; i++)
			Bp = addParamHeuristique2(paramsHeuristiqueCut[i + 1], i + 1, Bp);
		const
		paramNew = document.getElementById('divParamNew').innerHTML;
		document.getElementById('divParamNew').innerHTML = "";
		$('#divParamNewLarge').append(paramNew);
		document.getElementById('divParamNew2').id = "##" + id;
		document.getElementById('##**').id = "##**" + Bp2;
		for (var i = 0; i < paramsHeuristiqueCut.length; i++) {
			document.getElementById('**' + i).value = paramsHeuristiqueDouble[i][0];
			document.getElementById('***' + i).value = paramsHeuristiqueDouble[i][1];
			document.getElementById('**' + i).id = 'already';
			document.getElementById('***' + i).id = 'already2';
		}
	}
	return Bp;
}



function getSelected() {
	const
	selectedMethodes = $('#methodes').DataTable().rows({
		selected : true
	}).nodes();
	for (var i = 0; i < selectedMethodes.length; i++) {
		$('#form').append(
				"<input type='hidden' name='selectedMethode[]' value='"
						+ selectedMethodes[i].id + "'/>");
	}
	const
	selectedData = $('#datasets').DataTable().rows({
		selected : true
	}).nodes();
	for (var i = 0; i < selectedData.length; i++) {
		$('#form').append(
				"<input type='hidden' name='selectedData[]' value='"
						+ selectedData[i].id + "'/>");
	}
}

function validForm() {
	const
	selectedMethodes = $('#methodes').DataTable().rows({
		selected : true
	}).nodes();
	if (selectedMethodes.length < 2) {
		alert("Vous devez sélectionner au moins deux méthodes pour les comparer !");
		return false;
	}
	const
	selectedData = $('#datasets').DataTable().rows({
		selected : true
	}).nodes();
	if (selectedData.length < 1) {
		alert("Vous devez sélectionner au moins un dataset ou un subset !");
		return false;
	}
}