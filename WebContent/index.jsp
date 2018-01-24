<%--
  Created by IntelliJ IDEA.
  User: kevmi
  Date: 05/01/2017
  Time: 14:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link type="text/css" rel="stylesheet" href="CSS/menu.css"/>
<div id="divMenu">
    <ul id="menu">
        <li>Méthodes
            <ul>
                <li><a href="/GraphMatching/ListeMethode">Liste des méthodes</a></li>
                <li><a href="/GraphMatching/FicheMethodeExacte">Ajout d'une méthode exacte</a></li>
                <li><a href="/GraphMatching/FicheMethodeHeuristique">Ajout d'une méthode heuristique</a></li>
            </ul>
        </li>
        <li>Datasets
            <ul>
                <li><a href="/GraphMatching/ListeDataset">Liste des datasets</a></li>
                <li><a href="/GraphMatching/FicheDataset">Ajout d'un dataset</a></li>
            </ul>
        </li>
        <li>Tests
            <ul>
                <li><a href="/GraphMatching/ListeTest">Liste des tests</a></li>
                <li><a href="/GraphMatching/FicheTest">Ajout d'un test</a></li>
            </ul>
        </li>
        <li>Exécutions
            <ul>
                <li><a href="/GraphMatching/ListeExecution">Liste des éxécutions</a></li>
                <li><a href="/GraphMatching/ListeTest">Ajout d'une éxécution</a></li>
            </ul>
        </li>
    </ul>
</div>