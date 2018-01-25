<%--
  Created by IntelliJ IDEA.
  User: kevmi
  Date: 11/01/2017
  Time: 10:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="CSS/css.css"/>
    <title>Fiche methode exacte</title>
</head>
<body>
<jsp:include page="index.jsp"/>
<br/><br/>
<c:choose>
    <c:when test="${not empty methode}">
        <h1>Modification de la méthode n° <c:out value="${methode.id}"/> :</h1>
    </c:when>
    <c:otherwise>
        <h1>Ajout d'une méthode exacte :</h1>
    </c:otherwise>
</c:choose>
<form action="/GraphMatching/FicheMethodeExacte" method="post" enctype="multipart/form-data">
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
    <div id="boutons">
        <input id="enregistrer" type="submit" value="Enregistrer">
        <c:if test="${not empty methode}">
            <form action="/GraphMatching/MasquerMethode" method="post">
                <input type="hidden" name="id" value="<c:out value="${methode.id}"/>"/>
                <input type="submit" id="masquer" value="Masquer cette méthode">
            </form>
            <br/>
        </c:if>

        <form action="/GraphMatching/ListeMethode" method="get">
            <input type="submit" id="retour" value="Liste des méthodes">
        </form>
        <br/>
        <form action="/GraphMatching/FicheMethodeExacte" type="GET">
            <input id="ajout" type="submit" value="+ Ajouter une méthode execte">
        </form>
    </div>
</form>
</body>
</html>
