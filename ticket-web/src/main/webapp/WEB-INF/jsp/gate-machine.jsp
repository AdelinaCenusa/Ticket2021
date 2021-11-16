<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Gate Machine</title>
        <meta charset="UTF-8">
        <meta name="description" content="Ticket Machine">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="resources/css/ticket-core.css">
    </head>
    <body>
        <div id="wrapper">
            <%@ include file="common/header.jsp" %>

            <div id="main-content">
                <form method="post">
                    <p>
                        <label class="col-label" for="stationName">Gate Location</label>
                        <select class="col-value" id="stationName" name="stationName" autocomplete="off">
                            <c:forEach items="${stationList}" var="station" varStatus="tagStatus">
                                <option value="${station.name}">${station.name} - Zone ${station.zone}</option>
                            </c:forEach>
                        </select>
                    </p>
                    <p>
                        <label for="hour">Time - Hour</label>
                        <input id="hour" name="hour" type="number" min="0" max="23" required>
                    </p>
                    <p>
                        <label for="minutes">Time - Minutes</label>
                        <input id="minutes" name="minutes" type="number" min="0" max="59" required>
                    </p>
                    <p>
                        <label for="ticket-xml">Ticket XML</label>
                        <textarea id="ticket-xml" name="ticketXml" rows="20" required></textarea>
                    </p>

                    <div id="row-buttons">
                        <button class="gate-button" type="submit" formaction="opengate?access=entry" >Validate - Entry Gate</button>
                        <button class="gate-button" type="submit" formaction="opengate?access=exit">Validate - Exit Gate</button>
                    </div>
                </form>

                <div id="gateStatus">
                    <c:if test="${gateOpen}"><p id="gate-open">Gate open</p></c:if>
                    <c:if test="${not gateOpen}"><p id="gate-closed">Gate closed</p></c:if>
                </div>
            </div>

            <%@ include file="common/footer.jsp" %>
        </div>
    </body>
</html>
