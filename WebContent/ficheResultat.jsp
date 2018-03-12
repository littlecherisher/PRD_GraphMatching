<%--
  Created by IntelliJ IDEA.
  User: kevmi
  Date: 11/01/2017
  Time: 10:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="CSS/css.css"/>
    <link type="text/css" rel="stylesheet" href="CSS/onglet.css"/>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <title>Fiche résultats</title>
</head>
<body>
<jsp:include page="index.jsp"/>
<br/><br/>
<h1>Résultats de l'exécution <c:out value="${execution.nom}"/> :</h1>
    <div class="onglets">
        <div class="<%=getOngletActif("general",(String)request.getAttribute("infos"))%> onglet">
            <a href="/GraphMatching/FicheResultat?id=<c:out value="${execution.id}"/>&info=general">Général</a></div>
        <div class="<%=getOngletActif("CPUTime",(String)request.getAttribute("infos"))%> onglet">
            <a href="/GraphMatching/FicheResultat?id=<c:out value="${execution.id}"/>&info=CPUTime">Temps CPU</a></div>
        <c:if test='${execution.paramsH == "[]"}'>
        <div class="<%=getOngletActif("nbExploredNodes",(String)request.getAttribute("infos"))%> onglet">
            <a href="/GraphMatching/FicheResultat?id=<c:out value="${execution.id}"/>&info=nbExploredNodes">Noeuds explorés</a></div>
        </c:if>
        <c:if test='${execution.paramsH == "[]"}'>
        <div class="<%=getOngletActif("SolutionState",(String)request.getAttribute("infos"))%> onglet">
            <a href="/GraphMatching/FicheResultat?id=<c:out value="${execution.id}"/>&info=SolutionState">Solutions optimales</a></div>
        </c:if>
        <div class="<%=getOngletActif("count",(String)request.getAttribute("infos"))%> onglet">
            <a href="/GraphMatching/FicheResultat?id=<c:out value="${execution.id}"/>&info=count">Instances traitées</a></div>
        <div class="<%=getOngletActif("ObjVal",(String)request.getAttribute("infos"))%> onglet">
            <a href="/GraphMatching/FicheResultat?id=<c:out value="${execution.id}"/>&info=ObjVal">Déviation</a></div>
        <div class="<%=getOngletActif("appareillement",(String)request.getAttribute("infos"))%> onglet">
            <a href="/GraphMatching/FicheResultat?id=<c:out value="${execution.id}"/>&info=appareillement">Appareillements</a></div>
        <c:if test="${execution.mode.equals(3)}">
            <div class="<%=getOngletActif("CPUTimePrepro",(String)request.getAttribute("infos"))%> onglet">
                <a href="/GraphMatching/FicheResultat?id=<c:out value="${execution.id}"/>&info=CPUTimePrepro">Temps CPU pré-processing</a></div>
            <div class="<%=getOngletActif("PerFixedVars",(String)request.getAttribute("infos"))%> onglet">
                <a href=/GraphMatching/FicheResultat?id=<c:out value="${execution.id}"/>&info=PerFixedVars">pré-processing</a></div>
            <div class="<%=getOngletActif("UB/LB",(String)request.getAttribute("infos"))%> onglet">
                <a href="/GraphMatching/FicheResultat?id=<c:out value="${execution.id}"/>&info=UB/LB">Écart bornes</a></div>
        </c:if>
    </div>
    <div class="contenu">
        <c:choose>
            <c:when test="${infos.equals('SolutionState') || infos.equals('count')}">
                <jsp:include page="ResultatsCount.jsp"/>
            </c:when>
            <c:when test="${infos.equals('general')}">
                <jsp:include page="ResultatsGeneral.jsp"/>
            </c:when>
            <c:when test="${infos.equals('appareillement')}">
                <jsp:include page="ResultatsAppareillement.jsp"/>
            </c:when>
            <c:otherwise>
                <jsp:include page="ResultatsMinAvgMax.jsp"/>
            </c:otherwise>
        </c:choose>
    </div>
    <br/>
    <div id="boutons">
        <form action="/GraphMatching/ListeExecution" method="get">
            <input type="submit" id="retour" value="Liste des exécutions"/>
        </form>
    </div>
</body>
</html>

<%!
    String getOngletActif(String onglet, String info){
        String res = "onglet_n";
        if(onglet.equals(info)) res = "onglet_y";
        return res;
    }
%>
