<%--
  Created by IntelliJ IDEA.
  User: kevmi
  Date: 05/01/2017
  Time: 16:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Liste des méthodes</title>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.13/css/jquery.dataTables.min.css">
    <script src="JS/http_code.jquery.com_jquery-1.12.0.js"></script>
    <script src="JS/http_cdn.datatables.net_1.10.11_js_jquery.dataTables.js"></script>
    <script src="JS/http_cdn.datatables.net_1.10.11_js_dataTables.uikit.js"></script>
    <script src="JS/listeMethodes.js"></script>
</head>
<body>
<jsp:include page="index.jsp"/>
<h1>Liste des méthodes</h1>

<div class="uk-overflow-container" width="80%">
    <fieldset>
        <legend>Rechercher :</legend>
        <label for="searchbox">Par nom / description : </label>
        <input type="text" id="searchbox">
    </fieldset>
    <br/>
    <table id="methodes" class="display" cellspacing="0">
        <thead>
        <tr>
            <th>Nom</th>
            <th>Description</th>
            <th>Chemin exécutable</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${methodes}" var="methode">
        <tr id=
                <c:out value="${methode.id}"/> ondblclick='modif(<c:out value="${methode.id}"/>)'>
            <td><c:out value="${methode.nom}"/></td>
            <td><c:out value="${methode.description}"/></td>
            <td><c:out value="${methode.executable}"/></td>
        </tr>
        </c:forEach>
        <tbody>
    </table>

    <form action="/GraphMatching/FicheMethodeExacte" type="GET">
        <input id="ajout" type="submit" value="+ ajouter une méthode exacte">
    </form>
    
    <form action="/GraphMatching/FicheMethodeHeuristique" type="GET">
        <input id="ajout" type="submit" value="+ ajouter une méthode heuristique">
    </form>
</div>
</body>
</html>
