<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="JS/ResultatsAppareillement.js"></script>
<script src="JS/http_code.jquery.com_jquery-1.12.0.js"></script>
<script src="JS/http_cdn.datatables.net_1.10.11_js_dataTables.uikit.js"></script>
<script src="JS/http_cdn.datatables.net_1.10.11_js_jquery.dataTables.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.13/css/jquery.dataTables.min.css">
<link rel="stylesheet" href="CSS/appareillement.css">
<fieldset>
    <legend>Sélection de la méthode et des graphes</legend>
    <label for="methode">Méthode : </label>
    <select id="methode">
        <c:forEach items="${methodes}" var="methode">
            <option value="<c:out value="${methode.id}"/>"><c:out value="${methode.nom}"/></option>
        </c:forEach>
    </select>
    <label for="G1">Graphe 1 : </label>
    <select id="G1">
        <c:forEach items="${listePaires[0]}" var="graphe">
            <option value="<c:out value="${graphe}"/>"><c:out value="${graphe}"/></option>
        </c:forEach>
    </select>
    <label for="G2">Graphe 2 : </label>
    <select id="G2">
        <c:forEach items="${listePaires[1]}" var="graphe">
            <option value="<c:out value="${graphe}"/>"><c:out value="${graphe}"/></option>
        </c:forEach>
    </select>
    <input type="button" value="Actualiser" onclick="info(<c:out value="${execution.id}"/>)">
</fieldset>
<div id="resultat">

</div>