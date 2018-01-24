<%--
  Created by IntelliJ IDEA.
  User: kevmi
  Date: 18/01/2017
  Time: 15:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="CSS/css.css"/>
    <title>Fiche collection de graphes</title>
</head>
<body>
<jsp:include page="index.jsp"/>
<c:choose>
    <c:when test="${not empty dataset}">
        <h1>Modification de la collection de graphes n° <c:out value="${dataset.id}"/> :</h1>
    </c:when>
    <c:otherwise>
        <h1>Ajout d'une collection de graphes :</h1>
    </c:otherwise>
</c:choose>
<form action="/GraphMatching/FicheDataset" method="post" enctype="multipart/form-data">
    <input type="hidden" name="dataset" value="<c:out value="${dataset.id}"/>"/>
    <label for="nom" class="label">Nom : </label>
    <input type="text" required id="nom" name="nom" value="<c:out value="${dataset.nom}"/>"/>
    <br/><br/>
    <label for="fileDatasetPath" class="label">Dataset : </label>
    <label id="fileDatasetPath"><c:out value="${dataset.dataset}"/></label>
    <br/><br/>
    <c:choose>
        <c:when test="${not empty dataset}">
            <input type="file" name="fileDataset" id="fileDataset" accept=".zip"/>
        </c:when>
        <c:otherwise>
            <input type="file" required name="fileDataset" id="fileDataset" accept=".zip"/>
        </c:otherwise>
    </c:choose>

    <br/><br/>
    <label for="fileSubsetPath" class="label">Subset : </label>
    <label id="fileSubsetPath"><c:out value="${dataset.subset}"/></label>
    <br/><br/>
    <input type="file" name="fileSubset" id="fileSubset" accept=".zip"/>
    <br/><br/>
    <label for="description" class="label">Description : </label>
    <textarea id="description" name="description"><c:out value="${dataset.description}"/></textarea>
    <div id="boutons">
        <input id="enregistrer" type="submit" value="Enregistrer">
        <c:if test="${not empty dataset}">
            <form action="/GraphMatching/MasquerDataset" method="post">
                <input type="hidden" name="id" value="<c:out value="${dataset.id}"/>"/>
                <input type="submit" id="masquer" value="Masquer cette collection">
            </form>
            <br/>
        </c:if>

        <form action="/GraphMatching/ListeDataset" method="get">
            <input type="submit" id="retour" value="Liste des collections">
        </form>
        <br/>
        <form action="/GraphMatching/FicheDataset" type="GET">
            <input id="ajout" type="submit" value="+ ajouter une collection">
        </form>
    </div>
</form>
</body>
</html>

