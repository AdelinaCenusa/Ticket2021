<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Ticket Machine</title>
        <meta charset="UTF-8">
        <meta name="description" content="Ticket Machine">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="resources/css/ticket-core.css">
        <link rel="stylesheet" type="text/css" href="resources/css/ticket-configure.css">
    </head>
    <body>
        <% response.setIntHeader("Refresh", 30); %>
        <div id="wrapper">
            <%@ include file="common/header.jsp" %>

            <div id="main-content">
                <section id="messages">
                    <p class="message">Last update attempt: ${lastAttempt.toString()}</p>
                    <p class="message">Last successful update: ${lastUpdate.toString()}</p>
                </section>

                <section id="config-machine">
                    <form method="post">
                        <p>
                            <label class="col-label">Ticket Machine ID</label>
                            <label class="col-value">${currentUuid}</label>
                        </p>
                        <p>
                            <label class="col-label" for="stationName">Station Name</label>
                            <select class="col-value" id="stationName" name="stationName" autocomplete="off">
                                <c:if test="${empty currentStationName}">
                                    <option value="">Not Configured</option>
                                </c:if>
                                <c:forEach items="${stationList}" var="station" varStatus="tagStatus">
                                    <option value="${station.name}" <c:if test="${station.name == currentStationName}"> selected </c:if>>${station.name} - Zone ${station.zone}</option>
                                </c:forEach>
                            </select>
                        </p>
                        <p>
                            <button type="submit" formaction="updateConfig?uuid=${currentUuid}">Apply Changes</button>
                        </p>
                    </form>
                </section>

                <c:if test="${not empty currentStationName}">
                    <section id="nav-home">
                        <form method="post">
                            <button type="submit" formaction="home">Return to Main Menu</button>
                        </form>
                    </section>
                </c:if>
            </div>

            <%@ include file="common/footer.jsp" %>
        </div>
    </body>
</html>
