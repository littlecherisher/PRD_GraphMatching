<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- parcours datasets -->
<c:forEach items="${resultats}" var="tempsCPU">
    <script>
        google.charts.load('current', {'packages':['table','corechart']});
        google.charts.setOnLoadCallback(drawChart);
        function drawChart() {
            const data = new google.visualization.DataTable();
            data.addColumn('string', 'Méthode');
            data.addColumn('number', 'Minimum');
            data.addColumn('number', 'Moyenne');
            data.addColumn('number', 'Maximum');
            <c:set var="ligne" scope="session" value="${0}"/>
            <!-- parcours modèles -->
            <c:forEach items="${tempsCPU.value}" var="tempsCPUmethode">
                data.addRow();
                <c:forEach items="${execution.methodes}" var="methode">
                    <c:if test="${methode.id == tempsCPUmethode.key}">
                        data.setCell(<c:out value="${ligne}"/> , 0, "<c:out value="${methode.nom}"/>");
                    </c:if>
                </c:forEach>
                <c:choose>
                    <c:when test="${tempsCPUmethode.value[3].equals(0.0)}">
                        data.setCell(<c:out value="${ligne}"/> , 1, 0);
                        data.setCell(<c:out value="${ligne}"/> , 2, 0);
                        data.setCell(<c:out value="${ligne}"/> , 3, 0);
                    </c:when>
                    <c:otherwise>
                        data.setCell(<c:out value="${ligne}"/> , 1, <c:out value="${tempsCPUmethode.value[0]}"/>);
                        data.setCell(<c:out value="${ligne}"/> , 2, <c:out value="${tempsCPUmethode.value[2]/tempsCPUmethode.value[3]}"/>);
                        data.setCell(<c:out value="${ligne}"/> , 3, <c:out value="${tempsCPUmethode.value[1]}"/>);
                    </c:otherwise>
                </c:choose>
                <c:set var="ligne" scope="session" value="${ligne+1}"/>
            </c:forEach>
            var table = new google.visualization.Table(document.getElementById('table_div<c:out value="${tempsCPU.key}"/>'));
            table.draw(data);

            var options = {
                isStacked: true,
                hAxis: {
                    title: 'Méthodes',
                },
                vAxis: {
                    title:
                        <c:choose>
                        <c:when test="${infos.equals('CPUTime')}">
                        '<c:out value="Temps CPU (en secondes)"/>'
                    </c:when>
                    <c:when test="${infos.equals('nbExploredNodes')}">
                    '<c:out value="Noeuds explorés"/>'
                        </c:when>
                        <c:when test="${infos.equals('SolutionState')}">
                        '<c:out value="Instances traitées"/>'
                    </c:when>
                    <c:when test="${infos.equals('PerFixedVars')}">
                        '<c:out value="Pré-processing (en %)"/>'
                    </c:when>
                    <c:when test="${infos.equals('UB/LB')}">
                        '<c:out value="Écart UB / LB"/>'
                    </c:when>
                    <c:otherwise>
                    '<c:out value="Déviation fonction objectif"/>'
                    </c:otherwise>
                    </c:choose>,
                    scaleType: 'mirrorLog',
                },
                legend: {position: 'top', maxLines: 3},
                width:350
            };
            var chart = new google.visualization.ColumnChart(document.getElementById("graph_div<c:out value="${tempsCPU.key}"/>"));
            chart.draw(data,options);
        }
    </script>
    <div class="groupe">
        <div class="subgroupe">
            <c:choose>
                <c:when test="${fn:contains(tempsCPU.key,'-')}">
                    <c:set var="chaine" value="${fn:split(tempsCPU.key, '-')}" />
                    <c:forEach items="${execution.subsets}" var="subset">
                        <c:if test="${subset.id == chaine[0]}">
                            <h3><c:out value="${subset.nom}"/> (<c:out value="${subset.dataset.nom}"/>)</h3>
                        </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${execution.datasets}" var="dataset">
                        <c:if test="${dataset.id == tempsCPU.key}">
                            <h3><c:out value="${dataset.nom}"/></h3>
                        </c:if>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            <div id="table_div<c:out value="${tempsCPU.key}" />" class="subgroupe"></div>
        </div>
        <div id="graph_div<c:out value="${tempsCPU.key}"/>" class="subgroupe"></div>
    </div>
</c:forEach>