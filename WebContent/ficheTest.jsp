<%--
  User: Kai 
  Date: 27/01/2018
  Time: 14:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Fiche tests</title>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.13/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/select/1.2.1/css/select.dataTables.min.css">
    <link rel="stylesheet" href="CSS/testFiche.css">
    <script src="JS/http_code.jquery.com_jquery-1.12.0.js"></script>
    <script src="JS/http_cdn.datatables.net_1.10.11_js_dataTables.uikit.js"></script>
    <script src="JS/http_cdn.datatables.net_1.10.11_js_jquery.dataTables.js"></script>
    <script src="JS/http_cdn.datatables.net_select_1.2.1_js_dataTables.select.js"></script>
    <script src="JS/ficheTest.js"></script>
</head>
<body>
<jsp:include page="index.jsp"/>
<script type="text/javascript">
	var countFlag = 0;
	var countParamFlag = 0;
	var selectFlag = 0;
</script>
<br/><br/>
<c:choose>
    <c:when test="${not empty test}">
        <h1 id='title'>Modification du test n° <c:out value="${test.id}"/> :</h1>
    </c:when>
    <c:otherwise>
        <h1 id='title'>Ajout d'un test :</h1>
    </c:otherwise>
</c:choose>
<form action="/GraphMatching/FicheTest" method="post" id="form" onsubmit="return validForm()">
    <input type="hidden" name="test" value="<c:out value="${test.id}"/>"/>
		<div class="tableau">
			<table id="methodes" name="methodes" class="display" cellspacing="0">
				<thead>
					<tr>
						<th></th>
						<th>Méthodes</th>
						<th>Types</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${methodes}" var="methode">
						<tr
							<c:if test="${test.methodes.contains(methode)}">
                				<c:out value="class=memo "/>
                			</c:if>
							id=<c:out value="${methode.id}"/> />
						<td id=<c:out value="#td:${methode.id}"/>
							onclick='selectFlag = getParams("<c:out value="${methode.id}"/>","<c:out value="${methode.nom}"/>","<c:out value="${methode.paramsHeuristique}"/>",selectFlag,countFlag)' />
						<td><c:out value="${methode.nom}" /></td>
						<td><c:out value="${methode.type}" /></td>
						 <input id="iMethodeNom" name="iMethodeNom" value="${methode.nom}" style="border:0px;" readonly="readonly"/>
						 <input id="iMethodeParamsHeuristique" name="iMethodeParamsHeuristique" value="${methode.paramsHeuristique}" style="border:0px;" readonly="readonly"/>
						 <script type="text/javascript">
						 	countFlag++;
						 	document.getElementById('iMethodeNom').id = "#nom#" + ${methode.id};
						 	document.getElementById('iMethodeParamsHeuristique').id = "#params#" + ${methode.id};
						 </script>
					</c:forEach>
				</tbody>
			</table>
			<script type="text/javascript">
			var titleGet = document.getElementById('title').innerHTML;
			var titleCut=titleGet.substring(0,12) ;
			if (titleCut != 'Modification')
			{
				document.getElementById('buttonModification').style.display = "none";
			}
			</script>
		</div>
		<div class="tableau">
        <table id="datasets" name="Datasets" class="display" cellspacing="0">
            <thead>
            <tr>
                <th></th>
                <th>Datasets</th>
                <th>Subsets</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${datasets}" var="dataset">
                <tr
                        <c:if test="${test.datasets.contains(dataset)}">
                            <c:out value="class=memo "/>
                        </c:if>
                        id=<c:out value="${dataset.id}"/>>
                    <td></td>
                    <td><c:out value="${dataset.nom}"/></td>
                    <td></td>
                </tr>
                <c:forEach items="${dataset.getSubsets()}" var="subset">
                    <tr
                            <c:if test="${test.subsets.contains(subset)}">
                                <c:out value="class=memo "/>
                            </c:if>
                            id=<c:out value="${dataset.id}-${subset.id}"/>>
                        <td></td>
                        <td></td>
                        <td><c:out value="${subset.nom}"/></td>
                    </tr>
                </c:forEach>
            </c:forEach>
            <!-- 
            <c:forEach items="${datasets}" var="dataset">
                <c:forEach items="${dataset.getSubsets()}" var="subset">
                    <tr
                            <c:if test="${test.subsets.contains(subset)}">
                                <c:out value="class=memo "/>
                            </c:if>
                            id=<c:out value="${dataset.id}-${subset.id}"/>>
                        <td></td>
                        <td><c:out value="${dataset.nom}"/></td>
                        <td><c:out value="${subset.nom}"/></td>
                    </tr>
                </c:forEach>
            </c:forEach>
             -->
            </tbody>
        </table>
    </div>
    <div class="tableau">
        <fieldset id="test">
            <legend>Test</legend>
            <label for="nom" class="label">Nom :</label>
            <input type="text" required id="nom" name="nom" value="<c:out value="${test.nom}"/>">
            <br/><br/>
            <label for="description" class="label">Description :</label>
            <textarea type="text" id="description" name="description"><c:out value="${test.description}"/></textarea>
            <br/><br/>
            <label for="mode" class="label">Mode d'exécution :</label>
            <select id="mode" name="mode">
                <option <c:if test="${test.mode == 1}">
                            <c:out value="selected "/>
                        </c:if>value="1">IP
                </option>
                <option
                        <c:if test="${test.mode == 2}">
                            <c:out value="selected "/>
                        </c:if>value="2">LP
                </option>
                <option
                        <c:if test="${test.mode == 3}">
                            <c:out value="selected "/>
                        </c:if>value="3">IP pré-processing
                </option>
                <option
                        <c:if test="${test.mode == 4}">
                            <c:out value="selected "/>
                        </c:if>value="4">Heuristique
                </option>
            </select>
        </fieldset>
    </div>
    <br/><br/>
    <c:set var="params" value="${test.getParametres()}"/>
    <div class="tableau">
        <fieldset>
            <legend>Paramètres :</legend>
            <label for="memoire" class="labelParam">Mémoire limite (en Mo) :</label>
            <input type="text" id="memoire" name="memoire" value="<c:out value="${params['memoire']}"/>">
            <br/><br/>
            <label for="temps" class="labelParam">Temps limite (en sec):</label>
            <input type="text" id="temps" name="temps" value="<c:out value="${params['temps']}"/>">
            <br/><br/>
            <label for="tempsHeur" class="labelParam">Temps limite heuristique (en sec):</label>
            <input type="text" id="tempsHeur" name="tempsHeur" value="<c:out value="${params['tempsHeur']}"/>">
            <br/><br/>
            <label for="tolerence" class="labelParam">Tolérence d'écart :</label>
            <input type="text" id="tolerence" name="tolerence" value="<c:out value="${params['tolerence']}"/>">
            <br/><br/>
            <label for="thread" class="labelParam">Nombre de threads :</label>
            <input type="text" id="thread" name="thread" value="<c:out value="${params['thread']}"/>">
            <br/><br/>
        </fieldset>
    </div>
    <div id="divParam">
        <fieldset id="paramSup">
            <legend>Paramètres supplémentaires :</legend>
            <input type="button" value="+ Ajouter" onclick="addParam()">
            <c:forEach items="${params.keySet()}" var="key">
                <c:if test="${key != 'memoire' && key != 'temps' && key != 'tempsHeur' && key != 'tolerence' && key != 'thread'}">
                    <div>
                        <br/><br/>
                        <label for="nomsParam[]">Nom : </label>
                        <input type="text" name="nomsParam[]" id="nomsParam[]" value="<c:out value="${key}"/>"/>
                        <label for="valeursParam[]">Valeur : </label>
                        <input type="text" name="valeursParam[]" id="valeursParam[]" value="<c:out value="${params[key]}"/>"/>
                        <input type='button' value='Supprimer' class="deleteParam"/>
                    </div>
                </c:if>
            </c:forEach>
        </fieldset>
    </div>
    <br/><br/>
    <div id="gettestmethodeparametres">
  		<c:forEach items="${testmethodeparametres}" var="testmethodeparametre">
  			<c:set var="testmethodeparametre1" value="${testmethodeparametre.testMethode.test.id}"/>
  			<c:set var="testmethodeparametre2" value="${test.id}"/>
			 <c:if test='${testmethodeparametre1 == testmethodeparametre2}'>
			 <%--
			 	<c:out value="${testmethodeparametre.parametre.methode.id}" />
				<c:out value="${testmethodeparametre.parametre.nom}" />
				<c:out value="${testmethodeparametre.parametre.type}" />
				<c:out value="${testmethodeparametre.valeur}" />
			 --%>
			 <input id="testMethodeParametreValeur" name="testMethodeParametreValeur" value="${testmethodeparametre.valeur}" style="border:0px;" readonly="readonly"/>
				<br /><br />
				<script type="text/javascript">
					document.getElementById('testMethodeParametreValeur').id = "**##" + countParamFlag;
					countParamFlag++;
				</script>
			</c:if>	
		</c:forEach>
	</div>
    <div id="divParamNewLarge">
    	<div id="divParamNew">
    	</div>
    </div>
    <div id="boutons">
        <input type="submit" value="Enregistrer" name="enregistrer" id="enregistrer" onclick="getSelected()"/>
        <c:if test="${not empty test}">
            <form action="/GraphMatching/MasquerTest" method="post">
                <input type="hidden" name="id" value="<c:out value="${test.id}"/>"/>
                <input type="submit" id="masquer" value="Masquer ce test">
            </form>
        </c:if>
        <form action="/GraphMatching/FicheTest" type="GET">
            <input type="submit" id="ajout" value="+ Ajouter un test">
        </form>
        <form action="/GraphMatching/ListeTest" method="get">
            <input type="submit" id="retour" value="Liste des tests">
        </form>
    </div>
</form>
<script type="text/javascript">
	var iMethodeNom = [];
	var iMethodeParamsHeuristique = [];
	var j = 0;
	var Bp = 0;
	for (var i=1;i<=countFlag;i++)
	{	
		var checkboxSelectNew = document.getElementById(i).className;
		if( checkboxSelectNew == 'memo' )
			{	
					
				iMethodeNom[j] = document.getElementById('#nom#' + i).value;
				iMethodeParamsHeuristique[j] = document.getElementById('#params#' + i).value;
				Bp = getParams2(i,iMethodeNom[j],iMethodeParamsHeuristique[j],Bp);
				if (iMethodeParamsHeuristique[j].length > 2 )//Comparaison de différents types de méthodes(不同类型方法比较操作)
					selectFlag = 2;//heuristique
				else
					selectFlag = 1;//exacte
				j++;
			}
		document.getElementById('#nom#' + i).style.display = "none";
		document.getElementById('#params#' + i).style.display = "none";
		document.getElementById('#nom#' + i).id = "delete2";
		document.getElementById('#params#' + i).id = "delete3";
	}
	var testMethodeParametreValeur = [];
	for (i=0;i<countParamFlag;i++)
		{
			testMethodeParametreValeur[i] = document.getElementById('**##' + i).value;
			//alert(testMethodeParametreValeur[i]);//Pour voir le processus de mettre en chiffres
			document.getElementById('##**' + i).value = testMethodeParametreValeur[i];
		}
	document.getElementById('gettestmethodeparametres').style.display = "none";
	document.getElementById('gettestmethodeparametres').id = "delete4";
</script>
</body>
</html>