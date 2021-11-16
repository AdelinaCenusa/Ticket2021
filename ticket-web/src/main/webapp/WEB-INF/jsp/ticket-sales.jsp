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
    </head>
    <body>
        <div id="wrapper">
            <%@ include file="common/header.jsp" %>

            <div id="main-content">
                <section id="start-point">
                    <p>Start: ${startStation} in zone ${startZone}</p>
                </section>
                <section id="end-point">
                    <form method="post">
                        <p>
                            <label class="col-label" for="stationName">Destination</label>
                            <select class="col-value" id="stationName" name="stationName" autocomplete="off">
                                <c:forEach items="${stationList}" var="station" varStatus="tagStatus">
                                    <c:if test="${station.name != startStation}">
                                        <option value="${station.name}">${station.name} - Zone ${station.zone}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </p>
                        <p>
                            <button type="submit" formaction="checkout">Buy Ticket</button>
                        </p>
                    </form>
                </section>
            </div>

            <%@ include file="common/footer.jsp" %>
        </div>
    </body>
</html>
