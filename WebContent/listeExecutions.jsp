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
    <title>Liste des exécutions</title>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.13/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="CSS/testFiche.css">
    <script src="JS/http_code.jquery.com_jquery-1.12.0.js"></script>
    <script src="JS/http_cdn.datatables.net_1.10.11_js_dataTables.uikit.js"></script>
    <script src="JS/http_cdn.datatables.net_1.10.11_js_jquery.dataTables.js"></script>
    <script src="JS/listeExecutions.js"></script>
</head>
<body>
<jsp:include page="index.jsp"/>
<br/><br/>
<h1>Liste des exécutions</h1>
<div class="uk-overflow-container" width="80%">
    <fieldset>
        <legend>Rechercher :</legend>
        <label for="searchbox">Par nom / description : </label>
        <input type="text" id="searchbox">
        <label for="selectboxTest">Par test : </label>
        <select id="selectboxTest">
            <option value=""></option>
            <c:forEach items="${tests}" var="test">
                <option value="<c:out value="${test.nom}"/>"/><c:out value="${test.nom}"/></option>
            </c:forEach>
        </select>
        <label for="selectboxEtat">Par état : </label>
        <select id="selectboxEtat">
            <option value=""></option>
            <option value="En attente">En attente</option>
            <option value="Erreur">Erreur</option>
            <option value="Terminé">Terminé</option>
            <option value="En cours">En cours</option>
        </select>
    </fieldset>
    <br/>
    <table id="executions" class="display" cellspacing="0">
        <thead>
        <tr>
            <th>Nom</th>
            <th>Description</th>
            <th>Test</th>
            <th>Nb instances</th>
            <th>Etat</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${executions}" var="execution">
        <tr id=<c:out value="${execution.id}"/> onclick='info(<c:out value="${execution.id}"/>)'>
            <td><c:out value="${execution.nom}"/></td>
            <td><c:out value="${execution.description}"/></td>
            <td><c:out value="${execution.test.nom}"/></td>
            <td><c:out value="${execution.getNbInstancesExec()}/${execution.nbInstances}"/></td>
            <td><c:out value="${execution.etat}"/></td>
        </tr>
        </c:forEach>
        <tbody>
    </table>
</div>
<div id="infos"></div>
<form action="/GraphMatching/ListeTest" type="GET">
    <input id="ajout" type="submit" value="+ ajouter une exécution">
</form>
</body>
</html>
