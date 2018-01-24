<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- parcours datasets -->
<c:forEach items="${resultats}" var="data">
    <script>
        google.charts.load('current', {'packages':['table','corechart']});
        google.charts.setOnLoadCallback(drawChart);
        function drawChart() {
            const data = new google.visualization.DataTable();
            data.addColumn('string', 'Méthode');
            data.addColumn('number', 'Instances');
            <c:set var="ligne" scope="session" value="${0}"/>
            <!-- parcours méthodes -->
            <c:forEach items="${data.value}" var="datamethode">
                data.addRow();
                <c:forEach items="${execution.methodes}" var="methode">
                    <c:if test="${methode.id == datamethode.key}">
                        data.setCell(<c:out value="${ligne}"/> , 0, "<c:out value="${methode.nom}"/>");
                    </c:if>
                </c:forEach>
                data.setCell(<c:out value="${ligne}"/> , 1, <c:out value="${datamethode.value}"/>);
                <c:set var="ligne" scope="session" value="${ligne+1}"/>
            </c:forEach>
            var table = new google.visualization.Table(document.getElementById('table_div<c:out value="${data.key}"/>'));
            table.draw(data);

            var options = {
                isStacked: true,
                hAxis: {
                    title: 'Méthodes',
                },
                vAxis: {
                    title:
                        <c:choose>
                            <c:when test="${infos.equals('count')}">
                                '<c:out value="Instances traitées"/>'
                            </c:when>
                            <c:when test="${infos.equals('SolutionState')}">
                                '<c:out value="Solution optimales"/>'
                            </c:when>
                        </c:choose>,
                    scaleType: 'mirrorLog',
                },
                legend: {position: 'top', maxLines: 3},
                width:350
            };
            var chart = new google.visualization.ColumnChart(document.getElementById("graph_div<c:out value="${data.key}"/>"));
            chart.draw(data,options);
        }
    </script>
    <div class="groupe">
        <div class="groupe">
            <c:choose>
                <c:when test="${fn:contains(data.key,'-')}">
                    <c:set var="chaine" value="${fn:split(data.key, '-')}" />
                    <c:forEach items="${execution.subsets}" var="subset">
                        <c:if test="${subset.id == chaine[0]}">
                            <h3><c:out value="${subset.nom}"/> (<c:out value="${subset.dataset.nom}"/>)</h3>
                        </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${execution.datasets}" var="dataset">
                        <c:if test="${dataset.id == data.key}">
                            <h3><c:out value="${dataset.nom}"/></h3>
                        </c:if>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            <div id="table_div<c:out value="${data.key}" />" class="groupe"></div>
        </div>
        <div id="graph_div<c:out value="${data.key}"/>" class="groupe"></div>
    </div>
</c:forEach>