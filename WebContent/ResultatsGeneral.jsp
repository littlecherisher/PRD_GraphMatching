<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<ul>
    <c:if test="${description != ''}">
        <li>Description : <c:out value="${description}"/></li></br>
    </c:if>

    <li>Mode : <c:out value="${mode}"/></li></br>


    <li><c:out value="${nbInstancesExec}"/> / <c:out value="${nbInstances}"/> instances</li></br>

    <li>Méthodes : <c:out value="${methodes}"/></li></br>

    <li>Datasets : <c:out value="${datasets}"/></li></br>

    <li>Paramètres :
        <ul>
        <c:forEach items="${parametres.keySet()}" var="key">
            <li><c:out value="${key}"/> : <c:out value="${parametres[key]}"/> </li>
        </c:forEach>
        <c:forEach items="${parametresH.keySet()}" var="key">
            <li><c:out value="${key}"/> : <c:out value="${parametresH[key]}"/> </li>
        </c:forEach>
        </ul>
    </li>
</ul>

