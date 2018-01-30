/**
 * Created by kevmi on 26/01/2017.
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

function addParam2() {
	const
	param = "<div></br></br>"
			+ "<label for='nomsParam[]'> Nom : </label>"
			+ "<input type='text' name='nomsParam[]' id='nomsParam[]'/> "
			+ "<input class='deleteParam' type='button' value='Supprimer'/></div>";
	$('#paramSup2').append(param);
}

/*
function addParamHeuristique(nomtype) {
	const paramHeuristique = "<div></br></br>"
			+ "<label for='nom' class='label'>"
			+ nomtype
			+ " :</label>"
			+ "<input type='text' name='valeur' id='valeur' required='required'/> "
	$('#paramNecH').append(paramHeuristique);
}*/

function addParamHeuristique(nomtype,i) {
	const paramHeuristique = "<div></br></br>" + 
			"<input type='text' name='nomsParamHeu[]' id='nomsParamHeu[]' style='border:0px;font-size:14px' size='12' readonly='readonly'>" + " : " + "</input>" +
			"<input type='text' name='valeursParamHeu[]' id='valeursParamHeu[]' required='required'/> ";
	$('#paramNecH').append(paramHeuristique);
	document.getElementById('nomsParamHeu[]').id = "**" + i;
}


function getParams(id, nom, paramsHeuristique) {
	//alert('start');
	var newParamsHeuristique = paramsHeuristique.substring(1,paramsHeuristique.length - 1);
	paramsHeuristiqueCut = newParamsHeuristique.split(",");
	var checkboxSelect = document.getElementById(id).className;
	var list = document.getElementsByTagName("input");
	/*
	for (var i = 0; i < list.length; i++) {
		if (list[i].type == "text" && list[i].value == "" && list[i].name == "valeur" && checkboxSelect.indexOf("selected") <= -1 && paramsHeuristique.length > 2) 
		{
				alert("Entrée non validée, essayer encore une fois, s'il vous plaît !");
				return false;
		}
		if (list[i].type == "text" && list[i].value == "" && list[i].name == "valeur" && checkboxSelect.indexOf("selected") > -1 && paramsHeuristique.length > 2) 
		{
				return false;
		}
	}*/
	//for (var i = 0; i < paramsHeuristiqueCut.length; i++) {
		//alert(paramsHeuristiqueCut[i]);
	//}		
	var divid = "##" + id;
	if (checkboxSelect.indexOf("selected") > -1) 
	{
		if (paramsHeuristique.length > 2)
		{
			document.getElementById(divid).style.display = "none";
			document.getElementById(divid).id = "delete";
		}
	} 
	else
	{
		if (paramsHeuristique.length > 2) {
			//alert(paramsHeuristiqueCut.length);
			document.getElementById('divParamNew').style.display = "";
			const param =
			"<div id='divParamNew2'>"+
			    "<fieldset id='paramNecH'>"+
			    "<legend id='divParamNew3'>Veuillez entrer les valeurs de paramètre nécessaires pour " + nom + " :</legend>" +
			    //"<input type='button' value='Valider' onclick='upParamHeuristique()'>"+//bouton de Valider
			    	"<c:forEach items='${params.keySet()}' var='key'>"+             
			            "<div>"+
			               "<br/><br/>"+  
			               "<input type='text' name='nomsParamHeu[]' id='**0' style='border:0px;font-size:14px' size='12' readonly='readonly'>" + " : " + "</input>" +
			               //"<label for='nom' class='label'>" +  paramsHeuristiqueCut[0] + " : " + "</label>" +
			               "<input type='text' name='valeursParamHeu[]' id='valeursParamHeu[]' required='required' value=''${key}'/>"+    
			             "</div>"+
			        "</c:forEach>"+		        
			   "</fieldset>"+
			"</div>";
			//$('#divParamNew').append(param);	
			document.getElementById('divParamNew').innerHTML = param;
			for (var i = 0; i < paramsHeuristiqueCut.length - 1; i++)
				addParamHeuristique(paramsHeuristiqueCut[i + 1],i+1);		
			const paramNew = document.getElementById('divParamNew').innerHTML;
			document.getElementById('divParamNew').innerHTML = "";
			$('#divParamNewLarge').append(paramNew);
			//$('#divParamNew').append(paramNew);
			//document.getElementById('divParamNew').innerHTML = paramNew;
			document.getElementById('divParamNew2').id = "##" + id;
			for (var i = 0; i < paramsHeuristiqueCut.length ; i++)
			{
				document.getElementById('**'+i).value = paramsHeuristiqueCut[i];
				document.getElementById('**'+i).id = 'already';
			}

		} else {
			//说明：打开下一行代码（根据需求自行调整），获得功能：在选择一个带参数的方法后，再选择一个不带参数的方法，上一个方法的参数输入框消失
			//document.getElementById('divParamNew').style.display = "none";
		}
	}
	return 2;
}

$(document).on("click", ".deleteParam", function() {
	$(this).closest("div").remove();
});

function getParams2(id,nom,paramsHeuristique) {
	//alert('start');
	var newParamsHeuristique = paramsHeuristique.substring(1,paramsHeuristique.length - 1);
	paramsHeuristiqueCut = newParamsHeuristique.split(",");
	if (paramsHeuristique.length > 2) {
		//alert(paramsHeuristiqueCut.length);
		document.getElementById('divParamNew').style.display = "";
		const param =
		"<div id='divParamNew2'>"+
		    "<fieldset id='paramNecH'>"+
		    "<legend id='divParamNew3'>Veuillez entrer les valeurs de paramètre nécessaires pour " + nom + " :</legend>" +
		    //"<input type='button' value='Valider' onclick='upParamHeuristique()'>"+//bouton de Valider
		    	"<c:forEach items='${params.keySet()}' var='key'>"+             
		            "<div>"+
		               "<br/><br/>"+  
		               "<input type='text' name='nomsParamHeu[]' id='**0' style='border:0px;font-size:14px' size='12' readonly='readonly'>" + " : " + "</input>" +
		               //"<label for='nom' class='label'>" +  paramsHeuristiqueCut[0] + " : " + "</label>" +
		               "<input type='text' name='valeursParamHeu[]' id='valeursParamHeu[]' required='required' value=''${key}'/>"+    
		             "</div>"+
		        "</c:forEach>"+		        
		   "</fieldset>"+
		"</div>";
		//$('#divParamNew').append(param);	
		document.getElementById('divParamNew').innerHTML = param;
		for (var i = 0; i < paramsHeuristiqueCut.length - 1; i++)
			addParamHeuristique(paramsHeuristiqueCut[i + 1],i+1);		
		const paramNew = document.getElementById('divParamNew').innerHTML;
		document.getElementById('divParamNew').innerHTML = "";
		$('#divParamNewLarge').append(paramNew);
		//$('#divParamNew').append(paramNew);
		//document.getElementById('divParamNew').innerHTML = paramNew;
		document.getElementById('divParamNew2').id = "##" + id;
		for (var i = 0; i < paramsHeuristiqueCut.length ; i++)
		{
			document.getElementById('**'+i).value = paramsHeuristiqueCut[i];
			document.getElementById('**'+i).id = 'already';
		}

	} 
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

function getSelected2(buttonFlag) {
	if(buttonFlag == 1)
	{
		alert("Vous avez obtenu les paramètres historiques de cette méthode et vous n'avez pas besoin de la récupérer à plusieurs reprises.");	
		return buttonFlag;
	}
	if(buttonFlag == 2)
	{
		alert("Vous avez changé la méthode sous ce test.");	
		return buttonFlag;
	}
	const
	selectedMethodes = $('#methodes').DataTable().rows({
		selected : true
	}).nodes();
	for (var i = 0; i < selectedMethodes.length; i++) {
		var str = selectedMethodes[i].innerHTML;
		//alert(str);
		var x=str.indexOf('getParams(');
		var y=str.indexOf('class=');
		var str1=str.substring(x+10,y-4) 
		strCut = str1.split(";");
		var x1=strCut[1].indexOf('&quot');
		var strCut1=strCut[1].substring(0,x1);
		//alert(strCut1);
		var y1=strCut[3].indexOf('&quot');
		var strCut3=strCut[3].substring(0,y1);
		//alert(strCut3);
		var titleGet = document.getElementById('title').innerHTML;
		var titleCut=titleGet.substring(0,12) ;
		if(titleCut == 'Modification')
		{
			getParams2(selectedMethodes[i].id,strCut1,strCut3);
		}    
		
	}
	buttonFlag = 1 ;
	return buttonFlag;
}

function validForm() {
	const
	selectedMethodes = $('#methodes').DataTable().rows({
		selected : true
	}).nodes();
	if (selectedMethodes.length < 2) {
		alert("Vous devez sélectionner au moins deux modèles pour les comparer !");
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
