<%--
  User: Kai
  Date: 11/12/2017
  Time: 10:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Fiche methode heuristique</title>
    <link rel="stylesheet" type="text/css"  href="CSS/css.css"/>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.13/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="CSS/css.css">
    <script src="JS/http_code.jquery.com_jquery-1.12.0.js"></script>
    <script src="JS/http_cdn.datatables.net_1.10.11_js_dataTables.uikit.js"></script>
    <script src="JS/http_cdn.datatables.net_1.10.11_js_jquery.dataTables.js"></script>
    <script src="JS/ficheMethodeHeuristique.js"></script>
</head>
<body>
<jsp:include page="index.jsp"/>
<br/><br/>
<c:choose>
    <c:when test="${not empty methode}">
        <h1>Modification de la méthode n° <c:out value="${methode.id}"/> :</h1>
    </c:when>
    <c:otherwise>
        <h1>Ajout d'une méthode heuristique :</h1>
    </c:otherwise>
</c:choose>
<form action="/GraphMatching/FicheMethodeHeuristique" method="post" enctype="multipart/form-data">
    <input type="hidden" name="methode" value="<c:out value="${methode.id}"/>"/>
    <label for="nom" class="label">Nom : </label>
    <input type="text" required id="nom" name="nom" value="<c:out value="${methode.nom}"/>"/>
    <br/><br/>
    <label for="filePath" class="label">Executable : </label>
    <label id="filePath"><c:out value="${methode.executable}"/></label>
    <br/><br/>
    <c:choose>
        <c:when test="${not empty methode}">
            <input type="file" name="file" id="file" accept=".exe"/>
        </c:when>
        <c:otherwise>
            <input type="file" required name="file" id="file" accept=".exe"/>
        </c:otherwise>
    </c:choose>
    <br/><br/>
    <label for="description" class="label">Description : </label>
    <textarea id="description" name="description"><c:out value="${methode.description}"/></textarea>
    <br/><br/>
    <div id="divParam">
        <fieldset id="paramNec">
            <legend>Paramètres nécessaires :</legend>
            <input type="button" value="+ Ajouter" onclick="addParamHeuristique()">
            <c:forEach items="${params.keySet()}" var="key">
                    <div>
                        <label for="nomsParam[]">Nom : </label>
                        <input type="text" name="nomsParam[]" id="nomsParam[]" value="<c:out value="${key}"/>"/>
                        <label for="typesParam[]">Type : </label>
                        <input type="text" name="typesParam[]" id="typesParam[]" value="<c:out value="${params[key]}"/>"/>
						</select> <input type='button' value='Supprimer' class="deleteParam"/>
                    </div>
            </c:forEach>
        </fieldset>
    </div>
    <div id="boutons">
        <input id="enregistrer" type="submit" value="Enregistrer">
        <c:if test="${not empty methode}">
            <form action="/GraphMatching/MasquerMethode" method="post">
                <input type="hidden" name="id" value="<c:out value="${methode.id}"/>"/>
                <input type="submit" id="masquer" value="Masquer ce méthode">
            </form>
            <br/>
        </c:if>
        <form action="/GraphMatching/ListeMethode" method="get">
            <input type="submit" id="retour" value="Liste des méthodes">
        </form>
        <br/>
        <form action="/GraphMatching/FicheMethodeHeuristique" type="GET">
            <input id="ajout" type="submit" value="+ ajouter une méthode heuristique">
        </form>
    </div>
</form>
</body>
</html>
