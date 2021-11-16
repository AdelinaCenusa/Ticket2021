<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
    <head>
        <title>Underground Controller - Manage Stations</title>
        <meta charset="UTF-8">
        <meta name="description" content="Controller to manage underground stations">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="resources/css/underground-core.css">
        <link rel="stylesheet" type="text/css" href="resources/css/underground-manage-stations.css">
    </head>
    <body>
        <div id="wrapper">
            <%@ include file="common/header.jsp" %>

            <div id="main-content">
                <section id="messages">
                    <p id="message">
                        <c:if test="${not empty newStation}">
                            Station with name "${newStation.name}" added to zone ${newStation.zone}
                        </c:if>
                        <c:if test="${not empty delStation}">
                            Station with name "${delStation}" deleted
                        </c:if>
                        <c:if test="${not empty updatedStation}">
                            Station with ID "${updatedStation}" has been updated
                        </c:if>
                    </p>
                </section>

                <section id="manage-station">
                    <form method="post">
                        <div id="station-inputs">
                            <p>
                                <label class="col-label" for="stationName">Station Name:</label>
                                <input class="col-input" id="stationName" type="text" name="stationName"
                                       required="true" value="${editStationName}">
                                <button type="submit" formaction="manage-stations/add">Add</button>
                            </p>
                            <p>
                                <label class="col-label" for="zoneNumber">Zone:</label>
                                <select class="col-input" id="zoneNumber" name="zoneNumber">
                                    <c:forEach items="${zones}" var="zone" varStatus="tagStatus">
                                        <option value="${zone}" <c:if test="${zone == editStationZone}"> selected </c:if>>Zone ${zone}</option>
                                    </c:forEach>
                                </select>
                                <c:if test="${not empty editStationId}">
                                    <button type="submit" formaction="manage-stations/update?id=${editStationId}">Update</button>
                                </c:if>
                            </p>
                        </div>
                    </form>
                </section>

                <section id="list-stations">
                    <form method="post">
                        <table id="station-list">
                            <tr>
                                <th class="col-name">Station Name</th>
                                <th class="col-zone">Zone</th>
                                <th class="col-modify"></th>
                                <th class="col-delete"></th>
                            </tr>
                            <c:forEach items="${stations}" var="station" varStatus="tagStatus">
                                <tr>
                                    <td>${station.name}</td>
                                    <td>${station.zone}</td>
                                    <td>
                                        <button formaction="manage-stations/edit?id=${station.id}">Modify</button>
                                    </td>
                                    <td>
                                        <button formaction="manage-stations/delete?id=${station.id}">Delete</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </form>
                </section>
            </div>
        </div>
    </body>
</html>
