<%--
  Created by IntelliJ IDEA.
  User: kevmi
  Date: 01/02/2017
  Time: 14:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Liste des tests</title>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.13/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="CSS/css.css">
    <script src="JS/http_code.jquery.com_jquery-1.12.0.js"></script>
    <script src="JS/http_cdn.datatables.net_1.10.11_js_dataTables.uikit.js"></script>
    <script src="JS/http_cdn.datatables.net_1.10.11_js_jquery.dataTables.js"></script>
    <script src="JS/listeTest.js"></script>
</head>
<body>
<jsp:include page="index.jsp"/>
<br/><br/>
<h1>Liste des tests</h1>
<div class="uk-overflow-container" width="80%">
    <fieldset>
        <legend>Rechercher :</legend>
        <label for="searchbox">Par nom / description : </label>
        <input type="text" id="searchbox">
        <label for="selectboxMethode">Par méthode</label>
        <select id="selectboxMethode">
            <option value=""></option>
            <c:forEach items="${methodes}" var="methode">
                <option value="<c:out value="${methode.nom}"/>"/>
                <c:out value="${methode.nom}"/></option>
            </c:forEach>
        </select>
        <label for="selectboxDataset">Par dataset</label>
        <select id="selectboxDataset">
            <option value=""></option>
            <c:forEach items="${datasets}" var="dataset">
                <option value="<c:out value="${dataset.nom}"/>"/>
                <c:out value="${dataset.nom}"/></option>
            </c:forEach>
        </select>
    </fieldset>
    <br/>
    <table id="tests" class="display" cellspacing="0">
        <thead>
        <tr>
            <th>Nom</th>
            <th>Description</th>
            <th>Mode</th>
            <th>Nb executions</th>
            <th>Méthode</th>
            <th>Datasets</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${tests}" var="test">
            <tr id=
                    <c:out value="${test.id}"/> ondblclick='modif(<c:out value="${test.id}"/>)'
                onclick='getExec(<c:out value="${test.id}"/>,"<c:out value="${test.nom}"/>")'/>
            <td><c:out value="${test.nom}"/></td>
            <td><c:out value="${test.description}"/></td>
            <td><c:out value="${test.printMode()}"/></td>
            <td><c:out value="${test.getNbExecutions()}"/></td>
            <td><c:out value="${test.printMethodes()}"/></td>
            <td><c:out value="${test.printData()}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div id="exec"></div>
    <form action="/GraphMatching/FicheTest" type="GET">
        <input id="ajout" type="submit" value="+ ajouter un test">
    </form>
</div>
</body>
</html>
