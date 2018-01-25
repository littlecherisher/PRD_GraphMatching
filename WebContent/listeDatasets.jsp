<%--
  Created by IntelliJ IDEA.
  User: kevmi
  Date: 18/01/2017
  Time: 12:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Liste des collections de graphes</title>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.13/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="CSS/testFiche.css">
    <script src="JS/http_code.jquery.com_jquery-1.12.0.js"></script>
    <script src="JS/http_cdn.datatables.net_1.10.11_js_dataTables.uikit.js"></script>
    <script src="JS/http_cdn.datatables.net_1.10.11_js_jquery.dataTables.js"></script>
    <script src="JS/listeDataset.js"></script>
</head>
<body>
<jsp:include page="index.jsp"/>
<br/><br/>
<h1>Liste des collections de graphes</h1>
<div class="uk-overflow-container" width="80%">
    <fieldset>
        <legend>Rechercher :</legend>
        <label for="searchbox">Par nom / description : </label>
        <input type="text" id="searchbox">
        <label for="selectbox">Par type de graphe</label>
        <select id="selectbox">
            <option value=""></option>
            <option value="Dirigé">Dirigé</option>
            <option value="Non dirigé">Non dirigé</option>
        </select>
    </fieldset>
    <br/>
    <table id="datasets" class="display" cellspacing="0">
        <thead>
        <tr>
            <th>Nom</th>
            <th>Description</th>
            <th>Dirigé</th>
            <th>Nb graphes</th>
            <th>Dataset</th>
            <th>Subset</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${datasets}" var="dataset">
        <tr id=
                <c:out value="${dataset.id}"/> ondblclick='modif(<c:out value="${dataset.id}"/>)'>
            <td><c:out value="${dataset.nom}"/></td>
            <td><c:out value="${dataset.description}"/></td>
            <c:choose>
                <c:when test="${dataset.dirige}">
                    <td><c:out value="Dirigé"/></td>
                </c:when>
                <c:otherwise>
                    <td><c:out value="Non dirigé"/></td>
                </c:otherwise>
            </c:choose>
            <td><c:out value="${dataset.nbGraphes}"/></td>
            <td><c:out value="${dataset.dataset}"/></td>
            <td><c:out value="${dataset.subset}"/></td>
        </tr>
        </c:forEach>
        <tbody>
    </table>
</div>
<form action="/GraphMatching/FicheDataset" type="GET">
    <input id="ajout" type="submit" value="+ Ajouter une collection">
</form>
</body>
</html>
